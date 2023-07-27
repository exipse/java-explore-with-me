package ru.practicum.explore_with_me.requests.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.event.dto.State;
import ru.practicum.explore_with_me.event.model.Event;
import ru.practicum.explore_with_me.event.repository.EventRepository;
import ru.practicum.explore_with_me.exception.NoFoundException;
import ru.practicum.explore_with_me.exception.ValidateDataException;
import ru.practicum.explore_with_me.requests.dto.ParticipationRequestDto;
import ru.practicum.explore_with_me.requests.dto.Status;
import ru.practicum.explore_with_me.requests.mapper.RequestMapper;
import ru.practicum.explore_with_me.requests.model.Request;
import ru.practicum.explore_with_me.requests.repository.RequestRepository;
import ru.practicum.explore_with_me.user.model.User;
import ru.practicum.explore_with_me.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getByUserId(Long userId) {
        User user = checkUser(userId);

        List<ParticipationRequestDto> requestinDb =
                requestMapper.toRequestListDto(requestRepository.findAllByRequesterId(userId));
        log.info("Найдено {} запросов ", requestinDb.size());
        return requestinDb;
    }

    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {
        User user = checkUser(userId);
        Event event = checkEvent(eventId);
        Long confirmedRequestsByEvent =
                (long) requestRepository.findAllByStatusAndEventId(Status.CONFIRMED, eventId).size();
        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidateDataException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        Optional<Request> requestDb = requestRepository.findByEventIdAndRequesterId(eventId, userId);
        if (requestDb.isPresent()) {
            throw new ValidateDataException("Нельзя добавить повторный запрос");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ValidateDataException("нельзя участвовать в неопубликованном событии");
        }
        if (event.getParticipantLimit().equals(confirmedRequestsByEvent) &&
                !event.getParticipantLimit().equals(0L)) {
            throw new ValidateDataException("У события достигнут лимит запросов на участие");
        }

        Request request = new Request(null, LocalDateTime.now(), event, user, Status.PENDING);

        if (!event.getRequestModeration() || event.getParticipantLimit().equals(0L)) {
            request.setStatus(Status.CONFIRMED);
            event.setConfirmedRequests(confirmedRequestsByEvent + 1);
            eventRepository.save(event);
        }
        Request saveRequest = requestRepository.save(request);
        ParticipationRequestDto dto = requestMapper.toRequestDto(saveRequest);
        log.info("Добавлен запрос от пользователя {} на участие в событии {}", userId, eventId);
        return dto;
    }

    @Override
    public ParticipationRequestDto update(Long userId, Long requestsId) {
        Request request = requestRepository.findByIdAndRequesterId(requestsId, userId)
                .orElseThrow(() -> new NoFoundException("Запрос не найден"));
        request.setStatus(Status.CANCELED);
        ParticipationRequestDto saveDto = requestMapper.toRequestDto(requestRepository.save(request));
        log.info("Запрос пользователя ={} на участие в событие = {} отменен", userId, requestsId);
        return saveDto;
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoFoundException("Пользователь не найден"));
    }

    private Event checkEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NoFoundException("Событие не найдено"));
    }
}

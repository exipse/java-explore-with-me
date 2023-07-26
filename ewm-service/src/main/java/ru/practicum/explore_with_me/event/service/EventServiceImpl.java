package ru.practicum.explore_with_me.event.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.StatsClient;
import ru.practicum.explore_with_me.category.model.Category;
import ru.practicum.explore_with_me.category.repository.CategoryRepository;
import ru.practicum.explore_with_me.dto.ViewStatsDto;
import ru.practicum.explore_with_me.event.dto.*;
import ru.practicum.explore_with_me.event.mapper.EventMapper;
import ru.practicum.explore_with_me.event.model.Event;
import ru.practicum.explore_with_me.event.repository.EventRepository;
import ru.practicum.explore_with_me.exception.NoFoundException;
import ru.practicum.explore_with_me.exception.ValidateDataException;
import ru.practicum.explore_with_me.location.dto.LocationDto;
import ru.practicum.explore_with_me.location.mapper.LocationMapper;
import ru.practicum.explore_with_me.location.model.Location;
import ru.practicum.explore_with_me.location.repository.LocationRepository;
import ru.practicum.explore_with_me.requests.dto.ParticipationRequestDto;
import ru.practicum.explore_with_me.requests.dto.RequestStatusUpdateRequestDto;
import ru.practicum.explore_with_me.requests.dto.RequestStatusUpdateResultDto;
import ru.practicum.explore_with_me.requests.dto.Status;
import ru.practicum.explore_with_me.requests.mapper.RequestMapper;
import ru.practicum.explore_with_me.requests.model.Request;
import ru.practicum.explore_with_me.requests.repository.RequestRepository;
import ru.practicum.explore_with_me.user.model.User;
import ru.practicum.explore_with_me.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;

    private final LocationMapper locationMapper;
    private final RequestMapper requestMapper;
    private final LocationRepository locationRepository;

    @Override
    public List<EventFullDto> getAllEventByAdmin(List<Long> users, List<State> states, List<Long> categories,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(from / size, size, sortById);
        if (rangeStart != null && rangeEnd != null) {
            validateDate(rangeStart, rangeEnd);
        }
        List<Event> events
                = eventRepository.findEventsByParams(users, states, categories, rangeStart, rangeEnd, pageable);
        events.forEach(event -> event.setViews(event.getViews() + 1));
        eventRepository.saveAll(events);
        return eventMapper.toEventListDto(events);
    }

    @Transactional
    @Override
    public EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        if (updateEventAdminRequestDto.getEventDate() != null) {
            checkDate(updateEventAdminRequestDto.getEventDate(), 1);
        }

        Event eventinDB = checkEvent(eventId);

        if (updateEventAdminRequestDto.getStateAction() != null) {
            if (updateEventAdminRequestDto.getStateAction().equals(StateAdminAction.PUBLISH_EVENT)
                    && !eventinDB.getState().equals(State.PENDING)) {
                throw new ValidateDataException("Cобытие можно публиковать," +
                        " только если оно в состоянии ожидания публикации");
            }
            if ((updateEventAdminRequestDto.getStateAction().equals(StateAdminAction.REJECT_EVENT)
                    || updateEventAdminRequestDto.getStateAction().equals(StateAdminAction.PUBLISH_EVENT))
                    && eventinDB.getState().equals(State.PUBLISHED)) {
                throw new ValidateDataException("Cобытие опубликовано, его невозможно отменить");
            }
        }

        eventinDB = updateEventsFieldsByAdmin(eventinDB, updateEventAdminRequestDto);
        Event saveEvent = eventRepository.save(eventinDB);
        log.info("Событие eventId={} обновлено администратором", eventId);
        return eventMapper.toEventDto(saveEvent);
    }


    @Override
    public List<EventShortDto> findEventsWithFilter(String text, List<Long> categories, Boolean paid,
                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                    Boolean onlyAvailable, String sort, int from, int size) {

        if ((rangeStart != null) && (rangeEnd != null)) {
            validateDate(rangeStart, rangeEnd);
        }
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(from / size, size, sortById);

        List<Event> events = eventRepository.findPublicEvents(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, pageable);
        events.forEach(event -> event.setViews(event.getViews() + 1));
        eventRepository.saveAll(events);
        return eventMapper.fromListEventModeltoEventListShortDto(events);
    }


    @Override
    public EventFullDto getPublicEventById(Long id, HttpServletRequest request) {
        Event event = checkEvent(id);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NoFoundException("Событие не доступно для просмотра");
        }

        List<String> uris = new ArrayList<>();
        uris.add(request.getRequestURI());

        LocalDateTime start = event.getPublishedOn();
        LocalDateTime end = LocalDateTime.now();


        List<ViewStatsDto> views = (List<ViewStatsDto>) statsClient.get(start, end, uris, true).getBody();

        if (views != null) {
            event.setViews((long) views.size());
        } else {
            event.setViews(0L);
        }

        EventFullDto fullDto = eventMapper.toEventDto(eventRepository.save(event));
        log.info("Событие найдено");
        return eventMapper.toEventDto(eventRepository.save(event));
    }


    @Override
    public List<EventShortDto> getMyEvents(Long userId, Integer from, Integer size) {

        User user = checkUser(userId);
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(from / size, size, sortById);

        List<Event> findEventsInDb = eventRepository.findAllByInitiatorId(userId, pageable).getContent();
        log.info("Список событий пользователя {} найден. Размер списка = {} элементов", userId, findEventsInDb.size());

        findEventsInDb.forEach(event -> event.setViews(event.getViews() + 1));
        eventRepository.saveAll(findEventsInDb);
        return eventMapper.fromListEventModeltoEventListShortDto(findEventsInDb);
    }

    @Transactional
    @Override
    public EventFullDto createNewEvent(Long userId, NewEventDto newEventDto) {
        checkDate(newEventDto.getEventDate(), 2);
        User user = checkUser(userId);
        Category category = checkCategory(newEventDto.getCategory());
        Location location = checkLocation(newEventDto.getLocation());
        Event eventModel = eventMapper.toEventModelFromNewEventDto(newEventDto);
        eventModel.setConfirmedRequests(0L);
        eventModel.setCategory(category);
        eventModel.setCreatedOn(LocalDateTime.now());
        eventModel.setInitiator(user);
        eventModel.setLocation(location);
        eventModel.setState(State.PENDING);
        eventModel.setViews(0L);
        Event saveEvent = eventRepository.save(eventModel);
        return eventMapper.toEventDto(saveEvent);
    }

    @Override
    public EventFullDto getMyEventById(Long userId, Long eventId) {
        User user = checkUser(userId);
        Event eventInDB = eventRepository.findEventByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NoFoundException(String.format("У пользователя id=%d событие id=%d не найдено",
                        userId, eventId)));
        log.info("Для пользователя id={} найдено событие id={}", userId, eventId);
        eventInDB.setViews(eventInDB.getViews() + 1);
        return eventMapper.toEventDto(eventRepository.save(eventInDB));
    }


    private Event updateEventsFieldsByAdmin(Event event, UpdateEventAdminRequestDto updateEventAdmin) {
        if (updateEventAdmin.getAnnotation() != null && !updateEventAdmin.getAnnotation().isBlank()) {
            event.setAnnotation(updateEventAdmin.getAnnotation());
        }
        if (updateEventAdmin.getCategory() != null) {
            event.setCategory(checkCategory(updateEventAdmin.getCategory()));
        }
        if (updateEventAdmin.getDescription() != null && !updateEventAdmin.getDescription().isBlank()) {
            event.setDescription(updateEventAdmin.getDescription());
        }
        if (updateEventAdmin.getEventDate() != null) {
            event.setEventDate(updateEventAdmin.getEventDate());
        }
        if (updateEventAdmin.getLocation() != null) {
            event.setLocation(checkLocation(updateEventAdmin.getLocation()));
        }
        if (updateEventAdmin.getPaid() != null) {
            event.setPaid(updateEventAdmin.getPaid());
        }
        if (updateEventAdmin.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdmin.getParticipantLimit());
        }
        if (updateEventAdmin.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdmin.getRequestModeration());
        }
        if (StateAdminAction.PUBLISH_EVENT.equals(updateEventAdmin.getStateAction())) {
            event.setState(State.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        }
        if (StateAdminAction.REJECT_EVENT.equals(updateEventAdmin.getStateAction())) {
            event.setState(State.CANCELED);
        }
        if (updateEventAdmin.getTitle() != null && !updateEventAdmin.getTitle().isBlank()) {
            event.setTitle(updateEventAdmin.getTitle());
        }
        return event;
    }

    @Override
    public EventFullDto updateMyEvent(Long userId, Long eventId, UpdateEventUserRequestDto userRequest) {
        checkDate(userRequest.getEventDate(), 2);
        User user = checkUser(userId);
        Event eventinDB = checkEvent(eventId);

        if (!eventinDB.getInitiator().getId().equals(userId)) {
            throw new ValidateDataException("Событие создал не пользовательть с id = " + userId);
        }
        if (eventinDB.getState().equals(State.PUBLISHED)) {
            throw new ValidateDataException("Можно изменять только отмененные события или события в состоянии модерации");
        }
        checkDate(eventinDB.getEventDate(), 2);
        eventinDB = updateEventFieldsByUser(eventinDB, userRequest);
        Event saveEvent = eventRepository.save(eventinDB);
        log.info("Событие eventId={} обновлено", eventId);
        return eventMapper.toEventDto(saveEvent);
    }


    private Event updateEventFieldsByUser(Event event, UpdateEventUserRequestDto updateEvent) {
        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            event.setCategory(checkCategory(updateEvent.getCategory()));
        }
        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(checkLocation(updateEvent.getLocation()));
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (StateUserAction.SEND_TO_REVIEW.equals(updateEvent.getStateAction())) {
            event.setState(State.PENDING);
            event.setPublishedOn(LocalDateTime.now());
        }
        if (StateUserAction.CANCEL_REVIEW.equals(updateEvent.getStateAction())) {
            event.setState(State.CANCELED);
        }
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            event.setTitle(updateEvent.getTitle());
        }
        return event;
    }


    private User checkUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NoFoundException("Пользователь не найден"));
    }

    private Category checkCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoFoundException("Категория не найдена"));
    }

    private Event checkEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NoFoundException("Событие не найдено"));
    }

    private Location checkLocation(LocationDto locationDto) {
        Location locationModel = locationMapper.toLocationModel(locationDto);
        Optional<Location> locationInDb
                = locationRepository.findByLatAndLon(locationModel.getLat(), locationModel.getLon());
        Location location;
        if (locationInDb.isEmpty()) {
            location = locationRepository.save(locationModel);
        } else {
            location = locationInDb.get();
        }
        return location;
    }

    private void checkDate(LocalDateTime dateTime, int hours) {
        LocalDateTime now = LocalDateTime.now().plusHours(hours);
        if (dateTime != null && dateTime.isBefore(now)) {
            throw new IllegalArgumentException(
                    "Дата и время на которые намечено событие не может быть раньше, чем через два часа");
        }
    }

    private void validateDate(LocalDateTime start, LocalDateTime end) {
        if (!(start.isBefore(end) && !start.equals(end))) {
            throw new IllegalArgumentException("StartDate должна быть раньше чем EndDate");
        }
    }


    @Override
    public List<ParticipationRequestDto> getMyEventsByRequest(Long userId, Long eventId) {

        eventRepository.findEventByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NoFoundException(String.format("У пользователя id=%d событие id=%d не найдено",
                        userId, eventId)));
        List<ParticipationRequestDto> resulRequests =
                requestMapper.toRequestListDto(requestRepository.findAllByEventId(eventId));
        log.info("Получена информация о {} запросах по событию {}", resulRequests.size(), eventId);
        return resulRequests;
    }


    @Override
    @Transactional
    public RequestStatusUpdateResultDto updateRequestsStatus(Long userId, Long eventId,
                                                             RequestStatusUpdateRequestDto requestDto) {
        User user = checkUser(userId);
        Event event = checkEvent(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidateDataException("У пользователя нет события с id = " + eventId);
        }
        RequestStatusUpdateResultDto result = new RequestStatusUpdateResultDto();
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            return result;
        }

        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ValidateDataException("Нельзя подтвердить заявку, уже достигнут лимит по заявкам на данное событие");
        }

        List<Request> requestsForUpdate = requestRepository.findAllByIdIn(requestDto.getRequestIds());

        for (Request request : requestsForUpdate) {

            if (!request.getStatus().equals(Status.PENDING)) {
                throw new ValidateDataException("Cтатус заявки не PENDING");
            }
            if ((event.getParticipantLimit() - event.getConfirmedRequests() > 0)
                    && requestDto.getStatus().equals(Status.CONFIRMED)) {
                request.setStatus(Status.CONFIRMED);
                requestRepository.save(request);
                ParticipationRequestDto dto = requestMapper.toRequestDto(request);
                result.getConfirmedRequests().add(dto);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            } else {
                request.setStatus(Status.REJECTED);
                requestRepository.save(request);
                result.getRejectedRequests().add(requestMapper.toRequestDto(request));
            }
        }
        return result;
    }

}

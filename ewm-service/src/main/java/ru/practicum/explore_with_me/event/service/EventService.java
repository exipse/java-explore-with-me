package ru.practicum.explore_with_me.event.service;

import ru.practicum.explore_with_me.event.dto.*;
import ru.practicum.explore_with_me.requests.dto.ParticipationRequestDto;
import ru.practicum.explore_with_me.requests.dto.RequestStatusUpdateRequestDto;
import ru.practicum.explore_with_me.requests.dto.RequestStatusUpdateResultDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    /**
     * Поиск событий
     */
    List<EventFullDto> getAllEventByAdmin(List<Long> users, List<State> states, List<Long> categories,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);


    /**
     * Редактирование данных события и его статуса (отклонение/публикация).
     */
    EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto);


    //Получение событий с возможностью фильтрации
    List<EventShortDto> findEventsWithFilter(
            String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
            Boolean onlyAvailable, String sort, int from, int size);


    /**
     * Получение информации о категории по её идентификатору
     */
    EventFullDto getPublicEventById(Long id, HttpServletRequest request);

    /**
     * Получение событий, добавленных текущим пользователем
     */
    List<EventShortDto> getMyEvents(Long userId, Integer from, Integer size);

    /**
     * Добавление нового события
     */
    EventFullDto createNewEvent(Long userId, NewEventDto newEventDto);

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     */
    EventFullDto getMyEventById(Long userId, Long eventId);

    /**
     * Изменение события добавленного текущим пользователем
     */
    EventFullDto updateMyEvent(Long userId, Long eventId, UpdateEventUserRequestDto userRequest);


    List<ParticipationRequestDto> getMyEventsByRequest(Long userId, Long eventId);

    RequestStatusUpdateResultDto updateRequestsStatus(Long userId, Long eventId,
                                                      RequestStatusUpdateRequestDto requestDto);
}

package ru.practicum.explore_with_me.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.StatsClient;
import ru.practicum.explore_with_me.dto.EndpointHitDto;
import ru.practicum.explore_with_me.event.dto.EventFullDto;
import ru.practicum.explore_with_me.event.dto.EventShortDto;
import ru.practicum.explore_with_me.event.dto.NewEventDto;
import ru.practicum.explore_with_me.event.dto.UpdateEventUserRequestDto;
import ru.practicum.explore_with_me.event.service.EventService;
import ru.practicum.explore_with_me.requests.dto.ParticipationRequestDto;
import ru.practicum.explore_with_me.requests.dto.RequestStatusUpdateRequestDto;
import ru.practicum.explore_with_me.requests.dto.RequestStatusUpdateResultDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@AllArgsConstructor
@Validated
@Slf4j
public class EventControllerPrivate {

    EventService eventService;
    private final StatsClient statsClient;
    private static final String eventName = "ewm-service";

    /**
     * Получение событий, добавленных текущим пользователем
     */
    @GetMapping
    public List<EventShortDto> getMyEvents(@PathVariable @Positive Long userId,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                           @Positive @RequestParam(defaultValue = "10") Integer size,
                                           HttpServletRequest request) {
        log.info("GET /users/{}/events}", userId);
        saveStats(request);
        return eventService.getMyEvents(userId, from, size);
    }

    /**
     * Добавление нового события
     */
    @PostMapping
    public EventFullDto createNewEvent(@Positive @PathVariable Long userId,
                                       @Valid @RequestBody NewEventDto newEventDto) {
        log.info("POST /users/{}/events}", userId);
        return eventService.createNewEvent(userId, newEventDto);
    }

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     */
    @GetMapping("/{eventId}")
    public EventFullDto getMyEventById(@Positive @PathVariable Long userId,
                                       @Positive @PathVariable Long eventId,
                                       HttpServletRequest request) {
        log.info("GET /users/{}/events/{}", userId, eventId);
        saveStats(request);
        return eventService.getMyEventById(userId, eventId);
    }

    /**
     * Изменение события добавленного текущим пользователем
     */
    @PatchMapping("/{eventId}")
    public EventFullDto updateMyEvent(@Positive @PathVariable Long userId,
                                      @Positive @PathVariable Long eventId,
                                      @RequestBody UpdateEventUserRequestDto userRequest) {
        log.info("PATCH /users/{}/events/{}", userId, eventId);
        return eventService.updateMyEvent(userId, eventId, userRequest);
    }


    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     */
    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getMyEventsByRequest(@Positive @PathVariable Long userId,
                                                              @Positive @PathVariable Long eventId) {
        log.info("GET /users/{}/events/{}/requests", userId, eventId);
        return eventService.getMyEventsByRequest(userId, eventId);

    }

    /**
     * Изменение статуса (подтверждена/отменена) заявок на участие в событии текущего пользователя
     */
    @PatchMapping("/{eventId}/requests")
    public RequestStatusUpdateResultDto updateRequestsStatus(@Positive @PathVariable Long userId,
                                                             @Positive @PathVariable Long eventId,
                                                             @RequestBody RequestStatusUpdateRequestDto requestDto) {
        log.info("PATCH /{}/events/{}/requests", userId, eventId);
        return eventService.updateRequestsStatus(userId, eventId, requestDto);
    }


    private void saveStats(HttpServletRequest request) {
        statsClient.create(EndpointHitDto.builder()
                .app(eventName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }
}

package ru.practicum.explore_with_me.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.StatsClient;
import ru.practicum.explore_with_me.dto.EndpointHitDto;
import ru.practicum.explore_with_me.event.dto.EventFullDto;
import ru.practicum.explore_with_me.event.dto.EventShortDto;
import ru.practicum.explore_with_me.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@Slf4j
@Validated
public class EventControllerPublic {

    private final EventService eventService;
    private final StatsClient statsClient;
    private final String eventName;

    public EventControllerPublic(EventService eventService, StatsClient statsClient,
                                  @Value("${service.url}") String eventName) {
        this.eventService = eventService;
        this.statsClient = statsClient;
        this.eventName = eventName;
    }

    /**
     * Получение событий с возможностью фильтрации
     */
    @GetMapping
    public List<EventShortDto> findEventsWithFilter(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") String sort,
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        log.info("GET /events?text={}&categories={}&paid={}&rangeStart={}&rangeEnd={}&onlyAvailable={}&sort={}" +
                "&from={}&size={}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        saveStats(request);
        return eventService.findEventsWithFilter(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);
    }

    /**
     * Получение подробной информации об опубликованном событии по его идентификатору
     */
    @GetMapping("/{id}")
    public EventFullDto getPublicEventById(@Positive @PathVariable Long id, HttpServletRequest request) {
        log.info("GET /events/{}", id);
        saveStats(request);
        return eventService.getPublicEventById(id, request);
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

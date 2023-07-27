package ru.practicum.explore_with_me.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.StatsClient;
import ru.practicum.explore_with_me.dto.EndpointHitDto;
import ru.practicum.explore_with_me.event.dto.EventFullDto;
import ru.practicum.explore_with_me.event.dto.State;
import ru.practicum.explore_with_me.event.dto.UpdateEventAdminRequestDto;
import ru.practicum.explore_with_me.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Slf4j
@Validated
public class EventControllerAdmin {

    private final EventService eventService;
    private final StatsClient statsClient;
    private final String eventName;

    public EventControllerAdmin(EventService eventService, StatsClient statsClient,
                                @Value("${service.url}") String eventName) {
        this.eventService = eventService;
        this.statsClient = statsClient;
        this.eventName = eventName;
    }


    @GetMapping
    public List<EventFullDto> getAllEventByAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<State> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        log.info("GET /admin/events?users={}&states={}&categories={}&rangeStart={}&rangeEnd={}&from={}&size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        saveStats(request);
        return eventService.getAllEventByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateByAdmin(@PositiveOrZero @PathVariable Long eventId,
                                      @Valid @RequestBody UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        log.info("PATCH /admin/events/{}", eventId);
        return eventService.updateByAdmin(eventId, updateEventAdminRequestDto);
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



package ru.practicum.explore_with_me.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.EndpointHitDto;
import ru.practicum.explore_with_me.dto.ViewStatsDto;
import ru.practicum.explore_with_me.service.StatsServiceImpl;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@Slf4j
@AllArgsConstructor
@Validated
public class StatsController {

    private final StatsServiceImpl statsService;

    /**
     * Сохранение информации о том, что к эндпоинту был запрос
     */
    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto create(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("POST /hit");
        return statsService.create(endpointHitDto);
    }

    /**
     * Получение статистики по посещениям
     */
    @GetMapping("/stats")
    public List<ViewStatsDto> get(@RequestParam @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                  @RequestParam(required = false) List<String> uris,
                                  @RequestParam(defaultValue = "false") boolean unique) {
        log.info("GET /stats");
        return statsService.get(start, end, uris, unique);
    }

}

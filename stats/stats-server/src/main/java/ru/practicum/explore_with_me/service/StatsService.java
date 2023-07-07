package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.dto.EndpointHitDto;
import ru.practicum.explore_with_me.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    EndpointHitDto create(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}

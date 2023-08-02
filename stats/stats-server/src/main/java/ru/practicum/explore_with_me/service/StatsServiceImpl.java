package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.EndpointHitDto;
import ru.practicum.explore_with_me.dto.ViewStatsDto;
import ru.practicum.explore_with_me.exception.DBConstraintException;
import ru.practicum.explore_with_me.mapper.StatsEndPointMapper;
import ru.practicum.explore_with_me.mapper.ViewStatsMapper;
import ru.practicum.explore_with_me.model.EndpointHit;
import ru.practicum.explore_with_me.model.ViewStats;
import ru.practicum.explore_with_me.repository.StatsRepository;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final StatsEndPointMapper endPointMapper;
    private final ViewStatsMapper viewStatsMapper;


    @Override
    public EndpointHitDto create(EndpointHitDto endpointHitDto) {
        try {
            EndpointHit endpoint = statsRepository.save(endPointMapper.toEndpointModel(endpointHitDto));

            return endPointMapper.toEndpointDto(endpoint);
        } catch (Throwable e) {
            throw new DBConstraintException("Ошибка валидации при добавлении в БД");
        }
    }


    @Override
    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start.isAfter(end)) {
            throw new InvalidParameterException("Start date must be earlier than the end date");
        }
        List<ViewStats> result;
        if (unique) {
            result = statsRepository.getUniqueStats(start, end, uris);
        } else {
            result = statsRepository.getStats(start, end, uris);
        }
        List<ViewStatsDto> dtos = viewStatsMapper.toViewStatsListDto(result);
        return dtos;
    }

}

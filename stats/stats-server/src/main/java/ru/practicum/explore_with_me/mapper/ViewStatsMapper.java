package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.dto.ViewStatsDto;
import ru.practicum.explore_with_me.model.ViewStats;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ViewStatsMapper {

    ViewStats toViewStatsModel(ViewStatsDto dto);

    ViewStatsDto toViewStatsDto(ViewStats model);

    List<ViewStatsDto> toViewStatsListDto(List<ViewStats> model);

}

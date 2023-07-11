package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore_with_me.dto.EndpointHitDto;
import ru.practicum.explore_with_me.model.EndpointHit;

@Mapper(componentModel = "spring")
public interface StatsEndPointMapper {

    @Mapping(target = "id", ignore = true)
    EndpointHit toEndpointModel(EndpointHitDto dto);

    EndpointHitDto toEndpointDto(EndpointHit model);
}

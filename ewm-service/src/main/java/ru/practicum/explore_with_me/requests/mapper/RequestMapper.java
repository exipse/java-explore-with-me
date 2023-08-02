package ru.practicum.explore_with_me.requests.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore_with_me.requests.dto.ParticipationRequestDto;
import ru.practicum.explore_with_me.requests.model.Request;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestDto toRequestDto(Request model);

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    List<ParticipationRequestDto> toRequestListDto(List<Request> model);
}

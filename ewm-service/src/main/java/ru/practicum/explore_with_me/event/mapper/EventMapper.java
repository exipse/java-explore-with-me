package ru.practicum.explore_with_me.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore_with_me.category.mapper.CategoryMapper;
import ru.practicum.explore_with_me.event.dto.EventFullDto;
import ru.practicum.explore_with_me.event.dto.EventShortDto;
import ru.practicum.explore_with_me.event.dto.NewEventDto;
import ru.practicum.explore_with_me.event.model.Event;
import ru.practicum.explore_with_me.location.mapper.LocationMapper;
import ru.practicum.explore_with_me.user.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LocationMapper.class, CategoryMapper.class, UserMapper.class})
public interface EventMapper {


    @Mapping(target = "category.id", source = "category")
    @Mapping(target = "location", ignore = true)
    Event toEventModelFromNewEventDto(NewEventDto newEventDto);

    Event toEventModel(EventFullDto eventFullDto);


    // @Mapping(target = "state", ignore = true)
    EventFullDto toEventDto(Event model);

    List<EventFullDto> toEventListDto(List<Event> model);


    // EventFullDto toEventFullFromShort(EventShortDto dto);


    EventShortDto fromEventFulltoEventShortDto(EventFullDto eventFullDto);

    List<EventShortDto> fromListEventFulltoEventListShortDto(List<EventFullDto> eventFullDto);


    EventShortDto fromEventModeltoEventShortDto(Event event);

    List<EventShortDto> fromListEventModeltoEventListShortDto(List<Event> event);

}

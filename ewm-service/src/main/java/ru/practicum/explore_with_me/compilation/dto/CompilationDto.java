package ru.practicum.explore_with_me.compilation.dto;

import lombok.Data;
import ru.practicum.explore_with_me.event.dto.EventShortDto;

import java.util.Set;

@Data
public class CompilationDto {
    private Long id;
    private boolean pinned;
    private String title;
    private Set<EventShortDto> events;
}

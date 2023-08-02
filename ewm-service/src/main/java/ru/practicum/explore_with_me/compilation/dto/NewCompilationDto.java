package ru.practicum.explore_with_me.compilation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
public class NewCompilationDto {

    private Set<Long> events;

    private boolean pinned;

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}

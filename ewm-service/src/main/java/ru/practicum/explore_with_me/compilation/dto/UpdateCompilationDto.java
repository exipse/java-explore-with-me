package ru.practicum.explore_with_me.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

//@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateCompilationDto {

    private Boolean pinned;

    @Length(min = 1, max = 50)
    private String title;

    private Set<Long> events;
}

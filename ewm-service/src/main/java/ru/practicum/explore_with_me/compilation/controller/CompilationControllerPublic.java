package ru.practicum.explore_with_me.compilation.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.compilation.dto.CompilationDto;
import ru.practicum.explore_with_me.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@AllArgsConstructor
@Validated
@Slf4j
public class CompilationControllerPublic {

    private final CompilationService compilationService;

    /**
     * Получение подборок событий
     */
    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                       @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET /compilations?pinned={}&from={}&size={}", pinned, from, size);
        return compilationService.getAll(pinned, from, size);
    }

    /**
     * Получение подборки событий по его id
     */
    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable @PositiveOrZero Long compId) {
        log.info("GET /compilations/{}", compId);
        return compilationService.getById(compId);
    }
}

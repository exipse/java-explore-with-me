package ru.practicum.explore_with_me.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.compilation.dto.CompilationDto;
import ru.practicum.explore_with_me.compilation.dto.NewCompilationDto;
import ru.practicum.explore_with_me.compilation.dto.UpdateCompilationDto;
import ru.practicum.explore_with_me.compilation.service.CompilationService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationControllerAdmin {

    private final CompilationService compilationService;

    /**
     * Добавление новой подборки (подборка может не содержать событий)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("POST /admin/compilations");
        return compilationService.createCompilation(newCompilationDto);
    }

    /**
     * Удаление подборки.
     */
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCompilation(@PathVariable Long compId) {
        compilationService.removeCompilation(compId);
        log.info("DELETE /admin/compilations/{}", compId);
    }


    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @RequestBody @Valid UpdateCompilationDto updateCompilationRequest) {
        log.info("PATCH /admin/compilations/{}", compId);
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }
}

package ru.practicum.explore_with_me.compilation.service;

import ru.practicum.explore_with_me.compilation.dto.CompilationDto;
import ru.practicum.explore_with_me.compilation.dto.NewCompilationDto;
import ru.practicum.explore_with_me.compilation.dto.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {


    /**
     * Добавление новой подборки (подборка может не содержать событий)
     */
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    /**
     * Удаление подборки.
     */
    void removeCompilation(Long compId);

    /**
     * Обновить информацию о подборке
     */
    CompilationDto updateCompilation(Long compId, UpdateCompilationDto updateCompilationRequest);


    /**
     * Получение подборок событий
     */
    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    /**
     * Получение подборки событий по его id
     */
    CompilationDto getById(Long compId);
}

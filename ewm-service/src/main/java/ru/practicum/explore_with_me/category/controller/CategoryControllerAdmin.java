package ru.practicum.explore_with_me.category.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.category.dto.CategoryDto;
import ru.practicum.explore_with_me.category.dto.CategoryRequestDto;
import ru.practicum.explore_with_me.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;


@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
@Validated
@Slf4j
public class CategoryControllerAdmin {

    private final CategoryService categoryService;

    /**
     * Добавление новой категории
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryRequestDto categoryDto) {
        log.info("POST /admin/categories");
        return categoryService.createCategory(categoryDto);
    }

    /**
     * Удаление категории
     */
    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable @PositiveOrZero Long catId) {
        log.info("DELETE /compilations/{}", catId);
        categoryService.deleteCategoryById(catId);
    }

    /**
     * Изменение категорий
     */
    @PatchMapping("/{catId}")
    public CategoryDto updateCategoryById(@PathVariable @PositiveOrZero Long catId,
                                          @Valid @RequestBody CategoryRequestDto dto) {
        log.info("PATCH /compilations/{}", catId);
        return categoryService.updateCategoryById(catId, dto);
    }
}

package ru.practicum.explore_with_me.category.service;

import ru.practicum.explore_with_me.category.dto.CategoryDto;
import ru.practicum.explore_with_me.category.dto.CategoryRequestDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryRequestDto categoryDto);

    void deleteCategoryById(Long catId);

    CategoryDto updateCategoryById(Long catId, CategoryRequestDto dto);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(Long catId);
}

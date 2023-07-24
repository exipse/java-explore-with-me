package ru.practicum.explore_with_me.category.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.category.dto.CategoryDto;
import ru.practicum.explore_with_me.category.dto.CategoryRequestDto;
import ru.practicum.explore_with_me.category.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategoryModel(CategoryDto categoryDto);

    Category toCategoryModelByRequest(CategoryRequestDto categoryRequestDto);

    CategoryDto toCategoryDto(Category category);

    List<CategoryDto> toCategoryListDto(List<Category> categories);


}

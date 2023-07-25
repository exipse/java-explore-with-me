package ru.practicum.explore_with_me.category.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.category.dto.CategoryDto;
import ru.practicum.explore_with_me.category.dto.CategoryRequestDto;
import ru.practicum.explore_with_me.category.mapper.CategoryMapper;
import ru.practicum.explore_with_me.category.model.Category;
import ru.practicum.explore_with_me.category.repository.CategoryRepository;
import ru.practicum.explore_with_me.event.model.Event;
import ru.practicum.explore_with_me.event.repository.EventRepository;
import ru.practicum.explore_with_me.exception.NoFoundException;
import ru.practicum.explore_with_me.exception.ValidateDataException;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j

public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;


    @Override
    public CategoryDto createCategory(CategoryRequestDto categoryDto) {
        Category category = categoryRepository.save(categoryMapper.toCategoryModelByRequest(categoryDto));
        CategoryDto dto = categoryMapper.toCategoryDto(category);
        log.info("Категория сохранена");
        return dto;
    }


    @Override
    public void deleteCategoryById(Long catId) {
        CategoryDto dto = getCategoryById(catId);
        List<Event> evensInDb = eventRepository.findCategoryInEvents(categoryMapper.toCategoryModel(dto));
        if (evensInDb.isEmpty()) {
            categoryRepository.deleteById(catId);
        } else {
            throw new ValidateDataException("Категория содержит события");
        }
        log.info("Категория удалена");
    }

    @Override
    public CategoryDto updateCategoryById(Long catId, CategoryRequestDto dto) {
        CategoryDto categoryDto = getCategoryById(catId);
        Category updCategory = categoryMapper.toCategoryModelByRequest(dto);
        updCategory.setId(catId);
        CategoryDto updcat = categoryMapper.toCategoryDto(categoryRepository.save(updCategory));
        log.info("Категория c id = {} обновлена", catId);
        return updcat;
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(from / size, size, sortById);
        List<Category> findCategoriesInDb = categoryRepository.findAll(pageable).getContent();
        log.info("Список категорий найден. Размер списка = {} элементов", findCategoriesInDb.size());
        return categoryMapper.toCategoryListDto(findCategoriesInDb);
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NoFoundException("Категория не найдена"));
        log.info("Категория {} найдена", catId);
        return categoryMapper.toCategoryDto(category);
    }
}

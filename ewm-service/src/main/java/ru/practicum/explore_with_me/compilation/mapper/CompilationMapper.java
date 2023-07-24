package ru.practicum.explore_with_me.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore_with_me.category.mapper.CategoryMapper;
import ru.practicum.explore_with_me.compilation.dto.CompilationDto;
import ru.practicum.explore_with_me.compilation.dto.NewCompilationDto;
import ru.practicum.explore_with_me.compilation.model.Compilation;
import ru.practicum.explore_with_me.event.mapper.EventMapper;
import ru.practicum.explore_with_me.location.mapper.LocationMapper;
import ru.practicum.explore_with_me.user.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EventMapper.class, CategoryMapper.class, UserMapper.class, LocationMapper.class})
public interface CompilationMapper {

    Compilation toCompilationModel(CompilationDto compilationDto);

    CompilationDto toCompilationDto(Compilation compilation);

    List<CompilationDto> toCompilationListDto(List<Compilation> compilation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Compilation fromNewCompilationtoModel(NewCompilationDto compilationDto);

}

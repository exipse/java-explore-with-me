package ru.practicum.explore_with_me.compilation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.compilation.dto.CompilationDto;
import ru.practicum.explore_with_me.compilation.dto.NewCompilationDto;
import ru.practicum.explore_with_me.compilation.dto.UpdateCompilationDto;
import ru.practicum.explore_with_me.compilation.mapper.CompilationMapper;
import ru.practicum.explore_with_me.compilation.model.Compilation;
import ru.practicum.explore_with_me.compilation.repository.CompilationRepository;
import ru.practicum.explore_with_me.event.model.Event;
import ru.practicum.explore_with_me.event.repository.EventRepository;
import ru.practicum.explore_with_me.exception.NoFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final CompilationMapper compilationMapper;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationMapper.fromNewCompilationtoModel(newCompilationDto);
        List<Event> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events = eventRepository.findAllById(newCompilationDto.getEvents());
        }
        Set<Event> eventSet = new HashSet<>(events);
        compilation.setEvents(eventSet);
        CompilationDto dto = compilationMapper.toCompilationDto(compilationRepository.save(compilation));
        log.info("Новая подборка создана");
        return dto;
    }

    @Override
    public void removeCompilation(Long compId) {
        Compilation compilation = findEvent(compId);
        compilationRepository.deleteById(compilation.getId());
        log.info("Подборка id ={} удалена", compId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationDto updateCompilationRequest) {
        Compilation compilation = findEvent(compId);

        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null && !updateCompilationRequest.getTitle().isBlank()) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getEvents() != null && !updateCompilationRequest.getEvents().isEmpty()) {
            compilation.setEvents(new HashSet<>(eventRepository.findAllById(updateCompilationRequest.getEvents())));
        }
        CompilationDto dto = compilationMapper.toCompilationDto(compilationRepository.save(compilation));
        log.info("Подборка compID={} обновлена", compId);
        return dto;
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {

        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(from / size, size, sortById);

        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(pageable).getContent();
        } else {
            compilations = compilationRepository.findAllByPinned(pinned, pageable);
        }
        List<CompilationDto> listDto = compilationMapper.toCompilationListDto(compilations);
        log.info("Найдено {} подборок", listDto.size());
        return listDto;
    }


    @Override
    public CompilationDto getById(Long compId) {
        Compilation compilation =
                findEvent(compId);
        log.info("Подборка с id={} найдена", compId);
        return compilationMapper.toCompilationDto(compilation);
    }


    private Compilation findEvent(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> new NoFoundException("Подборка не найдена"));
    }
}

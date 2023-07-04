package ru.practicum.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilation) {
        Compilation compilation = MappingCompilation.mapToCompilation(newCompilation,
                getEventList(newCompilation.getEvents()));
        Compilation savedCompilation = compilationRepository.save(compilation);
        return MappingCompilation.mapToCompilationDto(savedCompilation, savedCompilation.getEvents());
    }

    @Override
    public void deleteCompilation(int compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(int compId, UpdateCompilationRequest updatedCompilation) {
        Compilation oldCompilation = compilationRepository.findById(compId).orElseThrow(NotFoundException::new);
        Compilation compilation = MappingCompilation.mapToCompilation(updatedCompilation,
                getEventList(updatedCompilation.getEvents()));
        if (compilation.getTitle() != null) {
            oldCompilation.setTitle(compilation.getTitle());
        }
        if (compilation.getEvents() != null) {
            oldCompilation.setEvents(compilation.getEvents());
        }
        if (compilation.getPinned() != null) {
            oldCompilation.setPinned(compilation.getPinned());
        }
        Compilation savedCompilation = compilationRepository.save(oldCompilation);
        return MappingCompilation.mapToCompilationDto(savedCompilation, savedCompilation.getEvents());
    }

    @Override
    public List<CompilationDto> getCompList(Boolean pinned, Integer from, Integer size) {
        List<CompilationDto> compDtoList = new ArrayList<>();
        List<Compilation> compList;
        if (pinned == null) {
            compList = compilationRepository.getCompList(size, from);
        } else {
            compList = compilationRepository.getCompListWithPinned(pinned, size, from);
        }
        for (Compilation comp : compList) {
            compDtoList.add(MappingCompilation.mapToCompilationDto(comp, comp.getEvents()));
        }
        return compDtoList;
    }

    @Override
    public CompilationDto getCompById(int compId) {
        Compilation savedCompilation = compilationRepository.findById(compId).orElseThrow(NotFoundException::new);
        return MappingCompilation.mapToCompilationDto(savedCompilation, savedCompilation.getEvents());
    }

    private Set<Event> getEventList(Set<Integer> eventNumList) {
        if (eventNumList == null) {
            return new HashSet<>();
        }
        List<Event> eventList = eventRepository.findAllById(eventNumList);
        return new HashSet<>(eventList);
    }
}

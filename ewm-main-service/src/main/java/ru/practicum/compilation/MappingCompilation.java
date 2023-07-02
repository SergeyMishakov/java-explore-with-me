package ru.practicum.compilation;

import ru.practicum.event.Event;
import java.util.HashSet;
import java.util.Set;

public class MappingCompilation {

    public static Compilation mapToCompilation(NewCompilationDto newCompilationDto, Set<Event> eventList) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        } else {
            compilation.setPinned(false);
        }
        if (newCompilationDto.getEvents() == null) {
            compilation.setEvents(new HashSet<>());
        }

        compilation.setEvents(eventList);
        return compilation;
    }

    public static Compilation mapToCompilation(UpdateCompilationRequest updateCompilationRequest, Set<Event> eventList) {
        Compilation compilation = new Compilation();
        compilation.setTitle(updateCompilationRequest.getTitle());
        compilation.setPinned(updateCompilationRequest.getPinned());
        if (eventList != null) {
            compilation.setEvents(eventList);
        }
        return compilation;
    }

    public static CompilationDto mapToCompilationDto(Compilation compilation, Set<Event> eventList) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setEvents(eventList);
        return compilationDto;
    }
}


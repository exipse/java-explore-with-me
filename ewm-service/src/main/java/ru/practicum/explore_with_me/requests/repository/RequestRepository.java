package ru.practicum.explore_with_me.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore_with_me.event.model.Event;
import ru.practicum.explore_with_me.requests.dto.Status;
import ru.practicum.explore_with_me.requests.model.Request;
import ru.practicum.explore_with_me.requests.model.RequestShort;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findByEventIdAndRequesterId(Long eventId, Long requesterId);

    Optional<Request> findByIdAndRequesterId(Long id, Long requesterId);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByEventId(Long eventId);

    List<Request> findAllByIdIn(List<Long> requestIds);

    List<Request> findAllByStatusAndEventId(Status status, Long eventId);

    List<RequestShort> findAllByStatusAndEventIn(Status status, List<Event> events);
}

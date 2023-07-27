package ru.practicum.explore_with_me.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore_with_me.category.model.Category;
import ru.practicum.explore_with_me.event.dto.State;
import ru.practicum.explore_with_me.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e " +
            "where (e.initiator.id in :users OR :users = null) " +
            "and (e.state in :states OR :states = null) " +
            "and (e.category.id in :categories OR :categories = null) " +
            "and (cast(:rangeStart as date) != null and cast(:rangeEnd as date) != null " +
            "and e.eventDate between cast(:rangeStart as date) and cast(:rangeEnd as date)) " +
            "or (cast(:rangeStart as date) = null and e.eventDate < cast(:rangeEnd as date)) " +
            "or (cast(:rangeStart as date) < e.eventDate and cast(:rangeEnd as date) = null)  " +
            "or (cast(:rangeStart as date) = null and cast(:rangeStart as date) = null) ")
    List<Event> findEventsByParams(@Param("users") List<Long> users,
                                   @Param("states") List<State> states,
                                   @Param("categories") List<Long> categories,
                                   @Param("rangeStart") LocalDateTime rangeStart,
                                   @Param("rangeEnd") LocalDateTime rangeEnd,
                                   Pageable pageable);


    @Query(" select e from Event e " +
            "where lower(e.annotation) like lower(concat('%', :text, '%')) " +
            "or lower(e.description) like lower(concat('%', :text, '%')) " +
            "or lower(e.title) like lower(concat('%', :text, '%'))" +
            "or :text = null " +
            "and e.category.id in :categories or :categories = null " +
            "and e.paid = :paid or :paid = null " +
            "and (cast(:rangeStart as date) != null and cast(:rangeEnd as date) != null " +
            "and e.eventDate between cast(:rangeStart as date) and cast(:rangeEnd as date) ) " +
            "or (cast(:rangeStart as date) = null and e.eventDate < cast(:rangeEnd as date) )" +
            "or (cast(:rangeEnd as date) = null and e.eventDate > cast(:rangeStart as date) )" +
            "or (cast(:rangeStart as date) = null and cast(:rangeStart as date) = null) " +
            "and :onlyAvailable = null " +
            "and  e.state = 'PUBLISHED' " +
            "order by :sortType")
    List<Event> findPublicEvents(@Param("text") String text,
                                 @Param("categories") List<Long> categories,
                                 @Param("paid") Boolean paid,
                                 @Param("rangeStart") LocalDateTime rangeStart,
                                 @Param("rangeEnd") LocalDateTime rangeEnd,
                                 @Param("onlyAvailable") Boolean onlyAvailable,
                                 @Param("sortType") String sort,
                                 Pageable pageable);

    Optional<Event> findEventByInitiatorIdAndId(Long userId, Long eventId);

    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    @Query("select e from Event as e where e.category =?1")
    List<Event> findCategoryInEvents(Category category);

}

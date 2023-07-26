package ru.practicum.explore_with_me.event.model;

import lombok.*;
import ru.practicum.explore_with_me.category.model.Category;
import ru.practicum.explore_with_me.event.dto.State;
import ru.practicum.explore_with_me.location.model.Location;
import ru.practicum.explore_with_me.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Table(name = "EVENT", schema = "public")
@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //*
    private String annotation;

    //*
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    //Количество одобренных заявок на участие в данном событии
    @Column(name = "confirmed_request", nullable = false)
    private Long confirmedRequests; //-

    //*
    //Дата и время создания события
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn; //-

    //*
    private String description;

    //*
    //Дата и время на которые намечено событие
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    //*
    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator; //-

    //*
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    //*
    //Нужно ли оплачивать участие
    private Boolean paid;

    //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    @Column(name = "participant_limit", nullable = false)
    private Long participantLimit;

    //Дата и время публикации события
    @Column(name = "published_on", nullable = false)
    private LocalDateTime publishedOn; //-

    //Нужна ли пре-модерация заявок на участие
    @Column(name = "request_moderator", nullable = false)
    private Boolean requestModeration;

    //Список состояний жизненного цикла события
    @Enumerated(EnumType.STRING)
    private State state;// -
    //*
    private String title;

    //Количество просмотрев события
    private Long views;// -
}

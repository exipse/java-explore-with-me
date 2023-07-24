package ru.practicum.explore_with_me.requests.model;

import lombok.*;
import ru.practicum.explore_with_me.event.model.Event;
import ru.practicum.explore_with_me.requests.dto.Status;
import ru.practicum.explore_with_me.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Table(name = "REQUEST", schema = "public")
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;
    @Enumerated(EnumType.STRING)
    private Status status;
}

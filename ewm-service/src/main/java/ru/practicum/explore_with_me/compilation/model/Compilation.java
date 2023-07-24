package ru.practicum.explore_with_me.compilation.model;

import lombok.*;
import ru.practicum.explore_with_me.event.model.Event;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@Table(name = "COMPILATION", schema = "public")
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean pinned;

    private String title;

    @ManyToMany
    @JoinTable(name = "COMPILATION_EVENT",
            joinColumns = @JoinColumn(name = "compilation_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "event_id ", referencedColumnName = "id")
    )
    private Set<Event> events;

}
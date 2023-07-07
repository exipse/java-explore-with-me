package ru.practicum.explore_with_me.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "HITS", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "APP")
    private String app;

    @Column(name = "URI")
    private String uri;

    @Column(name = "IP")
    private String ip;

    @Column(name = "TIMES")
    private LocalDateTime timestamp;
}


package ru.practicum.explore_with_me.location.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Table(name = "LOCATION", schema = "public")
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float lat;

    private float lon;
}

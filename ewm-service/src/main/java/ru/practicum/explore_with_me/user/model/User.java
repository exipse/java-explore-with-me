package ru.practicum.explore_with_me.user.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Table(name = "USERS", schema = "public")
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;
}

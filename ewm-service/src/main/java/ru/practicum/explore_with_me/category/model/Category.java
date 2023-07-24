package ru.practicum.explore_with_me.category.model;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Table(name = "CATEGORY", schema = "public")
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

}

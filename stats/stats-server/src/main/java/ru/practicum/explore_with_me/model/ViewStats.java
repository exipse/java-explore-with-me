package ru.practicum.explore_with_me.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class ViewStats {
    /**
     * Название сервиса
     */
    private String app;
    /**
     * URI сервиса
     */
    private String uri;
    /**
     * Количество просмотров
     */
    private long hits;

}
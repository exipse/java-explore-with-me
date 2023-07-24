package ru.practicum.explore_with_me.requests.service;

import ru.practicum.explore_with_me.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     */
    List<ParticipationRequestDto> getByUserId(Long userId);

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     */
    ParticipationRequestDto create(Long userId, Long eventId);

    /**
     * Отмена своего запроса на участие в событии
     */
    ParticipationRequestDto update(Long userId, Long requestsId);
}

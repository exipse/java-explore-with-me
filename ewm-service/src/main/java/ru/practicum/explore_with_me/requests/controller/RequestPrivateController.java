package ru.practicum.explore_with_me.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.requests.dto.ParticipationRequestDto;
import ru.practicum.explore_with_me.requests.service.RequestService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class RequestPrivateController {

    private final RequestService requestService;

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     */
    @GetMapping
    public List<ParticipationRequestDto> getByUserId(@PathVariable Long userId) {
        log.info("GET /users/{userId}/requests");
        return requestService.getByUserId(userId);
    }

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("POST /users/{userId}/requests");
        return requestService.create(userId, eventId);
    }

    /**
     * Отмена своего запроса на участие в событии
     */
    @PatchMapping("/{requestsId}/cancel")
    public ParticipationRequestDto update(@PathVariable Long userId, @PathVariable Long requestsId) {
        log.info("PATCH /users/{userId}/requests/{}/cancel", requestsId);
        return requestService.update(userId, requestsId);
    }

}

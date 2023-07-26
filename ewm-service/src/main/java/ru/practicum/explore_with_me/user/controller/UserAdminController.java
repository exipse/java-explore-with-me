package ru.practicum.explore_with_me.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.user.dto.UserDto;
import ru.practicum.explore_with_me.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/admin/users")
@Slf4j
public class UserAdminController {

    private UserService userService;

    /**
     * Получение информации о пользователях
     */
    @GetMapping
    public List<UserDto> findAll(@RequestParam(required = false) List<Long> ids,
                                 @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                 @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET /admin/users");
        return userService.findAllUsers(ids, from, size);
    }

    /**
     * Добавление нового пользователя.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.info("POST /admin/users");
        return userService.addUser(userDto);
    }

    /**
     * Удаление пользователя.
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long userId) {
        log.info("DELETE /admin/users/{userId}");
        userService.delete(userId);
    }
}

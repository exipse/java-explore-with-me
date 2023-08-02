package ru.practicum.explore_with_me.user.service;

import ru.practicum.explore_with_me.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAllUsers(List<Long> ids, int from, int size);

    UserDto addUser(UserDto userDto);

    void delete(Long userId);
}
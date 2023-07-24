package ru.practicum.explore_with_me.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.exception.NoFoundException;
import ru.practicum.explore_with_me.user.dto.UserDto;
import ru.practicum.explore_with_me.user.mapper.UserMapper;
import ru.practicum.explore_with_me.user.model.User;
import ru.practicum.explore_with_me.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;


    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers(List<Long> ids, int from, int size) {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(from / size, size, sortById);
        List<User> findUsersInDb;

        if (ids == null || ids.isEmpty()) {
            findUsersInDb = userRepository.findAll(pageable).getContent();
        } else {
            findUsersInDb = userRepository.findAllByIdIn(ids, pageable).getContent();
        }
        log.info("Список пользователей найден. Размер списка = {} элементов", findUsersInDb.size());

        return userMapper.toUserListDto(findUsersInDb);
    }


    @Override
    public UserDto addUser(UserDto userDto) {
        User saveUser = userRepository.save(userMapper.toUserModel(userDto));
        log.info("Пользователь создан");
        return userMapper.toUserDto(saveUser);
    }

    @Override
    public void delete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoFoundException("Пользователь не найден"));
        userRepository.delete(user);
    }
}

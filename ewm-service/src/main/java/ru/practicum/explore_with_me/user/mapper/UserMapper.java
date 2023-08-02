package ru.practicum.explore_with_me.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.user.dto.UserDto;
import ru.practicum.explore_with_me.user.dto.UserShortDto;
import ru.practicum.explore_with_me.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUserModel(UserDto userDto);

    UserDto toUserDto(User userModel);

    UserShortDto toUserShortDto(User userModel);

    List<UserDto> toUserListDto(List<User> userModelList);

}

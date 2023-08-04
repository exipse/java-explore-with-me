package ru.practicum.explore_with_me.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore_with_me.comment.dto.CommentDto;
import ru.practicum.explore_with_me.comment.dto.NewCommentDto;
import ru.practicum.explore_with_me.comment.model.Comment;
import ru.practicum.explore_with_me.event.mapper.EventMapper;
import ru.practicum.explore_with_me.event.model.Event;
import ru.practicum.explore_with_me.user.mapper.UserMapper;
import ru.practicum.explore_with_me.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EventMapper.class, UserMapper.class})
public interface CommentMapper {

    @Mapping(target = "user", source = "user.id")
    @Mapping(target = "event", source = "event.id")
    CommentDto toCommentDto(Comment comment);

    @Mapping(target = "event", source = "eventMod")
    @Mapping(target = "user", source = "userMod")
    @Mapping(target = "id", ignore = true)
    Comment toCommentModel(NewCommentDto newCommentDto, Event eventMod, User userMod);

    List<CommentDto> toCommentDtoList(List<Comment> comment);
}

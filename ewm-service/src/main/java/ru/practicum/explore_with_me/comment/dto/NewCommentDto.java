package ru.practicum.explore_with_me.comment.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {

    @NotNull
    @Length(max = 2000)
    private String text;

    @NotNull
    private Long eventId;
}

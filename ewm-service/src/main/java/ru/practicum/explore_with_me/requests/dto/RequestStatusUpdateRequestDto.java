package ru.practicum.explore_with_me.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestStatusUpdateRequestDto {

    private List<Long> requestIds;
    private Status status;
}

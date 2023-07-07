package ru.practicum.explore_with_me.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndpointHitDto {
    /**
     * Идентификатор сервиса для которого записывается информация
     */
    @NotBlank
    private String app;
    /**
     * URI для которого был осуществлен запрос
     */
    @NotBlank
    private String uri;
    /**
     * IP-адрес пользователя, осуществившего запрос
     */
    @NotBlank
    private String ip;
    /**
     * Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
     */

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}

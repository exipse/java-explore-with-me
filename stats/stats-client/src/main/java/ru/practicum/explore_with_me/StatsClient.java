package ru.practicum.explore_with_me;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explore_with_me.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(EndpointHitDto endpointHitDto) {
        return post("/hit", endpointHitDto);
    }


    public ResponseEntity<Object> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        Map<String, Object> parameters = Map.of("start", start,
                "end", end,
                "uris", uris,
                "unique", unique);

        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}
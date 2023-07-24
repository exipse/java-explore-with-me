package ru.practicum.explore_with_me;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explore_with_me.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        StringBuilder urisBuilder = new StringBuilder();
        for (int i = 0; i < uris.size(); i++) {
            if (i < (uris.size() - 1)) {
                urisBuilder.append("uris=").append(uris.get(i)).append("&");
            } else {
                urisBuilder.append("uris=").append(uris.get(i));
            }
        }
        Map<String, Object> parameters = Map.of("start", start.format(formatter),
                "end", end.format(formatter),
                "uris", urisBuilder.toString(),
                "unique", unique);

        ResponseEntity<Object> result =  get("/stats?start={start}&end={end}&{uris}&unique={unique}", parameters);

        return result;
    }
}

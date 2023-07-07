package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.model.EndpointHit;
import ru.practicum.explore_with_me.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.explore_with_me.model.ViewStats(eh.app, eh.uri, count(distinct eh.ip)) " +
            "from EndpointHit as eh " +
            "where eh.timestamp between ?1 and ?2 " +
            "and (eh.uri in ?3 or ?3 = null) " +
            "group by eh.app, eh.uri " +
            "order by count(distinct eh.ip) desc")
    List<ViewStats> getUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.explore_with_me.model.ViewStats(eh.app, eh.uri, count(eh.ip)) " +
            "from EndpointHit as eh " +
            "where eh.timestamp between ?1 and ?2 " +
            "and (eh.uri in ?3 or ?3 = null) " +
            "group by eh.app, eh.uri " +
            "order by count(eh.ip) desc")
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);

}

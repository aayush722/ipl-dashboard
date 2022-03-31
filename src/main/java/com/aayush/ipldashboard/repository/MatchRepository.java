package com.aayush.ipldashboard.repository;

import com.aayush.ipldashboard.model.Match;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MatchRepository extends CrudRepository<Match, Long> {

    List<Match> getByTeam1OrTeam2OrderByDateDesc(String teamName1, String teamName2, Pageable page);

    @Query("Select m from Match m where (m.team1 = :team or m.team2 = :team) and date Between :startDate and :endDate order by date desc")
    List<Match> getMatchByTeamBetweenDate(
            @Param("team") String team,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    default List<Match> findLatestMatchesByTeam(String teamName, int count) {
        return getByTeam1OrTeam2OrderByDateDesc(teamName, teamName, PageRequest.of(0, count));
    }
}

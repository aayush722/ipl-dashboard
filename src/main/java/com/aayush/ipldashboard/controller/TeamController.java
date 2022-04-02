package com.aayush.ipldashboard.controller;

import com.aayush.ipldashboard.model.Match;
import com.aayush.ipldashboard.model.Team;
import com.aayush.ipldashboard.repository.MatchRepository;
import com.aayush.ipldashboard.repository.TeamRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class TeamController {

    private TeamRepository teamRepository;
    private MatchRepository matchRepository;

    public TeamController(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName) {
        Team team = teamRepository.findByTeamName(teamName);
        team.setMatches(matchRepository.findLatestMatchesByTeam(teamName, 4));
        return team;
    }

    @GetMapping("/team")
    public Iterable<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @GetMapping("/team/{teamName}/matches")
    public List<Match> getMatchByYear(@PathVariable String teamName, @RequestParam int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year + 1, 1, 1);
        return matchRepository.getMatchByTeamBetweenDate(teamName, startDate, endDate);
    }
}

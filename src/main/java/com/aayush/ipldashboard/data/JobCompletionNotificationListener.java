package com.aayush.ipldashboard.data;

import com.aayush.ipldashboard.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final EntityManager em;

    @Autowired
    public JobCompletionNotificationListener(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            Map<String, Team> teamData = new HashMap<>();

            em.createQuery("select m.team1 , count(*) from Match m group by m.team1", Object[].class)
                    .getResultList()
                    .stream()
                    .map(obj -> new Team((String) obj[0], (long) obj[1]))
                    .forEach(team -> teamData.put(team.getTeamName(), team));

            em.createQuery("select m.team2 , count(*) from Match m group by m.team2", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(obj -> {
                        Team team = teamData.get((String) obj[0]);
                        if (team != null)
                        team.setTotalMatches(team.getTotalMatches() + (long) obj[1]);
                    });

            em.createQuery("select m.matchWinner, count(*) from Match m group by m.matchWinner", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(obj -> {
                        Team team = teamData.get((String) obj[0]);
                        if (team != null)
                            team.setTotalWins((long) obj[1]);
                    });

            teamData.values().forEach(team -> em.persist(team));
            teamData.values().forEach(team -> log.info(team.toString()));
        }
    }
}
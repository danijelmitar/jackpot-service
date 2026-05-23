package com.example.jackpotservice.jackpot;

import com.example.jackpotservice.jackpot.application.ContributeToJackpotUseCase;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotContributionJpaRepository;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotJpaRepository;
import com.example.jackpotservice.jackpot.infrastructure.messaging.JackpotBetPlacedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProcessJackpotBetPlacedEventE2EShould {

    @Autowired
    private ContributeToJackpotUseCase contributeToJackpotUseCase;

    @Autowired
    private JackpotJpaRepository jackpotJpaRepository;

    @Autowired
    private JackpotContributionJpaRepository jackpotContributionJpaRepository;

    @Test
    void persist_jackpot_contribution_when_bet_placed_event_received() {
        var event = new JackpotBetPlacedEvent("bet-1", "user-1", "jackpot-fixed", new BigDecimal("10.00"));
        var poolBefore = jackpotJpaRepository.findById("jackpot-fixed")
                .orElseThrow().getPoolAmount();

        contributeToJackpotUseCase.execute(event.betId(), event.userId(), event.jackpotId(), event.betAmount());

        var contribution = jackpotContributionJpaRepository.findByIdBetId("bet-1");
        assertThat(contribution).isPresent();
        assertThat(contribution.get().getId().getBetId()).isEqualTo("bet-1");
        assertThat(contribution.get().getId().getJackpotId()).isEqualTo("jackpot-fixed");
        assertThat(contribution.get().getStakeAmount()).isEqualByComparingTo("10.00");

        var poolAfter = jackpotJpaRepository.findById("jackpot-fixed")
                .orElseThrow().getPoolAmount();
        assertThat(poolAfter).isGreaterThan(poolBefore);
    }

    @Test
    void discard_event_when_jackpot_does_not_exist() {
        var event = new JackpotBetPlacedEvent("bet-3", "user-1", "jackpot-unknown", new BigDecimal("10.00"));

        contributeToJackpotUseCase.execute(event.betId(), event.userId(), event.jackpotId(), event.betAmount());

        assertThat(jackpotContributionJpaRepository.findByIdBetId("bet-3")).isEmpty();
    }
}

package com.example.jackpotservice.jackpot;

import com.example.jackpotservice.common.messaging.BetPlacedMessage;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotContributionJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class ProcessBetPlacedEventE2EShould {

    @Autowired
    private KafkaTemplate<String, BetPlacedMessage> kafkaTemplate;

    @Autowired
    private JackpotContributionJpaRepository jackpotContributionJpaRepository;

    @Test
    void persist_jackpot_contribution_when_bet_placed_event_received_via_kafka() {
        var betId = UUID.randomUUID().toString();
        var event = new BetPlacedMessage(betId, "user-1", "jackpot-fixed", new BigDecimal("10.00"));

        kafkaTemplate.send("jackpot-bets", "jackpot-fixed", event);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            var contribution = jackpotContributionJpaRepository.findByIdBetId(betId);
            assertThat(contribution).isPresent();
            assertThat(contribution.get().getId().getBetId()).isEqualTo(betId);
            assertThat(contribution.get().getId().getJackpotId()).isEqualTo("jackpot-fixed");
            assertThat(contribution.get().getStakeAmount()).isEqualByComparingTo("10.00");
        });
    }

    @Test
    void discard_event_when_jackpot_does_not_exist() {
        var betId = UUID.randomUUID().toString();
        var event = new BetPlacedMessage(betId, "user-1", "jackpot-unknown", new BigDecimal("10.00"));

        kafkaTemplate.send("jackpot-bets", "jackpot-unknown", event);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() ->
                assertThat(jackpotContributionJpaRepository.findByIdBetId(betId)).isEmpty()
        );
    }
}

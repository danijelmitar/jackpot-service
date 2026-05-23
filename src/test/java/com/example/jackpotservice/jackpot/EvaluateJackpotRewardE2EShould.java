package com.example.jackpotservice.jackpot;

import com.example.jackpotservice.jackpot.application.ContributeToJackpotUseCase;
import com.example.jackpotservice.jackpot.api.JackpotRewardResponse;
import com.example.jackpotservice.jackpot.infrastructure.messaging.JackpotBetPlacedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class EvaluateJackpotRewardE2EShould {

    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    private ContributeToJackpotUseCase contributeToJackpotUseCase;

    @Test
    void return_reward_response_when_bet_has_been_contributed() {
        var event = new JackpotBetPlacedEvent("bet-e2e-1", "user-1", "jackpot-fixed", new BigDecimal("10.00"));
        contributeToJackpotUseCase.execute(event.betId(), event.userId(), event.jackpotId(), event.betAmount());

        var response = restTestClient.post()
                .uri("/api/v1/jackpots/bets/bet-e2e-1/evaluate")
                .exchange()
                .expectStatus().isOk()
                .returnResult(JackpotRewardResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.betId()).isEqualTo("bet-e2e-1");
        assertThat(response.userId()).isEqualTo("user-1");
        assertThat(response.jackpotId()).isEqualTo("jackpot-fixed");
        assertThat(response.rewardAmount()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    }

    @Test
    void return_404_when_bet_has_not_been_contributed() {
        restTestClient.post()
                .uri("/api/v1/jackpots/bets/bet-nonexistent/evaluate")
                .exchange()
                .expectStatus().isNotFound();
    }
}

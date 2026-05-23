package com.example.jackpotservice.bet;

import com.example.jackpotservice.bet.api.BetRequest;
import com.example.jackpotservice.bet.api.BetResponse;
import com.example.jackpotservice.bet.infrastructure.BetJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class PlaceBetE2EShould {

    @Autowired
    private RestTestClient restClient;

    @Autowired
    private BetJpaRepository betJpaRepository;

    @Test
    void accept_bet_and_persist_it() {
        var request = new BetRequest("user-1", "jackpot-1", new BigDecimal("10.00"));

        var result = restClient.post()
                .uri("/api/v1/bets")
                .body(request)
                .exchange()
                .expectStatus().isAccepted()
                .returnResult(BetResponse.class);

        assertThat(result.getResponseBody()).isNotNull();
        var betId = result.getResponseBody().betId();
        assertThat(betId).isNotNull().isNotEmpty();

        var savedBet = betJpaRepository.findById(betId);
        assertThat(savedBet).isPresent();
        assertThat(savedBet.get().getUserId()).isEqualTo("user-1");
        assertThat(savedBet.get().getJackpotId()).isEqualTo("jackpot-1");
        assertThat(savedBet.get().getBetAmount()).isEqualByComparingTo("10.00");
    }

    @Test
    void reject_bet_when_amount_is_zero() {
        var request = new BetRequest("user-1", "jackpot-1", BigDecimal.ZERO);

        restClient.post()
                .uri("/api/v1/bets")
                .body(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void reject_bet_when_amount_is_negative() {
        var request = new BetRequest("user-1", "jackpot-1", new BigDecimal("-10.00"));

        restClient.post()
                .uri("/api/v1/bets")
                .body(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void reject_bet_when_amount_is_missing() {
        var request = new BetRequest("user-1", "jackpot-1", null);

        restClient.post()
                .uri("/api/v1/bets")
                .body(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void reject_bet_when_user_id_is_missing() {
        var request = new BetRequest(null, "jackpot-1", new BigDecimal("10.00"));

        restClient.post()
                .uri("/api/v1/bets")
                .body(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void reject_bet_when_jackpot_id_is_missing() {
        var request = new BetRequest("user-1", null, new BigDecimal("10.00"));

        restClient.post()
                .uri("/api/v1/bets")
                .body(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void generate_unique_bet_id_for_each_placed_bet() {
        var request = new BetRequest("user-1", "jackpot-1", new BigDecimal("10.00"));

        var first = restClient.post()
                .uri("/api/v1/bets")
                .body(request)
                .exchange()
                .expectStatus().isAccepted()
                .returnResult(BetResponse.class);

        var second = restClient.post()
                .uri("/api/v1/bets")
                .body(request)
                .exchange()
                .expectStatus().isAccepted()
                .returnResult(BetResponse.class);

        assertThat(first.getResponseBody()).isNotNull();
        assertThat(second.getResponseBody()).isNotNull();
        assertThat(first.getResponseBody().betId())
                .isNotEqualTo(second.getResponseBody().betId());
    }
}

package com.example.jackpotservice.jackpot.api;

import com.example.jackpotservice.jackpot.application.EvaluateJackpotRewardUseCase;
import com.example.jackpotservice.jackpot.domain.JackpotReward;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JackpotController.class)
class JackpotControllerShould {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EvaluateJackpotRewardUseCase evaluateJackpotRewardUseCase;

    @Test
    void return_200_with_reward_amount_when_bet_wins() throws Exception {
        when(evaluateJackpotRewardUseCase.execute("bet-1"))
                .thenReturn(Optional.of(JackpotReward.builder()
                        .betId("bet-1")
                        .userId("user-1")
                        .jackpotId("jackpot-1")
                        .rewardAmount(new BigDecimal("1000.00"))
                        .build()
                ));

        mockMvc.perform(post("/api/v1/jackpots/bets/bet-1/evaluate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.betId").value("bet-1"))
                .andExpect(jsonPath("$.jackpotId").value("jackpot-1"))
                .andExpect(jsonPath("$.rewardAmount").value(1000.00));
    }

    @Test
    void return_200_with_zero_reward_when_bet_does_not_win() throws Exception {
        when(evaluateJackpotRewardUseCase.execute("bet-1"))
                .thenReturn(Optional.of(JackpotReward.builder()
                        .betId("bet-1")
                        .userId("user-1")
                        .jackpotId("jackpot-1")
                        .rewardAmount(BigDecimal.ZERO)
                        .build()
        ));

        mockMvc.perform(post("/api/v1/jackpots/bets/bet-1/evaluate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.betId").value("bet-1"))
                .andExpect(jsonPath("$.rewardAmount").value(0));
    }

    @Test
    void return_404_when_bet_not_found() throws Exception {
        when(evaluateJackpotRewardUseCase.execute("bet-1"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/jackpots/bets/bet-1/evaluate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

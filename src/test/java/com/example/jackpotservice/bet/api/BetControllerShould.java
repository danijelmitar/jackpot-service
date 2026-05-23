package com.example.jackpotservice.bet.api;

import com.example.jackpotservice.bet.application.PlaceBetUseCase;
import com.example.jackpotservice.bet.domain.Bet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BetController.class)
class BetControllerShould {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PlaceBetUseCase placeBetUseCase;

    @Test
    void place_bet_valid_request_returns_202() throws Exception {
        var userId = "user-007";
        var jackpotId = "jackpot-001";
        var amount = new BigDecimal("25");
        var betRequest = new BetRequest(userId, jackpotId, amount);

        when(placeBetUseCase.execute(userId, jackpotId, amount)).thenReturn(Bet.place(userId, jackpotId, amount));

        mockMvc.perform(
                        post("/api/v1/bets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(betRequest))
                ).andExpect(status().isAccepted())
                .andExpect(jsonPath("$.betId").isNotEmpty());

        verify(placeBetUseCase).execute(userId, jackpotId, amount);
    }

    @Test
    void place_bet_missing_user_id_returns_400() throws Exception {
        var betRequest = new BetRequest(null, "jackpot-001", new BigDecimal("25"));

        mockMvc.perform(
                post("/api/v1/bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(betRequest))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void place_bet_missing_jackpot_id_returns_400() throws Exception {
        var betRequest = new BetRequest("user-007", null, new BigDecimal("25"));

        mockMvc.perform(
                post("/api/v1/bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(betRequest))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void place_bet_missing_bet_amount_returns_400() throws Exception {
        var betRequest = new BetRequest("user-007", "jackpot-001", null);

        mockMvc.perform(
                post("/api/v1/bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(betRequest))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void place_bet_zero_bet_amount_returns_400() throws Exception {
        var betRequest = new BetRequest("user-007", "jackpot-001", new BigDecimal("0"));

        mockMvc.perform(
                post("/api/v1/bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(betRequest))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void place_bet_negative_bet_amount_returns_400() throws Exception {
        var betRequest = new BetRequest("user-007", "jackpot-001", new BigDecimal("-1"));

        mockMvc.perform(
                post("/api/v1/bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(betRequest))
        ).andExpect(status().isBadRequest());
    }
}

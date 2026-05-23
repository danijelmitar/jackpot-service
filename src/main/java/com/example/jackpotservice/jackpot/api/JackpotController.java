package com.example.jackpotservice.jackpot.api;

import com.example.jackpotservice.jackpot.application.EvaluateJackpotRewardUseCase;
import com.example.jackpotservice.jackpot.domain.JackpotReward;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jackpots")
@RequiredArgsConstructor
public class JackpotController {

    private final EvaluateJackpotRewardUseCase evaluateJackpotRewardUseCase;

    @PostMapping("/bets/{betId}/evaluate")
    public ResponseEntity<JackpotRewardResponse> evaluate(@PathVariable String betId) {
        return evaluateJackpotRewardUseCase.execute(betId)
                .map(reward -> ResponseEntity.ok(toResponse(reward)))
                .orElse(ResponseEntity.notFound().build());
    }

    private JackpotRewardResponse toResponse(JackpotReward reward) {
        return new JackpotRewardResponse(
                reward.getBetId(),
                reward.getUserId(),
                reward.getJackpotId(),
                reward.getRewardAmount()
        );
    }
}

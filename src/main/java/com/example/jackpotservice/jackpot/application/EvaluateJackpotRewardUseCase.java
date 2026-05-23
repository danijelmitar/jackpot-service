package com.example.jackpotservice.jackpot.application;

import com.example.jackpotservice.jackpot.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EvaluateJackpotRewardUseCase {

    private final JackpotContributionRepository jackpotContributionRepository;
    private final JackpotRepository jackpotRepository;
    private final JackpotRewardRepository jackpotRewardRepository;

    @Transactional
    public Optional<JackpotReward> execute(String betId) {
        var contribution = jackpotContributionRepository.findByBetId(betId);
        if (contribution.isEmpty()) {
            return Optional.empty();
        }

        var jackpot = jackpotRepository.findById(contribution.get().getJackpotId())
                .orElseThrow(() -> new IllegalStateException("Jackpot not found: " + contribution.get().getJackpotId()));

        var reward = jackpot.evaluate(contribution.get());
        jackpotRepository.save(jackpot);
        jackpotRewardRepository.save(reward);

        return Optional.of(reward);
    }
}

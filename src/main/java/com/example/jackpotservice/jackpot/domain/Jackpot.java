package com.example.jackpotservice.jackpot.domain;

import com.example.jackpotservice.jackpot.domain.contribution.ContributionStrategy;
import com.example.jackpotservice.jackpot.domain.reward.RewardStrategy;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public final class Jackpot {

    private final String id;
    private final BigDecimal initialPoolAmount;
    private final ContributionStrategy contributionStrategy;
    private final RewardStrategy rewardStrategy;
    private BigDecimal poolAmount;

    private Jackpot(String id, BigDecimal initialPoolAmount, BigDecimal poolAmount,
                    ContributionStrategy contributionStrategy, RewardStrategy rewardStrategy) {
        Objects.requireNonNull(id, "jackpotId is required");
        Objects.requireNonNull(initialPoolAmount, "initialPoolAmount is required");
        Objects.requireNonNull(contributionStrategy, "contributionStrategy is required");
        Objects.requireNonNull(rewardStrategy, "rewardStrategy is required");
        this.id = id;
        this.initialPoolAmount = initialPoolAmount;
        this.poolAmount = poolAmount;
        this.contributionStrategy = contributionStrategy;
        this.rewardStrategy = rewardStrategy;
    }

    public static Jackpot reconstitute(
            String jackpotId,
            BigDecimal initialPoolAmount,
            BigDecimal poolAmount,
            ContributionStrategy contributionStrategy,
            RewardStrategy rewardStrategy
    ) {
        return new Jackpot(
                jackpotId,
                initialPoolAmount,
                poolAmount,
                contributionStrategy,
                rewardStrategy
        );
    }

    public JackpotContribution contribute(String betId, String userId, BigDecimal betAmount) {
        Objects.requireNonNull(betId, "betId is required");
        Objects.requireNonNull(userId, "userId is required");
        Objects.requireNonNull(betAmount, "betAmount is required");
        var contributionAmount = contributionStrategy.calculate(betAmount, poolAmount);
        this.poolAmount = poolAmount.add(contributionAmount);
        return JackpotContribution.builder()
                .betId(betId)
                .userId(userId)
                .jackpotId(id)
                .stakeAmount(betAmount)
                .contributionAmount(contributionAmount)
                .currentJackpotAmount(poolAmount)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public JackpotReward evaluate(JackpotContribution contribution) {
        Objects.requireNonNull(contribution, "contribution is required");
        var won = rewardStrategy.evaluate(poolAmount);
        var rewardAmount = BigDecimal.ZERO;
        if (won) {
            rewardAmount = poolAmount;
            this.poolAmount = initialPoolAmount;
        }
        return JackpotReward.builder()
                .betId(contribution.getBetId())
                .userId(contribution.getUserId())
                .jackpotId(id)
                .rewardAmount(rewardAmount)
                .createdAt(LocalDateTime.now())
                .build();
    }
}

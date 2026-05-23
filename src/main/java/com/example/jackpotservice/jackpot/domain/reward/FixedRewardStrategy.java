package com.example.jackpotservice.jackpot.domain.reward;

import java.math.BigDecimal;
import java.util.random.RandomGenerator;

public final class FixedRewardStrategy implements RewardStrategy {

    private final BigDecimal chance = new BigDecimal("0.10");

    @Override
    public boolean evaluate(BigDecimal poolAmount) {
        if (chance.compareTo(BigDecimal.ZERO) == 0) return false;
        if (chance.compareTo(BigDecimal.ONE) == 0) return true;
        return RandomGenerator.getDefault().nextDouble() < chance.doubleValue();
    }

    @Override
    public StrategyType type() {
        return StrategyType.FIXED;
    }
}

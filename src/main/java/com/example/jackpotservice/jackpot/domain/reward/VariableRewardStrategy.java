package com.example.jackpotservice.jackpot.domain.reward;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.random.RandomGenerator;

public final class VariableRewardStrategy implements RewardStrategy {

    private final BigDecimal initialChance = new BigDecimal("0.01");
    private final BigDecimal growthRate = new BigDecimal("0.001");
    private final BigDecimal threshold = new BigDecimal("1000.00");

    @Override
    public boolean evaluate(BigDecimal poolAmount) {
        if (poolAmount.compareTo(threshold) >= 0) return true;
        var effectiveChance = initialChance
                .add(poolAmount.multiply(growthRate))
                .min(BigDecimal.ONE)
                .setScale(4, RoundingMode.HALF_UP);
        return RandomGenerator.getDefault().nextDouble() < effectiveChance.doubleValue();
    }

    @Override
    public StrategyType type() {
        return StrategyType.VARIABLE;
    }
}

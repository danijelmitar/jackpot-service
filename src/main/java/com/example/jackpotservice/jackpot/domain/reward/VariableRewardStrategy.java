package com.example.jackpotservice.jackpot.domain.reward;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.random.RandomGenerator;

@Slf4j
public final class VariableRewardStrategy implements RewardStrategy {

    private final BigDecimal threshold = new BigDecimal("2000.00");

    @Override
    public boolean evaluate(BigDecimal currentPoolAmount, BigDecimal initialPoolAmount) {
        if (currentPoolAmount.compareTo(threshold) >= 0) {
            log.info("Jackpot pool exceeds threshold, always winning");
            return true;
        }

        var range = threshold.subtract(initialPoolAmount);
        var progress = currentPoolAmount.subtract(initialPoolAmount);
        var chance = progress.divide(range, 4, RoundingMode.HALF_UP).max(BigDecimal.ZERO);
        log.info("Effective chance for variable reward jackpot: {}", chance);
        return RandomGenerator.getDefault().nextDouble() < chance.doubleValue();
    }

    @Override
    public StrategyType type() {
        return StrategyType.VARIABLE;
    }
}

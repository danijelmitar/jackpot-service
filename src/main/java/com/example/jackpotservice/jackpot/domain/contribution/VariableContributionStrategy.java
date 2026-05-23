package com.example.jackpotservice.jackpot.domain.contribution;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class VariableContributionStrategy implements ContributionStrategy {

    private final BigDecimal initialRate = new BigDecimal("0.20");
    private final BigDecimal decayRate = new BigDecimal("0.0001");
    private final BigDecimal minimumRate = new BigDecimal("0.05");

    @Override
    public BigDecimal calculate(BigDecimal betAmount, BigDecimal currentPoolAmount) {
        var decayedRate = initialRate.subtract(currentPoolAmount.multiply(decayRate));
        var effectiveRate = decayedRate.max(minimumRate);
        return betAmount.multiply(effectiveRate).setScale(4, RoundingMode.HALF_UP);
    }

    @Override
    public StrategyType type() {
        return StrategyType.VARIABLE;
    }
}

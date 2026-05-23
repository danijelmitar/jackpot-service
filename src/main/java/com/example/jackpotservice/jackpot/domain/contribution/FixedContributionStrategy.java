package com.example.jackpotservice.jackpot.domain.contribution;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class FixedContributionStrategy implements ContributionStrategy {

    private final BigDecimal percentage = new BigDecimal("0.10");

    @Override
    public BigDecimal calculate(BigDecimal betAmount, BigDecimal currentPoolAmount) {
        return betAmount.multiply(percentage).setScale(4, RoundingMode.HALF_UP);
    }

    @Override
    public StrategyType type() {
        return StrategyType.FIXED;
    }
}

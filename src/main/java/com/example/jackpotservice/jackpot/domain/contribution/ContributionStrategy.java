package com.example.jackpotservice.jackpot.domain.contribution;

import java.math.BigDecimal;

public interface ContributionStrategy {
    BigDecimal calculate(BigDecimal betAmount, BigDecimal currentPoolAmount);
    StrategyType type();

    enum StrategyType {
        FIXED,
        VARIABLE
    }
}

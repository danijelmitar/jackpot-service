package com.example.jackpotservice.jackpot.domain.reward;

import java.math.BigDecimal;

public interface RewardStrategy {
    boolean evaluate(BigDecimal currentPoolAmount, BigDecimal initialPoolAmount);
    StrategyType type();

    enum StrategyType {
        FIXED,
        VARIABLE
    }
}

package com.example.jackpotservice.jackpot.domain.reward;

import java.math.BigDecimal;

public interface RewardStrategy {
    boolean evaluate(BigDecimal poolAmount);
    StrategyType type();

    enum StrategyType {
        FIXED,
        VARIABLE
    }
}

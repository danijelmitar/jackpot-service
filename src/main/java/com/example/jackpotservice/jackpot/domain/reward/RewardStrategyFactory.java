package com.example.jackpotservice.jackpot.domain.reward;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RewardStrategyFactory {
    public static RewardStrategy create(RewardStrategy.StrategyType type) {
        return switch (type) {
            case FIXED -> new FixedRewardStrategy();
            case VARIABLE -> new VariableRewardStrategy();
        };
    }
}

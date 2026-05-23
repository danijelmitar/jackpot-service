package com.example.jackpotservice.jackpot.domain.contribution;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ContributionStrategyFactory {
    public static ContributionStrategy create(ContributionStrategy.StrategyType type) {
        return switch (type) {
            case FIXED -> new FixedContributionStrategy();
            case VARIABLE -> new VariableContributionStrategy();
        };
    }
}

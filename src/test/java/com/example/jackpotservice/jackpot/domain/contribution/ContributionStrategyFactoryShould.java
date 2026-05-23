package com.example.jackpotservice.jackpot.domain.contribution;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContributionStrategyFactoryShould {

    @Test
    void create_fixed_contribution_strategy() {
        var strategy = ContributionStrategyFactory.create(ContributionStrategy.StrategyType.FIXED);

        assertThat(strategy).isInstanceOf(FixedContributionStrategy.class);
    }

    @Test
    void create_variable_contribution_strategy() {
        var strategy = ContributionStrategyFactory.create(ContributionStrategy.StrategyType.VARIABLE);

        assertThat(strategy).isInstanceOf(VariableContributionStrategy.class);
    }
}

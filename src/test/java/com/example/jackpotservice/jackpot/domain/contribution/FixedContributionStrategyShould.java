package com.example.jackpotservice.jackpot.domain.contribution;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class FixedContributionStrategyShould {

    @Test
    void calculate_fixed_percentage_of_bet_amount() {
        var strategy = new FixedContributionStrategy();

        var contribution = strategy.calculate(new BigDecimal("100.00"), new BigDecimal("1000.00"));

        assertThat(contribution).isEqualByComparingTo("10.00");
    }

    @Test
    void return_same_contribution_regardless_of_pool_amount() {
        var strategy = new FixedContributionStrategy();

        var firstContribution = strategy.calculate(new BigDecimal("100.00"), new BigDecimal("1000.00"));
        var secondContribution = strategy.calculate(new BigDecimal("100.00"), new BigDecimal("5000.00"));

        assertThat(firstContribution).isEqualByComparingTo(secondContribution);
    }

    @Test
    void return_correct_strategy_type() {
        var strategy = new FixedContributionStrategy();

        var type = strategy.type();

        assertThat(type).isEqualTo(ContributionStrategy.StrategyType.FIXED);
    }
}

package com.example.jackpotservice.jackpot.domain.contribution;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class VariableContributionStrategyShould {

    @Test
    void calculate_contribution_based_on_initial_rate_when_pool_is_zero() {
        var strategy = new VariableContributionStrategy();

        var contribution = strategy.calculate(new BigDecimal("100.00"), new BigDecimal("0.00"));

        // rate = 0.20 - (0.00 * 0.0001) = 0.20 → contribution = 100 * 0.20 = 20.00
        assertThat(contribution).isEqualByComparingTo("20.00");
    }

    @Test
    void decrease_contribution_rate_as_pool_increases() {
        var strategy = new VariableContributionStrategy();

        var lowPoolContribution = strategy.calculate(new BigDecimal("100.00"), new BigDecimal("100.00"));
        var highPoolContribution = strategy.calculate(new BigDecimal("100.00"), new BigDecimal("1000.00"));

        assertThat(lowPoolContribution).isGreaterThan(highPoolContribution);
    }

    @Test
    void never_go_below_minimum_rate() {
        var strategy = new VariableContributionStrategy();

        // pool is very large — rate should floor at minimumRate (5%)
        var contribution = strategy.calculate(new BigDecimal("100.00"), new BigDecimal("999999.00"));

        // contribution should be 100 * 0.05 = 5.00
        assertThat(contribution).isEqualByComparingTo("5.00");
    }

    @Test
    void return_correct_strategy_type() {
        var strategy = new VariableContributionStrategy();

        var type = strategy.type();

        assertThat(type).isEqualTo(ContributionStrategy.StrategyType.VARIABLE);
    }
}

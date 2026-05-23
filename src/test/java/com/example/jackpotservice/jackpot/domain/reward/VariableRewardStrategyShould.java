package com.example.jackpotservice.jackpot.domain.reward;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class VariableRewardStrategyShould {

    @Test
    void return_zero_chance_when_pool_equals_initial_pool() {
        var strategy = new VariableRewardStrategy();

        // pool = initial = 1000, threshold = 2000 → 0% chance
        assertThat(strategy.evaluate(new BigDecimal("1000.00"), new BigDecimal("1000.00"))).isFalse();
    }

    @Test
    void always_win_when_pool_reaches_threshold() {
        var strategy = new VariableRewardStrategy();

        // pool = threshold = 2000 → 100% chance
        assertThat(strategy.evaluate(new BigDecimal("2000.00"), new BigDecimal("1000.00"))).isTrue();
    }

    @Test
    void always_win_when_pool_exceeds_threshold() {
        var strategy = new VariableRewardStrategy();

        assertThat(strategy.evaluate(new BigDecimal("3000.00"), new BigDecimal("1000.00"))).isTrue();
    }

    @Test
    void return_variable_strategy_type() {
        var strategy = new VariableRewardStrategy();

        assertThat(strategy.type()).isEqualTo(RewardStrategy.StrategyType.VARIABLE);
    }
}

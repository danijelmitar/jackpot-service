package com.example.jackpotservice.jackpot.domain.reward;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class VariableRewardStrategyShould {

    @Test
    void always_win_when_pool_reaches_threshold() {
        var strategy = new VariableRewardStrategy();

        assertThat(strategy.evaluate(new BigDecimal("1000.00"))).isTrue();
    }

    @Test
    void always_win_when_pool_exceeds_threshold() {
        var strategy = new VariableRewardStrategy();

        assertThat(strategy.evaluate(new BigDecimal("2000.00"))).isTrue();
    }

    @Test
    void return_variable_strategy_type() {
        var strategy = new VariableRewardStrategy();

        assertThat(strategy.type()).isEqualTo(RewardStrategy.StrategyType.VARIABLE);
    }

    @Test
    void return_correct_strategy_type() {
        var strategy = new VariableRewardStrategy();

        var type = strategy.type();

        assertThat(type).isEqualTo(RewardStrategy.StrategyType.VARIABLE);
    }

}

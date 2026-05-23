package com.example.jackpotservice.jackpot.domain.reward;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class FixedRewardStrategyShould {

    @Test
    void return_boolean_result_for_evaluation() {
        var strategy = new FixedRewardStrategy();

        var result = strategy.evaluate(new BigDecimal("1000.00"), new BigDecimal("1000.00"));

        assertThat(result).isIn(true, false);
    }

    @Test
    void return_fixed_strategy_type() {
        var strategy = new FixedRewardStrategy();

        assertThat(strategy.type()).isEqualTo(RewardStrategy.StrategyType.FIXED);
    }
}

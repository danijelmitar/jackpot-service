package com.example.jackpotservice.jackpot.domain.reward;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RewardStrategyFactoryShould {

    @Test
    void create_fixed_reward_strategy() {
        var strategy = RewardStrategyFactory.create(RewardStrategy.StrategyType.FIXED);

        assertThat(strategy).isInstanceOf(FixedRewardStrategy.class);
    }

    @Test
    void create_variable_reward_strategy() {
        var strategy = RewardStrategyFactory.create(RewardStrategy.StrategyType.VARIABLE);

        assertThat(strategy).isInstanceOf(VariableRewardStrategy.class);
    }
}

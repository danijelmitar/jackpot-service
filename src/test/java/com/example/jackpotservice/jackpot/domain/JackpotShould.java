package com.example.jackpotservice.jackpot.domain;

import com.example.jackpotservice.jackpot.domain.contribution.ContributionStrategy;
import com.example.jackpotservice.jackpot.domain.reward.RewardStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JackpotShould {

    private Jackpot jackpot(ContributionStrategy contributionStrategy, RewardStrategy rewardStrategy) {
        return Jackpot.reconstitute("jackpot-1", new BigDecimal("100.00"),
                new BigDecimal("100.00"), contributionStrategy, rewardStrategy);
    }

    @Test
    void increase_pool_amount_after_contribution() {
        var contributionStrategy = mock(ContributionStrategy.class);
        when(contributionStrategy.calculate(new BigDecimal("10.00"), new BigDecimal("100.00")))
                .thenReturn(new BigDecimal("2.00"));
        var jackpot = jackpot(contributionStrategy, mock(RewardStrategy.class));

        jackpot.contribute("bet-1", "user-1", new BigDecimal("10.00"));

        assertThat(jackpot.getPoolAmount()).isEqualByComparingTo("102.00");
    }

    @Test
    void return_contribution_with_correct_fields() {
        var contributionStrategy = mock(ContributionStrategy.class);
        when(contributionStrategy.calculate(new BigDecimal("10.00"), new BigDecimal("100.00")))
                .thenReturn(new BigDecimal("2.00"));
        var jackpot = jackpot(contributionStrategy, mock(RewardStrategy.class));

        var contribution = jackpot.contribute("bet-1", "user-1", new BigDecimal("10.00"));

        assertThat(contribution.getBetId()).isEqualTo("bet-1");
        assertThat(contribution.getUserId()).isEqualTo("user-1");
        assertThat(contribution.getJackpotId()).isEqualTo("jackpot-1");
        assertThat(contribution.getStakeAmount()).isEqualByComparingTo("10.00");
        assertThat(contribution.getContributionAmount()).isEqualByComparingTo("2.00");
        assertThat(contribution.getCurrentJackpotAmount()).isEqualByComparingTo("102.00");
        assertThat(contribution.getCreatedAt()).isNotNull();
    }

    @Test
    void delegate_contribution_calculation_to_strategy() {
        var contributionStrategy = mock(ContributionStrategy.class);
        when(contributionStrategy.calculate(new BigDecimal("10.00"), new BigDecimal("100.00")))
                .thenReturn(new BigDecimal("2.00"));
        var jackpot = jackpot(contributionStrategy, mock(RewardStrategy.class));

        jackpot.contribute("bet-1", "user-1", new BigDecimal("10.00"));

        verify(contributionStrategy).calculate(new BigDecimal("10.00"), new BigDecimal("100.00"));
    }

    @Test
    void return_reward_with_zero_amount_when_bet_does_not_win() {
        var rewardStrategy = mock(RewardStrategy.class);
        when(rewardStrategy.evaluate(any())).thenReturn(false);
        var jackpot = jackpot(mock(ContributionStrategy.class), rewardStrategy);
        var contribution = JackpotContribution.builder()
                .betId("bet-1").userId("user-1").jackpotId("jackpot-1")
                .stakeAmount(new BigDecimal("10.00")).contributionAmount(new BigDecimal("2.00"))
                .currentJackpotAmount(new BigDecimal("102.00")).createdAt(java.time.LocalDateTime.now())
                .build();

        var reward = jackpot.evaluate(contribution);

        assertThat(reward.getRewardAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(reward.getBetId()).isEqualTo("bet-1");
        assertThat(reward.getUserId()).isEqualTo("user-1");
        assertThat(reward.getJackpotId()).isEqualTo("jackpot-1");
    }

    @Test
    void return_reward_with_pool_amount_when_bet_wins() {
        var rewardStrategy = mock(RewardStrategy.class);
        when(rewardStrategy.evaluate(any())).thenReturn(true);
        var jackpot = jackpot(mock(ContributionStrategy.class), rewardStrategy);
        var contribution = JackpotContribution.builder()
                .betId("bet-1").userId("user-1").jackpotId("jackpot-1")
                .stakeAmount(new BigDecimal("10.00")).contributionAmount(new BigDecimal("2.00"))
                .currentJackpotAmount(new BigDecimal("102.00")).createdAt(java.time.LocalDateTime.now())
                .build();

        var reward = jackpot.evaluate(contribution);

        assertThat(reward.getRewardAmount()).isEqualByComparingTo("100.00");
    }

    @Test
    void reset_pool_to_initial_amount_when_bet_wins() {
        var rewardStrategy = mock(RewardStrategy.class);
        when(rewardStrategy.evaluate(any())).thenReturn(true);
        var jackpot = jackpot(mock(ContributionStrategy.class), rewardStrategy);
        var contribution = JackpotContribution.builder()
                .betId("bet-1").userId("user-1").jackpotId("jackpot-1")
                .stakeAmount(new BigDecimal("10.00")).contributionAmount(new BigDecimal("2.00"))
                .currentJackpotAmount(new BigDecimal("102.00")).createdAt(java.time.LocalDateTime.now())
                .build();

        jackpot.evaluate(contribution);

        assertThat(jackpot.getPoolAmount()).isEqualByComparingTo("100.00");
    }

    @Test
    void not_reset_pool_when_bet_does_not_win() {
        var rewardStrategy = mock(RewardStrategy.class);
        when(rewardStrategy.evaluate(any())).thenReturn(false);
        var jackpot = jackpot(mock(ContributionStrategy.class), rewardStrategy);
        var contribution = JackpotContribution.builder()
                .betId("bet-1").userId("user-1").jackpotId("jackpot-1")
                .stakeAmount(new BigDecimal("10.00")).contributionAmount(new BigDecimal("2.00"))
                .currentJackpotAmount(new BigDecimal("102.00")).createdAt(java.time.LocalDateTime.now())
                .build();

        jackpot.evaluate(contribution);

        assertThat(jackpot.getPoolAmount()).isEqualByComparingTo("100.00");
    }

    @Test
    void delegate_evaluation_to_reward_strategy() {
        var rewardStrategy = mock(RewardStrategy.class);
        when(rewardStrategy.evaluate(any())).thenReturn(false);
        var jackpot = jackpot(mock(ContributionStrategy.class), rewardStrategy);
        var contribution = JackpotContribution.builder()
                .betId("bet-1").userId("user-1").jackpotId("jackpot-1")
                .stakeAmount(new BigDecimal("10.00")).contributionAmount(new BigDecimal("2.00"))
                .currentJackpotAmount(new BigDecimal("102.00")).createdAt(java.time.LocalDateTime.now())
                .build();

        jackpot.evaluate(contribution);

        verify(rewardStrategy).evaluate(new BigDecimal("100.00"));
    }

    @Test
    void throw_exception_when_jackpot_id_is_null() {
        var poolAmount = new BigDecimal("100.00");
        assertThatThrownBy(() -> Jackpot.reconstitute(null, poolAmount,
                poolAmount, mock(ContributionStrategy.class), mock(RewardStrategy.class)))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("jackpotId is required");
    }

    @Test
    void throw_exception_when_initial_pool_amount_is_null() {
        var poolAmount = new BigDecimal("100.00");
        assertThatThrownBy(() -> Jackpot.reconstitute("jackpot-1", null,
                poolAmount, mock(ContributionStrategy.class), mock(RewardStrategy.class)))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("initialPoolAmount is required");
    }

    @Test
    void throw_exception_when_contribution_strategy_is_null() {
        var poolAmount = new BigDecimal("100.00");
        assertThatThrownBy(() -> Jackpot.reconstitute("jackpot-1", poolAmount,
                poolAmount, null, mock(RewardStrategy.class)))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("contributionStrategy is required");
    }

    @Test
    void throw_exception_when_reward_strategy_is_null() {
        var poolAmount = new BigDecimal("100.00");
        assertThatThrownBy(() -> Jackpot.reconstitute("jackpot-1", poolAmount,
                poolAmount, mock(ContributionStrategy.class), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("rewardStrategy is required");
    }

    @Test
    void throw_exception_when_bet_id_is_null() {
        var jackpot = jackpot(mock(ContributionStrategy.class), mock(RewardStrategy.class));

        var betAmount = new BigDecimal("10.00");
        assertThatThrownBy(() -> jackpot.contribute(null, "user-1", betAmount))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("betId is required");
    }

    @Test
    void throw_exception_when_user_id_is_null() {
        var jackpot = jackpot(mock(ContributionStrategy.class), mock(RewardStrategy.class));

        var betAmount = new BigDecimal("10.00");
        assertThatThrownBy(() -> jackpot.contribute("bet-1", null, betAmount))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("userId is required");
    }

    @Test
    void throw_exception_when_bet_amount_is_null() {
        var jackpot = jackpot(mock(ContributionStrategy.class), mock(RewardStrategy.class));

        assertThatThrownBy(() -> jackpot.contribute("bet-1", "user-1", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("betAmount is required");
    }

    @Test
    void throw_exception_when_contribution_is_null_on_evaluate() {
        var jackpot = jackpot(mock(ContributionStrategy.class), mock(RewardStrategy.class));

        assertThatThrownBy(() -> jackpot.evaluate(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("contribution is required");
    }
}

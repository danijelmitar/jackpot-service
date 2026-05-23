package com.example.jackpotservice.jackpot.application;

import com.example.jackpotservice.jackpot.domain.*;
import com.example.jackpotservice.jackpot.domain.contribution.FixedContributionStrategy;
import com.example.jackpotservice.jackpot.domain.reward.FixedRewardStrategy;
import com.example.jackpotservice.jackpot.domain.reward.RewardStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluateJackpotRewardUseCaseShould {

    @Mock
    private JackpotContributionRepository jackpotContributionRepository;

    @Mock
    private JackpotRepository jackpotRepository;

    @Mock
    private JackpotRewardRepository jackpotRewardRepository;

    private EvaluateJackpotRewardUseCase evaluateJackpotRewardUseCase;

    private final JackpotContribution contribution = JackpotContribution.builder()
            .betId("bet-1")
            .userId("user-1")
            .jackpotId("jackpot-1")
            .stakeAmount(new BigDecimal("10.00"))
            .contributionAmount(new BigDecimal("1.00"))
            .currentJackpotAmount(new BigDecimal("1001.00"))
            .createdAt(LocalDateTime.now())
            .build();

    private final JackpotReward existingReward = JackpotReward.builder()
            .betId("bet-1")
            .userId("user-1")
            .jackpotId("jackpot-1")
            .rewardAmount(new BigDecimal("1001.00"))
            .createdAt(LocalDateTime.now())
            .build();

    @BeforeEach
    void setUp() {
        evaluateJackpotRewardUseCase = new EvaluateJackpotRewardUseCase(
                jackpotContributionRepository, jackpotRepository, jackpotRewardRepository);
    }

    @Test
    void return_existing_reward_when_already_evaluated() {
        when(jackpotRewardRepository.findByBetId("bet-1")).thenReturn(Optional.of(existingReward));

        var result = evaluateJackpotRewardUseCase.execute("bet-1");

        assertThat(result).isPresent();
        assertThat(result.get().getRewardAmount()).isEqualByComparingTo("1001.00");
        verifyNoInteractions(jackpotContributionRepository, jackpotRepository);
    }

    @Test
    void not_evaluate_again_when_reward_already_exists() {
        when(jackpotRewardRepository.findByBetId("bet-1")).thenReturn(Optional.of(existingReward));

        evaluateJackpotRewardUseCase.execute("bet-1");

        verify(jackpotRewardRepository, never()).save(any());
        verify(jackpotRepository, never()).save(any());
    }

    @Test
    void return_empty_when_contribution_not_found() {
        when(jackpotRewardRepository.findByBetId("bet-1")).thenReturn(Optional.empty());
        when(jackpotContributionRepository.findByBetId("bet-1")).thenReturn(Optional.empty());

        var result = evaluateJackpotRewardUseCase.execute("bet-1");

        assertThat(result).isEmpty();
        verifyNoInteractions(jackpotRepository);
    }

    @Test
    void evaluate_and_return_reward_when_contribution_found() {
        var jackpot = Jackpot.reconstitute("jackpot-1", new BigDecimal("1000.00"),
                new BigDecimal("1000.00"), new FixedContributionStrategy(), new RewardStrategy() {
                    @Override
                    public boolean evaluate(BigDecimal poolAmount, BigDecimal initialPoolAmount) { return true; }

                    @Override
                    public StrategyType type() {
                        return StrategyType.FIXED;
                    }
                });
        when(jackpotRewardRepository.findByBetId("bet-1")).thenReturn(Optional.empty());
        when(jackpotContributionRepository.findByBetId("bet-1")).thenReturn(Optional.of(contribution));
        when(jackpotRepository.findById("jackpot-1")).thenReturn(Optional.of(jackpot));

        var result = evaluateJackpotRewardUseCase.execute("bet-1");

        assertThat(result).isPresent();
        assertThat(result.get().getBetId()).isEqualTo("bet-1");
        assertThat(result.get().getRewardAmount()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    }

    @Test
    void save_jackpot_and_reward_after_evaluation() {
        var jackpot = Jackpot.reconstitute("jackpot-1", new BigDecimal("1000.00"),
                new BigDecimal("1000.00"), new FixedContributionStrategy(), new FixedRewardStrategy());
        when(jackpotRewardRepository.findByBetId("bet-1")).thenReturn(Optional.empty());
        when(jackpotContributionRepository.findByBetId("bet-1")).thenReturn(Optional.of(contribution));
        when(jackpotRepository.findById("jackpot-1")).thenReturn(Optional.of(jackpot));

        evaluateJackpotRewardUseCase.execute("bet-1");

        verify(jackpotRepository).save(jackpot);
        verify(jackpotRewardRepository).save(any(JackpotReward.class));
    }

    @Test
    void throw_exception_when_jackpot_not_found() {
        when(jackpotRewardRepository.findByBetId("bet-1")).thenReturn(Optional.empty());
        when(jackpotContributionRepository.findByBetId("bet-1")).thenReturn(Optional.of(contribution));
        when(jackpotRepository.findById("jackpot-1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> evaluateJackpotRewardUseCase.execute("bet-1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("jackpot-1");
    }
}

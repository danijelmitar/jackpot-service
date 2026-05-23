package com.example.jackpotservice.jackpot.infrastructure.persistence;

import com.example.jackpotservice.jackpot.domain.Jackpot;
import com.example.jackpotservice.jackpot.domain.contribution.ContributionStrategy;
import com.example.jackpotservice.jackpot.domain.contribution.FixedContributionStrategy;
import com.example.jackpotservice.jackpot.domain.contribution.VariableContributionStrategy;
import com.example.jackpotservice.jackpot.domain.reward.FixedRewardStrategy;
import com.example.jackpotservice.jackpot.domain.reward.RewardStrategy;
import com.example.jackpotservice.jackpot.domain.reward.VariableRewardStrategy;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotEntity;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JackpotRepositoryAdapterShould {

    @Mock
    private JackpotJpaRepository jackpotJpaRepository;

    private JackpotRepositoryAdapter jackpotRepositoryAdapter;

    @BeforeEach
    void setUp() {
        jackpotRepositoryAdapter = new JackpotRepositoryAdapter(jackpotJpaRepository);
    }

    @Test
    void save_jackpot_with_correct_fields() {
        var jackpot = Jackpot.reconstitute("jackpot-1", new BigDecimal("1000.00"),
                new BigDecimal("1000.00"), new FixedContributionStrategy(), new FixedRewardStrategy());
        var captor = ArgumentCaptor.forClass(JackpotEntity.class);

        jackpotRepositoryAdapter.save(jackpot);

        verify(jackpotJpaRepository).save(captor.capture());
        var saved = captor.getValue();
        assertThat(saved.getJackpotId()).isEqualTo("jackpot-1");
        assertThat(saved.getPoolAmount()).isEqualByComparingTo("1000.00");
        assertThat(saved.getInitialPoolAmount()).isEqualByComparingTo("1000.00");
        assertThat(saved.getContributionStrategyType()).isEqualTo(ContributionStrategy.StrategyType.FIXED);
        assertThat(saved.getRewardStrategyType()).isEqualTo(RewardStrategy.StrategyType.FIXED);
    }

    @Test
    void return_empty_when_jackpot_not_found() {
        when(jackpotJpaRepository.findById("jackpot-1")).thenReturn(Optional.empty());

        var result = jackpotRepositoryAdapter.findById("jackpot-1");

        assertThat(result).isEmpty();
    }

    @Test
    void return_jackpot_with_fixed_strategies_when_found() {
        var entity = new JackpotEntity("jackpot-1", new BigDecimal("100.00"),
                new BigDecimal("100.00"),
                ContributionStrategy.StrategyType.FIXED,
                RewardStrategy.StrategyType.FIXED);
        when(jackpotJpaRepository.findById("jackpot-1")).thenReturn(Optional.of(entity));

        var result = jackpotRepositoryAdapter.findById("jackpot-1");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo("jackpot-1");
        assertThat(result.get().getPoolAmount()).isEqualByComparingTo("100.00");
        assertThat(result.get().getContributionStrategy()).isInstanceOf(FixedContributionStrategy.class);
        assertThat(result.get().getRewardStrategy()).isInstanceOf(FixedRewardStrategy.class);
    }

    @Test
    void return_jackpot_with_variable_strategies_when_found() {
        var entity = new JackpotEntity("jackpot-1", new BigDecimal("100.00"),
                new BigDecimal("100.00"),
                ContributionStrategy.StrategyType.VARIABLE,
                RewardStrategy.StrategyType.VARIABLE);
        when(jackpotJpaRepository.findById("jackpot-1")).thenReturn(Optional.of(entity));

        var result = jackpotRepositoryAdapter.findById("jackpot-1");

        assertThat(result).isPresent();
        assertThat(result.get().getContributionStrategy()).isInstanceOf(VariableContributionStrategy.class);
        assertThat(result.get().getRewardStrategy()).isInstanceOf(VariableRewardStrategy.class);
    }
}

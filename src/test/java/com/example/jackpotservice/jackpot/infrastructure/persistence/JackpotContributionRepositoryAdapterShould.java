package com.example.jackpotservice.jackpot.infrastructure.persistence;

import com.example.jackpotservice.jackpot.domain.JackpotContribution;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotContributionEntity;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotContributionId;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotContributionJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JackpotContributionRepositoryAdapterShould {

    @Mock
    private JackpotContributionJpaRepository jackpotContributionJpaRepository;

    private JackpotContributionRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new JackpotContributionRepositoryAdapter(jackpotContributionJpaRepository);
    }

    @Test
    void save_contribution_with_correct_fields() {
        var contribution = JackpotContribution.builder()
                .betId("bet-1")
                .jackpotId("jackpot-1")
                .userId("user-1")
                .stakeAmount(new BigDecimal("10.00"))
                .contributionAmount(new BigDecimal("1.00"))
                .currentJackpotAmount(new BigDecimal("1001.00"))
                .createdAt(LocalDateTime.now())
                .build();
        var captor = ArgumentCaptor.forClass(JackpotContributionEntity.class);

        adapter.save(contribution);

        verify(jackpotContributionJpaRepository).save(captor.capture());
        JackpotContributionEntity saved = captor.getValue();
        assertThat(saved.getId().getBetId()).isEqualTo("bet-1");
        assertThat(saved.getId().getJackpotId()).isEqualTo("jackpot-1");
        assertThat(saved.getUserId()).isEqualTo("user-1");
        assertThat(saved.getStakeAmount()).isEqualByComparingTo("10.00");
        assertThat(saved.getContributionAmount()).isEqualByComparingTo("1.00");
        assertThat(saved.getCurrentJackpotAmount()).isEqualByComparingTo("1001.00");
    }

    @Test
    void return_empty_when_contribution_not_found() {
        when(jackpotContributionJpaRepository.findByIdBetId("bet-1")).thenReturn(Optional.empty());

        var result = adapter.findByBetId("bet-1");

        assertThat(result).isEmpty();
    }

    @Test
    void return_contribution_with_correct_fields_when_found() {
        var entity = JackpotContributionEntity.builder()
                .id(new JackpotContributionId("bet-1", "jackpot-1"))
                .userId("user-1")
                .stakeAmount(new BigDecimal("10.00"))
                .contributionAmount(new BigDecimal("1.00"))
                .currentJackpotAmount(new BigDecimal("1001.00"))
                .createdAt(LocalDateTime.now())
                .build();
        when(jackpotContributionJpaRepository.findByIdBetId("bet-1")).thenReturn(Optional.of(entity));

        var result = adapter.findByBetId("bet-1");

        assertThat(result).isPresent();
        assertThat(result.get().getBetId()).isEqualTo("bet-1");
        assertThat(result.get().getJackpotId()).isEqualTo("jackpot-1");
        assertThat(result.get().getUserId()).isEqualTo("user-1");
        assertThat(result.get().getStakeAmount()).isEqualByComparingTo("10.00");
        assertThat(result.get().getContributionAmount()).isEqualByComparingTo("1.00");
        assertThat(result.get().getCurrentJackpotAmount()).isEqualByComparingTo("1001.00");
    }
}

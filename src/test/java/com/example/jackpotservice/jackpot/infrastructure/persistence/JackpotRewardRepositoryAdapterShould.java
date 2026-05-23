package com.example.jackpotservice.jackpot.infrastructure.persistence;

import com.example.jackpotservice.jackpot.domain.JackpotReward;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotRewardEntity;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotRewardId;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotRewardJpaRepository;
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
class JackpotRewardRepositoryAdapterShould {

    @Mock
    private JackpotRewardJpaRepository jackpotRewardJpaRepository;

    private JackpotRewardRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new JackpotRewardRepositoryAdapter(jackpotRewardJpaRepository);
    }

    @Test
    void save_reward_with_correct_fields() {
        var reward = JackpotReward.builder()
                .betId("bet-1")
                .userId("user-1")
                .jackpotId("jackpot-1")
                .rewardAmount(new BigDecimal("1000.00"))
                .createdAt(LocalDateTime.now())
                .build();
        var captor = ArgumentCaptor.forClass(JackpotRewardEntity.class);

        adapter.save(reward);

        verify(jackpotRewardJpaRepository).save(captor.capture());
        var saved = captor.getValue();
        assertThat(saved.getId().getBetId()).isEqualTo("bet-1");
        assertThat(saved.getId().getJackpotId()).isEqualTo("jackpot-1");
        assertThat(saved.getUserId()).isEqualTo("user-1");
        assertThat(saved.getRewardAmount()).isEqualByComparingTo("1000.00");
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void save_reward_with_zero_amount_when_bet_did_not_win() {
        var reward = JackpotReward.builder()
                .betId("bet-1")
                .userId("user-1")
                .jackpotId("jackpot-1")
                .rewardAmount(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .build();
        var captor = ArgumentCaptor.forClass(JackpotRewardEntity.class);

        adapter.save(reward);

        verify(jackpotRewardJpaRepository).save(captor.capture());
        assertThat(captor.getValue().getRewardAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void return_empty_when_reward_not_found() {
        when(jackpotRewardJpaRepository.findByIdBetId("bet-1")).thenReturn(Optional.empty());

        var result = adapter.findByBetId("bet-1");

        assertThat(result).isEmpty();
    }

    @Test
    void return_reward_with_correct_fields_when_found() {
        var entity = JackpotRewardEntity.builder()
                .id(new JackpotRewardId("bet-1", "jackpot-1"))
                .userId("user-1")
                .rewardAmount(new BigDecimal("1000.00"))
                .createdAt(LocalDateTime.now())
                .build();
        when(jackpotRewardJpaRepository.findByIdBetId("bet-1")).thenReturn(Optional.of(entity));

        var result = adapter.findByBetId("bet-1");

        assertThat(result).isPresent();
        assertThat(result.get().getBetId()).isEqualTo("bet-1");
        assertThat(result.get().getJackpotId()).isEqualTo("jackpot-1");
        assertThat(result.get().getUserId()).isEqualTo("user-1");
        assertThat(result.get().getRewardAmount()).isEqualByComparingTo("1000.00");
    }
}

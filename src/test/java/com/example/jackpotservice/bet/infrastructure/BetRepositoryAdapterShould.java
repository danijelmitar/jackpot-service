package com.example.jackpotservice.bet.infrastructure;

import com.example.jackpotservice.bet.domain.Bet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BetRepositoryAdapterShould {

    @Mock
    private BetJpaRepository betJpaRepository;

    private BetRepositoryAdapter betRepositoryAdapter;

    @BeforeEach
    void setUp() {
        betRepositoryAdapter = new BetRepositoryAdapter(betJpaRepository);
    }

    @Test
    void save_bet_with_correct_fields() {
        var bet = Bet.place("user-1", "jackpot-1", new BigDecimal("10.00"));
        var betEntity = BetEntity.builder()
                .betId(bet.getId())
                .userId(bet.getUserId())
                .jackpotId(bet.getJackpotId())
                .betAmount(bet.getBetAmount())
                .build();

        betRepositoryAdapter.save(bet);

        verify(betJpaRepository).save(betEntity);
    }
}

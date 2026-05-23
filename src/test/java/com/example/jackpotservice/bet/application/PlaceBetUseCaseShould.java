package com.example.jackpotservice.bet.application;

import com.example.jackpotservice.bet.domain.Bet;
import com.example.jackpotservice.bet.domain.BetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlaceBetUseCaseShould {

    @Mock
    private BetRepository betRepository;

    private PlaceBetUseCase placeBetUseCase;

    @BeforeEach
    void setUp() {
        placeBetUseCase = new PlaceBetUseCase(betRepository);
    }

    @Test
    void return_response_with_generated_bet_id() {
        var userId = "user-1";
        var jackpotId = "jackpot-1";
        var amount = new BigDecimal("10.00");

        var bet = placeBetUseCase.execute(userId, jackpotId, amount);

        assertThat(bet.getId()).isNotNull().isNotEmpty();
        verify(betRepository).save(any(Bet.class));
    }

    @Test
    void propagate_exception_when_bet_persistence_fails() {
        var userId = "user-1";
        var jackpotId = "jackpot-1";
        var amount = new BigDecimal("10.00");
        doThrow(new RuntimeException("DB unavailable")).when(betRepository).save(any(Bet.class));

        assertThatThrownBy(() -> placeBetUseCase.execute(userId, jackpotId, amount))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("DB unavailable");
    }
}

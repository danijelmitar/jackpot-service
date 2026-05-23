package com.example.jackpotservice.bet.application;

import com.example.jackpotservice.bet.domain.Bet;
import com.example.jackpotservice.bet.domain.BetEventPublisher;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceBetUseCaseShould {

    @Mock
    private BetRepository betRepository;

    @Mock
    private BetEventPublisher betEventPublisher;

    private PlaceBetUseCase placeBetUseCase;

    @BeforeEach
    void setUp() {
        placeBetUseCase = new PlaceBetUseCase(betRepository, betEventPublisher);
    }

    @Test
    void return_response_with_generated_bet_id() {
        var userId = "user-1";
        var jackpotId = "jackpot-1";
        var amount = new BigDecimal("10.00");

        var response = placeBetUseCase.execute(userId, jackpotId, amount);

        assertThat(response.getId()).isNotNull().isNotEmpty();
        verify(betRepository).save(any(Bet.class));
    }

    @Test
    void publish_bet_event_after_persisting_bet() {
        var userId = "user-1";
        var jackpotId = "jackpot-1";
        var amount = new BigDecimal("10.00");
        var inOrder = inOrder(betRepository, betEventPublisher);

        placeBetUseCase.execute(userId, jackpotId, amount);

        inOrder.verify(betRepository).save(any(Bet.class));
        inOrder.verify(betEventPublisher).publish(any(Bet.class));
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

        verify(betEventPublisher, never()).publish(any());
    }

    @Test
    void propagate_exception_when_event_publishing_fails() {
        var userId = "user-1";
        var jackpotId = "jackpot-1";
        var amount = new BigDecimal("10.00");
        doThrow(new RuntimeException("Kafka unavailable")).when(betEventPublisher).publish(any(Bet.class));

        assertThatThrownBy(() -> placeBetUseCase.execute(userId, jackpotId, amount))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Kafka unavailable");
    }
}

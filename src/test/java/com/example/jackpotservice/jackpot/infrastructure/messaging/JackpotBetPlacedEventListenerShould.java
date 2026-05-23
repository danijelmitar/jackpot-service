package com.example.jackpotservice.jackpot.infrastructure.messaging;

import com.example.jackpotservice.jackpot.application.ContributeToJackpotUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JackpotBetPlacedEventListenerShould {

    @Mock
    private ContributeToJackpotUseCase contributeToJackpotUseCase;

    private JackpotBetPlacedEventListener betPlacedEventListener;

    @BeforeEach
    void setUp() {
        betPlacedEventListener = new JackpotBetPlacedEventListener(contributeToJackpotUseCase);
    }

    @Test
    void delegate_to_contribute_to_jackpot_use_case_when_bet_placed_event_received() {
        var event = new JackpotBetPlacedEvent("bet-1", "user-1", "jackpot-1", new BigDecimal("10.00"));

        betPlacedEventListener.onBetPlaced(event);

        verify(contributeToJackpotUseCase).execute(event.betId(), event.userId(), event.jackpotId(), event.betAmount());
    }

}

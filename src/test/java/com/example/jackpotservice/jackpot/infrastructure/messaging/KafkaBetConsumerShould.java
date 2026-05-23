package com.example.jackpotservice.jackpot.infrastructure.messaging;

import com.example.jackpotservice.common.messaging.BetPlacedMessage;
import com.example.jackpotservice.jackpot.application.ContributeToJackpotUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaBetConsumerShould {

    @Mock
    private ContributeToJackpotUseCase contributeToJackpotUseCase;

    private KafkaBetConsumer kafkaBetConsumer;

    @BeforeEach
    void setUp() {
        kafkaBetConsumer = new KafkaBetConsumer(contributeToJackpotUseCase);
    }

    @Test
    void delegate_to_contribute_to_jackpot_use_case_when_message_received() {
        var event = new BetPlacedMessage("bet-1", "user-1", "jackpot-1", new BigDecimal("10.00"));

        kafkaBetConsumer.consume(event);

        verify(contributeToJackpotUseCase).execute("bet-1", "user-1", "jackpot-1", new BigDecimal("10.00"));
    }
}

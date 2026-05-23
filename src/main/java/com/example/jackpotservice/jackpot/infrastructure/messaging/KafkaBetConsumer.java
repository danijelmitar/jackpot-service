package com.example.jackpotservice.jackpot.infrastructure.messaging;

import com.example.jackpotservice.common.messaging.BetPlacedMessage;
import com.example.jackpotservice.jackpot.application.ContributeToJackpotUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBetConsumer {

    private final ContributeToJackpotUseCase contributeToJackpotUseCase;

    @KafkaListener(topics = "jackpot-bets", groupId = "jackpot-service", containerFactory = "betPlacedKafkaListenerContainerFactory")
    public void consume(BetPlacedMessage event) {
        log.info("Received BetPlaced event for betId: {}", event.betId());
        contributeToJackpotUseCase.execute(
                event.betId(),
                event.userId(),
                event.jackpotId(),
                event.betAmount()
        );
    }
}

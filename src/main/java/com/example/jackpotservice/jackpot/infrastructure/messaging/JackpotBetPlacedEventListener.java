package com.example.jackpotservice.jackpot.infrastructure.messaging;

import com.example.jackpotservice.jackpot.application.ContributeToJackpotUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JackpotBetPlacedEventListener {

    private final ContributeToJackpotUseCase contributeToJackpot;

    public void onBetPlaced(JackpotBetPlacedEvent event) {
        contributeToJackpot.execute(
                event.betId(),
                event.userId(),
                event.jackpotId(),
                event.betAmount()
        );
    }

}

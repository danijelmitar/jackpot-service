package com.example.jackpotservice.bet.infrastructure.messaging;

import com.example.jackpotservice.bet.domain.Bet;
import com.example.jackpotservice.bet.domain.BetEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBetProducer implements BetEventPublisher {

    static final String TOPIC = "jackpot-bets";

    private final KafkaTemplate<String, BetPlacedMessage> kafkaTemplate;

    @Override
    public void publish(Bet bet) {
        kafkaTemplate.send(TOPIC, bet.getJackpotId(), toMessage(bet));
        log.info("Published BetPlaced event for betId: {}", bet.getId());
    }

    private static BetPlacedMessage toMessage(Bet bet) {
        return new BetPlacedMessage(bet.getId(), bet.getUserId(), bet.getJackpotId(), bet.getBetAmount());
    }

    public record BetPlacedMessage(
            String betId,
            String userId,
            String jackpotId,
            BigDecimal betAmount
    ) {}
}

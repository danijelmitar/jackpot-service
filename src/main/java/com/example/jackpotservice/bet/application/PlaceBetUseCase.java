package com.example.jackpotservice.bet.application;

import com.example.jackpotservice.bet.domain.Bet;
import com.example.jackpotservice.bet.domain.BetEventPublisher;
import com.example.jackpotservice.bet.domain.BetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlaceBetUseCase {

    private final BetRepository betRepository;
    private final BetEventPublisher betEventPublisher;

    @Transactional
    public Bet execute(String userId, String jackpotId, BigDecimal amount) {
        var bet = Bet.place(userId, jackpotId, amount);

        betRepository.save(bet);
        betEventPublisher.publish(bet);

        log.info("Bet published: {}", bet); // Mocked kafka publisher

        return bet;
    }

}

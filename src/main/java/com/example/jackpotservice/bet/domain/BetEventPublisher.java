package com.example.jackpotservice.bet.domain;

public interface BetEventPublisher {
    void publish(Bet bet);
}

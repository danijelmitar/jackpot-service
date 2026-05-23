package com.example.jackpotservice.bet.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
public final class Bet {
    private final String id;
    private final String userId;
    private final String jackpotId;
    private final BigDecimal betAmount;

    private Bet(String userId, String jackpotId, BigDecimal betAmount) {
        Objects.requireNonNull(userId, "userId is required");
        Objects.requireNonNull(jackpotId, "jackpotId is required");
        Objects.requireNonNull(betAmount, "betAmount is required");
        if (betAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("betAmount must be positive");
        }

        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.betAmount = betAmount;
    }

    public static Bet place(String userId, String jackpotId, BigDecimal betAmount) {
        return new Bet(userId, jackpotId, betAmount);
    }

}

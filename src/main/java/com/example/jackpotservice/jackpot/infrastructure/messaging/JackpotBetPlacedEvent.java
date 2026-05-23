package com.example.jackpotservice.jackpot.infrastructure.messaging;

import java.math.BigDecimal;

public record JackpotBetPlacedEvent(String betId, String userId, String jackpotId, BigDecimal betAmount) {
}

package com.example.jackpotservice.common.messaging;

import java.math.BigDecimal;

public record BetPlacedMessage(
        String betId,
        String userId,
        String jackpotId,
        BigDecimal betAmount
) {
}

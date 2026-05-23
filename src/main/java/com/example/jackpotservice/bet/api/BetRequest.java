package com.example.jackpotservice.bet.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BetRequest(
        @NotEmpty String userId,
        @NotEmpty String jackpotId,
        @Positive @NotNull BigDecimal amount) {
}

package com.example.jackpotservice.jackpot.api;

import java.math.BigDecimal;

public record JackpotRewardResponse(
        String betId,
        String userId,
        String jackpotId,
        BigDecimal rewardAmount
) {}

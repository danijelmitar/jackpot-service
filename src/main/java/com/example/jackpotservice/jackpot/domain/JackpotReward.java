package com.example.jackpotservice.jackpot.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public final class JackpotReward {

    private final String betId;
    private final String userId;
    private final String jackpotId;
    private final BigDecimal rewardAmount;
    private final LocalDateTime createdAt;
}

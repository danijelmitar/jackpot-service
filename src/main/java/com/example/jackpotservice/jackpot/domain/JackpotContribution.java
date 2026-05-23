package com.example.jackpotservice.jackpot.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public final class JackpotContribution {

    private final String betId;
    private final String userId;
    private final String jackpotId;
    private final BigDecimal stakeAmount;
    private final BigDecimal contributionAmount;
    private final BigDecimal currentJackpotAmount;
    private final LocalDateTime createdAt;
}

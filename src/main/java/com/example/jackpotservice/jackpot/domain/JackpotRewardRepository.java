package com.example.jackpotservice.jackpot.domain;

import java.util.Optional;

public interface JackpotRewardRepository {
    void save(JackpotReward reward);

    Optional<JackpotReward> findByBetId(String betId);
}

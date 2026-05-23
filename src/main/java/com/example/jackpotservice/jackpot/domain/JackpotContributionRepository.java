package com.example.jackpotservice.jackpot.domain;

import java.util.Optional;

public interface JackpotContributionRepository {
    void save(JackpotContribution jackpotContribution);

    Optional<JackpotContribution> findByBetId(String betId);

}

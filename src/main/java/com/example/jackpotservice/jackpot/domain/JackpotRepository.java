package com.example.jackpotservice.jackpot.domain;

import java.util.Optional;

public interface JackpotRepository {
    Optional<Jackpot> findById(String jackpotId);

    void save(Jackpot jackpot);
}

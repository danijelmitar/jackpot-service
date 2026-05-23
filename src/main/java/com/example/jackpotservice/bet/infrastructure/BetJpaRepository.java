package com.example.jackpotservice.bet.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BetJpaRepository extends JpaRepository<BetEntity, String> {
}

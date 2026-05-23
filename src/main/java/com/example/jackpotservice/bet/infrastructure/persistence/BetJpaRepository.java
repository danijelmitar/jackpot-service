package com.example.jackpotservice.bet.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BetJpaRepository extends JpaRepository<BetEntity, String> {
}

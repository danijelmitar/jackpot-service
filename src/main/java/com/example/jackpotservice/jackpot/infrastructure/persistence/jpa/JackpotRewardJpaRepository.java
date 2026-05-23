package com.example.jackpotservice.jackpot.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JackpotRewardJpaRepository extends JpaRepository<JackpotRewardEntity, JackpotRewardId> {
}

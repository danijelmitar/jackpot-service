package com.example.jackpotservice.jackpot.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JackpotContributionJpaRepository extends JpaRepository<JackpotContributionEntity, JackpotContributionId> {

    Optional<JackpotContributionEntity> findByIdBetId(String betId);

}

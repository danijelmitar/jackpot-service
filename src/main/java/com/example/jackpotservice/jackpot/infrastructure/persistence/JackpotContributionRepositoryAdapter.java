package com.example.jackpotservice.jackpot.infrastructure.persistence;

import com.example.jackpotservice.jackpot.domain.JackpotContribution;
import com.example.jackpotservice.jackpot.domain.JackpotContributionRepository;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotContributionEntity;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotContributionId;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotContributionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JackpotContributionRepositoryAdapter implements JackpotContributionRepository {

    private final JackpotContributionJpaRepository jackpotContributionJpaRepository;

    @Override
    public void save(JackpotContribution jackpotContribution) {
        jackpotContributionJpaRepository.save(toEntity(jackpotContribution));
    }

    @Override
    public Optional<JackpotContribution> findByBetId(String betId) {
        return jackpotContributionJpaRepository.findByIdBetId(betId)
                .map(this::toDomain);
    }

    private JackpotContributionEntity toEntity(JackpotContribution contribution) {
        return JackpotContributionEntity.builder()
                .id(new JackpotContributionId(contribution.getBetId(), contribution.getJackpotId()))
                .userId(contribution.getUserId())
                .stakeAmount(contribution.getStakeAmount())
                .contributionAmount(contribution.getContributionAmount())
                .currentJackpotAmount(contribution.getCurrentJackpotAmount())
                .createdAt(contribution.getCreatedAt())
                .build();
    }

    private JackpotContribution toDomain(JackpotContributionEntity entity) {
        return JackpotContribution.builder()
                .betId(entity.getId().getBetId())
                .jackpotId(entity.getId().getJackpotId())
                .userId(entity.getUserId())
                .stakeAmount(entity.getStakeAmount())
                .contributionAmount(entity.getContributionAmount())
                .currentJackpotAmount(entity.getCurrentJackpotAmount())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}

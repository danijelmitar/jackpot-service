package com.example.jackpotservice.jackpot.infrastructure.persistence;

import com.example.jackpotservice.jackpot.domain.Jackpot;
import com.example.jackpotservice.jackpot.domain.JackpotRepository;
import com.example.jackpotservice.jackpot.domain.contribution.ContributionStrategyFactory;
import com.example.jackpotservice.jackpot.domain.reward.RewardStrategyFactory;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotEntity;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JackpotRepositoryAdapter implements JackpotRepository {

    private final JackpotJpaRepository jackpotJpaRepository;

    @Override
    public void save(Jackpot jackpot) {
        jackpotJpaRepository.save(toEntity(jackpot));
    }

    @Override
    public Optional<Jackpot> findById(String jackpotId) {
        return jackpotJpaRepository.findById(jackpotId)
                .map(this::toDomain);
    }

    private JackpotEntity toEntity(Jackpot jackpot) {
        return JackpotEntity.builder()
                .jackpotId(jackpot.getId())
                .poolAmount(jackpot.getPoolAmount())
                .initialPoolAmount(jackpot.getInitialPoolAmount())
                .contributionStrategyType(jackpot.getContributionStrategy().type())
                .rewardStrategyType(jackpot.getRewardStrategy().type())
                .build();
    }

    private Jackpot toDomain(JackpotEntity entity) {
        var contributionStrategy =
                ContributionStrategyFactory.create(entity.getContributionStrategyType());
        var rewardStrategy =
                RewardStrategyFactory.create(entity.getRewardStrategyType());
        return Jackpot.reconstitute(
                entity.getJackpotId(),
                entity.getPoolAmount(),
                entity.getInitialPoolAmount(),
                contributionStrategy,
                rewardStrategy
        );
    }

}

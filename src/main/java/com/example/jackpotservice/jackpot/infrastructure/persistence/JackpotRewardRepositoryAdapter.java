package com.example.jackpotservice.jackpot.infrastructure.persistence;

import com.example.jackpotservice.jackpot.domain.JackpotReward;
import com.example.jackpotservice.jackpot.domain.JackpotRewardRepository;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotRewardEntity;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotRewardId;
import com.example.jackpotservice.jackpot.infrastructure.persistence.jpa.JackpotRewardJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JackpotRewardRepositoryAdapter implements JackpotRewardRepository {

    private final JackpotRewardJpaRepository jackpotRewardJpaRepository;

    @Override
    public void save(JackpotReward reward) {
        jackpotRewardJpaRepository.save(toEntity(reward));
    }

    private JackpotRewardEntity toEntity(JackpotReward reward) {
        return JackpotRewardEntity.builder()
                .id(new JackpotRewardId(reward.getBetId(), reward.getJackpotId()))
                .userId(reward.getUserId())
                .rewardAmount(reward.getRewardAmount())
                .createdAt(reward.getCreatedAt())
                .build();
    }
}

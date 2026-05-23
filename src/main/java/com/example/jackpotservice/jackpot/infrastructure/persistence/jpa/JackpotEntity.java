package com.example.jackpotservice.jackpot.infrastructure.persistence.jpa;

import com.example.jackpotservice.jackpot.domain.contribution.ContributionStrategy;
import com.example.jackpotservice.jackpot.domain.reward.RewardStrategy;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Table(name = "jackpot")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "jackpotId")
public class JackpotEntity {

    @Id
    private String jackpotId;

    @Column(nullable = false)
    private BigDecimal poolAmount;

    @Column(nullable = false)
    private BigDecimal initialPoolAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContributionStrategy.StrategyType contributionStrategyType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardStrategy.StrategyType rewardStrategyType;

}

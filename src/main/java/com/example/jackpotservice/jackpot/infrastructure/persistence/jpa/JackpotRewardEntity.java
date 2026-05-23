package com.example.jackpotservice.jackpot.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "jackpot_reward")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class JackpotRewardEntity {

    @EmbeddedId
    private JackpotRewardId id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private BigDecimal rewardAmount;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}

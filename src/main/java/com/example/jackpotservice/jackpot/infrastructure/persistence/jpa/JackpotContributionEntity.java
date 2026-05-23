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
@Table(name = "jackpot_contribution")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class JackpotContributionEntity {

    @EmbeddedId
    private JackpotContributionId id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private BigDecimal stakeAmount;

    @Column(nullable = false)
    private BigDecimal contributionAmount;

    @Column(nullable = false)
    private BigDecimal currentJackpotAmount;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}

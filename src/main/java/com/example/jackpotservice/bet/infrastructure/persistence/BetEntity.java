package com.example.jackpotservice.bet.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@Entity
@Table(name = "bet")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "betId")
public class BetEntity {

    @Id
    private String betId;
    private String userId;
    private String jackpotId;
    private BigDecimal betAmount;

}

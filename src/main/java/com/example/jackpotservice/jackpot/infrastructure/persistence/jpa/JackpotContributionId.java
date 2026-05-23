package com.example.jackpotservice.jackpot.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class JackpotContributionId implements Serializable {

    @Column(nullable = false)
    private String betId;

    @Column(nullable = false)
    private String jackpotId;
}

package com.example.jackpotservice.bet.infrastructure;

import com.example.jackpotservice.bet.domain.Bet;
import com.example.jackpotservice.bet.domain.BetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BetRepositoryAdapter implements BetRepository {

    private final BetJpaRepository betJpaRepository;

    @Override
    public void save(Bet bet) {
        betJpaRepository.save(toEntity(bet));
    }

    private static BetEntity toEntity(Bet bet) {
        return BetEntity.builder()
                .betId(bet.getId())
                .userId(bet.getUserId())
                .jackpotId(bet.getJackpotId())
                .betAmount(bet.getBetAmount())
                .build();
    }
}

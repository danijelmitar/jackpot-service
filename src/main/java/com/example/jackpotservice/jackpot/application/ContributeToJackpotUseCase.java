package com.example.jackpotservice.jackpot.application;

import com.example.jackpotservice.jackpot.domain.JackpotContributionRepository;
import com.example.jackpotservice.jackpot.domain.JackpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContributeToJackpotUseCase {

    private final JackpotRepository jackpotRepository;
    private final JackpotContributionRepository jackpotContributionRepository;

    @Transactional
    public void execute(String betId, String userId, String jackpotId, BigDecimal betAmount) {
        jackpotRepository.findById(jackpotId).ifPresentOrElse(
                jackpot -> {
                    var contribution = jackpot.contribute(betId, userId, betAmount);
                    jackpotRepository.save(jackpot);
                    jackpotContributionRepository.save(contribution);
                },
                () -> log.warn("No jackpot found for id: {}", jackpotId)
        );
    }
}

package com.example.jackpotservice;

import com.example.jackpotservice.jackpot.domain.*;
import com.example.jackpotservice.jackpot.domain.contribution.FixedContributionStrategy;
import com.example.jackpotservice.jackpot.domain.contribution.VariableContributionStrategy;
import com.example.jackpotservice.jackpot.domain.reward.FixedRewardStrategy;
import com.example.jackpotservice.jackpot.domain.reward.VariableRewardStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class JackpotDataInitializer implements ApplicationRunner {

    public static final String POOL_AMOUNT = "1000.00";
    private final JackpotRepository jackpotRepository;

    @Override
    public void run(ApplicationArguments args) {
        seedFixedJackpot();
        seedVariableJackpot();
    }

    private void seedFixedJackpot() {
        if (jackpotRepository.findById("jackpot-fixed").isEmpty()) {
            jackpotRepository.save(Jackpot.reconstitute(
                    "jackpot-fixed",
                    new BigDecimal(POOL_AMOUNT),
                    new BigDecimal(POOL_AMOUNT),
                    new FixedContributionStrategy(),
                    new FixedRewardStrategy()
            ));
            log.info("Seeded fixed jackpot");
        }
    }

    private void seedVariableJackpot() {
        if (jackpotRepository.findById("jackpot-variable").isEmpty()) {
            jackpotRepository.save(Jackpot.reconstitute(
                    "jackpot-variable",
                    new BigDecimal(POOL_AMOUNT),
                    new BigDecimal(POOL_AMOUNT),
                    new VariableContributionStrategy(),
                    new VariableRewardStrategy()
            ));
            log.info("Seeded variable jackpot");
        }
    }
}

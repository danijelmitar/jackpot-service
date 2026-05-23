package com.example.jackpotservice.jackpot.application;

import com.example.jackpotservice.jackpot.domain.Jackpot;
import com.example.jackpotservice.jackpot.domain.JackpotContribution;
import com.example.jackpotservice.jackpot.domain.JackpotContributionRepository;
import com.example.jackpotservice.jackpot.domain.JackpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContributeToJackpotUseCaseShould {

    @Mock
    private JackpotRepository jackpotRepository;

    @Mock
    private JackpotContributionRepository jackpotContributionRepository;

    @Mock
    private Jackpot jackpot;

    private ContributeToJackpotUseCase contributeToJackpotUseCase;

    @BeforeEach
    void setUp() {
        contributeToJackpotUseCase = new ContributeToJackpotUseCase(
                jackpotRepository, jackpotContributionRepository);
    }

    @Test
    void contribute_to_jackpot_when_jackpot_exists() {
        when(jackpotRepository.findById("jackpot-1")).thenReturn(Optional.of(jackpot));

        contributeToJackpotUseCase.execute("bet-1", "user-1", "jackpot-1", new BigDecimal("10.00"));

        verify(jackpot).contribute("bet-1", "user-1", new BigDecimal("10.00"));
    }

    @Test
    void save_jackpot_contribution_after_contribution_to_jackpot() {
        var contribution = JackpotContribution.builder()
                .betId("bet-1").userId("user-1").jackpotId("jackpot-1")
                .stakeAmount(new BigDecimal("10.00")).contributionAmount(new BigDecimal("1.00"))
                .currentJackpotAmount(new BigDecimal("1001.00")).createdAt(LocalDateTime.now())
                .build();
        when(jackpotRepository.findById("jackpot-1")).thenReturn(Optional.of(jackpot));
        when(jackpot.contribute("bet-1", "user-1", new BigDecimal("10.00"))).thenReturn(contribution);
        var inOrder = inOrder(jackpot, jackpotRepository, jackpotContributionRepository);

        contributeToJackpotUseCase.execute("bet-1", "user-1", "jackpot-1", new BigDecimal("10.00"));

        inOrder.verify(jackpot).contribute("bet-1", "user-1", new BigDecimal("10.00"));
        inOrder.verify(jackpotRepository).save(jackpot);
        inOrder.verify(jackpotContributionRepository).save(contribution);
    }

    @Test
    void discard_event_when_jackpot_not_found() {
        when(jackpotRepository.findById("jackpot-1")).thenReturn(Optional.empty());

        contributeToJackpotUseCase.execute("bet-1", "user-1", "jackpot-1", new BigDecimal("10.00"));

        verify(jackpot, never()).contribute(any(), any(), any());
        verify(jackpotRepository, never()).save(any());
        verify(jackpotContributionRepository, never()).save(any());
    }
}

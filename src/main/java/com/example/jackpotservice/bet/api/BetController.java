package com.example.jackpotservice.bet.api;

import com.example.jackpotservice.bet.application.PlaceBetUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/bets")
@RequiredArgsConstructor
public class BetController {

    private final PlaceBetUseCase placeBetUseCase;

    @PostMapping
    public ResponseEntity<BetResponse> placeBet(@RequestBody @Valid BetRequest betRequest) {
        var bet = placeBetUseCase.execute(
                betRequest.userId(),
                betRequest.jackpotId(),
                betRequest.amount()
        );

        return ResponseEntity.accepted().body(new BetResponse(bet.getId()));
    }

}

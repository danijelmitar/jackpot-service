package com.example.jackpotservice.bet.infrastructure.messaging;

import com.example.jackpotservice.bet.domain.Bet;
import com.example.jackpotservice.common.messaging.BetPlacedMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaBetProducerShould {

    @Mock
    private KafkaTemplate<String, BetPlacedMessage> kafkaTemplate;

    private KafkaBetProducer kafkaBetProducer;

    @BeforeEach
    void setUp() {
        kafkaBetProducer = new KafkaBetProducer(kafkaTemplate);
    }

    @Test
    void publish_bet_to_correct_topic_with_jackpot_id_as_key() {
        var bet = Bet.place("user-1", "jackpot-1", new BigDecimal("10.00"));
        var topicCaptor = ArgumentCaptor.forClass(String.class);
        var keyCaptor = ArgumentCaptor.forClass(String.class);
        var messageCaptor = ArgumentCaptor.forClass(BetPlacedMessage.class);

        kafkaBetProducer.publish(bet);

        verify(kafkaTemplate).send(topicCaptor.capture(), keyCaptor.capture(), messageCaptor.capture());
        assertThat(topicCaptor.getValue()).isEqualTo(KafkaBetProducer.TOPIC);
        assertThat(keyCaptor.getValue()).isEqualTo("jackpot-1");
        var message = messageCaptor.getValue();
        assertThat(message.betId()).isEqualTo(bet.getId());
        assertThat(message.userId()).isEqualTo("user-1");
        assertThat(message.jackpotId()).isEqualTo("jackpot-1");
        assertThat(message.betAmount()).isEqualByComparingTo("10.00");
    }
}

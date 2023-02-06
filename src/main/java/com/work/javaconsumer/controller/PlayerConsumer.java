package com.work.javaconsumer.controller;

import com.work.javaconsumer.PlayerAlreadyExistException;
import com.work.javaconsumer.service.PlayerConsumerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.work.kafka.api.RegisterPlayerDTO;
import org.work.kafka.api.WinDepositDTO;

import static com.work.javaconsumer.util.Constants.*;

@Component
public class PlayerConsumer {

    private final PlayerConsumerService playerConsumerService;

    public PlayerConsumer(PlayerConsumerService playerConsumerService) {
        this.playerConsumerService = playerConsumerService;

    }
    // Register new player!
    @KafkaListener(topics = REGISTER_TOPIC, groupId = GROUP_ID)
    public RegisterPlayerDTO registerPlayer(RegisterPlayerDTO registerPlayerDTO) {
        return playerConsumerService.registerPlayer(registerPlayerDTO);
    }

    // Deposit win amount!
    @KafkaListener(topics = DEPOSIT_TOPIC, groupId = GROUP_ID)
    public WinDepositDTO deposit(WinDepositDTO winDepositDTO) {
        return playerConsumerService.deposit(winDepositDTO);
    }
}

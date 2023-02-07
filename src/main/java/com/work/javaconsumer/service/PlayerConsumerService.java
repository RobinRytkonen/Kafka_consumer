package com.work.javaconsumer.service;

import com.work.javaconsumer.entity.Player;
import com.work.javaconsumer.entity.PlayerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.work.kafka.api.RegisterPlayerDTO;
import org.work.kafka.api.WinDepositDTO;

@Service
public class PlayerConsumerService {

    private static final Logger log = LogManager.getLogger(PlayerConsumerService.class);
    private final PlayerRepository playerRepository;

    public PlayerConsumerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public RegisterPlayerDTO registerPlayer(RegisterPlayerDTO registerPlayerDTO) {
        if (playerRepository.findByEmail(registerPlayerDTO.getEmail()).isPresent()) {
            log.info("A player already registered with that email!");
            return null;
        }
        Player newPlayer = new Player(
                registerPlayerDTO.getName(),
                registerPlayerDTO.getEmail(),
                registerPlayerDTO.getBalance());
        playerRepository.save(newPlayer);

        log.info("Created Player: "
                + "Name: " + newPlayer.getName()
                + ", Email: " + newPlayer.getEmail());
        return registerPlayerDTO;
    }

    public WinDepositDTO deposit(WinDepositDTO winDepositDTO) {
        if (playerRepository.findById(winDepositDTO.getPlayerId()).isEmpty()) {
            log.info("No registered player with that id!");
            return null;
        }
        Player player = playerRepository.findById(winDepositDTO.getPlayerId()).get();
        player.setBalance(winDepositDTO.getWinAmount() + player.getBalance());
        playerRepository.save(player);

        log.info("Deposited = "
                + "Amount: " + winDepositDTO.getWinAmount()
                + ", Balance: " + player.getBalance()
                + ", Name: " + player.getName()
                + ", PlayerID: " + winDepositDTO.getPlayerId());
        return winDepositDTO;
    }
}

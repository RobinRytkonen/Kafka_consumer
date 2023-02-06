package com.work.javaconsumer.service;

import com.work.javaconsumer.entity.Player;
import com.work.javaconsumer.entity.PlayerRepository;
import org.springframework.stereotype.Service;
import org.work.kafka.api.RegisterPlayerDTO;
import org.work.kafka.api.WinDepositDTO;

@Service
public class PlayerConsumerService {

    private final PlayerRepository playerRepository;

    public PlayerConsumerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public RegisterPlayerDTO registerPlayer(RegisterPlayerDTO registerPlayerDTO) {
        if (playerRepository.findByName(registerPlayerDTO.getName()).isPresent()) {
            System.out.println("A player already registered with that name!");

        }

        Player newPlayer = new Player(
                registerPlayerDTO.getName(),
                registerPlayerDTO.getEmail(),
                registerPlayerDTO.getBalance());
        playerRepository.save(newPlayer);

        System.out.println("Created Player: "
                + "Name: " + newPlayer.getName()
                + ", Email: " + newPlayer.getEmail());
        return registerPlayerDTO;
    }

    public WinDepositDTO deposit(WinDepositDTO winDepositDTO) {
        if (playerRepository.findById(winDepositDTO.getPlayerId()).isEmpty()) {
            System.out.println("No registered player with that id!");
        }

        Player player = playerRepository.findById(winDepositDTO.getPlayerId()).get();
        player.setBalance(winDepositDTO.getWinAmount() + player.getBalance());
        playerRepository.save(player);

        System.out.println("Deposited = "
                + "Amount: " + winDepositDTO.getWinAmount()
                + ", Balance: " + player.getBalance()
                + ", Name: " + player.getName()
                + ", PlayerID: " + winDepositDTO.getPlayerId());
        return winDepositDTO;
    }
}

package com.work.javaconsumer.Service;
// ToDo space here and Service is better to write from a small letter
import com.work.javaconsumer.Entity.Player;
import com.work.javaconsumer.Entity.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.work.kafka.api.RegisterPlayerDTO;
import org.work.kafka.api.WinDepositDTO;
import static com.work.javaconsumer.Config.Constants.*;

//ToDo I recommend you to separate PlayerConsumer and PlayerEventService for example
@Component
public class PlayerConsumer {

    private final PlayerRepository playerRepository;

    public PlayerConsumer(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    // Register new player!
    @KafkaListener(topics = REGISTER_TOPIC, groupId = GROUP_ID)
    public void registerPlayer(RegisterPlayerDTO registerPlayerDTO) {
        if (playerRepository.findByName(registerPlayerDTO.getName()).isPresent()) {
            System.out.println("A player already registered with that name!");
            return;
        }
        Player newPlayer = new Player(
                registerPlayerDTO.getName(),
                registerPlayerDTO.getEmail(),
                registerPlayerDTO.getBalance());
        playerRepository.save(newPlayer);

        System.out.println("Created Player: "
                + "Name: " + newPlayer.getName()
                + ", Email: " + newPlayer.getEmail());
    }

    // Deposit win amount!
    @KafkaListener(topics = DEPOSIT_TOPIC, groupId = GROUP_ID)
    public void deposit(WinDepositDTO winDepositDTO) {
        if (playerRepository.findById(winDepositDTO.getPlayerId()).isEmpty()) {
            System.out.println("No registered player with that id!");
            return;
        }

        Player player = playerRepository.findById(winDepositDTO.getPlayerId()).get();
        player.setBalance(winDepositDTO.getWinAmount() + player.getBalance());
        playerRepository.save(player);

        System.out.println("Deposited = "
                + "Amount: " + winDepositDTO.getWinAmount()
                + ", Balance: " + player.getBalance()
                + ", Name: " + player.getName()
                + ", PlayerID: " + winDepositDTO.getPlayerId());
    }
}
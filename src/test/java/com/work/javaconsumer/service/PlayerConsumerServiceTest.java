package com.work.javaconsumer.service;

import com.work.javaconsumer.entity.Player;
import com.work.javaconsumer.entity.PlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.work.kafka.api.RegisterPlayerDTO;
import org.work.kafka.api.WinDepositDTO;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerConsumerServiceTest {

    @Mock
    PlayerRepository playerRepository;

    @InjectMocks
    PlayerConsumerService playerConsumerService;

    @Test
    public void should_find_one_player()  {
        //given
        RegisterPlayerDTO dto = new RegisterPlayerDTO("Bobba", "Bobzoor@gmail.com", 0);
        Player newPlayer = new Player(dto.getName(), dto.getEmail(), dto.getBalance());

        when(playerRepository.findByName(newPlayer.getName())).thenReturn(Optional.of(newPlayer));

        //when
        playerConsumerService.registerPlayer(dto);

        //then
        Assertions.assertEquals(dto.getName(), newPlayer.getName());
        verify(playerRepository, times(1)).findByName(newPlayer.getName());
    }

    @Test
    public void should_save_one_player()  {
        //given
        RegisterPlayerDTO dto = new RegisterPlayerDTO("Bobba", "Bobzoor@gmail.com", 0);
        Player newPlayer = new Player(dto.getName(), dto.getEmail(), dto.getBalance());

        when(playerRepository.save(any(Player.class))).thenReturn(newPlayer);

        //when
        playerConsumerService.registerPlayer(dto);

        //then
        Assertions.assertEquals(dto.getName(), newPlayer.getName());
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    public void should_deposit_in_balance() {
        //given
        Player newPlayer = new Player("Bob", "Bobzoor@gmail.com", 0);
        WinDepositDTO dto = new WinDepositDTO(newPlayer.getPlayerId(), 500);

        when(playerRepository.findById(dto.getPlayerId())).thenReturn(Optional.of(newPlayer));

        //when
        playerConsumerService.deposit(dto);

        //then
        Assertions.assertEquals(500, newPlayer.getBalance());
        verify(playerRepository, times(2)).findById(dto.getPlayerId());
        verify(playerRepository, times(1)).save(any(Player.class));
    }

}
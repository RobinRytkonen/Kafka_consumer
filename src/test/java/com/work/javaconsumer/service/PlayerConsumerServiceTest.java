package com.work.javaconsumer.service;

import com.work.javaconsumer.entity.Player;
import com.work.javaconsumer.entity.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.work.kafka.api.RegisterPlayerDTO;
import org.work.kafka.api.WinDepositDTO;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerConsumerServiceTest {

    @Mock
    PlayerRepository playerRepository;

    @InjectMocks
    PlayerConsumerService playerConsumerService;

    @Captor
    ArgumentCaptor<Player> playerArgumentCaptor;

    @Test
    void should_find_one_player_by_email()  {
        //given
        RegisterPlayerDTO dto = new RegisterPlayerDTO("Bobba", "Bobzoor@gmail.com", 0);
        Player newPlayer = new Player(dto.getName(), dto.getEmail(), dto.getBalance());

        when(playerRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(newPlayer));

        //when
        playerConsumerService.registerPlayer(dto);

        //then
        verify(playerRepository, times(1)).findByEmail(dto.getEmail());
    }
    @Test
    void should_find_one_player_by_id()  {
        //given
        Player newPlayer = new Player("Bob", "Bobzoor@gmail.com", 0);
        WinDepositDTO dto = new WinDepositDTO(newPlayer.getPlayerId(), 500);

        when(playerRepository.findById(newPlayer.getPlayerId())).thenReturn(Optional.of(newPlayer));

        //when
        playerConsumerService.deposit(dto);

        //then
        verify(playerRepository, times(2)).findById(newPlayer.getPlayerId());
    }

    @Test
    void should_save_one_player()  {
        //given
        RegisterPlayerDTO dto = new RegisterPlayerDTO("Bobba", "Bobzoor@gmail.com", 0);
        Player newPlayer = new Player(dto.getName(), dto.getEmail(), dto.getBalance());

        when(playerRepository.save(any(Player.class))).thenReturn(newPlayer);

        //when
        playerConsumerService.registerPlayer(dto);

        //then
        verify(playerRepository, times(1)).save(playerArgumentCaptor.capture()); // capture the object that is passed through the save method.
        assertEquals("Bobba", playerArgumentCaptor.getValue().getName());
        assertEquals("Bobzoor@gmail.com", playerArgumentCaptor.getValue().getEmail());
        assertEquals(0, playerArgumentCaptor.getValue().getBalance());
    }

    @Test
    void should_deposit_in_balance() {
        //given
        Player newPlayer = new Player("Bob", "Bobzoor@gmail.com", 0);
        WinDepositDTO dto = new WinDepositDTO(newPlayer.getPlayerId(), 500);

        when(playerRepository.findById(dto.getPlayerId())).thenReturn(Optional.of(newPlayer));

        //when
        playerConsumerService.deposit(dto);

        //then
        verify(playerRepository, times(2)).findById(dto.getPlayerId());
        verify(playerRepository, times(1)).save(playerArgumentCaptor.capture());
        assertEquals(500, playerArgumentCaptor.getValue().getBalance());
        assertEquals("Bob", playerArgumentCaptor.getValue().getName());
        assertEquals("Bobzoor@gmail.com", playerArgumentCaptor.getValue().getEmail());
    }
}
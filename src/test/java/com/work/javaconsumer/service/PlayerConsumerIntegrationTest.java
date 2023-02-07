package com.work.javaconsumer.service;

import com.work.javaconsumer.entity.PlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.work.kafka.api.RegisterPlayerDTO;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerConsumerIntegrationTest {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PlayerConsumerService playerConsumerService;

    @Test
    public void should_save_one_player000() {
        //given
        RegisterPlayerDTO dto = new RegisterPlayerDTO("Bob1", "Bobzoor@gmail.com1", 0);

        //when
        RegisterPlayerDTO registerPlayerDTO = playerConsumerService.registerPlayer(dto);

        //then
        Assertions.assertEquals(dto.getEmail(), playerRepository.findByName(registerPlayerDTO.getName()).get().getEmail());
    }
}

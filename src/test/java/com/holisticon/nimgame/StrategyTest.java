package com.holisticon.nimgame;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class StrategyTest {

    @Autowired GameStateService gameStateService;

    @Autowired ComputerService computerService;

    @Test
    public void testOptimalMoveByComputer() {

        //Set optimal strategy
        this.computerService.setStrategy(true);

        this.gameStateService.drawMatches(1, PlayerEnum.HUMAN);
        assertThat(this.computerService.makeMove()).isEqualTo(3);

        this.gameStateService.drawMatches(2, PlayerEnum.HUMAN);
        assertThat(this.computerService.makeMove()).isEqualTo(2);

        this.gameStateService.drawMatches(3, PlayerEnum.HUMAN);
        assertThat(this.computerService.makeMove()).isEqualTo(1);
    }
}

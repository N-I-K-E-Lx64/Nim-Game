package com.holisticon.nimgame;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class GameStateTest {

    @Autowired GameStateService gameStateService;

    @Autowired ComputerService computerService;

    @Test
    public void whenDrawingMatches_correctGameState() {
        assertThat(this.gameStateService.drawMatches(2, PlayerEnum.HUMAN)).isEqualTo(2);
        assertThat(this.gameStateService.getDrawnMatches()).isEqualTo(2);
        assertThat(this.gameStateService.getAvailableMatches()).isEqualTo(13 - 2);
    }

    @Test
    public void whenHumanDrawsLastMatch_thenComputerWins() {
        this.gameStateService.drawMatches(3, PlayerEnum.HUMAN);
        this.gameStateService.drawMatches(3, PlayerEnum.COMPUTER);

        assertThat(this.gameStateService.winner()).isEqualTo(PlayerEnum.NOBODY);

        this.gameStateService.drawMatches(3, PlayerEnum.HUMAN);
        this.gameStateService.drawMatches(3, PlayerEnum.COMPUTER);

        assertThat(this.gameStateService.winner()).isEqualTo(PlayerEnum.NOBODY);

        this.gameStateService.drawMatches(1, PlayerEnum.HUMAN);
        assertThat(this.gameStateService.winner()).isEqualTo(PlayerEnum.COMPUTER);
    }

    @Test
    public void whenComputerDrawsLastMatch_thenHumanWins() {
        this.gameStateService.drawMatches(3, PlayerEnum.COMPUTER);
        this.gameStateService.drawMatches(3, PlayerEnum.HUMAN);

        assertThat(this.gameStateService.winner()).isEqualTo(PlayerEnum.NOBODY);

        this.gameStateService.drawMatches(3, PlayerEnum.COMPUTER);
        this.gameStateService.drawMatches(3, PlayerEnum.HUMAN);

        assertThat(this.gameStateService.winner()).isEqualTo(PlayerEnum.NOBODY);

        this.gameStateService.drawMatches(1, PlayerEnum.COMPUTER);
        assertThat(this.gameStateService.winner()).isEqualTo(PlayerEnum.HUMAN);
    }

    @Test
    public void whenDrawingMoreThanThreeMatches_thenException() {
        Exception exception = assertThrows(
                RuntimeException.class, () -> this.gameStateService.drawMatches(4, PlayerEnum.HUMAN));

        String expectedMessage = "Not less than 1 and not more than 3 matches can be drawn in one turn!";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void whenDrawingLessThanThreeMatches_thenException() {
        Exception exception = assertThrows(
                RuntimeException.class, () -> this.gameStateService.drawMatches(0, PlayerEnum.HUMAN));

        String expectedMessage = "Not less than 1 and not more than 3 matches can be drawn in one turn!";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void whenDrawingMoreMatchesThanAvailable_thenException() {
        this.gameStateService.drawMatches(3, PlayerEnum.HUMAN);
        this.gameStateService.drawMatches(3, PlayerEnum.COMPUTER);

        // 7 matches available
        this.gameStateService.drawMatches(3, PlayerEnum.HUMAN);
        this.gameStateService.drawMatches(3, PlayerEnum.COMPUTER);

        // 1 matchstick available

        assertThrows(
                RuntimeException.class, () -> this.gameStateService.drawMatches(2, PlayerEnum.HUMAN));
    }

    @Test
    public void whenGameHasEnded_thenDrawingMatch_thenException() {
        this.gameStateService.drawMatches(3, PlayerEnum.COMPUTER);
        this.gameStateService.drawMatches(3, PlayerEnum.HUMAN);

        this.gameStateService.drawMatches(3, PlayerEnum.COMPUTER);
        this.gameStateService.drawMatches(3, PlayerEnum.HUMAN);

        this.gameStateService.drawMatches(1, PlayerEnum.COMPUTER);

        Exception exception = assertThrows(
                RuntimeException.class, () -> this.gameStateService.drawMatches(2, PlayerEnum.HUMAN));

        String expectedMessage = "No more matches can be drawn after the end of the game!";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void whenGameHasRestarted_thenDefaultState() {
        this.gameStateService.drawMatches(3, PlayerEnum.HUMAN);
        this.gameStateService.drawMatches(3, PlayerEnum.COMPUTER);

        // Restart the game
        this.gameStateService.resetState();

        assertThat(this.gameStateService.getAvailableMatches()).isEqualTo(13);
        assertThat(this.gameStateService.getDrawnMatches()).isEqualTo(0);
    }
}

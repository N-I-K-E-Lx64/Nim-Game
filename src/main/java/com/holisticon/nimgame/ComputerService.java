package com.holisticon.nimgame;

import java.util.Random;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;

@Service
public class ComputerService {

  private boolean playsOptimalStrategy;
  private final GameStateService gameStateService;

  private final Random rand = new Random();

  /**
   * CTOR - Default strategy is random.
   * @param gameStateService Reference for the game state injection.
   */
  public ComputerService(GameStateService gameStateService) {
    this.playsOptimalStrategy = false;

    this.gameStateService = gameStateService;
  }

  /**
   * Calculates the computer's move based on the selected strategy.
   * @return The number of matches drawn by the computer
   */
  public int makeMove() {
    int drawnMatches = this.gameStateService.getDrawnMatches();
    int availableMatches = this.gameStateService.getAvailableMatches();

    // If the player has already lost the game, the computer draws 0.
    if (availableMatches == 0) return 0;

    // The maximum number of matches the computer can draw.
    int range = Math.min(availableMatches, 3);

    if (!playsOptimalStrategy) {
      // Random number between 1 and the previously computed maximum.
      int randomCount = rand.ints(1, (range + 1))
          .findFirst()
          .getAsInt();

      this.gameStateService.drawMatches(randomCount, PlayerEnum.COMPUTER);

      return randomCount;
    } else {
      /*The computer always wins if he draws the 12th matchstick.
      Since both players can draw max. 3 matches per turn the computer can control the human in steps of 4.
      -> Each number modulo 4 == 0 is a winning point (4,8,12)
      -> The computer tries to get to draw matches until he reaches such a point.*/
      int optimalCount = IntStream.rangeClosed(1, range)
          .boxed()
          .filter(possibleMove -> (drawnMatches + possibleMove) % 4 == 0)
          .findFirst()
          .orElseThrow(RuntimeException::new);

      this.gameStateService.drawMatches(optimalCount, PlayerEnum.COMPUTER);

      return optimalCount;
    }
  }

  public void setStrategy(boolean playsOptimalStrategy) {
    this.playsOptimalStrategy = playsOptimalStrategy;
  }

}

package com.holisticon.nimgame;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * Service that is responsible for managing the game state.
 */
@Service
public class GameStateService {

  private static final Logger logger = LoggerFactory.getLogger(GameStateService.class);

  private List<Matchstick> matchstickPile;
  private PlayerEnum winner;
  private boolean gameHasStarted;

  /**
   * Ctor - Initializes the Matchstick pile and sets the winner to "nobody"
   */
  @Autowired
  public GameStateService() {
    this.matchstickPile = resetPile();
    this.winner = PlayerEnum.NOBODY;
    this.gameHasStarted = false;
  }

  /**
   * Draws the specified number of matches.
   * @param drawnMatches Number of matches to be drawn
   * @param playerReference Reference to the player that draws the matches (either Human or Computer)
   * @return The number of matches actually drawn.
   */
  public int drawMatches(@NonNull final int drawnMatches, @NonNull final PlayerEnum playerReference) {
    // Starts the game
    if (!this.gameHasStarted) this.gameHasStarted = true;

    // Prevents errors when the game has ended and the user still sends draw-requests
    if (getAvailableMatches() == 0) {
      throw new RuntimeException("You cannot draw matches since the game has already ended!");
    }

    // Checks if the number of matches to be drawn is available in the stack
    if (getAvailableMatches() >= drawnMatches) {
      logger.info(playerReference + " draws " + drawnMatches + " matches.");

      // Draws the matches
      for (int i = 0; i < drawnMatches; i++) {
        drawMatch(playerReference);
      }
    } else {
      throw new RuntimeException(MessageFormat.format(
          "You want to draw {0} matches, but there are only {1} matches available!", drawnMatches, getAvailableMatches()));
    }

    logger.info("Available matches: " + getAvailableMatches());
    logger.info("Drawn matches: " + getDrawnMatches());

    // Check the win-condition (There are no available matches and both players together have drawn 13 matches).
    if (getAvailableMatches() == 0 && getDrawnMatches() == 13) {
      this.winner = switch (playerReference) {
        case HUMAN -> PlayerEnum.COMPUTER;
        case COMPUTER -> PlayerEnum.HUMAN;
        case NOBODY -> throw new RuntimeException("There must be a winner, that is either the player or the computer!");
      };
    }

    return drawnMatches;
  }

  /**
   * Searches the array for the first unassigned matchstick and assigns it to the respective player.
   * @param playerReference Reference to the player that draws the matchstick (either Human or Computer)
   */
  public void drawMatch(@NonNull final PlayerEnum playerReference) {
    matchstickPile.stream()
        .filter(matchstick -> matchstick.playerReference() == PlayerEnum.NOBODY)
        .findFirst()
        .ifPresent(matchstick -> matchstick.setPlayerReference(playerReference));
  }

  /**
   * Counts the number of matches that are not yet assigned to a player.
   * @return Number of unsigned matches
   */
  public int getAvailableMatches() {
    return (int) matchstickPile.stream()
        .filter(matchstick -> matchstick.playerReference() == PlayerEnum.NOBODY)
        .count();
  }

  /**
   * Counts the number of matches assigned to one of both players.
   * @return Number of assigned matches
   */
  public int getDrawnMatches() {
    return (int) matchstickPile.stream()
        .filter(matchstick -> matchstick.playerReference() != PlayerEnum.NOBODY)
        .count();
  }

  /**
   * Resets the game state
   */
  public void resetState() {
    this.matchstickPile = resetPile();
    this.winner = PlayerEnum.NOBODY;
    this.gameHasStarted = false;
  }

  /**
   * Resets the matchstick pile by creating a new list.
   * @return New matchstick pile
   */
  public List<Matchstick> resetPile() {
    return IntStream.range(0, 13)
        .boxed()
        .map(Matchstick::new)
        .toList();
  }

  public List<Matchstick> matchstickPile() {
    return matchstickPile;
  }

  public PlayerEnum winner() {
    return winner;
  }

  public boolean gameHasStarted() {
    return gameHasStarted;
  }
}

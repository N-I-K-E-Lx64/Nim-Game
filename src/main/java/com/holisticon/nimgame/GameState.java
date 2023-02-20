package com.holisticon.nimgame;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class GameState {

  private List<Matchstick> matchstickPile;
  private PlayerEnum winner;
  private boolean gameHasStarted;

  @Autowired
  public GameState() {
    this.matchstickPile = resetPile();
    this.winner = PlayerEnum.NOBODY;
    this.gameHasStarted = false;
  }

  public void drawMatches(@NonNull final int drawnMatches) {
    this.gameHasStarted = true;

    // Check if the
    long availableMatches = matchstickPile.stream()
        .filter(matchstick -> matchstick.playerReference() == PlayerEnum.NOBODY)
        .count();

    if (availableMatches > drawnMatches) {
      for (int i = 0; i < drawnMatches; i++) {
        drawMatch(PlayerEnum.PLAYER);
      }
    } else {
      throw new RuntimeException(MessageFormat.format(
          "You want to draw {0} matches, but there are only {1} matches available!", drawnMatches, availableMatches));
    }
  }

  public void drawMatch(@NonNull final PlayerEnum playerReference) {
    matchstickPile.stream()
        .filter(matchstick -> matchstick.playerReference() == PlayerEnum.NOBODY)
        .findFirst()
        .ifPresent(matchstick -> matchstick.setPlayerReference(playerReference));
  }


  public void resetState() {
    this.matchstickPile = resetPile();
    this.winner = PlayerEnum.NOBODY;
    this.gameHasStarted = false;
  }

  public List<Matchstick> resetPile() {
    return IntStream.range(0, 12)
        .boxed()
        .map(Matchstick::new)
        .toList();
  }

  public List<Matchstick> matchstickPile() {
    return matchstickPile;
  }

  public boolean gameHasStarted() {
    return gameHasStarted;
  }
}

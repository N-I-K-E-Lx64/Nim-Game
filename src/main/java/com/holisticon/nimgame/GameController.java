package com.holisticon.nimgame;

import java.text.MessageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

  private static final Logger logger = LoggerFactory.getLogger(GameController.class);

  private final GameState gameState;

  @Autowired
  public GameController(GameState gameState) {
    this.gameState = gameState;
  }

  @CrossOrigin
  @GetMapping(value = "/start", produces = "application/json")
  public ResponseEntity<?> startGame() {

    // Check if the game has already started. If that's the case the game state is reset.
    if (this.gameState.gameHasStarted()) {
      logger.info("The game has already started. The game state is reset.");

      this.gameState.resetState();
    }

    return ResponseEntity.ok().body(this.gameState.matchstickPile());
  }

  @CrossOrigin
  @PostMapping(value = "/draw", produces = "application/json")
  public ResponseEntity<?> drawMatches(@RequestBody DrawMatchesRequest request) {

    logger.info(MessageFormat.format("The player want's to draw {0} matches", request.drawnMatches));

    if (request.drawnMatches < 1 || request.drawnMatches > 3)
      return ResponseEntity.badRequest().body("ERROR : You can draw only one, two, or three matches! Please try again!");

    this.gameState.drawMatches(request.drawnMatches);

    return ResponseEntity.ok().body(this.gameState.matchstickPile());
  }
}

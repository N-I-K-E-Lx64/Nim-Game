package com.holisticon.nimgame;

import com.holisticon.nimgame.request.DrawMatchesRequest;
import com.holisticon.nimgame.request.StartGameRequest;
import com.holisticon.nimgame.response.MoveResponse;
import com.holisticon.nimgame.response.StartGameResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller which is used to communicate with the user.
 */
@RestController
public class GameController {

  private static final Logger logger = LoggerFactory.getLogger(GameController.class);

  private final GameStateService gameStateService;
  private final ComputerService computerService;

  /**
   * CTOR: Injects the needed services
   * @param gameStateService Service that holds the game state and most of the game logic
   * @param computerService Service that is responsible for computing the computer moves
   */
  @Autowired
  public GameController(GameStateService gameStateService, ComputerService computerService) {
    this.gameStateService = gameStateService;
    this.computerService = computerService;
  }

  /**
   * Starts a new game. If the game is already running this can be used to clear the old game state.
   * @param request Player request - Contains the strategy for the computer (should he play random or optimally)
   * @return StartGameResponse - Overview over the matchstick pile and the selected computer strategy
   * @see StartGameResponse
   */
  @CrossOrigin
  @PostMapping(value = "/start", produces = "application/json")
  public ResponseEntity<StartGameResponse> startGame(@RequestBody StartGameRequest request) {

    // Set the computer strategy.
    this.computerService.setStrategy(request.playsOptimalStrategy());

    // Check if the game has already started. If that's the case the game state is reset.
    if (this.gameStateService.gameHasStarted()) {
      logger.info("The game has already started. The game state is reset.");

      this.gameStateService.resetState();
    }

    logger.info("A new game has been started.");

    return ResponseEntity.ok().body(new StartGameResponse(
        "A new game has been started!",
        request.playsOptimalStrategy(),
        this.gameStateService.matchstickPile()
    ));
  }

  /**
   * Handles the draw matches request by the user. In this case the player always draws first. After
   * the player's move is completely handled, the computer's move is calculated based on the selected
   * strategy.
   * @param request Player request - Contains the number of matches to be drawn.
   * @return MoveResponse - Contains information about the moves of both players (how many matches
   * each party has drawn), the possible winner and an overview over the matchstick pile
   * @see MoveResponse
   */
  @CrossOrigin
  @PostMapping(value = "/draw", produces = "application/json")
  public ResponseEntity<MoveResponse> drawMatches(@RequestBody DrawMatchesRequest request) {

    logger.info("The player wants to draw " + request.drawnMatches() + " matches.");

    // Checks whether the request parameter (drawn matches) matches the guidelines
    if (request.drawnMatches() < 1 || request.drawnMatches() > 3) {
      throw new RuntimeException(
          "ERROR : You can draw only one, two, or three matches! Please try again!");
    }

    // Handle player turn
    int playerMove = this.gameStateService.drawMatches(request.drawnMatches(), PlayerEnum.HUMAN);

    // Compute and handle computer turn
    int computerMove = this.computerService.makeMove();

    return ResponseEntity.ok().body(new MoveResponse(
        "Test",
        playerMove,
        computerMove,
        this.gameStateService.getAvailableMatches(),
        this.gameStateService.winner(),
        this.gameStateService.matchstickPile()
    ));
  }
}

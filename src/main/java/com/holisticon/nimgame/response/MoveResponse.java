package com.holisticon.nimgame.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.holisticon.nimgame.Matchstick;
import com.holisticon.nimgame.PlayerEnum;
import java.util.List;

public record MoveResponse(
    @JsonProperty ("message") String message,
    @JsonProperty ("player_move") int playerMove,
    @JsonProperty ("computer_move") int computerMove,
    @JsonProperty ("available_matches") int availableMatches,
    @JsonProperty ("winner") PlayerEnum winner,
    @JsonProperty ("game state") List<Matchstick> gameState
    ) {

}

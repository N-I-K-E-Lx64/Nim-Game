package com.holisticon.nimgame.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.holisticon.nimgame.Matchstick;
import java.util.List;

public record StartGameResponse(
    @JsonProperty("message") String message,
    @JsonProperty("optimal_strategy") boolean optimalStrategy,
    @JsonProperty("game_state") List<Matchstick> gameState
) {

}

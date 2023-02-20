package com.holisticon.nimgame.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StartGameRequest(
    @JsonProperty("strategy") boolean playsOptimalStrategy
) {

}

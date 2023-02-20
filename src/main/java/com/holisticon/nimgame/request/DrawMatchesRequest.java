package com.holisticon.nimgame.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DrawMatchesRequest(
    @JsonProperty("matches") int drawnMatches
) {

}

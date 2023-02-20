package com.holisticon.nimgame;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DrawMatchesRequest {

  @JsonProperty("matches")
  public int drawnMatches;

}

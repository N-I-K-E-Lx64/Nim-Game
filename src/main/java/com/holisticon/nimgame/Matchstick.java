package com.holisticon.nimgame;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public class Matchstick {

  @JsonProperty("Id")
  private int id;

  @JsonProperty("Player")
  private PlayerEnum playerReference;

  public Matchstick(@NonNull final int id) {
    this.id = id;
    this.playerReference = PlayerEnum.NOBODY;
  }

  public PlayerEnum playerReference() {
    return playerReference;
  }

  public void setPlayerReference(PlayerEnum playerReference) {
    this.playerReference = playerReference;
  }
}

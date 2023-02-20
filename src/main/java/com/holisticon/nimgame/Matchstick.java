package com.holisticon.nimgame;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

/**
 * Matchstick object.
 */
public class Matchstick {

  @JsonProperty("Id")
  private int id;

  // Represents the player who drew the matchstick.
  @JsonProperty("Drawn by")
  private PlayerEnum playerReference;

  /**
   * CTOR
   * @param id: Matchstick numbering (for better clarity)
   */
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

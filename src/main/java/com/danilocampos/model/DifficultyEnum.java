package com.danilocampos.model;

public enum DifficultyEnum {
  BEGINNER(46),
  INTERMEDIATE(38),
  ADVANCED(30),
  MASTER(1);

  private final int amount;

  DifficultyEnum(int amount) {
    this.amount = amount;
  }

  public int getAmount() {
    return amount;
  }

}

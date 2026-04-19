package com.danilocampos.model;

public enum GameStatusEnum {

  INCOMPLETE("Incompleto"),
  INCORRECT("Incorreto"),
  CORRECT("Correto");

  private String label;

  private GameStatusEnum(final String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

}

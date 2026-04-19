package com.danilocampos.model;

import java.util.Optional;

public class Field {

  private final Region region;
  private Integer value;
  private Integer correctValue;
  private boolean fixed;
  private final int absoluteRow;
  private final int absoluteColumn;

  public Field(Region region, int row, int column) {
    this.region = region;
    this.absoluteRow = row;
    this.absoluteColumn = column;
  }

  public Optional<Integer> getValue() {
    return Optional.ofNullable(value);
  }

  public void setValue(int value) {
    this.value = value;
  }

  public Optional<Integer> getCorrectValue() {
    return Optional.ofNullable(correctValue);
  }

  public void setCorrectValue(Integer correctValue) {
    this.correctValue = correctValue;
  }

  public void setFixed(boolean fixed) {
    this.fixed = fixed;
  }

  public int getAbsoluteRow() {
    return absoluteRow;
  }

  public int getAbsoluteColumn() {
    return absoluteColumn;
  }

  public Region getRegion() {
    return region;
  }

  public void setValue(Integer value) {
    if (this.fixed != true)
      this.value = value;
  }

  public boolean isFixed() {
    return fixed;
  }

}

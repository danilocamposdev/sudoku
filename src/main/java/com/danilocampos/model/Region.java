package com.danilocampos.model;

public class Region {

  public final static int REGION_SIZE = 3;
  private final int column;
  private final int row;

  public static int getRegionSize() {
    return REGION_SIZE;
  }

  private final Field[][] fields;

  public Region(int row, int column) {
    this.row = row;
    this.column = column;
    this.fields = new Field[REGION_SIZE][REGION_SIZE];
    for (int i = 0; i < REGION_SIZE; i++) {
      for (int j = 0; j < REGION_SIZE; j++) {
        int absoluteFieldRow = this.row * REGION_SIZE + i;
        int absoluteFieldColumn = this.column * REGION_SIZE + j;
        this.fields[i][j] = new Field(this, absoluteFieldRow, absoluteFieldColumn);
      }
    }
  }

  public Field[][] getFields() {
    return fields;
  }

  public Field getField(final int i, final int j) {
    return fields[i][j];
  }

  public int getColumn() {
    return column;
  }

  public int getRow() {
    return row;
  }

}

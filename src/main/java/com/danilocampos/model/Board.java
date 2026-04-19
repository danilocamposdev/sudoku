package com.danilocampos.model;

import static com.danilocampos.model.GameStatusEnum.CORRECT;
import static com.danilocampos.model.GameStatusEnum.INCOMPLETE;
import static com.danilocampos.model.GameStatusEnum.INCORRECT;
import static com.danilocampos.model.Region.REGION_SIZE;
import static com.danilocampos.util.BoardTemplate.BOARD_TEMPLATE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Board {

  private final DifficultyEnum DIFFICULTY;
  private final static int BOARD_SIZE = 3;
  private final static int TOTAL_ROWS_OR_COLUMNS = BOARD_SIZE * REGION_SIZE;
  private final static int TOTALPOSITIONS = BOARD_SIZE * BOARD_SIZE * REGION_SIZE * REGION_SIZE;
  private static int solutionCount;

  private final Region[][] regions = new Region[BOARD_SIZE][BOARD_SIZE];
  private boolean[][] rowContainNumber = new boolean[BOARD_SIZE * REGION_SIZE][10];
  private boolean[][] columnContainNumber = new boolean[BOARD_SIZE * REGION_SIZE][10];
  private boolean[][][] regionContainNumber = new boolean[BOARD_SIZE][BOARD_SIZE][10];

  public Board(DifficultyEnum difficulty) {
    this.DIFFICULTY = difficulty;
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        this.regions[i][j] = new Region(i, j);
      }
    }
    generateDiagonalRegions();
    generateRemainingRegions();
    removeRandomnFields();
    turnNonEmptiesImmutable();
  }

  private void generateDiagonalRegions() {
    for (int ri = 0; ri < BOARD_SIZE; ri++) {
      Region diagonalRegion = regions[ri][ri];
      List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
      java.util.Collections.shuffle(numbers);
      for (int i = 0; i < REGION_SIZE; i++) {
        for (int j = 0; j < REGION_SIZE; j++) {
          Field field = diagonalRegion.getField(i, j);
          int value = numbers.removeFirst();
          field.setCorrectValue(value);
          rowContainNumber[field.getAbsoluteRow()][value] = true;
          columnContainNumber[field.getAbsoluteColumn()][value] = true;
          regionContainNumber[ri][ri][value] = true;
        }
      }
    }
  }

  private void generateRemainingRegions() {
    if (!fillAndTestFieldsRecursively(0, 0)) {
      throw new RuntimeException("Não foi possível gerar o Sudoku");
    }
  }

  private boolean fillAndTestFieldsRecursively(int absoluteRow, int absoluteCol) {

    if (absoluteRow == TOTAL_ROWS_OR_COLUMNS)
      return true;

    int nextRow = (absoluteCol == TOTAL_ROWS_OR_COLUMNS - 1) ? absoluteRow + 1 : absoluteRow;
    int nextCol = (absoluteCol + 1) % (TOTAL_ROWS_OR_COLUMNS);

    Field field = getField(absoluteRow, absoluteCol).get();

    if (field.getCorrectValue().isPresent()) {
      return fillAndTestFieldsRecursively(nextRow, nextCol);
    }

    List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    Collections.shuffle(numbers);

    for (int number : numbers) {
      if (!checkConstraints(absoluteRow, absoluteCol, number))
        continue;

      field.setCorrectValue(number);
      updateConstraints(absoluteRow, absoluteCol, number, true);

      if (fillAndTestFieldsRecursively(nextRow, nextCol))
        return true;

      field.setCorrectValue(null);
      updateConstraints(absoluteRow, absoluteCol, number, false);
    }

    return false;
  }

  private void updateConstraints(int absoluteRow, int absoluteCol, int number, boolean contains) {
    int ri = absoluteRow / REGION_SIZE;
    int rj = absoluteCol / REGION_SIZE;

    rowContainNumber[absoluteRow][number] = contains;
    columnContainNumber[absoluteCol][number] = contains;
    regionContainNumber[ri][rj][number] = contains;
  }

  private void removeRandomnFields() {
    rowContainNumber = new boolean[TOTAL_ROWS_OR_COLUMNS][10];
    columnContainNumber = new boolean[TOTAL_ROWS_OR_COLUMNS][10];
    regionContainNumber = new boolean[BOARD_SIZE][BOARD_SIZE][10];

    for (int i = 0; i < TOTALPOSITIONS; i++) {
      Field field = getField(i).get();
      field.setValue(field.getCorrectValue().get());
      int row = field.getAbsoluteRow();
      int col = field.getAbsoluteColumn();
      int value = field.getValue().get();

      updateConstraints(row, col, value, true);

    }

    List<Integer> indexes = new ArrayList<>();
    for (int i = 0; i < TOTALPOSITIONS; i++)
      indexes.add(i);
    Collections.shuffle(indexes);

    int targetEmptyFields = TOTALPOSITIONS - DIFFICULTY.getAmount();
    int emptyFieldsCount = 0;

    for (int index : indexes) {
      if (emptyFieldsCount >= targetEmptyFields)
        break;

      Field field = getField(index).get();
      int originalValue = field.getCorrectValue().get();

      field.setValue(null);
      updateConstraints(field.getAbsoluteRow(), field.getAbsoluteColumn(), originalValue, false);

      solutionCount = 0;
      countCurrentSolutions(0, 0);

      if (solutionCount == 1) {
        emptyFieldsCount++;
      } else {
        field.setValue(originalValue);
        updateConstraints(field.getAbsoluteRow(), field.getAbsoluteColumn(), originalValue, true);
      }
    }
  }

  private void turnNonEmptiesImmutable() {
    for (int i = 0; i < TOTALPOSITIONS; i++) {
      Field field = getField(i).get();
      if (field.getValue().isPresent()) {
        field.setFixed(true);
      }
    }

  }

  private Optional<Field> getField(int absoluteRow, int absoluteColumn) {
    if (absoluteRow >= TOTAL_ROWS_OR_COLUMNS || absoluteColumn >= TOTAL_ROWS_OR_COLUMNS)
      return Optional.empty();
    int regionRow = absoluteRow / REGION_SIZE;
    int regionCol = absoluteColumn / REGION_SIZE;

    int innerRow = absoluteRow % REGION_SIZE;
    int innerCol = absoluteColumn % REGION_SIZE;

    Field field = regions[regionRow][regionCol]
        .getField(innerRow, innerCol);

    return Optional.of(field);
  }

  private Optional<Field> getField(int absolutePosition) {
    int totalColumns = TOTAL_ROWS_OR_COLUMNS;
    int absoluteRow = absolutePosition / totalColumns;
    int absoluteColumn = absolutePosition % totalColumns;
    return getField(absoluteRow, absoluteColumn);
  }

  public boolean changeValue(final int row, final int col, final int value) {
    var field = getField(row, col).get();
    if (field.isFixed())
      return false;
    field.setValue(value);
    return true;
  }

  public boolean clearValue(final int row, final int col) {
    var field = getField(row, col).get();
    if (field.isFixed())
      return false;
    field.setValue(null);
    return true;
  }

  public GameStatusEnum getStatus() {
    boolean hasEmptyFields = false;

    for (int position = 0; position < TOTALPOSITIONS; position++) {
      Field field = getField(position)
          .orElseThrow(() -> new RuntimeException("Posição inválida"));

      Optional<Integer> value = field.getValue();
      int correctValue = field.getCorrectValue().get();

      if (value.isPresent()) {
        if (value.get() != correctValue) {
          return INCORRECT;
        }
      } else {
        hasEmptyFields = true;
      }
    }

    return hasEmptyFields ? INCOMPLETE : CORRECT;
  }

  public void clearUnfixeds() {
    for (int position = 0; position < TOTALPOSITIONS; position++) {
      Field field = getField(position)
          .orElseThrow(() -> new RuntimeException("Posição inválida"));

      if (!field.getValue().isPresent())
        continue;
      Optional<Integer> value = field.getValue();

      if (value.isPresent()) {
        field.setValue(null);
      }
    }
  }

  private void countCurrentSolutions(int absoluteRow, int absoluteCol) {
    if (solutionCount > 1)
      return;

    if (absoluteRow == TOTAL_ROWS_OR_COLUMNS) {
      solutionCount++;
      return;
    }

    int nextRow = (absoluteCol == TOTAL_ROWS_OR_COLUMNS - 1) ? absoluteRow + 1 : absoluteRow;
    int nextCol = (absoluteCol + 1) % (TOTAL_ROWS_OR_COLUMNS);

    Field field = getField(absoluteRow, absoluteCol).get();

    if (field.getValue().isPresent()) {
      countCurrentSolutions(nextRow, nextCol);
      return;
    }
    for (int number = 1; number <= 9; number++) {
      if (!checkConstraints(absoluteRow, absoluteCol, number))
        continue;

      field.setValue(number); // Preenche temporariamente
      updateConstraints(absoluteRow, absoluteCol, number, true);

      countCurrentSolutions(nextRow, nextCol);

      updateConstraints(absoluteRow, absoluteCol, number, false);
      field.setValue(null);

      if (solutionCount > 1)
        return;
    }
  }

  private boolean checkConstraints(int absoluteRow, int absoluteCol, int number) {
    int ri = absoluteRow / REGION_SIZE;
    int rj = absoluteCol / REGION_SIZE;

    return !rowContainNumber[absoluteRow][number] &&
        !columnContainNumber[absoluteCol][number] &&
        !regionContainNumber[ri][rj][number];
  }

  @Override
  public String toString() {
    Object[] values = new Object[81];
    int index = 0;

    for (int row = 0; row < 9; row++) {
      for (int col = 0; col < 9; col++) {
        Field field = getField(row, col).get();
        values[index++] = field.getValue()
            .map(v -> " ".concat(v.toString()))
            .orElse("  ");
      }
    }
    return String.format(BOARD_TEMPLATE, values);
  }

}

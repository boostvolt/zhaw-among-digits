package ch.zhaw.pm2.amongdigits.utils;

import static ch.zhaw.pm2.amongdigits.utils.Creator.BacktrackingResult.CONTEST;
import static ch.zhaw.pm2.amongdigits.utils.Creator.BacktrackingResult.CONTINUE;
import static ch.zhaw.pm2.amongdigits.utils.Creator.BacktrackingResult.FOUND;
import static ch.zhaw.pm2.amongdigits.utils.matrix.MatrixManager.FreeCellResult.CONTRADICTION;
import static ch.zhaw.pm2.amongdigits.utils.matrix.MatrixManager.FreeCellResult.NONE_FREE;

import ch.zhaw.pm2.amongdigits.DifficultyLevel;
import ch.zhaw.pm2.amongdigits.utils.matrix.CachedMatrixManager;
import ch.zhaw.pm2.amongdigits.utils.matrix.Matrix;
import ch.zhaw.pm2.amongdigits.utils.matrix.MatrixManager;
import ch.zhaw.pm2.amongdigits.utils.schema.Schema;
import ch.zhaw.pm2.amongdigits.utils.schema.SchemaTypes;
import ch.zhaw.pm2.amongdigits.utils.sudoku.Sudoku;
import ch.zhaw.pm2.amongdigits.utils.sudoku.SudokuManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * This class provides functionality to create a Sudoku puzzle with a given difficulty level. It
 * uses a cached matrix manager to create a full Sudoku matrix and then removes a certain number of
 * elements to generate the puzzle.
 */
public final class Creator {

  private static final int SUDOKU_EMPTY_FIELDS_RANDOM = 10;
  private static final Random RANDOM = new Random();

  private final Function<Matrix, Boolean> resultConsumer;
  private final MatrixManager matrixManager;
  private final Schema schema;
  private Matrix winner;

  private Creator(final Schema schema) {
    this.schema = schema;
    matrixManager = new CachedMatrixManager(schema);

    resultConsumer =
        matrix -> {
          winner = matrix;
          return true;
        };
  }

  /**
   * Creates a Sudoku puzzle with the given difficulty level.
   *
   * @param difficultyLevel the difficulty level of the puzzle to create
   * @return a Sudoku puzzle with the given difficulty level
   */
  public static Sudoku createSudoku(final DifficultyLevel difficultyLevel) {
    final Matrix fullMatrix = createFull();
    final Schema schema = fullMatrix.getSchema();
    final int width = schema.getWidth();
    final byte unset = schema.getUnsetValue();

    SudokuManager sudokuManager = new SudokuManager(schema);
    sudokuManager.setAll(fullMatrix.getAll());

    int numbersToClear = difficultyLevel.getMaxNumbersToClear();
    int randomClearCount = 0;

    while (numbersToClear > 0 && randomClearCount < SUDOKU_EMPTY_FIELDS_RANDOM) {
      int i = RANDOM.nextInt(width);
      int j = RANDOM.nextInt(width);
      if (sudokuManager.get(j, i) != schema.getUnsetValue()) {
        if (isClearable(sudokuManager, j, i)) {
          sudokuManager.set(j, i, schema.getUnsetValue());
          numbersToClear--;
        } else {
          randomClearCount++;
        }
      }
    }

    clearNumbers(sudokuManager, width, unset, numbersToClear);
    setWritableCells(sudokuManager, width, unset);

    return sudokuManager;
  }

  /**
   * Creates a full Sudoku matrix.
   *
   * @return a full Sudoku matrix
   */
  static Matrix createFull() {
    Schema schema = SchemaTypes.SCHEMA_9X9;
    Creator creator = new Creator(schema);

    BacktrackingResult backtrackingResult;
    do {
      creator.matrixManager.clear();
      for (int i = 0; i < creator.matrixManager.getSchema().getBlockCount(); i++) {
        creator.fillBlock(i * schema.getBlockWidth(), i * schema.getBlockWidth());
      }

      backtrackingResult =
          creator.backtrack(
              schema.getTotalFields() - creator.matrixManager.getSetCount(), new int[2]);
    } while (backtrackingResult != FOUND);

    return creator.winner;
  }

  /**
   * Creates an array of numbers to distribute across the Sudoku matrix.
   *
   * @param schema the schema of the Sudoku matrix
   * @param multiplicity the multiplicity of each number to distribute
   * @return an array of numbers to distribute across the Sudoku matrix
   */
  static byte[] createNumbersToDistribute(final Schema schema, final int multiplicity) {
    int totalNumbers = schema.getMaximumValue() - schema.getMinimumValue() + 1;
    List<Integer> numbersToDistribute = new ArrayList<>(totalNumbers * multiplicity);
    for (int number = schema.getMinimumValue(); number <= schema.getMaximumValue(); number++) {
      for (int j = 0; j < multiplicity; j++) {
        numbersToDistribute.add(number);
      }
    }

    Collections.shuffle(numbersToDistribute);
    byte[] numbersToDistributeArray = new byte[numbersToDistribute.size()];
    int k = 0;
    for (Integer number : numbersToDistribute) {
      numbersToDistributeArray[k++] = number.byteValue();
    }

    return numbersToDistributeArray;
  }

  /**
   * Calculates the offset of a set bit in a mask at the given bit index.
   *
   * @param mask the mask to check
   * @param bitIndex the index of the bit to check
   * @return the offset of the set bit at the given bit index, or -1 if no set bit was found
   */
  static int getSetBitOffset(final int mask, final int bitIndex) {
    int count = 0;
    int workingMask = mask;
    int low = Integer.numberOfTrailingZeros(workingMask);
    workingMask >>>= low;
    assert (workingMask & 1) == 1 || workingMask == 0;
    for (int i = low; workingMask != 0; i++) {
      if ((workingMask & 1) != 0) {
        if (count == bitIndex) {
          return i;
        }
        count++;
      }
      workingMask >>>= 1;
    }

    return -1;
  }

  private static boolean isClearable(
      final SudokuManager sudokuManager, final int row, final int column) {
    Schema schema = sudokuManager.getSchema();
    assert sudokuManager.get(row, column) != schema.getUnsetValue();

    int freeMask = sudokuManager.getFreeMask(row, column);
    int freeValues = Integer.bitCount(freeMask);
    if (freeValues == 0) {
      return true;
    }

    int oldSudoku = sudokuManager.get(row, column);
    sudokuManager.set(row, column, schema.getUnsetValue());

    List<Matrix> results = Solver.solve(sudokuManager, 2);
    boolean result = results.size() == 1;

    sudokuManager.set(row, column, (byte) oldSudoku);
    return result;
  }

  private static void clearNumbers(
      SudokuManager sudokuManager, int width, byte unset, int numbersToClear) {
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < width; j++) {
        if (numbersToClear > 0
            && unset != sudokuManager.get(j, i)
            && isClearable(sudokuManager, j, i)) {
          sudokuManager.set(j, i, unset);
          numbersToClear--;
        }
      }
    }
  }

  private static void setWritableCells(SudokuManager sudokuManager, int width, byte unset) {
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < width; j++) {
        sudokuManager.setWritable(j, i, sudokuManager.get(j, i) == unset);
      }
    }
  }

  private void fillBlock(final int row, final int column) {
    final int blockSize = schema.getBlockWidth();

    assert schema.areCoordsValid(row, column);
    assert row % blockSize == 0;
    assert column % blockSize == 0;

    byte[] numbers = createNumbersToDistribute(schema, 1);
    int k = 0;
    for (int colOfs = 0; colOfs < blockSize; colOfs++) {
      for (int rowOfs = 0; rowOfs < blockSize; rowOfs++) {
        matrixManager.set(row + rowOfs, column + colOfs, numbers[k++]);
      }
    }
  }

  private BacktrackingResult backtrack(final int numbersToDistribute, final int[] minimumCell) {
    if (numbersToDistribute == 0) {
      assert matrixManager.isValid();
      if (Boolean.TRUE.equals(resultConsumer.apply(matrixManager))) {
        return FOUND;
      } else {
        return CONTINUE;
      }
    }

    MatrixManager.FreeCellResult result = matrixManager.findLeastFreeCell(minimumCell);
    if (result == CONTRADICTION || result == NONE_FREE) {
      return CONTEST;
    }

    int minimumRow = minimumCell[0];
    int minimumColumn = minimumCell[1];
    int minimumFree = matrixManager.getFreeMask(minimumRow, minimumColumn);
    int minimumBits = Integer.bitCount(minimumFree);

    for (int bit = 0; bit < minimumBits; bit++) {
      int number = getSetBitOffset(minimumFree, bit);
      assert number >= schema.getMinimumValue() && number <= schema.getMaximumValue();
      assert (matrixManager.getFreeMask(minimumRow, minimumColumn) & (1 << number)) == 1 << number;

      matrixManager.set(minimumRow, minimumColumn, (byte) (number));
      assert (matrixManager.getFreeMask(minimumRow, minimumColumn) & (1 << number)) == 0;
      BacktrackingResult subResult = backtrack(numbersToDistribute - 1, minimumCell);
      if (subResult == FOUND) {
        return subResult;
      }
    }
    matrixManager.set(minimumRow, minimumColumn, schema.getUnsetValue());

    return CONTINUE;
  }

  /** The result of a backtracking operation. */
  enum BacktrackingResult {
    FOUND,
    CONTINUE,
    CONTEST
  }
}

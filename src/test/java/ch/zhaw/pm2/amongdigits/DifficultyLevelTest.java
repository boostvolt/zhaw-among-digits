package ch.zhaw.pm2.amongdigits;

import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.SUDOKU_GRID_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** The DifficultyLevelTest class is used to test the DifficultyLevel enumeration. */
class DifficultyLevelTest {

  private static final Map<DifficultyLevel, byte[][]> DIFFICULTY_TEST_GRIDS = new HashMap<>();

  static {
    DIFFICULTY_TEST_GRIDS.put(
        DifficultyLevel.BEGINNER,
        generateGridWithZeros(DifficultyLevel.BEGINNER.getMaxNumbersToClear() - 10));
    DIFFICULTY_TEST_GRIDS.put(
        DifficultyLevel.MEDIUM,
        generateGridWithZeros(DifficultyLevel.MEDIUM.getMaxNumbersToClear()));
    DIFFICULTY_TEST_GRIDS.put(
        DifficultyLevel.EXPERT,
        generateGridWithZeros(DifficultyLevel.EXPERT.getMaxNumbersToClear() + 5));
  }

  /**
   * Generates a Sudoku grid with the specified number of zeros.
   *
   * @param numZeros the number of zeros to include in the grid.
   * @return the generated grid.
   */
  public static byte[][] generateGridWithZeros(int numZeros) {
    byte[][] grid = new byte[SUDOKU_GRID_SIZE][SUDOKU_GRID_SIZE];
    int numFilled = (SUDOKU_GRID_SIZE * SUDOKU_GRID_SIZE) - numZeros;
    for (int i = 0; i < numFilled; i++) {
      int row = i / SUDOKU_GRID_SIZE;
      int col = i % SUDOKU_GRID_SIZE;
      grid[row][col] = 1;
    }
    return grid;
  }

  /** Tests the determineDifficultyLevel method of the DifficultyLevel enumeration. */
  @Test
  void testDetermineDifficultyLevel() {
    DIFFICULTY_TEST_GRIDS.forEach(
        (key, value) -> assertEquals(key, DifficultyLevel.determineDifficultyLevel(value)));
  }
}

package ch.zhaw.pm2.amongdigits.utils.sudoku;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import ch.zhaw.pm2.amongdigits.utils.schema.Schema;
import ch.zhaw.pm2.amongdigits.utils.schema.SchemaTypes;
import org.junit.jupiter.api.Test;

/** This class contains JUnit tests for the SudokuManager class. */
class SudokuManagerTest {

  private final Schema schema = SchemaTypes.SCHEMA_9X9;

  /**
   * Tests the creation of a new SudokuManager instance. Verifies that the set count is initially
   * zero.
   */
  @Test
  void testNew() {
    SudokuManager sudokuManager = new SudokuManager(schema);
    assertEquals(0, sudokuManager.getSetCount());
  }

  /**
   * Tests the cloning of a SudokuManager instance. Verifies that the clone is not the same as the
   * original, but has the same state.
   */
  @Test
  void testClone() {
    byte[][] matrix = {
      {3, 5, 9, 1, 6, 2, 4, 8, 7},
      {4, 1, 2, 8, 3, 7, 6, 5, 9},
      {6, 8, 7, 5, 9, 4, 1, 2, 3},
      {8, 7, 6, 4, 5, 9, 3, 1, 2},
      {9, 4, 1, 6, 2, 3, 8, 7, 5},
      {5, 2, 3, 7, 1, 8, 9, 4, 6},
      {2, 3, 4, 9, 8, 5, 7, 6, 1},
      {7, 6, 5, 3, 4, 1, 2, 9, 8},
      {1, 9, 8, 2, 7, 6, 5, 3, 4}
    };
    SudokuManager sudokuManager = new SudokuManager(schema);
    sudokuManager.setAll(matrix);
    SudokuManager clone = SudokuManager.clone(sudokuManager);
    assertNotSame(clone, sudokuManager);
    assertEquals(clone, sudokuManager);
  }
}

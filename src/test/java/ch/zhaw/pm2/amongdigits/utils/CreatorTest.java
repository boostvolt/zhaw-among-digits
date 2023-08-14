package ch.zhaw.pm2.amongdigits.utils;

import static ch.zhaw.pm2.amongdigits.DifficultyLevel.BEGINNER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.zhaw.pm2.amongdigits.TestUtils;
import ch.zhaw.pm2.amongdigits.utils.matrix.Matrix;
import ch.zhaw.pm2.amongdigits.utils.schema.Schema;
import ch.zhaw.pm2.amongdigits.utils.schema.SchemaTypes;
import ch.zhaw.pm2.amongdigits.utils.sudoku.Sudoku;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** A test class for the {@link Creator} class. */
class CreatorTest {

  /**
   * Test method for {@link Creator#getSetBitOffset(int, int)}. Tests the behavior of the method
   * when no bit is set in the integer.
   */
  @Test
  void testGetSetBitOffsetWithNothingSet1() {
    int bitOffset = Creator.getSetBitOffset(0, 0);
    assertEquals(-1, bitOffset);
  }

  /**
   * Test method for {@link Creator#getSetBitOffset(int, int)}. Tests the behavior of the method
   * when no bit is set in the integer.
   */
  @Test
  void testGetSetBitOffsetWithNothingSet2() {
    int bitOffset = Creator.getSetBitOffset(0, 1);
    assertEquals(-1, bitOffset);
  }

  /**
   * Test method for {@link Creator#getSetBitOffset(int, int)}. Tests the behavior of the method
   * when two bits are set in the integer.
   */
  @Test
  void testGetSetBitOffsetWithTwoSet1() {
    int bitOffset = Creator.getSetBitOffset(3, 0);
    assertEquals(0, bitOffset);
  }

  /**
   * Test method for {@link Creator#getSetBitOffset(int, int)}. Tests the behavior of the method
   * when two bits are set in the integer.
   */
  @Test
  void testGetSetBitOffsetWithTwoSet2() {
    int bitOffset = Creator.getSetBitOffset(3, 1);
    assertEquals(1, bitOffset);
  }

  /**
   * Test method for {@link Creator#getSetBitOffset(int, int)}. Tests the behavior of the method
   * when three bits are set in the integer.
   */
  @Test
  void testGetSetBitOffsetWithTreeSet1() {
    int bitOffset = Creator.getSetBitOffset(0x111, 0);
    assertEquals(0, bitOffset);
  }

  /**
   * Test method for {@link Creator#getSetBitOffset(int, int)}. Tests the behavior of the method
   * when three bits are set in the integer.
   */
  @Test
  void testGetSetBitOffsetWithTreeSet2() {
    int bitOffset = Creator.getSetBitOffset(0x111, 1);
    assertEquals(4, bitOffset);
  }

  /**
   * Tests the behavior of the {@link Creator#getSetBitOffset(int, int)} method when three bits are
   * set in the integer.
   */
  @Test
  void testGetSetBitOffsetWithTreeSet3() {
    int bitOffset = Creator.getSetBitOffset(0x111, 2);
    assertEquals(8, bitOffset);
  }

  /**
   * Tests the behavior of the {@link Creator#getSetBitOffset(int, int)} method when three bits are
   * set in the integer.
   */
  @Test
  void testGetSetBitOffsetWithTreeSet4() {
    int bitOffset = Creator.getSetBitOffset(0x111, 3);
    assertEquals(-1, bitOffset);
  }

  /**
   * Tests the behavior of the {@link Creator#getSetBitOffset(int, int)} method with all bits set.
   */
  @Test
  void testGetSetBitOffsetWithAll() {
    int bitOffset;

    bitOffset = Creator.getSetBitOffset(0xffffffff, 0);
    assertEquals(0, bitOffset);

    bitOffset = Creator.getSetBitOffset(0xffffffff, 1);
    assertEquals(1, bitOffset);

    bitOffset = Creator.getSetBitOffset(0xffffffff, 2);
    assertEquals(2, bitOffset);

    bitOffset = Creator.getSetBitOffset(0xffffffff, 3);
    assertEquals(3, bitOffset);

    bitOffset = Creator.getSetBitOffset(0xffffffff, 4);
    assertEquals(4, bitOffset);

    bitOffset = Creator.getSetBitOffset(0xffffffff, 5);
    assertEquals(5, bitOffset);

    bitOffset = Creator.getSetBitOffset(0xffffffff, 6);
    assertEquals(6, bitOffset);

    bitOffset = Creator.getSetBitOffset(0xffffffff, 7);
    assertEquals(7, bitOffset);

    bitOffset = Creator.getSetBitOffset(0xffffffff, 8);
    assertEquals(8, bitOffset);

    bitOffset = Creator.getSetBitOffset(0xffffffff, 31);
    assertEquals(31, bitOffset);

    bitOffset = Creator.getSetBitOffset(0xffffffff, 32);
    assertEquals(-1, bitOffset);
  }

  /** Tests the creation of a full 9x9 Sudoku matrix and validates it is a valid Sudoku. */
  @Test
  void testCreateFull() {
    Matrix matrix = Creator.createFull();
    assertEquals(9 * 9, matrix.getSetCount());
    assertTrue(matrix.isValid());
  }

  /**
   * Tests the creation of a full 9x9 Sudoku matrix with multiple invocations and validates each
   * matrix is valid.
   */
  @Test
  void testCreateFullWithMultipleInvocations() {
    for (int i = 0; i < 1000; i++) {
      Matrix matrix = Creator.createFull();
      assertEquals(9 * 9, matrix.getSetCount());
      assertTrue(matrix.isValid());
    }
  }

  /**
   * Tests the creation of a Sudoku puzzle from a full 9x9 Sudoku matrix with the specified
   * difficulty level, and validates the number of unset cells and that the puzzle has only one
   * solution.
   */
  @Test
  void testCreateSudokuWithOne() {
    Matrix matrix = Creator.createFull();
    Schema schema = matrix.getSchema();
    Sudoku sudoku = Creator.createSudoku(BEGINNER);
    int unsetCount = 0;
    for (int i = 0; i < schema.getWidth(); i++) {
      for (int j = 0; j < schema.getWidth(); j++) {
        if (sudoku.get(i, j) == schema.getUnsetValue()) {
          unsetCount++;
        }
      }
    }
    assertEquals(BEGINNER.getMaxNumbersToClear(), unsetCount);

    List<Matrix> results = Solver.solve(sudoku);
    assertEquals(1, results.size());
  }

  /**
   * Tests the creation of a byte array with a specified number of occurrences of each digit for a
   * given schema.
   */
  @Test
  void testCreateNumbersToDistributeWithOnce() {
    Schema schema = SchemaTypes.SCHEMA_9X9;
    byte[] bitOffset = Creator.createNumbersToDistribute(schema, 1);
    List<Integer> intList = TestUtils.toIntList(bitOffset);

    assertEquals(9, intList.size());
    assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), intList);
  }

  /** Tests the creation of a byte array with two occurrences of each digit for a given schema. */
  @Test
  void testCreateNumbersToDistributeWithTwice() {
    Schema schema = SchemaTypes.SCHEMA_9X9;
    byte[] bitOffset = Creator.createNumbersToDistribute(schema, 2);
    List<Integer> intList = TestUtils.toIntList(bitOffset);

    assertEquals(2 * 9, intList.size());
    assertEquals(Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9), intList);
  }
}

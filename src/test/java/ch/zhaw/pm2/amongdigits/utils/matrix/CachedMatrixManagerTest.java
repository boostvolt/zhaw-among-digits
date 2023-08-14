package ch.zhaw.pm2.amongdigits.utils.matrix;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.zhaw.pm2.amongdigits.utils.schema.Schema;
import ch.zhaw.pm2.amongdigits.utils.schema.SchemaTypes;
import org.junit.jupiter.api.Test;

/**

 A test class for {@link CachedMatrixManager}.
 */
class CachedMatrixManagerTest {

  private static final byte[][] FULL_MATRIX = {
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
  private static final byte[][] PARTIALLY_FULL_MATRIX = {
    {1, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 2, 0, 1, 0, 0, 0, 0, 0},
    {0, 0, 0, 3, 2, 0, 1, 0, 0},
    {0, 1, 0, 0, 0, 0, 4, 5, 6},
    {0, 0, 0, 0, 1, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 1, 0},
    {0, 0, 1, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 1, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 1},
  };

  /**
  * Tests the constructor when no sets are available. In this case we don't know how many sets should be
  */
  @Test
  void testNew() {
    CachedMatrixManager cachedMatrixManager = new CachedMatrixManager(SchemaTypes.SCHEMA_9X9);
    assertEquals(0, cachedMatrixManager.getSetCount());
  }

  /**
  * Test getting a value from 0 to 8. This is a test for bug 9x9 where we don't know the schema
  */
  @Test
  void testGet() {
    MatrixManager matrixManager = new CachedMatrixManager(SchemaTypes.SCHEMA_9X9);
    byte value = matrixManager.get(0, 0);
    assertEquals(matrixManager.getSchema().getUnsetValue(), value);

    value = matrixManager.get(8, 8);
    assertEquals(matrixManager.getSchema().getUnsetValue(), value);
  }
  
  
  /**
  * Test set ( int int byte ) with cached values. This is different from testGet () in that it doesn't check if the value is set
  */
  @Test
  void testSet() {
    MatrixManager matrixManager = new CachedMatrixManager(SchemaTypes.SCHEMA_9X9);
    byte value = matrixManager.get(0, 0);
    assertEquals(matrixManager.getSchema().getUnsetValue(), value);

    matrixManager.set(0, 0, (byte) 4);
    value = matrixManager.get(0, 0);
    assertEquals(4, value);
  }

  /**
  * Tests the setAll method. This test sets all elements to the full matrix and checks that the values are the same
  */
  @Test
  void testSetAll() {
    CachedMatrixManager cachedMatrixManager = new CachedMatrixManager(SchemaTypes.SCHEMA_9X9);
    cachedMatrixManager.setAll(FULL_MATRIX);
    // Checks that all the matrix matrixs are equal.
    for (int i = 0; i < cachedMatrixManager.getSchema().getWidth(); i++) {
      // Checks that the matrix is in full matrix.
      for (int j = 0; j < cachedMatrixManager.getSchema().getWidth(); j++) {
        assertEquals(FULL_MATRIX[i][j], cachedMatrixManager.get(i, j));
      }
    }
  }

  /**
  * Tests clone () method. This is a regression test for HADOOP - 9373 and should be removed
  */
  @Test
  void testClone() {
    CachedMatrixManager cachedMatrixManager = new CachedMatrixManager(SchemaTypes.SCHEMA_9X9);
    cachedMatrixManager.setAll(FULL_MATRIX);
    CachedMatrixManager clone = CachedMatrixManager.clone(cachedMatrixManager);
    assertArrayEquals(FULL_MATRIX, clone.getAll());
  }

  /**
  * Test isValid () with empty matrix and valid result. Should return true for valid result and false for invalid
  */
  @Test
  void testIsValidWithEmptyValid() {
    byte[][] matrix = {
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    CachedMatrixManager cachedMatrixManager = new CachedMatrixManager(SchemaTypes.SCHEMA_9X9);
    cachedMatrixManager.setAll(matrix);
    assertTrue(cachedMatrixManager.isValid());
  }

  /**
  * Tests isValid () with PARTIALLY_FULL_MATRIX set to validate. This is a bug
  */
  @Test
  void testIsValidWithPartlyFullValid() {
    CachedMatrixManager cachedMatrixManager = new CachedMatrixManager(SchemaTypes.SCHEMA_9X9);
    cachedMatrixManager.setAll(PARTIALLY_FULL_MATRIX);
    assertTrue(cachedMatrixManager.isValid());
  }

  /**
  * Test that RowFreeMask works as expected for matrices with partitioned matrices. This is a bit tricky because we don't want to create a copy of the matrix every time
  */
  @Test
  void testGetRowFreeMask() {
    Schema schema = SchemaTypes.SCHEMA_9X9;
    CachedMatrixManager cachedMatrixManager = new CachedMatrixManager(schema);
    cachedMatrixManager.setAll(PARTIALLY_FULL_MATRIX);
    int mask = cachedMatrixManager.getRowFreeMask(0);
    assertEquals(schema.getBitMask() & (~(1 << 1)), mask);

    mask = cachedMatrixManager.getRowFreeMask(1);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 2))), mask);

    mask = cachedMatrixManager.getRowFreeMask(2);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 2) | (1 << 3))), mask);

    mask = cachedMatrixManager.getRowFreeMask(3);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 4) | (1 << 5) | (1 << 6))), mask);
  }

  /**
  * Test that we can get the column free mask for a matrix that is part of the partition. This is a bit tricky because we don't know the number of rows in the partition
  */
  @Test
  void testGetColumnFreeMask() {
    Schema schema = SchemaTypes.SCHEMA_9X9;
    CachedMatrixManager cachedMatrixManager = new CachedMatrixManager(schema);
    cachedMatrixManager.setAll(PARTIALLY_FULL_MATRIX);
    int mask = cachedMatrixManager.getColumnFreeMask(0);
    assertEquals(schema.getBitMask() & (~(1 << 1)), mask);

    mask = cachedMatrixManager.getColumnFreeMask(1);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 2))), mask);

    mask = cachedMatrixManager.getColumnFreeMask(2);
    assertEquals(schema.getBitMask() & (~((1 << 1))), mask);

    mask = cachedMatrixManager.getColumnFreeMask(3);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 3))), mask);
  }

  /**
  * Tests get block free mask for a matrix. This is a test for bug 9497 which was fixed
  */
  @Test
  void testGetBlockFreeMask() {
    Schema schema = SchemaTypes.SCHEMA_9X9;
    CachedMatrixManager cachedMatrixManager = new CachedMatrixManager(schema);
    cachedMatrixManager.setAll(PARTIALLY_FULL_MATRIX);
    int mask = cachedMatrixManager.getBlockFreeMask(0, 0);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 2))), mask);

    mask = cachedMatrixManager.getBlockFreeMask(0, 3);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 2) | (1 << 3))), mask);

    mask = cachedMatrixManager.getBlockFreeMask(0, 6);
    assertEquals(schema.getBitMask() & (~((1 << 1))), mask);

    mask = cachedMatrixManager.getBlockFreeMask(3, 6);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 4) | (1 << 5) | (1 << 6))), mask);
  }

  /**
  * Tests getFreeMask ( int int ) method for 9X9 cache. This test is based on CachedMatrixManager
  */
  @Test
  void testGetFreeMask() {
    Schema schema = SchemaTypes.SCHEMA_9X9;
    CachedMatrixManager cachedMatrixManager = new CachedMatrixManager(schema);
    cachedMatrixManager.setAll(PARTIALLY_FULL_MATRIX);
    int mask = cachedMatrixManager.getFreeMask(0, 0);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 2))), mask);

    mask = cachedMatrixManager.getFreeMask(0, 3);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 2) | (1 << 3))), mask);

    mask = cachedMatrixManager.getFreeMask(0, 6);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 4))), mask);

    mask = cachedMatrixManager.getFreeMask(3, 6);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 4) | (1 << 5) | (1 << 6))), mask);
  }

  /**
  * Test setPossible ( int row int col int possible ) method for 9X9 matrix. Note that we don't test matrix sizes here
  */
  @Test
  void testIsSetPossible() {
    Schema schema = SchemaTypes.SCHEMA_9X9;
    CachedMatrixManager cachedMatrixManager = new CachedMatrixManager(schema);
    cachedMatrixManager.setAll(PARTIALLY_FULL_MATRIX);

    assertTrue(cachedMatrixManager.isSetPossible(0, 0, (byte) 0));
    assertFalse(cachedMatrixManager.isSetPossible(0, 0, (byte) 2));
    assertTrue(cachedMatrixManager.isSetPossible(0, 0, (byte) 3));

    assertTrue(cachedMatrixManager.isSetPossible(4, 8, (byte) 0));
    assertTrue(cachedMatrixManager.isSetPossible(4, 8, (byte) 2));
    assertTrue(cachedMatrixManager.isSetPossible(4, 8, (byte) 3));
  }

  /**
  * Test getSetCount and set methods for byte and byte types. This test is a variation of CachedMatrixManager#set ( int int byte )
  */
  @Test
  void testGetSetCount() {
    Schema schema = SchemaTypes.SCHEMA_9X9;
    CachedMatrixManager cachedMatrixManager = new CachedMatrixManager(schema);
    // Sets all the values in the matrix manager.
    for (int row = 0; row < cachedMatrixManager.getSchema().getWidth(); row++) {
      // Sets the matrix to the given column.
      for (int column = 0; column < cachedMatrixManager.getSchema().getWidth(); column++) {
        cachedMatrixManager.set(row, column, cachedMatrixManager.getSchema().getUnsetValue());
        assertEquals(0, cachedMatrixManager.getSetCount());
      }
    }

    int set = 0;
    cachedMatrixManager.set(0, 0, (byte) 1);
    set++;
    assertEquals(set, cachedMatrixManager.getSetCount());

    cachedMatrixManager.set(1, 1, (byte) 2);
    set++;
    assertEquals(set, cachedMatrixManager.getSetCount());

    cachedMatrixManager.set(1, 2, (byte) 3);
    set++;
    assertEquals(set, cachedMatrixManager.getSetCount());

    cachedMatrixManager.set(1, 2, cachedMatrixManager.getSchema().getUnsetValue());
    set--;
    assertEquals(set, cachedMatrixManager.getSetCount());
  }
}

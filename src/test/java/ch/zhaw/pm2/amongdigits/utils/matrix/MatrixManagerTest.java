package ch.zhaw.pm2.amongdigits.utils.matrix;

import static ch.zhaw.pm2.amongdigits.utils.matrix.MatrixManager.FreeCellResult.FOUND;
import static ch.zhaw.pm2.amongdigits.utils.matrix.MatrixManager.FreeCellResult.NONE_FREE;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.zhaw.pm2.amongdigits.TestUtils;
import ch.zhaw.pm2.amongdigits.utils.schema.Schema;
import ch.zhaw.pm2.amongdigits.utils.schema.SchemaTypes;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** A test class for the {@link MatrixManager} class. */
class MatrixManagerTest {

  private static final byte[][] INCREMENTAL_MATRIX = {
    {0, 0, 0, 0, 0, 0, 0, 0, 0},
    {1, 1, 1, 1, 1, 1, 1, 1, 1},
    {2, 2, 2, 2, 2, 2, 2, 2, 2},
    {3, 3, 3, 3, 3, 3, 3, 3, 3},
    {4, 4, 4, 4, 4, 4, 4, 4, 4},
    {5, 5, 5, 5, 5, 5, 5, 5, 5},
    {6, 6, 6, 6, 6, 6, 6, 6, 6},
    {7, 7, 7, 7, 7, 7, 7, 7, 7},
    {8, 8, 8, 8, 8, 8, 8, 8, 8}
  };
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
    {0, 0, 0, 0, 1, 0, 0, 0, 1},
    {0, 0, 0, 0, 0, 0, 0, 1, 0}
  };

  private final Schema schema = SchemaTypes.SCHEMA_9X9;

  /**
   * The testNewMatrix function tests the MatrixManager class' constructor. It checks if the
   * setCount is 0 after creating a new MatrixManager object.
   */
  @Test
  void testNewMatrix() {
    MatrixManager matrixManager = new MatrixManager(schema);
    assertEquals(0, matrixManager.getSetCount());
  }

  /**
   * The testGet function tests the get function of the MatrixManager class. The testGet function is
   * a JUnit5 test that checks if the get function returns an unset value when called with
   * coordinates (0, 0) and (2, 2).
   */
  @Test
  void testGet() {
    MatrixManager matrixManager = new MatrixManager(schema);
    Schema schema = matrixManager.getSchema();
    byte value = matrixManager.get(0, 0);
    assertEquals(schema.getUnsetValue(), value);

    value = matrixManager.get(1, 1);
    assertEquals(schema.getUnsetValue(), value);
  }

  /**
   * The testGetSetCount function tests the getSetCount function of the MatrixManager class. The
   * testGetSetCount function first creates a new MatrixManager object with a schema that has two
   * rows and three columns. Then, it asserts that the set count is 0, since no values have been set
   * yet. Next, it sets (0, 0) to 1 and asserts that the set count is now 1. It then sets (0, 0) to
   * 2 and asserts again that the set count is still 1 because we are setting an already-set value
   * in this case.
   */
  @Test
  void testGetSetCount() {
    MatrixManager matrixManager = new MatrixManager(schema);
    assertEquals(0, matrixManager.getSetCount());

    matrixManager.set(0, 0, (byte) 1);
    assertEquals(1, matrixManager.getSetCount());

    matrixManager.set(0, 0, (byte) 2);
    assertEquals(1, matrixManager.getSetCount());

    matrixManager.set(0, 1, (byte) 2);
    assertEquals(2, matrixManager.getSetCount());
  }

  /**
   * The testGetAll function tests the getAll function of the MatrixManager class. It does this by
   * first creating a new instance of the MatrixManager class, and then setting all values in it to
   * an incremental matrix. Then, it calls getAll on that instance and compares its result with what
   * was expected (the same incremental matrix).
   */
  @Test
  void testGetAll() {
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(INCREMENTAL_MATRIX);
    byte[][] result = matrixManager.getAll();
    assertArrayEquals(INCREMENTAL_MATRIX, result);
  }

  /**
   * The testSet function tests the set function of the MatrixManager class. The testSet function
   * first creates a new matrix manager object and then gets its schema. Then, it gets the value at
   * position (0, 0) in this matrix manager's matrix and checks if it is equal to -128 (the unset
   * value). If so, then we know that our get method works correctly. Next, we set position (0, 0)
   * to 1 using our set method and check if this worked by getting that same position again.
   */
  @Test
  void testSet() {
    MatrixManager matrixManager = new MatrixManager(schema);
    Schema schema = matrixManager.getSchema();
    byte value = matrixManager.get(0, 0);
    assertEquals(schema.getUnsetValue(), value);

    matrixManager.set(0, 0, (byte) 1);
    value = matrixManager.get(0, 0);
    assertEquals(1, value);
  }

  /**
   * The testEqualsWithSame function tests the equals function of the MatrixManager class. It does
   * so by creating a new instance of the MatrixManager class and then comparing it to itself. The
   * testEqualsWithSame function is expected to return true, as an object should always be equal to
   * itself.
   */
  @Test
  void testEqualsWithSame() {
    MatrixManager matrixManager = new MatrixManager(schema);
    assertEquals(matrixManager, matrixManager);
  }

  /**
   * The testEqualsWithNull function tests the MatrixManager class' equals function with a null
   * value. The testEqualsWithNull function is expected to return false, as the MatrixManager class
   * should not be equal to a null value.
   */
  @Test
  void testEqualsWithNull() {
    MatrixManager matrixManager = new MatrixManager(schema);
    assertNotSame(null, matrixManager);
  }

  /**
   * The testEqualsWithOtherClass function tests the MatrixManager class' equals function. It does
   * this by creating a new instance of the MatrixManager class, and then comparing it to an object
   * of another type. The testEqualsWithOtherClass function expects that these two objects are not
   * equal, and will fail if they are equal.
   */
  @Test
  void testEqualsWithOtherClass() {
    MatrixManager matrixManager = new MatrixManager(schema);
    assertNotSame(matrixManager, new Object());
  }

  /**
   * The testEqualsWithSameMatrix function tests the equals function of the MatrixManager class. It
   * creates two instances of a MatrixManager, and sets both to have an incremental matrix as their
   * value. Then it checks if they are equal using assertEquals().
   */
  @Test
  void testEqualsWithSameMatrix() {
    MatrixManager firstMatrixManager = new MatrixManager(schema);
    MatrixManager secondMatrixManager = new MatrixManager(schema);
    firstMatrixManager.setAll(INCREMENTAL_MATRIX);
    secondMatrixManager.setAll(INCREMENTAL_MATRIX);

    assertEquals(firstMatrixManager, secondMatrixManager);
  }

  /**
   * The testEqualsWithDifferentMatrix function tests the MatrixManager class' equals function. It
   * does so by creating two different MatrixManagers, one with an incremental matrix and one with a
   * partially full matrix. The testEqualsWithDifferentMatrix function then asserts that these two
   * objects are not equal to each other.
   */
  @Test
  void testEqualsWithDifferentMatrix() {
    MatrixManager firstMatrixManager = new MatrixManager(schema);
    MatrixManager secondMatrixManager = new MatrixManager(schema);
    firstMatrixManager.setAll(INCREMENTAL_MATRIX);
    secondMatrixManager.setAll(PARTIALLY_FULL_MATRIX);

    assertNotSame(firstMatrixManager, secondMatrixManager);
  }

  /**
   * The testSetAll function tests the setAll function of the MatrixManager class. The testSetAll
   * function creates a new instance of the MatrixManager class, and then calls its setAll method
   * with an array as parameter. Then it checks if this array is equal to what was passed in as
   * parameter to the setAll method.
   */
  @Test
  void testSetAll() {
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(INCREMENTAL_MATRIX);

    byte[][] result = matrixManager.getAll();
    assertArrayEquals(INCREMENTAL_MATRIX, result);
  }

  /**
   * The testRow function tests the row function of the MatrixManager class. The testRow function
   * creates a new matrix manager with a schema and sets all values in it to an incremental matrix.
   * Then, for each row in the schema, it gets that row from the matrix manager and checks if its
   * values are correct.
   */
  @Test
  void testRow() {
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(INCREMENTAL_MATRIX);
    byte[] target = new byte[9];
    for (int i = 0; i < schema.getWidth(); i++) {
      for (int j = 0; j < schema.getWidth(); j++) {
        matrixManager.row(i, target);
        List<Integer> values = TestUtils.toIntList(target);
        assertEquals(Arrays.asList(i, i, i, i, i, i, i, i, i), values);
      }
    }
  }

  /**
   * The testColumn function tests the column function of the MatrixManager class. The testColumn
   * function creates a new matrix manager with a schema and sets all values in it to an incremental
   * matrix. Then, for each column in the schema, it calls the column method on that specific index
   * and checks if its output is correct.
   */
  @Test
  void testColumn() {
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(INCREMENTAL_MATRIX);
    byte[] target = new byte[9];
    for (int i = 0; i < schema.getWidth(); i++) {
      for (int j = 0; j < schema.getWidth(); j++) {
        matrixManager.column(i, target);
        List<Integer> values = TestUtils.toIntList(target);
        assertEquals(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8), values);
      }
    }
  }

  /**
   * The testBlock function tests the block function in MatrixManager. The testBlock function
   * creates a new matrix manager with a schema, and then sets all of the values in that matrix to
   * an incremental list. Then, it creates a target byte array and calls the block method on it with
   * coordinates (0, 0). It then converts this target byte array into an integer list and compares
   * it to another integer list containing what we expect from calling block(0, 0).
   */
  @Test
  void testBlock() {
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(INCREMENTAL_MATRIX);
    byte[] target = new byte[9];
    matrixManager.block(0, 0, target);
    List<Integer> values = TestUtils.toIntList(target);
    assertEquals(Arrays.asList(0, 0, 0, 1, 1, 1, 2, 2, 2), values);

    matrixManager.block(0, 6, target);
    values = TestUtils.toIntList(target);
    assertEquals(Arrays.asList(0, 0, 0, 1, 1, 1, 2, 2, 2), values);
  }

  /**
   * The testClear function tests the clear function of the MatrixManager class. The testClear
   * function first creates a new instance of the MatrixManager class, and then sets all values in
   * this instance to an incremental matrix (see TestUtils). After that, it calls on the clear
   * method from within this instance. Finally, it asserts that all values in this matrix are now 0
   * by comparing them with a zero-matrix (see TestUtils).
   */
  @Test
  void testClear() {
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(INCREMENTAL_MATRIX);
    matrixManager.clear();
    byte[][] result = matrixManager.getAll();
    assertArrayEquals(new byte[9][9], result);
  }

  /**
   * The testClone function tests the clone function of the MatrixManager class. The testClone
   * function creates a new matrix manager object and sets its values to an incremental matrix.
   * Then, it clones this matrix manager object and checks if the cloned version has all of its
   * values set to those in the original one.
   */
  @Test
  void testClone() {
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(INCREMENTAL_MATRIX);
    MatrixManager clone = MatrixManager.clone(matrixManager);
    assertArrayEquals(INCREMENTAL_MATRIX, clone.getAll());
  }

  /**
   * The testFindDuplicate function tests the findDuplicateBits function in MatrixManager.java The
   * testFindDuplicate function is a JUnit5 test that checks if the findDuplicateBits function works
   * as intended. The first assertEquals statement checks if there are no duplicates in an array of
   * numbers, and returns 0 (no bits set). The second assertEquals statement checks if there is one
   * duplicate number in an array of numbers, and returns 2 (the second bit set). The third
   * assertEquals statement checks if there are multiple duplicate numbers in an array of numbers,
   * and returns 2
   */
  @Test
  void testFindDuplicate() {
    byte[] array = new byte[] {1, 2, 3, 4, 5};
    int bitMask = MatrixManager.findDuplicateBits(schema, array);
    assertEquals(0, bitMask);

    array = new byte[] {1, 1, 3, 4, 5};
    bitMask = MatrixManager.findDuplicateBits(schema, array);
    assertEquals(2, bitMask);

    array = new byte[] {1, 1, 1, 4, 5};
    bitMask = MatrixManager.findDuplicateBits(schema, array);
    assertEquals(2, bitMask);

    array = new byte[] {0, 0, 0, 4, 5};
    bitMask = MatrixManager.findDuplicateBits(schema, array);
    assertEquals(0, bitMask);
  }

  /**
   * The testGetNumberMask function tests the getNumberMask function in MatrixManager.java The
   * testGetNumberMask function takes a byte array and a schema as parameters, and returns an
   * integer bitmask. The testGetNumberMask function is used to determine which numbers are present
   * in the byte array, based on the schema provided.
   */
  @Test
  void testGetNumberMask() {
    byte[] array = new byte[] {1, 2, 3, 4, 5};
    int bitMask = MatrixManager.getNumberMask(schema, array);
    assertEquals(2 + 4 + 8 + 16 + 32, bitMask);

    array = new byte[] {1, 1, 3, 4, 5};
    bitMask = MatrixManager.getNumberMask(schema, array);
    assertEquals(2 + 8 + 16 + 32, bitMask);

    array = new byte[] {1, 1, 1, 4, 5};
    bitMask = MatrixManager.getNumberMask(schema, array);
    assertEquals(2 + 16 + 32, bitMask);

    array = new byte[] {0, 0, 0, 4, 5};
    bitMask = MatrixManager.getNumberMask(schema, array);
    assertEquals(16 + 32, bitMask);
  }

  /**
   * The testIsValidWithInvalid function tests the isValid function of the MatrixManager class. The
   * testIsValidWithInvalid function creates a new instance of the MatrixManager class, and sets all
   * values in its matrix to be equal to INCREMENTAL_MATRIX. Then, it asserts that isValid returns
   * false when called on this instance of MatrixManager.
   */
  @Test
  void testIsValidWithInvalid() {
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(INCREMENTAL_MATRIX);
    assertFalse(matrixManager.isValid());
  }

  /**
   * The testIsValidWithEmptyValid function tests the isValid function of the MatrixManager class.
   * The testIsValidWithEmptyValid function creates a new matrix manager with an empty schema, and
   * then sets all values in the matrix to 0. Then, it checks if this matrix is valid by calling the
   * isValid method on it.
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
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(matrix);
    assertTrue(matrixManager.isValid());
  }

  /**
   * The testIsValidWithPartlyFullValid function tests the isValid function of the MatrixManager
   * class. The testIsValidWithPartlyFullValid function creates a matrix with some values in it and
   * then checks if this matrix is valid. The testIsValidWithPartlyFullValid function expects that
   * the created matrix will be valid, because there are no duplicates in any row or column.
   */
  @Test
  void testIsValidWithPartlyFullValid() {
    byte[][] matrix = {
      {1, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 1, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 1, 0, 0},
      {0, 1, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 1, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 1, 0},
      {0, 0, 1, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 1, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 1}
    };
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(matrix);
    assertTrue(matrixManager.isValid());
  }

  /**
   * The testIsValidWithBlockTopLeftCollision function tests the isValid function of the
   * MatrixManager class. The testIsValidWithBlockTopLeftCollision function creates a matrix with
   * two blocks in the top left corner, and then checks if it is valid. It should not be valid
   * because there are two blocks in one block area.
   */
  @Test
  void testIsValidWithBlockTopLeftCollision() {
    byte[][] matrix = {
      {1, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 1, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(matrix);
    assertFalse(matrixManager.isValid());
  }

  /**
   * The testIsValidWithBlockTopRightCollision function tests the isValid function of the
   * MatrixManager class. The testIsValidWithBlockTopRightCollision function creates a matrix with
   * two blocks in the top right corner, and then checks if it is valid. It should return false
   * because there are two blocks in one cell.
   */
  @Test
  void testIsValidWithBlockTopRightCollision() {
    byte[][] matrix = {
      {0, 0, 0, 0, 0, 0, 0, 0, 1},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 1, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {1, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(matrix);
    assertFalse(matrixManager.isValid());
  }

  /**
   * The testIsValidWithBlockBottomRightCollision function tests the isValid function of the
   * MatrixManager class. The testIsValidWithBlockBottomRightCollision function creates a matrix
   * with two blocks in it, one at position (7, 7) and one at position (8, 8). The
   * testIsValidWithBlockBottomRightCollision function then checks if the isValid method returns
   * false.
   */
  @Test
  void testIsValidWithBlockBottomRightCollision() {
    byte[][] matrix = {
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 1, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 1}
    };
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(matrix);
    assertFalse(matrixManager.isValid());
  }

  /**
   * The testIsValidWithBlockCentralLeftCollision function tests the isValid function of the
   * MatrixManager class. The testIsValidWithBlockCentralLeftCollision function creates a matrix
   * with two blocks in it, one block being on top of another. The
   * testIsValidWithBlockCentralLeftCollision function then checks if this matrix is valid by
   * calling the isValid method from MatrixManager and asserts that it returns false.
   */
  @Test
  void testIsValidWithBlockCentralLeftCollision() {
    byte[][] matrix = {
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 1, 0, 0, 0, 0, 0, 0},
      {1, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(matrix);
    assertFalse(matrixManager.isValid());
  }

  /**
   * The testIsValidWithBlockCentralRightCollision function tests the isValid function of the
   * MatrixManager class. The testIsValidWithBlockCentralRightCollision function creates a matrix
   * with a block in its center and another block to its right, which should be invalid. The
   * testIsValidWithBlockCentralRightCollision function then calls the isValid method on this matrix
   * and checks if it returns false, as expected.
   */
  @Test
  void testIsValidWithBlockCentralRightCollision() {
    byte[][] matrix =
        new byte[][] {
          {0, 0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 0, 1},
          {0, 0, 0, 0, 0, 0, 1, 0, 0},
          {0, 0, 0, 0, 0, 1, 0, 0, 0},
          {0, 0, 0, 0, 1, 0, 0, 0, 0},
          {0, 0, 0, 1, 0, 0, 0, 0, 0}
        };

    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(matrix);
    assertFalse(matrixManager.isValid());
  }

  /**
   * The testIsValidWithBlockCentralCentralCollision function tests the isValid function of the
   * MatrixManager class. The testIsValidWithBlockCentralCentralCollision function creates a matrix
   * with a block in its center and another block in its central-right position. The
   * testIsValidWithBlockCentralCentralCollision function then checks if this matrix is valid, which
   * it should not be because there are two blocks that collide with each other.
   */
  @Test
  void testIsValidWithBlockCentralCentralCollision() {
    byte[][] matrix =
        new byte[][] {
          {0, 0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 1, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 1, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 1, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 1, 0, 0}
        };
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(matrix);
    assertFalse(matrixManager.isValid());
  }

  /**
   * The testIsSetPossible function tests the isSetPossible function in MatrixManager.java The
   * testIsSetPossible function checks if a number can be set to a specific position in the matrix.
   * It does this by checking if there are any other numbers of that value already present on that
   * row, column or block.
   */
  @Test
  void testIsSetPossible() {
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(PARTIALLY_FULL_MATRIX);
    assertTrue(matrixManager.isSetPossible(0, 0, (byte) 0));
    assertFalse(matrixManager.isSetPossible(0, 0, (byte) 2));
    assertTrue(matrixManager.isSetPossible(0, 0, (byte) 3));

    assertTrue(matrixManager.isSetPossible(4, 8, (byte) 0));
    assertTrue(matrixManager.isSetPossible(4, 8, (byte) 2));
    assertTrue(matrixManager.isSetPossible(4, 8, (byte) 3));
  }

  /**
   * The testRoundToBlock function tests the roundToBlock function in MatrixManager. The
   * roundToBlock function rounds a number to the nearest multiple of 3, which is used for rounding
   * coordinates.
   */
  @Test
  void testRoundToBlock() {
    MatrixManager matrixManager = new MatrixManager(schema);
    assertEquals(0, matrixManager.roundToBlock(0));
    assertEquals(0, matrixManager.roundToBlock(1));
    assertEquals(0, matrixManager.roundToBlock(2));
    assertEquals(3, matrixManager.roundToBlock(3));
    assertEquals(3, matrixManager.roundToBlock(4));
    assertEquals(3, matrixManager.roundToBlock(5));
    assertEquals(6, matrixManager.roundToBlock(6));
    assertEquals(6, matrixManager.roundToBlock(7));
    assertEquals(6, matrixManager.roundToBlock(8));
  }

  /**
   * The testGetRowFreeMask function tests the getRowFreeMask function in MatrixManager.java The
   * testGetRowFreeMask function is a unit test that checks if the getRowFreeMask function works as
   * intended. The testGetRowFreeMask function uses a partially filled matrix and then checks if it
   * returns the correct mask for each row of this matrix.
   */
  @Test
  void testGetRowFreeMask() {
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(PARTIALLY_FULL_MATRIX);
    int mask = matrixManager.getRowFreeMask(0);
    assertEquals(schema.getBitMask() & (~(1 << 1)), mask);

    mask = matrixManager.getRowFreeMask(1);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 2))), mask);

    mask = matrixManager.getRowFreeMask(2);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 2) | (1 << 3))), mask);

    mask = matrixManager.getRowFreeMask(3);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 4) | (1 << 5) | (1 << 6))), mask);
  }

  /**
   * The testGetColumnFreeMask function tests the getColumnFreeMask function in MatrixManager. The
   * testGetColumnFreeMask function is a unit test that checks if the getColumnFreeMask function
   * returns the correct mask for each column of a matrix. The mask returned by this method should
   * be equal to schema's bitmask with all bits set to 1, except for those bits corresponding to
   * numbers already present in that column of the matrix. For example, if we have a 4x4 matrix and
   * there are two ones and one three in its first column, then calling this method on it will
   * return: 11101
   */
  @Test
  void testGetColumnFreeMask() {
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(PARTIALLY_FULL_MATRIX);
    int mask = matrixManager.getColumnFreeMask(0);
    assertEquals(schema.getBitMask() & (~(1 << 1)), mask);

    mask = matrixManager.getColumnFreeMask(1);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 2))), mask);

    mask = matrixManager.getColumnFreeMask(2);
    assertEquals(schema.getBitMask() & (~((1 << 1))), mask);

    mask = matrixManager.getColumnFreeMask(3);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 3))), mask);
  }

  /**
   * The testGetBlockFreeMask function tests the getBlockFreeMask function in MatrixManager.java The
   * testGetBlockFreeMask function is a unit test that checks if the getBlockFreeMask function works
   * as intended. The getBlockFreeMask returns an integer mask of all numbers that are not present
   * in a block, given by its coordinates (x,y).
   */
  @Test
  void testGetBlockFreeMask() {
    Schema schema = SchemaTypes.SCHEMA_9X9;
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(PARTIALLY_FULL_MATRIX);
    int mask = matrixManager.getBlockFreeMask(0, 0);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 2))), mask);

    mask = matrixManager.getBlockFreeMask(0, 3);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 2) | (1 << 3))), mask);

    mask = matrixManager.getBlockFreeMask(0, 6);
    assertEquals(schema.getBitMask() & (~((1 << 1))), mask);

    mask = matrixManager.getBlockFreeMask(3, 6);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 4) | (1 << 5) | (1 << 6))), mask);
  }

  /**
   * The testGetFreeMask function tests the getFreeMask function in MatrixManager.java The
   * testGetFreeMask function is a unit test that checks if the getFreeMask function works as
   * intended. The getFreeMask function returns an integer mask of all possible values for a given
   * cell, based on its row and column index.
   */
  @Test
  void testGetFreeMask() {
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(PARTIALLY_FULL_MATRIX);
    int mask = matrixManager.getFreeMask(0, 0);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 2))), mask);

    mask = matrixManager.getFreeMask(0, 3);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 2) | (1 << 3))), mask);

    mask = matrixManager.getFreeMask(0, 6);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 4))), mask);

    mask = matrixManager.getFreeMask(3, 6);
    assertEquals(schema.getBitMask() & (~((1 << 1) | (1 << 4) | (1 << 5) | (1 << 6))), mask);
  }

  /**
   * The testFindLeastFreeCellWithAllFull function tests the findLeastFreeCell function in
   * MatrixManager.java by setting all cells to full and then checking if the result is NONE_FREE.
   */
  @Test
  void testFindLeastFreeCellWithAllFull() {
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(FULL_MATRIX);
    int[] min = new int[2];
    MatrixManager.FreeCellResult result = matrixManager.findLeastFreeCell(min);
    assertEquals(NONE_FREE, result);
  }

  /**
   * The testFindLeastFreeCellWithAlmostFull function tests the findLeastFreeCell function in the
   * MatrixManager class. The testFindLeastFreeCellWithAlmostFull function creates a matrix with all
   * cells filled except for one cell, and then calls the findLeastFreeCell function to see if it
   * can correctly identify that there is only one free cell left. If so, then it will return FOUND
   * as its result and set min[0] = 0 and min[2] = 0 (the coordinates of the free cell). This test
   * passes if these conditions are met. Otherwise, this test fails.
   */
  @Test
  void testFindLeastFreeCellWithAlmostFull() {
    byte[][] matrix = {
      {0, 6, 7, 9, 1, 5, 4, 8, 2},
      {1, 4, 9, 2, 6, 8, 3, 5, 7},
      {5, 8, 2, 4, 7, 3, 6, 1, 9},
      {4, 3, 6, 1, 8, 7, 9, 2, 5},
      {9, 7, 5, 6, 2, 4, 8, 3, 1},
      {2, 1, 8, 3, 5, 9, 7, 6, 4},
      {6, 2, 4, 7, 3, 1, 5, 9, 8},
      {7, 5, 3, 8, 9, 2, 1, 4, 6},
      {8, 9, 1, 5, 4, 6, 2, 7, 3}
    };
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(matrix);
    int[] min = new int[2];
    MatrixManager.FreeCellResult result = matrixManager.findLeastFreeCell(min);
    assertEquals(FOUND, result);
    assertEquals(0, min[0]);
    assertEquals(0, min[1]);
  }

  /**
   * The testFindLeastFreeCell function tests the findLeastFreeCell function in MatrixManager.java
   * The testFindLeastFreeCell function creates a matrix and then uses the setAll method to fill it
   * with values. Then, it calls the findLeastFreeCell method on that matrix and checks if its
   * result is FOUND (which means that there are still free cells). It also checks if min[0] and
   * min[2] are equal to 2, which is what they should be according to our schema.
   */
  @Test
  void testFindLeastFreeCell() {
    byte[][] matrix = {
      {1, 2, 3, 0, 0, 0, 0, 0, 0},
      {4, 5, 6, 0, 0, 0, 0, 0, 0},
      {7, 8, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    MatrixManager matrixManager = new MatrixManager(schema);
    matrixManager.setAll(matrix);
    int[] min = new int[2];
    MatrixManager.FreeCellResult result = matrixManager.findLeastFreeCell(min);
    assertEquals(FOUND, result);
    assertEquals(2, min[0]);
    assertEquals(2, min[1]);
  }
}

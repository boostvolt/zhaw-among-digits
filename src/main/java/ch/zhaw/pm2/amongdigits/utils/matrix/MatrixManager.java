package ch.zhaw.pm2.amongdigits.utils.matrix;

import static ch.zhaw.pm2.amongdigits.utils.matrix.MatrixManager.FreeCellResult.CONTRADICTION;
import static ch.zhaw.pm2.amongdigits.utils.matrix.MatrixManager.FreeCellResult.FOUND;
import static ch.zhaw.pm2.amongdigits.utils.matrix.MatrixManager.FreeCellResult.NONE_FREE;

import ch.zhaw.pm2.amongdigits.utils.schema.Schema;
import java.util.Arrays;

/**
 * MatrixManager represents an implementation of the Matrix interface. It stores a matrix of bytes
 * and a schema defining the valid values and dimensions of the matrix.
 */
public class MatrixManager implements Matrix {

  private final Schema schema;
  private final byte[][] matrix;

  /**
   * Constructs a new MatrixManager instance with the specified schema.
   *
   * @param schema the schema used by the matrix.
   */
  public MatrixManager(final Schema schema) {
    this.schema = schema;
    matrix = new byte[schema.getWidth()][schema.getWidth()];
  }

  /**
   * Clones a MatrixManager instance by creating a new MatrixManager instance with the same schema
   * and the same matrix values as the original.
   *
   * @param matrixManager the MatrixManager to be cloned.
   * @return the cloned MatrixManager instance.
   */
  static MatrixManager clone(final MatrixManager matrixManager) {
    MatrixManager clone = new MatrixManager(matrixManager.getSchema());
    clone.setAll(matrixManager.getAll());

    return clone;
  }

  /**
   * Finds the duplicate bits in an array of bytes.
   *
   * @param schema the schema used by the array.
   * @param array the array of bytes to be searched for duplicate bits.
   * @return an integer representing the duplicate bits found in the array.
   */
  static int findDuplicateBits(final Schema schema, final byte[] array) {
    int bitMask = 0;
    int duplicateBits = 0;
    byte unset = schema.getUnsetValue();
    for (final byte cellValue : array) {
      if (cellValue != unset) {
        final int shifted = 1 << cellValue;
        duplicateBits |= bitMask & shifted;
        bitMask |= shifted;
      }
    }

    return duplicateBits & (~1);
  }

  /**
   * Returns the number mask of an array of bytes.
   *
   * @param schema the schema used by the array.
   * @param array the array of bytes to be evaluated.
   * @return an integer representing the number mask of the array.
   */
  static int getNumberMask(final Schema schema, final byte[] array) {
    int bitMask = 0;
    final byte unset = schema.getUnsetValue();
    for (byte b : array) {
      if (b != unset) {
        bitMask |= 1 << b;
      }
    }

    return bitMask & (~1);
  }

  /** {@inheritDoc} */
  @Override
  public Schema getSchema() {
    return schema;
  }

  /** {@inheritDoc} */
  @Override
  public void clear() {
    byte unsetValue = schema.getUnsetValue();
    for (int i = 0; i < schema.getWidth(); i++) {
      for (int j = 0; j < schema.getWidth(); j++) {
        set(j, i, unsetValue);
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public byte get(final int row, final int column) {
    if (!getSchema().areCoordsValid(row, column)) {
      throw new IllegalArgumentException("Coordinates are not valid");
    }
    return matrix[row][column];
  }

  /** {@inheritDoc} */
  @Override
  public byte[][] getAll() {
    return matrix.clone();
  }

  /** {@inheritDoc} */
  @Override
  public void setAll(final byte[][] values) {
    for (int i = 0; i < schema.getWidth(); i++) {
      for (int j = 0; j < schema.getWidth(); j++) {
        set(j, i, values[j][i]);
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public int getSetCount() {
    int count = 0;
    for (int i = 0; i < schema.getWidth(); i++) {
      for (int j = 0; j < schema.getWidth(); j++) {
        assert getSchema().isValueValid(matrix[i][j]);
        if (matrix[i][j] != schema.getUnsetValue()) {
          count++;
        }
      }
    }

    assert count >= 0 && count <= schema.getTotalFields();
    return count;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isValid() {
    boolean result = true;
    byte[] array = new byte[schema.getWidth()];

    for (int i = 0; i < schema.getWidth() && result; i++) {
      row(i, array);
      result = findDuplicateBits(schema, array) == 0;
    }

    for (int i = 0; i < schema.getWidth() && result; i++) {
      column(i, array);
      result = findDuplicateBits(schema, array) == 0;
    }

    for (int i = 0; i < schema.getWidth() && result; i += schema.getBlockWidth()) {
      for (int j = 0; j < schema.getWidth() && result; j += schema.getBlockWidth()) {
        block(i, j, array);
        result = findDuplicateBits(schema, array) == 0;
      }
    }

    return result;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSetPossible(final int row, final int column, final byte value) {
    if (!schema.areCoordsValid(row, column)) {
      throw new IllegalArgumentException("Coordinates are not valid");
    }
    if (!schema.isValueValid(value)) {
      throw new IllegalArgumentException("Invalid value");
    }
    if (value == schema.getUnsetValue()) {
      return true;
    }

    int free = getFreeMask(row, column);
    return (free & (1 << value)) != 0;
  }

  /** {@inheritDoc} */
  @Override
  public void set(final int row, final int column, final byte value) {
    if (!getSchema().areCoordsValid(row, column)) {
      throw new IllegalArgumentException("Invalid row or column index");
    }
    if (!getSchema().isValueValid(value)) {
      throw new IllegalArgumentException("Invalid value");
    }
    matrix[row][column] = value;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Arrays.deepHashCode(matrix);
  }

  /** {@inheritDoc} */
  @Override
  public final boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof MatrixManager other)) {
      return false;
    }

    return Arrays.deepEquals(matrix, other.matrix);
  }

  /**
   * Returns a free mask for a given row.
   *
   * @param row The row index.
   * @return The free mask for the row.
   */
  int getRowFreeMask(final int row) {
    byte[] array = new byte[schema.getWidth()];
    row(row, array);
    return (~getNumberMask(schema, array)) & getSchema().getBitMask();
  }

  /**
   * Returns a free mask for a given column.
   *
   * @param column The column index.
   * @return The free mask for the column.
   */
  int getColumnFreeMask(final int column) {
    byte[] array = new byte[schema.getWidth()];
    column(column, array);
    return (~getNumberMask(schema, array)) & getSchema().getBitMask();
  }

  /**
   * Returns a free mask for a given block.
   *
   * @param row The row index of the block.
   * @param column The column index of the block.
   * @return The free mask for the block.
   */
  int getBlockFreeMask(final int row, final int column) {
    byte[] array = new byte[getSchema().getBlockWidth() * getSchema().getBlockWidth()];
    block(row, column, array);
    return (~getNumberMask(schema, array)) & getSchema().getBitMask();
  }

  /**
   * Returns a free mask for a given cell.
   *
   * @param row The row index of the cell.
   * @param column The column index of the cell.
   * @return The free mask for the cell.
   * @throws IllegalArgumentException If the row or column index is invalid.
   */
  public int getFreeMask(final int row, final int column) {
    int free = schema.getBitMask();
    if (!schema.areCoordsValid(row, column)) {
      throw new IllegalArgumentException("Invalid row or column index");
    }
    free &= getRowFreeMask(row);
    free &= getColumnFreeMask(column);
    free &= getBlockFreeMask(row, column);
    return free;
  }

  /**
   * Rounds a value down to the nearest multiple of the block width of the schema.
   *
   * @param value The value to round.
   * @return The rounded value.
   */
  final int roundToBlock(final int value) {
    return value - (value % schema.getBlockWidth());
  }

  /**
   * Copies a row of the schema into the target byte array.
   *
   * @param index The index of the row to copy.
   * @param target The target byte array to copy the row into.
   * @throws IllegalArgumentException If the length of the target array does not match the width of
   *     the schema.
   */
  final void row(final int index, final byte[] target) {
    if (target.length != schema.getWidth()) {
      throw new IllegalArgumentException("Array length does not match schema width");
    }
    System.arraycopy(matrix[index], 0, target, 0, schema.getWidth());
  }

  /**
   * Copies a column of the schema into the target byte array.
   *
   * @param index The index of the column to copy.
   * @param target The target byte array to copy the column into.
   * @throws IllegalArgumentException If the length of the target array does not match the width of
   *     the schema.
   */
  final void column(final int index, final byte[] target) {
    if (target.length != schema.getWidth()) {
      throw new IllegalArgumentException("Target array length does not match schema width");
    }
    for (int row = 0; row < schema.getWidth(); row++) {
      target[row] = matrix[row][index];
    }
  }

  /**
   * Copies a block of the schema into the target byte array.
   *
   * @param row The row index of the block to copy.
   * @param column The column index of the block to copy.
   * @param target The target byte array to copy the block into.
   * @throws IllegalArgumentException If the length of the target array does not match the width of
   *     the schema, or if the specified coordinates are invalid for the schema.
   */
  final void block(final int row, final int column, final byte[] target) {
    if (target.length != schema.getWidth()) {
      throw new IllegalArgumentException("Target array length does not match schema width");
    }
    if (!getSchema().areCoordsValid(row, column)) {
      throw new IllegalArgumentException("Invalid coordinates for schema");
    }

    int targetIndex = 0;
    int roundRow = roundToBlock(row);
    int roundColumn = roundToBlock(column);
    for (int i = 0; i < schema.getBlockWidth(); i++) {
      for (int j = 0; j < schema.getBlockWidth(); j++) {
        target[targetIndex++] = matrix[roundRow + i][roundColumn + j];
      }
    }
  }

  /**
   * Finds the cell with the least number of free values in the Sudoku matrix.
   *
   * @param rowColumnResult an array to store the row and column indices of the least free cell
   * @return a FreeCellResult representing the result of the search
   */
  public FreeCellResult findLeastFreeCell(final int[] rowColumnResult) {
    int minimumBits = -1;
    int minimumRow = -1;
    int minimumColumn = -1;

    final int width = getSchema().getWidth();
    final byte unset = getSchema().getUnsetValue();
    boolean cellFound = false;

    for (int row = 0; row < width && !cellFound; row++) {
      int rowMask = getRowFreeMask(row);
      if (rowMask == 0) {
        continue;
      }
      int[] result = findMinimumFreeCellInRow(row, unset);
      int bits = result[2];
      if (bits == 0) {
        return CONTRADICTION;
      }

      assert bits <= width;

      if (minimumBits == -1 || bits < minimumBits) {
        minimumRow = row;
        minimumColumn = result[1];
        minimumBits = bits;
        if (minimumBits == 1) {
          cellFound = true;
        }
      }
    }

    rowColumnResult[0] = minimumRow;
    rowColumnResult[1] = minimumColumn;

    return minimumBits != -1 ? FOUND : NONE_FREE;
  }

  private int[] findMinimumFreeCellInRow(final int row, final byte unset) {
    final int width = getSchema().getWidth();
    int minimumBits = -1;
    int minimumColumn = -1;
    int free;

    for (int column = 0; column < width; column++) {
      if (get(row, column) != unset) {
        continue;
      }
      free = getFreeMask(row, column);
      int bits = Integer.bitCount(free);

      if (bits != 0 && (minimumBits == -1 || bits < minimumBits)) {
        minimumColumn = column;
        minimumBits = bits;
      }
    }

    return new int[] {row, minimumColumn, minimumBits};
  }

  /**
   * Enumeration representing the result of finding the least free cell in a Sudoku puzzle. It has
   * three possible values:
   *
   * <ul>
   *   <li>{@code FOUND}: a cell with the least number of possible values was found
   *   <li>{@code NONE_FREE}: there are no free cells in the puzzle
   *   <li>{@code CONTRADICTION}: there is a contradiction in the puzzle, i.e., no possible values
   *       for a cell
   * </ul>
   */
  public enum FreeCellResult {
    FOUND,
    NONE_FREE,
    CONTRADICTION
  }
}

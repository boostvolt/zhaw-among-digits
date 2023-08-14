package ch.zhaw.pm2.amongdigits.utils.matrix;

import ch.zhaw.pm2.amongdigits.utils.schema.Schema;

/**
 * The CachedMatrixManager class extends the MatrixManager class and implements an optimized version
 * of a Sudoku puzzle matrix that uses caching to improve performance. The cached matrix stores the
 * free cell options in rowFree, columnFree, and blockFree arrays and the number of set cells in
 * setCount.
 */
public class CachedMatrixManager extends MatrixManager {

  private final int[] rowFree;
  private final int[] columnFree;
  private final int[][] blockFree;
  private int setCount;

  /**
   * Constructs a new CachedMatrixManager object with the specified schema.
   *
   * @param schema the schema to use for the Sudoku puzzle
   */
  public CachedMatrixManager(final Schema schema) {
    super(schema);
    final int blockCount = schema.getBlockCount();
    final int width = schema.getWidth();

    blockFree = new int[blockCount][blockCount];
    rowFree = new int[width];
    columnFree = new int[width];

    for (int i = 0; i < width; i++) {
      rowFree[i] = schema.getBitMask();
      columnFree[i] = schema.getBitMask();
    }

    for (int i = 0; i < blockCount; i++) {
      for (int j = 0; j < blockCount; j++) {
        blockFree[i][j] = schema.getBitMask();
      }
    }
  }

  /**
   * Clones the specified CachedMatrixManager object and returns the new clone.
   *
   * @param cachedMatrixManager the CachedMatrixManager object to clone
   * @return the new clone of the CachedMatrixManager object
   */
  static CachedMatrixManager clone(final CachedMatrixManager cachedMatrixManager) {
    CachedMatrixManager clone = new CachedMatrixManager(cachedMatrixManager.getSchema());
    clone.setAll(cachedMatrixManager.getAll());

    return clone;
  }

  /**
   * Returns the bit mask of the free values for the given block.
   *
   * @param row The row of the block
   * @param column The column of the block
   * @return The bit mask of the free values for the given block
   */
  @Override
  public int getBlockFreeMask(final int row, final int column) {
    final int blockWidth = getSchema().getBlockWidth();

    return blockFree[row / blockWidth][column / blockWidth];
  }

  /**
   * Returns the bit mask of the free values for the given column.
   *
   * @param column The column to check
   * @return The bit mask of the free values for the given column
   */
  @Override
  public int getColumnFreeMask(final int column) {
    return columnFree[column];
  }

  /**
   * Returns the bit mask of the free values for the given row.
   *
   * @param row The row to check
   * @return The bit mask of the free values for the given row
   */
  @Override
  public int getRowFreeMask(final int row) {
    return rowFree[row];
  }

  /**
   * Returns the bit mask of the free values for the given cell.
   *
   * @param row The row of the cell
   * @param column The column of the cell
   * @return The bit mask of the free values for the given cell
   */
  @Override
  public int getFreeMask(final int row, final int column) {
    final int blockWidth = getSchema().getBlockWidth();

    return rowFree[row] & columnFree[column] & blockFree[row / blockWidth][column / blockWidth];
  }

  /**
   * Sets a value in the Sudoku puzzle at the specified row and column.
   *
   * @param row the row index where the value will be set
   * @param column the column index where the value will be set
   * @param value the value to be set
   * @throws IllegalArgumentException if the value is not valid for the puzzle's schema or if the
   *     value is not allowed at the specified position
   * @throws IllegalStateException if any of the row, column, or block free masks are invalid after
   *     the value is set
   */
  @Override
  public void set(final int row, final int column, final byte value) {
    Schema schema = getSchema();

    if (!schema.isValueValid(value)) {
      throw new IllegalArgumentException("The value " + value + " is not valid.");
    }
    byte oldValue = super.get(row, column);
    assert schema.isValueValid(oldValue);

    final byte unset = schema.getUnsetValue();
    final int blockWidth = schema.getBlockWidth();

    if (oldValue != unset) {
      int bitMask = 1 << oldValue;
      rowFree[row] |= bitMask;
      columnFree[column] |= bitMask;
      blockFree[row / blockWidth][column / blockWidth] =
          blockFree[row / blockWidth][column / blockWidth] | bitMask;
      setCount--;
      assert setCount >= 0;
    }

    if (value != unset) {
      if ((getFreeMask(row, column) & (1 << value)) == 0) {
        throw new IllegalArgumentException(
            "Value " + value + " is not allowed at position (" + row + ", " + column + ")");
      }
      int bitMask = ~(1 << value);
      rowFree[row] &= bitMask;
      columnFree[column] &= bitMask;
      blockFree[row / blockWidth][column / blockWidth] &= bitMask;
      setCount++;
      assert setCount <= getSchema().getTotalFields();
    }

    if (!getSchema().isBitMaskValid(rowFree[row])) {
      throw new IllegalStateException("Row free mask is invalid: " + rowFree[row]);
    }

    if (!getSchema().isBitMaskValid(columnFree[column])) {
      throw new IllegalStateException("Column free mask is invalid: " + columnFree[column]);
    }

    if (!getSchema().isBitMaskValid(blockFree[row / blockWidth][column / blockWidth])) {
      throw new IllegalStateException(
          "Block free mask is invalid: " + blockFree[row / blockWidth][column / blockWidth]);
    }

    super.set(row, column, value);
  }

  /**
   * Returns the number of fields that have been set in the Sudoku puzzle.
   *
   * @return the number of set fields
   */
  @Override
  public int getSetCount() {
    return setCount;
  }
}

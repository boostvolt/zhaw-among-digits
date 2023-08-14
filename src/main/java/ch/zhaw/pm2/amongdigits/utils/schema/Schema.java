package ch.zhaw.pm2.amongdigits.utils.schema;

/**
 * An interface representing a schema for a sudoku-like game. A schema defines the size and
 * structure of the grid, as well as the valid values that can be entered in each field of the grid.
 */
public interface Schema {

  /**
   * Returns the minimum valid value that can be entered in any field of the schema.
   *
   * @return the minimum valid value
   */
  byte getMinimumValue();

  /**
   * Returns the maximum valid value that can be entered in any field of the schema.
   *
   * @return the maximum valid value
   */
  byte getMaximumValue();

  /**
   * Returns the value that represents an unset field in the schema.
   *
   * @return the value that represents an unset field
   */
  byte getUnsetValue();

  /**
   * Returns the width of the schema, i.e. the number of fields in each row and column of the grid.
   *
   * @return the width of the schema
   */
  int getWidth();

  /**
   * Returns the width of a block in the schema, i.e. the number of fields in each row and column of
   * a block of the grid.
   *
   * @return the width of a block in the schema
   */
  int getBlockWidth();

  /**
   * Returns the total number of fields in the schema, i.e. the number of fields in the entire grid.
   *
   * @return the total number of fields in the schema
   */
  int getTotalFields();

  /**
   * Returns the number of blocks in the schema, i.e. the number of sub-grids in the grid.
   *
   * @return the number of blocks in the schema
   */
  int getBlockCount();

  /**
   * Returns a bit mask representing the valid values for the fields in the schema. The i-th bit of
   * the mask is set if the value i+1 is a valid value for the schema.
   *
   * @return a bit mask representing the valid values for the fields in the schema
   */
  int getBitMask();

  /**
   * Returns whether the given value is a valid value for the schema.
   *
   * @param value the value to check
   * @return true if the value is valid, false otherwise
   */
  boolean isValueValid(byte value);

  /**
   * Returns whether the given bit mask is a valid bit mask for the schema.
   *
   * @param bitMask the bit mask to check
   * @return true if the bit mask is valid, false otherwise
   */
  boolean isBitMaskValid(int bitMask);

  /**
   * Returns whether the given coordinates are valid for the schema.
   *
   * @param row the row index (0-based)
   * @param column the column index (0-based)
   * @return true if the coordinates are valid, false otherwise
   */
  boolean areCoordsValid(int row, int column);
}

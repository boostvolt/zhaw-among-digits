package ch.zhaw.pm2.amongdigits.utils.matrix;

import ch.zhaw.pm2.amongdigits.utils.schema.Schema;

/** The Matrix interface defines a contract for working with matrices in a Sudoku puzzle. */
public interface Matrix {

  /**
   * Returns the schema of the matrix.
   *
   * @return the schema of the matrix
   */
  Schema getSchema();
  /** Clears the matrix, setting all values to the schema's unset value. */
  void clear();
  /**
   * Returns the value at the specified row and column in the matrix.
   *
   * @param row the row index
   * @param column the column index
   * @return the value at the specified position
   */
  byte get(int row, int column);
  /**
   * Returns a two-dimensional array containing all values in the matrix.
   *
   * @return a two-dimensional array containing all values in the matrix
   */
  byte[][] getAll();
  /**
   * Sets all values in the matrix to the values in the specified two-dimensional array.
   *
   * @param values the array of values to set
   * @throws IllegalArgumentException if the dimensions of the specified array do not match the
   *     dimensions of the matrix
   */
  void setAll(byte[][] values) throws IllegalArgumentException;
  /**
   * Returns the number of fields that have been set in the matrix.
   *
   * @return the number of set fields
   */
  int getSetCount();
  /**
   * Checks whether the matrix is valid according to the schema.
   *
   * @return true if the matrix is valid, false otherwise
   */
  boolean isValid();
  /**
   * Checks whether it is possible to set the specified value at the specified row and column in the
   * matrix.
   *
   * @param row the row index
   * @param column the column index
   * @param value the value to set
   * @return true if the value can be set at the specified position, false otherwise
   */
  boolean isSetPossible(int row, int column, byte value);
  /**
   * Sets a value in the matrix at the specified row and column.
   *
   * @param row the row index where the value will be set
   * @param column the column index where the value will be set
   * @param value the value to be set
   * @throws IllegalArgumentException if the value is not valid for the matrix's schema or if the
   *     value is not allowed at the specified position
   * @throws IllegalStateException if the matrix is no longer valid after the value is set
   */
  void set(int row, int column, byte value) throws IllegalArgumentException, IllegalStateException;
}

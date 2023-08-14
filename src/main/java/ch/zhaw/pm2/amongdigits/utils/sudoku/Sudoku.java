package ch.zhaw.pm2.amongdigits.utils.sudoku;

import ch.zhaw.pm2.amongdigits.utils.matrix.Matrix;

/**
 * The {@code Sudoku} interface represents the Sudoku game board, which extends the {@link
 * ch.zhaw.pm2.amongdigits.utils.matrix.Matrix Matrix} interface. It includes an additional method
 * to set a cell as writable or not.
 */
public interface Sudoku extends Matrix {

  /**
   * Sets the specified cell as writable or not.
   *
   * @param row the row index of the cell to be set
   * @param column the column index of the cell to be set
   * @param writable {@code true} to set the cell as writable, {@code false} to set it as read-only
   */
  void setWritable(int row, int column, boolean writable);
}

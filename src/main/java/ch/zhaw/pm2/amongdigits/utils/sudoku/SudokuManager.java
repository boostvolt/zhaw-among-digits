package ch.zhaw.pm2.amongdigits.utils.sudoku;

import ch.zhaw.pm2.amongdigits.utils.matrix.MatrixManager;
import ch.zhaw.pm2.amongdigits.utils.schema.Schema;

/**
 * A class for managing a Sudoku puzzle grid as a matrix, with the ability to set and get values for
 * cells and to mark cells as writable or read-only.
 */
public class SudokuManager extends MatrixManager implements Sudoku {

  private boolean[][] writeable;

  /**
   * Constructs a new SudokuManager object with the specified schema.
   *
   * @param schema the schema for the Sudoku puzzle.
   */
  public SudokuManager(final Schema schema) {
    super(schema);
    int width = schema.getWidth();
    writeable = new boolean[width][width];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < width; j++) {
        setWritable(i, j, true);
      }
    }
  }

  /**
   * Clones the specified SudokuManager object.
   *
   * @param sudokuManager the SudokuManager object to clone.
   * @return a clone of the specified SudokuManager object.
   */
  static SudokuManager clone(final SudokuManager sudokuManager) {
    SudokuManager clone = new SudokuManager(sudokuManager.getSchema());
    clone.setAll(sudokuManager.getAll());
    clone.writeable = sudokuManager.writeable.clone();

    return clone;
  }

  /**
   * Sets whether the specified cell is writable or read-only.
   *
   * @param row the row index of the cell.
   * @param column the column index of the cell.
   * @param set whether the cell should be set as writable (true) or read-only (false).
   */
  @Override
  public final void setWritable(final int row, final int column, final boolean set) {
    writeable[row][column] = set;
  }
}

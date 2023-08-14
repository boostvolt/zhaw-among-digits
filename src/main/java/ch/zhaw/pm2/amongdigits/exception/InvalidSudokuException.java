package ch.zhaw.pm2.amongdigits.exception;

/** An exception thrown when a Sudoku puzzle is invalid. */
public class InvalidSudokuException extends Exception {

  /**
   * Constructs an InvalidSudokuException with the specified detail message.
   *
   * @param message The detail message.
   */
  public InvalidSudokuException(String message) {
    super(message);
  }
}

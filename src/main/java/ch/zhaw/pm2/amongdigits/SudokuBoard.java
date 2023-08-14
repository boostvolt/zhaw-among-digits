package ch.zhaw.pm2.amongdigits;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;

/**
 * A record representing a Sudoku board.
 *
 * <p>The SudokuBoard record contains the unsolved grid, solved grid, and the difficulty level of
 * the Sudoku board.
 */
public record SudokuBoard(
    byte[][] unsolvedGrid, byte[][] solvedGrid, DifficultyLevel difficultyLevel) {

  /**
   * Constructs a SudokuBoard with the specified unsolved grid, solved grid, and difficulty level.
   *
   * @param unsolvedGrid The unsolved grid of the Sudoku board.
   * @param solvedGrid The solved grid of the Sudoku board.
   * @param difficultyLevel The difficulty level of the Sudoku board.
   */
  public SudokuBoard(
      final byte[][] unsolvedGrid,
      final byte[][] solvedGrid,
      final DifficultyLevel difficultyLevel) {
    this.unsolvedGrid = requireNonNull(unsolvedGrid);
    this.solvedGrid = requireNonNull(solvedGrid);
    this.difficultyLevel = requireNonNull(difficultyLevel);
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param o The object to compare.
   * @return {@code true} if this object is the same as the o argument; {@code false} otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SudokuBoard that = (SudokuBoard) o;
    return Arrays.deepEquals(unsolvedGrid, that.unsolvedGrid)
        && Arrays.deepEquals(solvedGrid, that.solvedGrid)
        && difficultyLevel == that.difficultyLevel;
  }

  /**
   * Returns a hash code value for the object. This method does not consider the DifficultyLevel to
   * provide a unique hash code for each SudokuBoard, which can be used when persisting the files.
   *
   * @return The hash code of this {@link SudokuBoard}.
   */
  @Override
  public int hashCode() {
    int result = Arrays.deepHashCode(unsolvedGrid);
    result = 31 * result + Arrays.deepHashCode(solvedGrid);
    return result;
  }

  /**
   * Returns a string representation of the SudokuBoard.
   *
   * @return A string representation of the SudokuBoard.
   */
  @Override
  public String toString() {
    return "SudokuBoard{"
        + "unsolvedGrid="
        + Arrays.toString(unsolvedGrid)
        + ", solvedGrid="
        + Arrays.toString(solvedGrid)
        + ", difficultyLevel="
        + difficultyLevel
        + '}';
  }
}

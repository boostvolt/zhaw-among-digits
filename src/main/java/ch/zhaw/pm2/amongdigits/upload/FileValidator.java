package ch.zhaw.pm2.amongdigits.upload;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

import ch.zhaw.pm2.amongdigits.utils.SudokuConstants;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * The FileValidator class is responsible for validating and processing a Sudoku file upload. It
 * provides methods to validate the file format, the grid size, the grid separator and empty cells,
 * as well as to create a scanner object to read the file contents. Additionally, it provides
 * methods to check if a string is null, empty or blank, to check if the separator is reached, to
 * match the grid size, to check if a cell is a non-zero digit or an empty cell, and to check if a
 * file is of the correct .txt format.
 */
public class FileValidator {

  private final Integer gridSize;
  private final Character gridSeparator;
  private final Character emptyGridCell;

  /**
   * Constructs a new {@code FileValidator} object with the specified parameters.
   *
   * @param gridSize the size of the Sudoku grid
   * @param gridSeparator the separator used between cells in the grid
   * @param emptyGridCell the character representing an empty cell in the grid
   */
  public FileValidator(
      final Integer gridSize, final Character gridSeparator, final Character emptyGridCell) {
    this.gridSize = requireNonNull(gridSize);
    this.gridSeparator = requireNonNull(gridSeparator);
    this.emptyGridCell = requireNonNull(emptyGridCell);
  }

  /**
   * Returns the size of the Sudoku grid.
   *
   * @return the size of the Sudoku grid
   */
  Integer getGridSize() {
    return gridSize;
  }

  /**
   * Returns the separator used between cells in the grid.
   *
   * @return the separator used between cells in the grid
   */
  Character getGridSeparator() {
    return gridSeparator;
  }

  /**
   * Returns the character representing an empty cell in the grid.
   *
   * @return the character representing an empty cell in the grid
   */
  Character getEmptyGridCell() {
    return emptyGridCell;
  }

  /**
   * Creates a {@link Scanner} object with the given {@link File} in the correct encoding.
   *
   * @param file the {@link File} to be read
   * @return a {@link Scanner} object ready to read the {@link File} contents
   * @throws IOException if an I/O error occurs opening the source
   */
  Scanner createScanner(final File file) throws IOException {
    return new Scanner(requireNonNull(file), UTF_8);
  }

  /**
   * Checks if the specified string is null, empty or blank.
   *
   * @param line The string to check.
   * @return true if the string is null, empty or blank, false otherwise.
   */
  boolean isLineBlank(final String line) {
    return line == null || line.isEmpty() || line.isBlank();
  }

  /**
   * Checks if the separator character has been reached in the specified string.
   *
   * @param line The string to check.
   * @return true if the separator character has been reached, false otherwise.
   */
  boolean isSeparatorReached(final String line) {
    return line != null && line.trim().length() == 1 && line.trim().charAt(0) == gridSeparator;
  }

  /**
   * Checks if the grid lines in the given list match the specified grid size.
   *
   * @param gridLines the list of grid lines to check
   * @return true if the grid lines match the specified grid size, false otherwise
   */
  boolean isMatchingGridSize(final List<String> gridLines) {
    if (gridLines == null) {
      return false;
    }
    boolean heightMatching = gridLines.size() == gridSize;
    boolean widthMatching = gridLines.stream().allMatch(gridLine -> gridLine.length() == gridSize);

    return heightMatching && widthMatching;
  }

  /**
   * Checks if the given grid cell is a non-zero digit.
   *
   * @param gridCell the grid cell to check
   * @return true if the grid cell is a non-zero digit, false otherwise
   */
  boolean isNonZeroDigit(final char gridCell) {
    return Character.isDigit(gridCell) && Character.getNumericValue(gridCell) != 0;
  }

  /**
   * Checks if the given grid cell is an empty cell in the grid.
   *
   * @param gridCell the grid cell to check
   * @return true if the grid cell is empty, false otherwise
   */
  boolean isEmptyGridCell(final char gridCell) {
    return emptyGridCell == gridCell;
  }

  /**
   * Checks if the given file name is a text file with the valid file ending.
   *
   * @param fileName the name of the file to check
   * @return true if the file is a text file with the valid file ending, false otherwise
   */
  boolean isTxtFile(final String fileName) {
    final int lastDotIndex = fileName.lastIndexOf(".");
    return lastDotIndex != -1
        && fileName.substring(lastDotIndex + 1).equals(SudokuConstants.VALID_FILE_ENDING);
  }
}

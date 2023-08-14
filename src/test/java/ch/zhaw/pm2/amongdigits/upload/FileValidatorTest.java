package ch.zhaw.pm2.amongdigits.upload;

import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.EMPTY_GRID_CELL;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.GRID_SEPARATOR;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.SUDOKU_GRID_SIZE;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/** The unit test class for FileValidator. */
class FileValidatorTest {

  private FileValidator fileValidator;

  private static Stream<Arguments> provideConstructorArgs() {
    return Stream.of(
        Arguments.of(null, null, null),
        Arguments.of(SUDOKU_GRID_SIZE, null, null),
        Arguments.of(SUDOKU_GRID_SIZE, GRID_SEPARATOR, null));
  }

  private static List<String> getGridLinesOfSize(int rowLength, int colLength) {
    final List<String> gridLines = new ArrayList<>();
    for (int i = 0; i < rowLength; i++) {
      gridLines.add("1".repeat(colLength));
    }
    return gridLines;
  }

  private static Stream<Arguments> provideInvalidGridLines() {
    return Stream.of(
        Arguments.of(getGridLinesOfSize(SUDOKU_GRID_SIZE + 1, SUDOKU_GRID_SIZE + 1)),
        Arguments.of(getGridLinesOfSize(SUDOKU_GRID_SIZE - 1, SUDOKU_GRID_SIZE - 1)),
        Arguments.of(getGridLinesOfSize(SUDOKU_GRID_SIZE, SUDOKU_GRID_SIZE + 1)),
        Arguments.of(getGridLinesOfSize(SUDOKU_GRID_SIZE + 1, SUDOKU_GRID_SIZE)),
        Arguments.of(getGridLinesOfSize(0, 0)));
  }

  @BeforeEach
  void setUp() {
    fileValidator = new FileValidator(SUDOKU_GRID_SIZE, GRID_SEPARATOR, EMPTY_GRID_CELL);
  }

  /**
   * The parameterized test for the null argument constructor of FileValidator.
   *
   * @param gridSize The size of the grid.
   * @param gridSeparator The separator character for the grid.
   * @param emptyGridCell The character representing an empty cell in the grid.
   */
  @ParameterizedTest
  @MethodSource("provideConstructorArgs")
  void testNullArgConstructor(
      final Integer gridSize, final Character gridSeparator, final Character emptyGridCell) {
    assertThrows(
        NullPointerException.class,
        () -> new FileValidator(gridSize, gridSeparator, emptyGridCell));
  }

  /**
   * Tests the creation of a Scanner object from a file.
   *
   * @throws IOException If an I/O error occurs.
   */
  @Test
  void testCreateScanner() throws IOException {
    final Scanner scanner =
        fileValidator.createScanner(
            new File(requireNonNull(getClass().getResource("/upload/validSudoku.txt")).getFile()));
    assertNotNull(scanner);
  }

  /**
   * Parameterized test to check if a line is blank or consists of whitespace characters.
   *
   * @param line The line to test.
   */
  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" "})
  void testIsLineBlankPositive(final String line) {
    assertTrue(fileValidator.isLineBlank(line));
  }

  /** Tests if a non-blank line is correctly identified. */
  @Test
  void testIsLineBlankNegative() {
    assertFalse(fileValidator.isLineBlank(" asdf "));
  }

  /** Tests if the separator character is correctly identified. */
  @Test
  void testIsSeparatorReachedPositive() {
    assertTrue(fileValidator.isSeparatorReached(String.valueOf(GRID_SEPARATOR)));
  }

  /**
   * Parameterized test to check if a line is not the separator.
   *
   * @param line The line to test.
   */
  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", " 1234 "})
  void testIsSeparatorReachedNegative(final String line) {
    assertFalse(fileValidator.isSeparatorReached(line));
  }

  /** Tests if a grid with matching size is correctly identified. */
  @Test
  void testIsMatchingGridSizePositive() {
    assertTrue(
        fileValidator.isMatchingGridSize(getGridLinesOfSize(SUDOKU_GRID_SIZE, SUDOKU_GRID_SIZE)));
  }

  /**
   * Parameterized test to check if a grid with an invalid size is detected.
   *
   * @param gridLines The grid lines to test.
   */
  @ParameterizedTest
  @NullAndEmptySource
  @MethodSource("provideInvalidGridLines")
  void testIsMatchingGridSizeNegative(final List<String> gridLines) {
    assertFalse(fileValidator.isMatchingGridSize(gridLines));
  }

  /**
   * Parameterized test to check if a non-zero digit is identified.
   *
   * @param gridCell The grid cell to test.
   */
  @ParameterizedTest
  @ValueSource(
      chars = {
        '1', '2', '3', '4', '5', '6', '7', '8', '9',
      })
  void testNonZeroDigitPositive(final char gridCell) {
    assertTrue(fileValidator.isNonZeroDigit(gridCell));
  }

  /**
   * Parameterized test to check if a non-digit or zero is identified.
   *
   * @param gridCell The grid cell to test.
   */
  @ParameterizedTest
  @ValueSource(chars = {'0', 'a'})
  void testNonZeroDigitNegative(final char gridCell) {
    assertFalse(fileValidator.isNonZeroDigit(gridCell));
  }

  /** Tests if an empty grid cell is correctly identified. */
  @Test
  void testEmptyGridCell() {
    assertTrue(fileValidator.isEmptyGridCell(EMPTY_GRID_CELL));
  }

  /** Tests if a file with a ".txt" extension is correctly identified. */
  @Test
  void testIsTxtFilePositive() {
    assertTrue(fileValidator.isTxtFile("asdf.txt"));
  }

  /** Tests if files with non-txt extensions are correctly identified. */
  @Test
  void testIsTxtFileNegative() {
    assertFalse(fileValidator.isTxtFile("txt.docx"));
    assertFalse(fileValidator.isTxtFile("asdf.pdf"));
  }
}

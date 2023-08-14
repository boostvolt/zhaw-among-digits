package ch.zhaw.pm2.amongdigits.upload;

import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.EMPTY_GRID_CELL;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.GRID_SEPARATOR;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.SUDOKU_GRID_SIZE;
import static ch.zhaw.pm2.amongdigits.utils.schema.SchemaTypes.SCHEMA_9X9;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import ch.zhaw.pm2.amongdigits.ChallengeType;
import ch.zhaw.pm2.amongdigits.SudokuBoard;
import ch.zhaw.pm2.amongdigits.exception.InvalidFileFormatException;
import ch.zhaw.pm2.amongdigits.exception.InvalidSudokuException;
import ch.zhaw.pm2.amongdigits.utils.Solver;
import ch.zhaw.pm2.amongdigits.utils.matrix.Matrix;
import ch.zhaw.pm2.amongdigits.utils.sudoku.SudokuManager;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

/** The unit test class for SudokuFileLoader. */
class SudokuFileLoaderTest {
  private static final String BASE_NAME = "MessagesBundle";
  private static final String UPLOADED_SUDOKU_NAME = "HARD_validUserSudoku_-2140424672.txt";
  private static final byte[][] EXPECTED_VALID_UNSOLVED_GRID = {
    {9, 0, 0, 0, 8, 0, 3, 0, 0},
    {0, 0, 0, 2, 5, 0, 7, 0, 0},
    {0, 2, 0, 3, 0, 0, 0, 0, 4},
    {0, 9, 4, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 7, 3, 0, 5, 6, 0},
    {7, 0, 5, 0, 6, 0, 4, 0, 0},
    {0, 0, 7, 8, 0, 3, 9, 0, 0},
    {0, 0, 1, 0, 0, 0, 0, 0, 3},
    {3, 0, 0, 0, 0, 0, 0, 0, 2}
  };
  private static final byte[][] EXPECTED_VALID_SOLVED_GRID = {
    {9, 7, 6, 4, 8, 1, 3, 2, 5},
    {1, 4, 3, 2, 5, 9, 7, 8, 6},
    {5, 2, 8, 3, 7, 6, 1, 9, 4},
    {6, 9, 4, 5, 1, 8, 2, 3, 7},
    {8, 1, 2, 7, 3, 4, 5, 6, 9},
    {7, 3, 5, 9, 6, 2, 4, 1, 8},
    {4, 6, 7, 8, 2, 3, 9, 5, 1},
    {2, 5, 1, 6, 9, 7, 8, 4, 3},
    {3, 8, 9, 1, 4, 5, 6, 7, 2}
  };

  private final FileValidator fileValidator = mock(FileValidator.class);
  private final SudokuManager sudokuManager = mock(SudokuManager.class);
  private SudokuFileLoader sudokuFileLoader;
  private ResourceBundle bundle;

  /**
   * The setUp function is used to set up the test environment before each test. It sets up a mocked
   * fileValidator, sudokuManager and bundle object. The mocked objects are then used in the tests
   * to verify that they are called correctly by the SudokuFileLoader class.
   */
  @BeforeEach
  void setUp() throws IOException {
    when(fileValidator.getGridSize()).thenReturn(SUDOKU_GRID_SIZE);
    when(fileValidator.getGridSeparator()).thenReturn(GRID_SEPARATOR);
    when(fileValidator.getEmptyGridCell()).thenReturn(EMPTY_GRID_CELL);
    when(fileValidator.createScanner(any(File.class)))
        .thenAnswer(
            invocation -> {
              final File file = invocation.getArgument(0);
              return new Scanner(file, UTF_8);
            });
    when(fileValidator.isSeparatorReached(String.valueOf(GRID_SEPARATOR))).thenReturn(true);
    when(fileValidator.isEmptyGridCell(EMPTY_GRID_CELL)).thenReturn(true);
    when(fileValidator.isLineBlank(anyString()))
        .thenAnswer(
            invocation -> {
              final String line = invocation.getArgument(0);
              return line == null || line.isEmpty() || line.isBlank();
            });
    when(fileValidator.isTxtFile(any(String.class))).thenReturn(true);

    this.bundle = ResourceBundle.getBundle(BASE_NAME);

    sudokuFileLoader = new SudokuFileLoader(fileValidator, sudokuManager, bundle);
  }

  /**
   * The testLoadValidSudoku function tests the loadSudokuFile function of the SudokuFileLoader
   * class. The testLoadValidSudoku function is a parameterized test, which means that it can be run
   * with multiple parameters. In this case, we are testing two different files: validSudoku and
   * validSudokuSpacesLineBreaks. The first file contains no spaces or line breaks in its contents;
   * the second file does contain spaces and line breaks in its contents.
   */
  @ParameterizedTest
  @ValueSource(strings = {"validSudoku", "validSudokuSpacesLineBreaks"})
  void testLoadValidSudoku(final String fileName)
      throws InvalidFileFormatException, InvalidSudokuException {
    when(fileValidator.isNonZeroDigit(not(eq(EMPTY_GRID_CELL)))).thenReturn(true);
    when(fileValidator.isMatchingGridSize(anyList())).thenReturn(true);
    final SudokuBoard board = sudokuFileLoader.loadSudokuFile(getInputFile(fileName), true);
    assertNotNull(board);
    assertArrayEquals(EXPECTED_VALID_UNSOLVED_GRID, board.unsolvedGrid());
    assertArrayEquals(EXPECTED_VALID_SOLVED_GRID, board.solvedGrid());
  }

  /**
   * The testLoadInvalidGridSize function tests the loadSudokuFile function in SudokuFileLoader.java
   * with invalid grid sizes. The test is parameterized, meaning that it will be run multiple times
   * with different parameters (in this case, different file names). The
   */
  @ParameterizedTest
  @ValueSource(
      strings = {
        "emptySudoku",
        "noSeparatorSudoku",
        "invalidGridColSudoku",
        "invalidGridRowSudoku"
      })
  void testLoadInvalidGridSize(final String fileName) {
    final InvalidFileFormatException exception =
        assertThrows(
            InvalidFileFormatException.class,
            () -> sudokuFileLoader.loadSudokuFile(getInputFile(fileName), true));
    assertEquals(
        "Sudoku Grid does not match the expected Grid Size of " + SUDOKU_GRID_SIZE,
        exception.getMessage());
  }

  /**
   * The testLoadIOException function tests the loadSudokuFile function in SudokuFileLoader.java
   * when an IOException is thrown. The test uses a mocked File object to simulate the file that
   * does not exist, and then checks if an InvalidFileFormatException is thrown with the correct
   * message. This test ensures that when a file cannot be found, it will throw an exception with
   * the correct message instead of crashing or returning null values. It also ensures that this
   * code works as intended for all possible files (not just one specific file). This makes sure
   * that no matter what kind of error occurs while loading a su
   */
  @Test
  void testLoadIOException() {
    final File mockedFile = mock(File.class);
    when(mockedFile.getName()).thenReturn("fileThatDoesNotExist.txt");
    when(mockedFile.getPath()).thenReturn("pathThatDoesNotExist");
    final InvalidFileFormatException exception =
        assertThrows(
            InvalidFileFormatException.class,
            () -> sudokuFileLoader.loadSudokuFile(mockedFile, true));
    assertEquals(
        "Not able to parse given Sudoku File fileThatDoesNotExist.txt", exception.getMessage());
  }

  /**
   * The testLoadInvalidCharacters function tests the loadSudokuFile function of the
   * SudokuFileLoader class. It checks if an InvalidFileFormatException is thrown when a file with
   * invalid characters is loaded. The test uses two different files, one containing special
   * characters and one containing letters and zeros.
   */
  @ParameterizedTest
  @ValueSource(strings = {"invalidSpecialCharactersSudoku", "invalidWithLetterAndZeroSudoku"})
  void testLoadInvalidCharacters(final String fileName) {
    when(fileValidator.isMatchingGridSize(anyList())).thenReturn(true);
    final InvalidFileFormatException exception =
        assertThrows(
            InvalidFileFormatException.class,
            () -> sudokuFileLoader.loadSudokuFile(getInputFile(fileName), true));
    assertEquals(
        format(
            "File Grid can only contain non-zero digits, a grid separator of type %s and empty grid cells of type %s",
            GRID_SEPARATOR, EMPTY_GRID_CELL),
        exception.getMessage());
  }

  /**
   * The testLoadNoSubsets function tests the loadSudokuFile function in SudokuFileLoader.java The
   * testLoadNoSubsets function is a parameterized test that takes in a String value as an input.
   * The testLoadNoSubsets function checks if the given file has no subsets, and throws an exception
   * if it does not have any subsets.
   */
  @Test
  void testLoadNoSubsets() {
    when(fileValidator.isNonZeroDigit(not(eq(EMPTY_GRID_CELL)))).thenReturn(true);
    when(fileValidator.isMatchingGridSize(anyList())).thenReturn(true);
    final InvalidSudokuException exception =
        assertThrows(
            InvalidSudokuException.class,
            () -> sudokuFileLoader.loadSudokuFile(getInputFile("noSubsetsSudoku"), true));
    assertEquals(
        "Given Sudoku Grids are not compatible as not all digits are matching",
        exception.getMessage());
  }

  /**
   * The testUploadSuccessful function tests the uploadSudoku function in SudokuFileLoader.java The
   * testUploadSuccessful function is a parameterized test, which means that it takes an input file
   * and checks if the output of the uploadSudoku function matches what we expect. In this case, we
   * are testing whether or not our program can successfully read a valid sudoku puzzle from an
   * input file and store it as a SudokuBoard object.
   */
  @Test
  void testUploadSuccessful()
      throws InvalidFileFormatException, InvalidSudokuException, IOException {
    when(fileValidator.isNonZeroDigit(not(eq(EMPTY_GRID_CELL)))).thenReturn(true);
    when(fileValidator.isMatchingGridSize(anyList())).thenReturn(true);

    try (final MockedStatic<Solver> solver = mockStatic(Solver.class)) {
      final SudokuManager sudokuManager = new SudokuManager(SCHEMA_9X9);
      sudokuManager.setAll(EXPECTED_VALID_SOLVED_GRID);
      List<Matrix> solution = new ArrayList<>();
      solution.add(sudokuManager);
      solver.when(() -> Solver.solve(any(Matrix.class))).thenReturn(solution);
      sudokuFileLoader.uploadSudoku(getInputFile("validUserSudoku"));
      final Optional<File> uploadedFile = getUploadedFile();
      assertTrue(uploadedFile.isPresent());
      final SudokuBoard board = sudokuFileLoader.loadSudokuFile(uploadedFile.get(), true);
      assertNotNull(board);
      assertArrayEquals(EXPECTED_VALID_UNSOLVED_GRID, board.unsolvedGrid());
      assertArrayEquals(EXPECTED_VALID_SOLVED_GRID, board.solvedGrid());
    } finally {
      deleteUploadedFileIfPresent();
    }
  }

  /**
   * The testUploadNoUniqueSolution function tests the uploadSudoku function in
   * SudokuFileLoader.java to ensure that it throws an InvalidSudokuException when a sudoku has more
   * than one solution.
   */
  @Test
  void testUploadNoUniqueSolution() {
    when(fileValidator.isNonZeroDigit(not(eq(EMPTY_GRID_CELL)))).thenReturn(true);
    when(fileValidator.isMatchingGridSize(anyList())).thenReturn(true);

    try (final MockedStatic<Solver> solver = mockStatic(Solver.class)) {
      final SudokuManager sudokuManager = new SudokuManager(SCHEMA_9X9);
      sudokuManager.setAll(EXPECTED_VALID_SOLVED_GRID);
      List<Matrix> solution = new ArrayList<>();
      solution.add(sudokuManager);
      solution.add(sudokuManager);
      solver.when(() -> Solver.solve(any(Matrix.class))).thenReturn(solution);
      final InvalidSudokuException exception =
          assertThrows(
              InvalidSudokuException.class,
              () -> sudokuFileLoader.uploadSudoku(getInputFile("validUserSudoku")));
      assertEquals(
          "Sudoku must have a unique solution to be considered valid.", exception.getMessage());
    }
  }

  /**
   * The testUploadNoTxtFile function tests the uploadSudoku function of the SudokuFileLoader class.
   * The testUploadNoTxtFile function is a parameterized test, which means that it can be run with
   * different parameters. In this case, we are testing if an InvalidFileFormatException is thrown
   * when a file that does not have the .txt extension is uploaded to uploadSudoku().
   */
  @Test
  void testUploadNoTxtFile() {
    when(fileValidator.isTxtFile(any(String.class))).thenReturn(false);
    final InvalidFileFormatException exception =
        assertThrows(
            InvalidFileFormatException.class,
            () -> sudokuFileLoader.uploadSudoku(getInputFile("validSudoku")));
    assertEquals("Given file must be a .txt file", exception.getMessage());
  }

  /**
   * The testUploadDuplicateFile function tests the uploadSudoku function in SudokuFileLoader.java
   * to see if it throws an InvalidSudokuException when a user tries to upload a sudoku that already
   * exists.
   */
  @Test
  void testUploadDuplicateFile()
      throws IOException, InvalidFileFormatException, InvalidSudokuException {
    when(fileValidator.isNonZeroDigit(not(eq(EMPTY_GRID_CELL)))).thenReturn(true);
    when(fileValidator.isMatchingGridSize(anyList())).thenReturn(true);

    try (final MockedStatic<Solver> solver = mockStatic(Solver.class)) {
      final SudokuManager sudokuManager = new SudokuManager(SCHEMA_9X9);
      sudokuManager.setAll(EXPECTED_VALID_SOLVED_GRID);
      List<Matrix> solution = new ArrayList<>();
      solution.add(sudokuManager);
      solver.when(() -> Solver.solve(any(Matrix.class))).thenReturn(solution);
      sudokuFileLoader.uploadSudoku(getInputFile("validUserSudoku"));
      final InvalidSudokuException exception =
          assertThrows(
              InvalidSudokuException.class,
              () -> sudokuFileLoader.uploadSudoku(getInputFile("validUserSudoku")));
      assertEquals(
          format(bundle.getString("sudoku_exists_exception"), UPLOADED_SUDOKU_NAME),
          exception.getMessage());
    } finally {
      deleteUploadedFileIfPresent();
    }
  }

  private File getInputFile(final String fileName) {
    return new File(
        requireNonNull(getClass().getResource(format("/upload/%s.txt", fileName))).getFile());
  }

  private Optional<File> getUploadedFile() {
    final URL uploadedFile =
        getClass()
            .getClassLoader()
            .getResource(ChallengeType.USER_GENERATED.getDirectory() + "/" + UPLOADED_SUDOKU_NAME);
    if (uploadedFile == null) {
      return Optional.empty();
    } else {
      return Optional.of(new File(uploadedFile.getFile()));
    }
  }

  private void deleteUploadedFileIfPresent() throws IOException {
    Optional<File> uploadedFile = getUploadedFile();
    if (uploadedFile.isPresent()) {
      Files.delete(uploadedFile.get().toPath());
    }
  }
}

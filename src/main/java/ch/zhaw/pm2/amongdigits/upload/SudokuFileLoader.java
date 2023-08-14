package ch.zhaw.pm2.amongdigits.upload;

import static ch.zhaw.pm2.amongdigits.DifficultyLevel.determineDifficultyLevel;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.FILE_AREA_NAME_SEPARATOR;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.VALID_FILE_ENDING;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

import ch.zhaw.pm2.amongdigits.ChallengeType;
import ch.zhaw.pm2.amongdigits.DifficultyLevel;
import ch.zhaw.pm2.amongdigits.SudokuBoard;
import ch.zhaw.pm2.amongdigits.exception.InvalidFileFormatException;
import ch.zhaw.pm2.amongdigits.exception.InvalidSudokuException;
import ch.zhaw.pm2.amongdigits.utils.Solver;
import ch.zhaw.pm2.amongdigits.utils.matrix.Matrix;
import ch.zhaw.pm2.amongdigits.utils.sudoku.SudokuManager;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXML;

/**
 * This class provides methods to load and upload Sudoku files. It uses a FileValidator to validate
 * the format and size of the files and a SudokuManager to manage the Sudoku puzzle.
 */
public class SudokuFileLoader {

  private final FileValidator fileValidator;
  private final SudokuManager sudokuManager;
  @FXML private final ResourceBundle resourceBundle;

  /**
   * Constructor for SudokuFileLoader class. It takes a FileValidator to validate files, a
   * SudokuManager to manage the puzzle, and a ResourceBundle to get localized strings.
   *
   * @param fileValidator The FileValidator to validate the files.
   * @param sudokuManager The SudokuManager to manage the puzzle.
   * @param resourceBundle The ResourceBundle to get localized strings.
   */
  public SudokuFileLoader(
      final FileValidator fileValidator,
      final SudokuManager sudokuManager,
      ResourceBundle resourceBundle) {
    this.fileValidator = requireNonNull(fileValidator);
    this.sudokuManager = sudokuManager;
    this.resourceBundle = resourceBundle;
  }

  /**
   * This method loads a Sudoku file and sets the unsolved SudokuBoard to the SudokuManager. Then it
   * solves the SudokuBoard and sets the solution to the SudokuManager. If the Sudoku has more than
   * one solution, it throws an InvalidSudokuException. Finally, it creates a new SudokuBoard with
   * the unsolved grid, solved grid and difficulty level, and persists the SudokuBoard to a file
   * with the same name as the original file.
   *
   * @param sudokuFile The file to load the Sudoku puzzle from.
   * @throws InvalidFileFormatException If the file is not in the correct format or size.
   * @throws InvalidSudokuException If the Sudoku puzzle has no unique solution.
   */
  public void uploadSudoku(final File sudokuFile)
      throws InvalidFileFormatException, InvalidSudokuException {
    final SudokuBoard unsolvedSudokuBoard = loadSudokuFile(sudokuFile, false);
    sudokuManager.setAll(unsolvedSudokuBoard.unsolvedGrid());
    final List<Matrix> solutions = Solver.solve(sudokuManager);
    if (solutions.size() != 1) {
      throw new InvalidSudokuException(resourceBundle.getString("no_unique_solution_exception"));
    }

    final SudokuBoard fullSudokuBoard =
        new SudokuBoard(
            unsolvedSudokuBoard.unsolvedGrid(),
            solutions.get(0).getAll(),
            DifficultyLevel.determineDifficultyLevel(unsolvedSudokuBoard.unsolvedGrid()));
    persistSudokuFile(fullSudokuBoard, resolveFileName(sudokuFile.getName()));
  }

  /**
   * This method loads a Sudoku file and returns an unsolved SudokuBoard with the unsolved grid. If
   * containsSolution is true, it also reads the solved grid from the file. It validates the file
   * format and size using the FileValidator. It throws an InvalidFileFormatException if the file is
   * not in the correct format or size. It throws an InvalidSudokuException if containsSolution is
   * true and the unsolved grid is not a subset of the solved grid.
   *
   * @param sudokuFile The file to load the Sudoku puzzle from.
   * @param containsSolution A boolean indicating whether the file contains the solution grid.
   * @return A SudokuBoard with the unsolved grid.
   * @throws InvalidFileFormatException If the file is not in the correct format or size.
   * @throws InvalidSudokuException If the unsolved grid is not a subset of the solved grid.
   */
  public SudokuBoard loadSudokuFile(final File sudokuFile, boolean containsSolution)
      throws InvalidFileFormatException, InvalidSudokuException {
    if (!fileValidator.isTxtFile(sudokuFile.getName())) {
      throw new InvalidFileFormatException(resourceBundle.getString("no_txt_file_exception"));
    }
    final byte[][] unsolvedGrid =
        new byte[fileValidator.getGridSize()][fileValidator.getGridSize()];
    final byte[][] solvedGrid = new byte[fileValidator.getGridSize()][fileValidator.getGridSize()];
    final List<String> unsolvedGridLines = new ArrayList<>(fileValidator.getGridSize());
    final List<String> solvedGridLines = new ArrayList<>(fileValidator.getGridSize());

    try (final Scanner scanner = fileValidator.createScanner(sudokuFile)) {
      readFile(unsolvedGridLines, solvedGridLines, scanner);
    } catch (final IOException e) {
      throw new InvalidFileFormatException(
          format(resourceBundle.getString("not_parseable_exception"), sudokuFile.getName()), e);
    }

    if (!fileValidator.isMatchingGridSize(unsolvedGridLines)
        || (containsSolution && !fileValidator.isMatchingGridSize(solvedGridLines))) {
      throw new InvalidFileFormatException(
          format(
              resourceBundle.getString("wrong_grid_size_exception"), fileValidator.getGridSize()));
    }

    fillGrid(unsolvedGridLines, unsolvedGrid);
    if (containsSolution) {
      fillGrid(solvedGridLines, solvedGrid);
    }

    if (containsSolution && !areSubsets(unsolvedGrid, solvedGrid)) {
      throw new InvalidSudokuException(resourceBundle.getString("sudoku_not_compatible_exception"));
    }
    return new SudokuBoard(
        unsolvedGrid, solvedGrid, DifficultyLevel.determineDifficultyLevel(unsolvedGrid));
  }

  private void readFile(
      final List<String> unsolvedGridLines,
      final List<String> solvedGridLines,
      final Scanner scanner) {
    boolean isParsingUnsolvedGrid = true;
    while (scanner.hasNextLine()) {
      final String currentLine = scanner.nextLine();
      if (!fileValidator.isLineBlank(currentLine)) {
        final String trimmedLine = currentLine.trim();
        if (isParsingUnsolvedGrid && fileValidator.isSeparatorReached(trimmedLine)) {
          isParsingUnsolvedGrid = false;
        } else if (isParsingUnsolvedGrid) {
          unsolvedGridLines.add(trimmedLine);
        } else {
          solvedGridLines.add(trimmedLine);
        }
      }
    }
  }

  private void fillGrid(final List<String> gridLines, final byte[][] grid)
      throws InvalidFileFormatException {
    for (int row = 0; row < fileValidator.getGridSize(); row++) {
      final String currentRow = gridLines.get(row);
      for (int col = 0; col < fileValidator.getGridSize(); col++) {
        fillGridCell(grid, row, col, currentRow);
      }
    }
  }

  private void fillGridCell(
      final byte[][] grid, final int row, final int col, final String currentRow)
      throws InvalidFileFormatException {
    final char currentGridCell = currentRow.charAt(col);
    if (fileValidator.isNonZeroDigit(currentGridCell)) {
      grid[row][col] = (byte) Character.getNumericValue(currentGridCell);
    } else if (!fileValidator.isEmptyGridCell(currentGridCell)) {
      throw new InvalidFileFormatException(
          format(
              resourceBundle.getString("invalid_grid_cell_exception"),
              fileValidator.getGridSeparator(),
              fileValidator.getEmptyGridCell()));
    }
  }

  private boolean areSubsets(final byte[][] unsolvedGrid, final byte[][] solvedGrid) {
    for (int row = 0; row < fileValidator.getGridSize(); row++) {
      for (int col = 0; col < fileValidator.getGridSize(); col++) {
        int unsolvedGridDigit = unsolvedGrid[row][col];
        int solvedGridDigit = solvedGrid[row][col];
        if (unsolvedGridDigit != 0 && unsolvedGridDigit != solvedGridDigit) {
          return false;
        }
      }
    }
    return true;
  }

  private String resolveFileName(final String fileName) {
    return fileName.substring(0, fileName.lastIndexOf("."));
  }

  private void persistSudokuFile(final SudokuBoard sudokuBoard, final String fileName)
      throws InvalidSudokuException, InvalidFileFormatException {
    final URL uploadDirectory =
        requireNonNull(
            getClass().getClassLoader().getResource(ChallengeType.USER_GENERATED.getDirectory()));
    final String uploadFileName =
        determineDifficultyLevel(sudokuBoard.unsolvedGrid()).name()
            + FILE_AREA_NAME_SEPARATOR
            + fileName
            + FILE_AREA_NAME_SEPARATOR
            + sudokuBoard.hashCode()
            + "."
            + VALID_FILE_ENDING;
    final File uploadFile = new File(uploadDirectory.getFile(), uploadFileName);

    createFile(uploadFile);
    writeFile(sudokuBoard, uploadFile);
  }

  private void createFile(File uploadFile)
      throws InvalidSudokuException, InvalidFileFormatException {
    try {
      if (!uploadFile.createNewFile()) {
        throw new InvalidSudokuException(
            format(resourceBundle.getString("sudoku_exists_exception"), uploadFile.getName()));
      }
    } catch (final IOException e) {
      throw new InvalidFileFormatException(
          format(resourceBundle.getString("sudoku_upload_io_exception"), e.getMessage()));
    }
  }

  private void writeFile(SudokuBoard sudokuBoard, File uploadFile)
      throws InvalidFileFormatException {
    try (final FileWriter fileWriter = new FileWriter(uploadFile, UTF_8, true);
        final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
      writeRow(sudokuBoard.unsolvedGrid(), bufferedWriter);
      bufferedWriter.write(fileValidator.getGridSeparator());
      bufferedWriter.newLine();
      writeRow(sudokuBoard.solvedGrid(), bufferedWriter);
    } catch (IOException e) {
      throw new InvalidFileFormatException(
          format(resourceBundle.getString("sudoku_upload_io_exception"), e.getMessage()));
    }
  }

  private void writeRow(byte[][] grid, BufferedWriter bufferedWriter) throws IOException {
    for (final byte[] currentRow : grid) {
      final StringBuilder rowBuilder = new StringBuilder();
      for (byte cell : currentRow) {
        rowBuilder.append(cell);
      }
      bufferedWriter.write(rowBuilder.toString().replace('0', fileValidator.getEmptyGridCell()));
      bufferedWriter.newLine();
    }
  }
}

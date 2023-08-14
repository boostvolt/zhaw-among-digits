package ch.zhaw.pm2.amongdigits.model;

import static ch.zhaw.pm2.amongdigits.PropertyType.SETTINGS;
import static ch.zhaw.pm2.amongdigits.PropertyType.STATISTICS;
import static ch.zhaw.pm2.amongdigits.utils.PropertiesHandler.getPropertyString;
import static ch.zhaw.pm2.amongdigits.utils.PropertiesHandler.updatePropertyString;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.EMPTY_GRID_CELL;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.GRID_SEPARATOR;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.SUDOKU_GRID_SIZE;
import static java.lang.Integer.parseInt;

import ch.zhaw.pm2.amongdigits.DifficultyLevel;
import ch.zhaw.pm2.amongdigits.SudokuBoard;
import ch.zhaw.pm2.amongdigits.exception.InvalidFileFormatException;
import ch.zhaw.pm2.amongdigits.exception.InvalidSudokuException;
import ch.zhaw.pm2.amongdigits.upload.FileValidator;
import ch.zhaw.pm2.amongdigits.upload.SudokuFileLoader;
import ch.zhaw.pm2.amongdigits.utils.Creator;
import ch.zhaw.pm2.amongdigits.utils.Solver;
import ch.zhaw.pm2.amongdigits.utils.matrix.Matrix;
import ch.zhaw.pm2.amongdigits.utils.schema.SchemaTypes;
import ch.zhaw.pm2.amongdigits.utils.sudoku.Sudoku;
import ch.zhaw.pm2.amongdigits.utils.sudoku.SudokuManager;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import lombok.extern.slf4j.Slf4j;

/**
 * The SudokuGameModel class represents the model of the Sudoku game. It provides the necessary
 * functionality for loading a Sudoku board from a file, setting up the game board, solving the
 * Sudoku, and managing the game's state.
 *
 * @author ch.zhaw.pm2.amongdigits.model
 */
@Slf4j
public class SudokuGameModel {

  private final SudokuFileLoader sudokuFileLoader;

  private final IntegerProperty mistakes;
  private final IntegerProperty maxMistakes;
  private final LongProperty elapsedTime;
  private final LongProperty timeLimit;
  private final BooleanProperty isSolved;
  private final BooleanProperty isLimitExceeded;

  private SudokuBoard sudokuBoard;
  private byte[][] currentGrid;
  private AnimationTimer timer;

  /**
   * Constructs a new SudokuGameModel object with the given ResourceBundle.
   *
   * @param resources the ResourceBundle used for loading the Sudoku game file and for localization
   */
  public SudokuGameModel(ResourceBundle resources) {
    mistakes = new SimpleIntegerProperty();
    maxMistakes = new SimpleIntegerProperty();
    elapsedTime = new SimpleLongProperty(System.nanoTime());
    timeLimit = new SimpleLongProperty(System.nanoTime());
    isSolved = new SimpleBooleanProperty();
    isLimitExceeded = new SimpleBooleanProperty();

    sudokuFileLoader =
        new SudokuFileLoader(
            new FileValidator(SUDOKU_GRID_SIZE, GRID_SEPARATOR, EMPTY_GRID_CELL),
            new SudokuManager(SchemaTypes.SCHEMA_9X9),
            resources);
  }

  /**
   * Returns the current Sudoku board.
   *
   * @return the current Sudoku board
   */
  public SudokuBoard getSudokuBoard() {
    return sudokuBoard;
  }

  /**
   * Returns the integer property of the number of mistakes made.
   *
   * @return the integer property of the number of mistakes made
   */
  public IntegerProperty getMistakesProperty() {
    return mistakes;
  }

  /**
   * Returns the integer property of the maximum number of mistakes allowed.
   *
   * @return the integer property of the maximum number of mistakes allowed
   */
  public IntegerProperty getMaxMistakesProperty() {
    return maxMistakes;
  }

  /**
   * Returns the long property of the elapsed time since the game started.
   *
   * @return the long property of the elapsed time since the game started
   */
  public LongProperty getElapsedTimeProperty() {
    return elapsedTime;
  }

  /**
   * Returns the long property of the time limit set for the game.
   *
   * @return the long property of the time limit set for the game
   */
  public LongProperty getTimeLimitProperty() {
    return timeLimit;
  }

  /**
   * Returns the boolean property indicating if the Sudoku game has been solved.
   *
   * @return the boolean property indicating if the Sudoku game has been solved
   */
  public BooleanProperty isSolvedProperty() {
    return isSolved;
  }

  /**
   * Returns the boolean property indicating if the time limit for the game has been exceeded.
   *
   * @return the boolean property indicating if the time limit for the game has been exceeded
   */
  public BooleanProperty isLimitExceededProperty() {
    return isLimitExceeded;
  }

  /** Starts a new game by resetting the game, initializing a new timer and starting it. */
  public void startGame() {
    resetGame();
    Long startTime = System.nanoTime();
    timer =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            long elapsedSeconds = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime);
            elapsedTime.set(elapsedSeconds);
            if (getPropertyString(SETTINGS, "checkTime").equals("true")
                && (elapsedSeconds >= sudokuBoard.difficultyLevel().getMaxSecondsToSolve())) {
              stop();
              Platform.runLater(() -> isLimitExceeded.set(true));
            }
          }
        };
    timer.start();
    increasePropertyByOne(sudokuBoard.difficultyLevel().getTranslationProperty() + "GameStarted");
  }

  /**
   * Checks if the input is valid by comparing it with the solution grid. Updates the mistake count
   * and game status accordingly.
   *
   * @param inputValue the input value to be checked
   * @param row the row index of the input field
   * @param column the column index of the input field
   * @return true if the input is valid, false otherwise
   */
  public boolean checkInput(byte inputValue, int row, int column) {
    currentGrid[row][column] = inputValue;

    if (sudokuBoard.solvedGrid()[row][column] == inputValue) {
      if (Arrays.deepEquals(currentGrid, sudokuBoard.solvedGrid())) {
        timer.stop();
        isSolved.set(true);
        updateStatistics();
      }
      return true;
    } else {
      mistakes.set(mistakes.get() + 1);
      if (getPropertyString(SETTINGS, "checkMistakes").equals("true")
          && (mistakes.get() >= maxMistakes.get())) {
        timer.stop();
        isLimitExceeded.set(true);
      }
      return false;
    }
  }

  /**
   * Checks if the input field is a default (given) number or not.
   *
   * @param fieldRow the row index of the input field
   * @param fieldColumn the column index of the input field
   * @return true if the input field contains a default number, false otherwise
   */
  public boolean isDefaultNumber(int fieldRow, int fieldColumn) {
    return sudokuBoard.unsolvedGrid()[fieldRow][fieldColumn] != 0;
  }

  /**
   * Creates a new Sudoku puzzle with the given difficulty level.
   *
   * @param difficultyLevel the desired difficulty level of the Sudoku puzzle
   */
  public void createSudoku(DifficultyLevel difficultyLevel) {
    Sudoku sudoku = Creator.createSudoku(difficultyLevel);

    List<Matrix> solutions = Solver.solve(sudoku);

    if (solutions.size() == 1) {
      sudokuBoard = new SudokuBoard(sudoku.getAll(), solutions.get(0).getAll(), difficultyLevel);
    } else {
      log.error("Sudoku generation failed. Solution size: " + solutions.size());
    }

    setDifficultyLevelLimits();
  }

  /**
   * Loads a sudoku game from a file and initializes the sudoku board with it. This method throws an
   * InvalidFileFormatException or InvalidSudokuException if the file format is incorrect or the
   * sudoku in the file is invalid.
   *
   * @param sudokuFile a File object representing the file from which to load the sudoku game
   * @throws InvalidFileFormatException if the file format is incorrect
   * @throws InvalidSudokuException if the sudoku in the file is invalid
   */
  public void createSudoku(File sudokuFile)
      throws InvalidFileFormatException, InvalidSudokuException {
    sudokuBoard = sudokuFileLoader.loadSudokuFile(sudokuFile, true);
    setGivenNumbers();
    setDifficultyLevelLimits();
  }

  /** Stops the timer.. */
  public void stopGame() {
    timer.stop();
  }

  private void setGivenNumbers() {
    currentGrid = new byte[sudokuBoard.unsolvedGrid().length][sudokuBoard.unsolvedGrid().length];

    // Copy the elements from the original array to the new array
    IntStream.range(0, sudokuBoard.unsolvedGrid().length)
        .forEach(i -> currentGrid[i] = sudokuBoard.unsolvedGrid()[i].clone());
  }

  private void resetGame() {
    mistakes.set(0);
    elapsedTime.set(0);
    isSolved.set(false);
    isLimitExceeded.set(false);
    setGivenNumbers();
    setDifficultyLevelLimits();
  }

  private void setDifficultyLevelLimits() {
    maxMistakes.set(sudokuBoard.difficultyLevel().getMaxErrorsToSolve());
    timeLimit.set(TimeUnit.SECONDS.toSeconds(sudokuBoard.difficultyLevel().getMaxSecondsToSolve()));
  }

  private void updateStatistics() {
    increasePropertyByOne(sudokuBoard.difficultyLevel().getTranslationProperty() + "GameWon");
    updateBestTime(
        sudokuBoard.difficultyLevel().getTranslationProperty() + "GameBestTime",
        (int) elapsedTime.get());
    increasePropertyByCount(
        sudokuBoard.difficultyLevel().getTranslationProperty() + "GameTimePlayed",
        (int) elapsedTime.get());
    increasePropertyByCount(
        sudokuBoard.difficultyLevel().getTranslationProperty() + "GameMistakes", mistakes.get());
  }

  private void increasePropertyByOne(String propertyName) {
    int gamesStarted = parseInt(getPropertyString(STATISTICS, propertyName));
    gamesStarted += 1;
    updatePropertyString(STATISTICS, propertyName, String.valueOf(gamesStarted));
  }

  private void increasePropertyByCount(String propertyName, int count) {
    int gamesStarted = parseInt(getPropertyString(STATISTICS, propertyName));
    gamesStarted += count;
    updatePropertyString(STATISTICS, propertyName, String.valueOf(gamesStarted));
  }

  private void updateBestTime(String propertyName, int newBestTime) {
    int oldBestTime = parseInt(getPropertyString(STATISTICS, propertyName));
    if (oldBestTime == 0 || newBestTime < oldBestTime) {
      updatePropertyString(STATISTICS, propertyName, String.valueOf(newBestTime));
    }
  }
}

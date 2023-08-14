package ch.zhaw.pm2.amongdigits.model;

import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.SUDOKU_GRID_SIZE;
import static java.util.Arrays.deepEquals;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;

import ch.zhaw.pm2.amongdigits.DifficultyLevel;
import ch.zhaw.pm2.amongdigits.exception.InvalidFileFormatException;
import ch.zhaw.pm2.amongdigits.exception.InvalidSudokuException;
import java.io.File;
import java.util.ResourceBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Test class for the SudokuGameModel. */
class SudokuGameModelTest {

  private static final String BASE_NAME = "MessagesBundle";

  private static final byte[][] EXPECTED_VALID_UNSOLVED_SUDOKU = {
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

  private SudokuGameModel sudokuGameModel;

  @BeforeEach
  void setUp() {
    ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME);
    sudokuGameModel = new SudokuGameModel(bundle);
  }

  /** Test if the sudoku board is null after initialization. */
  @Test
  void getSudokuBoard() {
    assertNull(sudokuGameModel.getSudokuBoard());
  }

  /** Test if the mistkes property get initialized properly. */
  @Test
  void getMistakesProperty() {
    assertEquals(0, sudokuGameModel.getMistakesProperty().get());
  }

  /** Test if the solved property get initialized properly. */
  @Test
  void isSolvedProperty() {
    assertFalse(sudokuGameModel.isSolvedProperty().get());
  }

  /**
   * Tests if the createSudoku Method using Random generation adds a new SudokuBoard to the model.
   */
  @Test
  void createRandomSudoku() {
    sudokuGameModel.createSudoku(DifficultyLevel.EASY);
    assertNotNull(sudokuGameModel.getSudokuBoard());
    assertFalse(
        deepEquals(
            new byte[SUDOKU_GRID_SIZE][SUDOKU_GRID_SIZE],
            sudokuGameModel.getSudokuBoard().unsolvedGrid()));
  }

  /** Tests if the createSudoku Method using File generation adds a new SudokuBoard to the model. */
  @Test
  void createFileSudoku() throws InvalidFileFormatException, InvalidSudokuException {
    sudokuGameModel.createSudoku(
        new File(
            requireNonNull(
                requireNonNull(getClass().getResource("/upload/validSudoku.txt")).getFile())));
    assertNotNull(sudokuGameModel.getSudokuBoard());
    assertTrue(
        deepEquals(
            EXPECTED_VALID_UNSOLVED_SUDOKU, sudokuGameModel.getSudokuBoard().unsolvedGrid()));
  }
}

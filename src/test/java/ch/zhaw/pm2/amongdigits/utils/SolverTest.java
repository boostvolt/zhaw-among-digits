package ch.zhaw.pm2.amongdigits.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.zhaw.pm2.amongdigits.utils.matrix.Matrix;
import ch.zhaw.pm2.amongdigits.utils.matrix.MatrixManager;
import ch.zhaw.pm2.amongdigits.utils.schema.Schema;
import ch.zhaw.pm2.amongdigits.utils.schema.SchemaTypes;
import ch.zhaw.pm2.amongdigits.utils.sudoku.SudokuManager;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

/** This class contains test cases for the Solver class. */
class SolverTest {

  private final Schema schema = SchemaTypes.SCHEMA_9X9;

  /** Tests the Solver.solve method with a full matrix and no steps. */
  @Test
  void testSolveWithNoStep() {
    Matrix matrix = Creator.createFull();

    SudokuManager sudokuManager = new SudokuManager(schema);
    sudokuManager.setAll(matrix.getAll());

    List<Matrix> solutions = Solver.solve(sudokuManager);
    assertEquals(1, solutions.size());
    assertEquals(matrix, solutions.get(0));
  }

  /** Tests the Solver.solve method with a matrix missing one value and one step required. */
  @Test
  void testSolveWithOneStep() {
    Matrix matrix = Creator.createFull();
    Random random = new Random();

    int row = random.nextInt(9);
    int column = random.nextInt(9);

    SudokuManager sudokuManager = new SudokuManager(schema);
    sudokuManager.setAll(matrix.getAll());
    sudokuManager.set(row, column, schema.getUnsetValue());
    sudokuManager.setWritable(row, column, true);

    List<Matrix> solutions = Solver.solve(sudokuManager);
    assertEquals(1, solutions.size());
    assertEquals(matrix, solutions.get(0));
  }

  /** Tests the Solver.solve method with an example problem. */
  @Test
  void testSolveWithExampleProblem() {
    byte[][] sudokuBytes = {
      {0, 0, 0, 0, 0, 0, 3, 0, 0},
      {9, 0, 3, 0, 0, 0, 0, 2, 0},
      {0, 0, 0, 0, 0, 8, 0, 1, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0},
      {6, 0, 0, 0, 2, 0, 0, 5, 1},
      {0, 0, 8, 0, 5, 0, 7, 9, 0},
      {7, 3, 0, 5, 0, 0, 9, 6, 0},
      {5, 0, 0, 0, 0, 2, 0, 0, 0},
      {2, 9, 0, 7, 0, 1, 5, 8, 0}
    };
    SudokuManager sudokuManager = new SudokuManager(schema);
    sudokuManager.setAll(sudokuBytes);

    byte[][] solutionBytes = {
      {8, 6, 2, 4, 1, 9, 3, 7, 5},
      {9, 1, 3, 6, 7, 5, 4, 2, 8},
      {4, 7, 5, 2, 3, 8, 6, 1, 9},
      {1, 5, 9, 8, 4, 7, 2, 3, 6},
      {6, 4, 7, 9, 2, 3, 8, 5, 1},
      {3, 2, 8, 1, 5, 6, 7, 9, 4},
      {7, 3, 1, 5, 8, 4, 9, 6, 2},
      {5, 8, 6, 3, 9, 2, 1, 4, 7},
      {2, 9, 4, 7, 6, 1, 5, 8, 3}
    };
    MatrixManager solution = new MatrixManager(schema);
    solution.setAll(solutionBytes);

    List<Matrix> solutions = Solver.solve(sudokuManager);
    assertEquals(1, solutions.size());
    assertEquals(solution, solutions.get(0));
  }
}

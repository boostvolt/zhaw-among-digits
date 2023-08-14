package ch.zhaw.pm2.amongdigits.utils;

import static ch.zhaw.pm2.amongdigits.utils.matrix.MatrixManager.FreeCellResult.FOUND;

import ch.zhaw.pm2.amongdigits.utils.matrix.CachedMatrixManager;
import ch.zhaw.pm2.amongdigits.utils.matrix.Matrix;
import ch.zhaw.pm2.amongdigits.utils.matrix.MatrixManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Utility class for solving matrix problems. */
public final class Solver {

  /** The maximum number of solutions to find. */
  public static final int MAX_SOLUTIONS = 1;

  private final CachedMatrixManager cachedMatrixManager;
  private final List<Matrix> possibleSolutions;

  private Solver(final Matrix matrix) {
    Objects.requireNonNull(matrix, "Matrix must not be null");
    cachedMatrixManager = new CachedMatrixManager(matrix.getSchema());
    cachedMatrixManager.setAll(matrix.getAll());
    possibleSolutions = new ArrayList<>();
  }

  /**
   * Solves the given matrix and returns a list of possible solutions.
   *
   * @param matrix The matrix to solve.
   * @return A list of possible solutions.
   */
  public static List<Matrix> solve(final Matrix matrix) {
    return solve(matrix, MAX_SOLUTIONS);
  }

  /**
   * Solves the given matrix and returns a list of possible solutions up to the specified maximum
   * number.
   *
   * @param matrix The matrix to solve.
   * @param maxSolutions The maximum number of solutions to find.
   * @return A list of possible solutions.
   */
  public static List<Matrix> solve(final Matrix matrix, final int maxSolutions) {
    Solver solver = new Solver(matrix);
    solver.possibleSolutions.clear();
    int freeCells =
        solver.cachedMatrixManager.getSchema().getTotalFields()
            - solver.cachedMatrixManager.getSetCount();

    backtrack(freeCells, new int[2], maxSolutions, solver);

    return Collections.unmodifiableList(solver.possibleSolutions);
  }

  private static int backtrack(
      final int freeCells, final int[] minimumCell, final int maxSolutions, final Solver solver) {
    assert freeCells >= 0;
    if (solver.possibleSolutions.size() >= maxSolutions) {
      return 0;
    }

    if (freeCells == 0) {
      Matrix matrix = new MatrixManager(solver.cachedMatrixManager.getSchema());
      matrix.setAll(solver.cachedMatrixManager.getAll());
      solver.possibleSolutions.add(matrix);

      return 1;
    }

    MatrixManager.FreeCellResult freeCellResult =
        solver.cachedMatrixManager.findLeastFreeCell(minimumCell);
    if (freeCellResult != FOUND) {
      return 0;
    }

    int result = 0;
    int minimumRow = minimumCell[0];
    int minimumColumn = minimumCell[1];
    int minimumFree = solver.cachedMatrixManager.getFreeMask(minimumRow, minimumColumn);
    int minimumBits = Integer.bitCount(minimumFree);

    for (int bit = 0; bit < minimumBits; bit++) {
      int index = Creator.getSetBitOffset(minimumFree, bit);
      assert index > 0;

      solver.cachedMatrixManager.set(minimumRow, minimumColumn, (byte) index);
      int resultCount = backtrack(freeCells - 1, minimumCell, maxSolutions, solver);
      result += resultCount;
    }
    solver.cachedMatrixManager.set(
        minimumRow, minimumColumn, solver.cachedMatrixManager.getSchema().getUnsetValue());

    return result;
  }
}

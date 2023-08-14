package ch.zhaw.pm2.amongdigits;

import java.util.Arrays;
import java.util.Comparator;

/** Enumeration representing different difficulty levels for Sudoku challenges. */
public enum DifficultyLevel implements Comparable<DifficultyLevel> {

  /** Beginner difficulty level. */
  BEGINNER("beginner", 25, 2, 600),

  /** Easy difficulty level. */
  EASY("easy", 32, 4, 1200),

  /** Medium difficulty level. */
  MEDIUM("medium", 40, 6, 2400),

  /** Hard difficulty level. */
  HARD("hard", 53, 8, 3600),

  /** Expert difficulty level. */
  EXPERT("expert", 64, 10, 7200);

  private final String translationProperty;
  private final int maxNumbersToClear;
  private final int maxErrorsToSolve;
  private final int maxSecondsToSolve;

  /**
   * Constructs a DifficultyLevel enum constant with the specified properties.
   *
   * @param translationProperty The translation property associated with the difficulty level.
   * @param maxNumbersToClear The maximum number of numbers to clear in the Sudoku grid.
   * @param maxErrorsToSolve The maximum number of errors allowed to solve the Sudoku.
   * @param maxSecondsToSolve The maximum number of seconds allowed to solve the Sudoku.
   */
  DifficultyLevel(
      String translationProperty,
      int maxNumbersToClear,
      int maxErrorsToSolve,
      int maxSecondsToSolve) {
    this.translationProperty = translationProperty;
    this.maxNumbersToClear = maxNumbersToClear;
    this.maxErrorsToSolve = maxErrorsToSolve;
    this.maxSecondsToSolve = maxSecondsToSolve;
  }

  /**
   * Determines the difficulty level based on the number of cells to clear in the Sudoku grid.
   *
   * @param unsolvedGrid The unsolved Sudoku grid.
   * @return The difficulty level.
   */
  public static DifficultyLevel determineDifficultyLevel(final byte[][] unsolvedGrid) {
    int numbersToClear = 0;
    for (byte[] currentRow : unsolvedGrid) {
      for (byte cells : currentRow) {
        if (cells == 0) {
          numbersToClear++;
        }
      }
    }

    final DifficultyLevel[] difficultyLevels = DifficultyLevel.values();
    Arrays.sort(difficultyLevels, new DifficultyLevel.DifficultyLevelComparator().reversed());
    for (DifficultyLevel difficultyLevel : difficultyLevels) {
      if (numbersToClear >= difficultyLevel.getMaxNumbersToClear()) {
        return difficultyLevel;
      }
    }
    return difficultyLevels[difficultyLevels.length - 1];
  }

  /**
   * Returns the translation property associated with the difficulty level.
   *
   * @return The translation property.
   */
  public String getTranslationProperty() {
    return translationProperty;
  }

  /**
   * Returns the maximum number of numbers to clear in the Sudoku grid.
   *
   * @return The maximum number of numbers to clear.
   */
  public int getMaxNumbersToClear() {
    return maxNumbersToClear;
  }

  /**
   * Returns the maximum number of errors allowed to solve the Sudoku.
   *
   * @return The maximum number of errors to solve.
   */
  public int getMaxErrorsToSolve() {
    return maxErrorsToSolve;
  }

  /**
   * Returns the maximum number of seconds allowed to solve the Sudoku.
   *
   * @return The maximum number of seconds to solve.
   */
  public int getMaxSecondsToSolve() {
    return maxSecondsToSolve;
  }

  /**
   * A comparator implementation used to compare DifficultyLevel objects based on their
   * maxNumbersToClear property.
   */
  private static class DifficultyLevelComparator implements Comparator<DifficultyLevel> {

    /**
     * Compares two DifficultyLevel objects based on their maxNumbersToClear property.
     *
     * @param level1 The first DifficultyLevel to compare.
     * @param level2 The second DifficultyLevel to compare.
     * @return The result of the comparison.
     */
    @Override
    public int compare(final DifficultyLevel level1, final DifficultyLevel level2) {
      return Integer.compare(level1.getMaxNumbersToClear(), level2.getMaxNumbersToClear());
    }
  }
}

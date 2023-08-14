package ch.zhaw.pm2.amongdigits;

/** Enumeration representing different types of Sudoku challenges. */
public enum ChallengeType {

  /** Pre-generated Sudoku challenges. */
  PRE_GENERATED("sudokus/pre-generated"),

  /** User-generated Sudoku challenges. */
  USER_GENERATED("sudokus/upload");

  private final String directory;

  /**
   * Constructs a ChallengeType enum constant with the specified directory.
   *
   * @param directory The directory associated with the challenge type.
   */
  ChallengeType(String directory) {
    this.directory = directory;
  }

  /**
   * Returns the directory associated with the challenge type.
   *
   * @return The directory.
   */
  public String getDirectory() {
    return directory;
  }
}

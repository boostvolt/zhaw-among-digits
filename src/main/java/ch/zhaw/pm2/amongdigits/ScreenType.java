package ch.zhaw.pm2.amongdigits;

/** An enumeration representing the types of screens in the application. */
public enum ScreenType {
  /** The main menu screen. */
  MAIN_MENU("/views/MainMenu.fxml"),

  /** The settings screen. */
  SETTINGS("/views/Settings.fxml"),

  /** The statistics screen. */
  STATISTICS("/views/Statistics.fxml"),

  /** The new game menu screen. */
  NEW_GAME_MENU("/views/NewGameMenu.fxml"),

  /** The Sudoku game screen. */
  SUDOKU("/views/SudokuGameView.fxml"),

  /** The challenges screen. */
  CHALLENGES("/views/Challenges.fxml");

  private final String fileName;

  /**
   * Constructs a new ScreenType with the specified file name.
   *
   * @param fileName The file name associated with the screen type.
   */
  ScreenType(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Returns the file name associated with the screen type.
   *
   * @return The file name.
   */
  public String getFileName() {
    return fileName;
  }
}

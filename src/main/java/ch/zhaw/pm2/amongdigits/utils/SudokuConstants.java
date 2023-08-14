package ch.zhaw.pm2.amongdigits.utils;

/** Constants related to Sudoku. */
public class SudokuConstants {

  /** The size of the Sudoku grid. */
  public static final int SUDOKU_GRID_SIZE = 9;

  /** The separator character used in the grid representation. */
  public static final char GRID_SEPARATOR = '*';

  /** The character used to represent an empty cell in the grid. */
  public static final char EMPTY_GRID_CELL = '-';

  /** The separator character used in file area names. */
  public static final char FILE_AREA_NAME_SEPARATOR = '_';

  /** The valid file ending for Sudoku files. */
  public static final String VALID_FILE_ENDING = "txt";

  /** The symbol for infinity. */
  public static final String INFINITY_SYMBOL = "\u221E";

  /** The format for displaying the clock time in minutes and seconds. */
  public static final String CLOCK_FORMAT = "%d:%02d";

  /** The format for displaying an empty clock time. */
  public static final String EMPTY_CLOCK_FORMAT = "--:--";

  /** The symbol for indicating an enabled state. */
  public static final String ENABLED_SYMBOL = "\u2713";

  /** The symbol for indicating a disabled state. */
  public static final String DISABLED_SYMBOL = "X";

  /** The threshold value for converting seconds to minutes. */
  public static final int SECOND_MINUTE_THRESHOLD = 60;

  private SudokuConstants() {
    throw new UnsupportedOperationException("Utility class");
  }
}

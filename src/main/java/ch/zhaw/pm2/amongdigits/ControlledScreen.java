package ch.zhaw.pm2.amongdigits;

/**
 * Abstract class that serves as a base for all screens that need to be controlled by the {@link
 * SudokuGui}. Provides a way to access the SudokuGui instance and set it.
 */
public abstract class ControlledScreen {
  /** The instance of the {@link SudokuGui} controlling this screen. */
  private SudokuGui sudokuGui;
  /**
   * Sets the SudokuGui instance that controls this screen.
   *
   * @param sudokuGui the SudokuGui instance that controls this screen
   */
  protected final void setSudokuGui(SudokuGui sudokuGui) {
    this.sudokuGui = sudokuGui;
  }
  /**
   * Gets the SudokuGui instance that controls this screen.
   *
   * @return the SudokuGui instance that controls this screen
   */
  protected SudokuGui getSudokuGui() {
    return sudokuGui;
  }
}

package ch.zhaw.pm2.amongdigits.controller;

import static ch.zhaw.pm2.amongdigits.ScreenType.CHALLENGES;
import static ch.zhaw.pm2.amongdigits.ScreenType.NEW_GAME_MENU;
import static ch.zhaw.pm2.amongdigits.ScreenType.SETTINGS;
import static ch.zhaw.pm2.amongdigits.ScreenType.STATISTICS;

import ch.zhaw.pm2.amongdigits.ControlledScreen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * The MainMenuController class is responsible for controlling the main menu screen of the game.
 * This class extends the ControlledScreen abstract class to provide access to the SudokuGui object.
 */
public class MainMenuController extends ControlledScreen {

  @FXML private Button newGame;
  @FXML private Button challenges;
  @FXML private Button settings;
  @FXML private Button statistics;

  @FXML
  private void initialize() {
    newGame.setOnAction(event -> getSudokuGui().changeScreenTo(NEW_GAME_MENU));
    challenges.setOnAction(event -> getSudokuGui().changeScreenTo(CHALLENGES));
    settings.setOnAction(event -> getSudokuGui().changeScreenTo(SETTINGS));
    statistics.setOnAction(event -> getSudokuGui().changeScreenTo(STATISTICS));
  }
}

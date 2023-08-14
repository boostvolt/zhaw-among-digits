package ch.zhaw.pm2.amongdigits.controller;

import static ch.zhaw.pm2.amongdigits.ScreenType.MAIN_MENU;

import ch.zhaw.pm2.amongdigits.ControlledScreen;
import ch.zhaw.pm2.amongdigits.model.StatisticsModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * The StatisticsController class controls the behavior of the statistics screen in the AmongDigits
 * game. It extends the ControlledScreen class and is responsible for initializing the labels with
 * values from the StatisticsModel and binding them to the corresponding properties in the model. It
 * also handles the event when the main menu button is clicked by changing the screen to the main
 * menu.
 */
public class StatisticsController extends ControlledScreen {

  @FXML public Button mainMenu;
  @FXML private Label veryEasyGameBestTime;
  @FXML private Label veryEasyGameMistakes;
  @FXML private Label veryEasyGameStarted;
  @FXML private Label veryEasyGameTimePlayed;
  @FXML private Label veryEasyGameWon;
  @FXML private Label easyGameBestTime;
  @FXML private Label easyGameMistakes;
  @FXML private Label easyGameStarted;
  @FXML private Label easyGameTimePlayed;
  @FXML private Label easyGameWon;
  @FXML private Label hardGameBestTime;
  @FXML private Label hardGameMistakes;
  @FXML private Label hardGameStarted;
  @FXML private Label hardGameTimePlayed;
  @FXML private Label hardGameWon;
  @FXML private Label veryHardGameBestTime;
  @FXML private Label veryHardGameMistakes;
  @FXML private Label veryHardGameStarted;
  @FXML private Label veryHardGameTimePlayed;
  @FXML private Label veryHardGameWon;
  @FXML private Label mediumGameBestTime;
  @FXML private Label mediumGameMistakes;
  @FXML private Label mediumGameStarted;
  @FXML private Label mediumGameTimePlayed;
  @FXML private Label mediumGameWon;
  private StatisticsModel statisticsModel;

  /**
   * Initializes the controller by setting the action of the main menu button to change the screen
   * to the main menu. It also initializes the statisticsModel and binds the labels to the
   * corresponding properties in the model.
   */
  @FXML
  public void initialize() {
    mainMenu.setOnAction(event -> getSudokuGui().changeScreenTo(MAIN_MENU));
    this.statisticsModel = new StatisticsModel();
    bindLabels();
  }

  private void bindLabels() {
    veryEasyGameWon.textProperty().bind(statisticsModel.beginnerGameWonPropertyProperty());
    veryEasyGameMistakes
        .textProperty()
        .bind(statisticsModel.beginnerGameMistakesPropertyProperty());
    veryEasyGameStarted.textProperty().bind(statisticsModel.beginnerGameStartedPropertyProperty());
    veryEasyGameBestTime
        .textProperty()
        .bind(statisticsModel.beginnerGameBestTimePropertyProperty());
    veryEasyGameTimePlayed
        .textProperty()
        .bind(statisticsModel.beginnerGameTimePlayedPropertyProperty());
    easyGameWon.textProperty().bind(statisticsModel.getEasyGameWonPropertyProperty());
    easyGameMistakes.textProperty().bind(statisticsModel.getEasyGameMistakesPropertyProperty());
    easyGameStarted.textProperty().bind(statisticsModel.getEasyGameStartedPropertyProperty());
    easyGameBestTime.textProperty().bind(statisticsModel.getEasyGameBestTimePropertyProperty());
    easyGameTimePlayed.textProperty().bind(statisticsModel.getEasyGameTimePlayedPropertyProperty());
    mediumGameStarted.textProperty().bind(statisticsModel.getMediumGameStartedPropertyProperty());
    mediumGameMistakes.textProperty().bind(statisticsModel.getMediumGameMistakesPropertyProperty());
    mediumGameWon.textProperty().bind(statisticsModel.getMediumGameWonPropertyProperty());
    mediumGameBestTime.textProperty().bind(statisticsModel.getMediumGameBestTimePropertyProperty());
    mediumGameTimePlayed
        .textProperty()
        .bind(statisticsModel.getMediumGameTimePlayedPropertyProperty());
    hardGameStarted.textProperty().bind(statisticsModel.getHardGameStartedPropertyProperty());
    hardGameMistakes.textProperty().bind(statisticsModel.getHardGameMistakesPropertyProperty());
    hardGameWon.textProperty().bind(statisticsModel.getHardGameWonPropertyProperty());
    hardGameBestTime.textProperty().bind(statisticsModel.getHardGameBestTimePropertyProperty());
    hardGameTimePlayed.textProperty().bind(statisticsModel.getHardGameTimePlayedPropertyProperty());
    veryHardGameStarted.textProperty().bind(statisticsModel.expertGameStartedPropertyProperty());
    veryHardGameMistakes.textProperty().bind(statisticsModel.expertGameMistakesPropertyProperty());
    veryHardGameWon.textProperty().bind(statisticsModel.expertGameWonPropertyProperty());
    veryHardGameBestTime.textProperty().bind(statisticsModel.expertGameBestTimePropertyProperty());
    veryHardGameTimePlayed
        .textProperty()
        .bind(statisticsModel.expertGameTimePlayedPropertyProperty());
  }
}

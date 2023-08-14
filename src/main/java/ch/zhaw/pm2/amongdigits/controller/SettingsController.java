package ch.zhaw.pm2.amongdigits.controller;

import static ch.zhaw.pm2.amongdigits.ScreenType.MAIN_MENU;

import ch.zhaw.pm2.amongdigits.ControlledScreen;
import ch.zhaw.pm2.amongdigits.model.SettingsModel;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

/**
 * The SettingsController class is responsible for controlling the settings screen of the Sudoku
 * game. It allows the user to change the game's language, toggle dark mode, check mistakes, check
 * time, and receive real-time feedback on their moves.
 */
public class SettingsController extends ControlledScreen {

  @FXML private ResourceBundle resources;
  @FXML private Button mainMenu;
  @FXML private ChoiceBox<Locale> languageChooser = new ChoiceBox<>();
  @FXML private Button resetStatistics;
  @FXML private Button toggleDarkMode;
  @FXML private Button toggleCheckMistakes;
  @FXML private Button toggleCheckTime;
  @FXML private Button toggleRealtimeFeedback;
  private SettingsModel settingsModel;

  /**
   * Initializes the settings screen. It sets the action for the main menu button, initializes the
   * language chooser, and binds the text for each setting to its corresponding property in the
   * settings model. It also sets the action for each setting to its corresponding toggle method in
   * the settings model. Finally, it sets the action for the reset statistics button and adds a
   * listener for the dark mode value property.
   */
  @FXML
  public void initialize() {
    mainMenu.setOnAction(event -> getSudokuGui().changeScreenTo(MAIN_MENU));
    initLanguageChooser();
    this.settingsModel = new SettingsModel();
    toggleDarkMode.textProperty().bind(settingsModel.getDarkModeProperty());
    toggleCheckMistakes.textProperty().bind(settingsModel.getCheckMistakesProperty());
    toggleCheckTime.textProperty().bind(settingsModel.getCheckTimeProperty());
    toggleRealtimeFeedback.textProperty().bind(settingsModel.getRealtimeFeedbackProperty());
    toggleDarkMode.setOnAction(event -> settingsModel.toggleDarkMode());
    toggleCheckMistakes.setOnAction(event -> settingsModel.toggleCheckMistakes());
    toggleCheckTime.setOnAction(event -> settingsModel.toggleCheckTime());
    toggleRealtimeFeedback.setOnAction(event -> settingsModel.toggleRealtimeFeedback());
    resetStatistics.setOnAction(event -> settingsModel.resetStatistics());
    settingsModel
        .getDarkModeValueProperty()
        .addListener((observable, oldValue, newValue) -> getSudokuGui().setStyleSheet(newValue));
  }

  private void initLanguageChooser() {
    languageChooser.getItems().add(Locale.ENGLISH);
    languageChooser.getItems().add(Locale.GERMAN);

    languageChooser.setValue(resources.getLocale());

    languageChooser.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(Locale locale) {
            return locale == null ? "" : locale.getDisplayName(locale);
          }

          @Override
          public Locale fromString(String string) {
            return Locale.forLanguageTag(string);
          }
        });
    languageChooser.setOnAction(
        event -> {
          languageChooser.setValue(languageChooser.getSelectionModel().getSelectedItem());
          settingsModel.changeLanguage(languageChooser.getValue().toString());
          getSudokuGui().reloadScreens();
        });
  }
}

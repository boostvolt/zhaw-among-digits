package ch.zhaw.pm2.amongdigits;

import static ch.zhaw.pm2.amongdigits.PropertyType.SETTINGS;
import static ch.zhaw.pm2.amongdigits.ScreenType.CHALLENGES;
import static ch.zhaw.pm2.amongdigits.ScreenType.MAIN_MENU;
import static ch.zhaw.pm2.amongdigits.ScreenType.STATISTICS;
import static ch.zhaw.pm2.amongdigits.ScreenType.SUDOKU;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.media.MediaPlayer.INDEFINITE;

import ch.zhaw.pm2.amongdigits.controller.SudokuGameController;
import ch.zhaw.pm2.amongdigits.exception.InvalidFileFormatException;
import ch.zhaw.pm2.amongdigits.exception.InvalidSudokuException;
import ch.zhaw.pm2.amongdigits.utils.PropertiesHandler;
import ch.zhaw.pm2.amongdigits.utils.alert.AlertBuilder;
import ch.zhaw.pm2.amongdigits.utils.alert.AlertOptions;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

/**
 * The graphical user interface for the Sudoku game.
 *
 * <p>The SudokuGui class extends the JavaFX Application class and provides methods for managing the
 * game screens, changing the stylesheet, initializing new games, and reloading screens.
 */
@Slf4j
public class SudokuGui extends Application {

  private static final String LANGUAGE = "language";
  private static final double GAME_MUSIC_VOLUME = 0.2;
  private final Map<ScreenType, Parent> screens = new EnumMap<>(ScreenType.class);
  private Scene scene;
  private MediaPlayer mainMenuMediaPlayer;
  private MediaPlayer gameMediaPlayer;

  /**
   * Starts the Sudoku game GUI by initializing the music players and loading the screens.
   *
   * @param primaryStage The primary stage for the application.
   */
  @Override
  public void start(Stage primaryStage) {
    try {
      initMusicPlayers();
    } catch (URISyntaxException e) {
      log.error("Error loading menu music");
    }
    EnumSet.allOf(ScreenType.class).forEach(this::loadScreen);
    mainWindow(primaryStage);
    setStyleSheet(
        requireNonNull(
                getClass()
                    .getResource(PropertiesHandler.getPropertyString(SETTINGS, "cssFileString")))
            .toString());
  }

  /**
   * Sets the stylesheet for the scene.
   *
   * @param styleSheetPath The path to the stylesheet file.
   */
  public void setStyleSheet(String styleSheetPath) {
    scene.getStylesheets().clear();
    scene.getStylesheets().add(styleSheetPath);
  }

  /**
   * Creates the main window of the Sudoku game with the specified primaryStage.
   *
   * @param primaryStage The primary stage for the application.
   */
  public void mainWindow(Stage primaryStage) {
    Parent rootPane = screens.get(MAIN_MENU);
    scene = new Scene(rootPane);

    primaryStage.setScene(scene);
    primaryStage.setMinWidth(850);
    primaryStage.setMinHeight(650);
    primaryStage.setTitle("Among Digits");
    primaryStage.show();
  }

  /**
   * Changes the screen to the specified screen type.
   *
   * @param screenType The screen type to change to.
   */
  public void changeScreenTo(ScreenType screenType) {
    if (screenType == MAIN_MENU) {
      gameMediaPlayer.stop();
      mainMenuMediaPlayer.play();
    } else if (screenType == SUDOKU) {
      mainMenuMediaPlayer.stop();
      gameMediaPlayer.play();
    }
    if (screenType == STATISTICS) {
      loadScreen(STATISTICS);
    }
    scene.setRoot(screens.get(screenType));
  }

  /** Reloads the game screens. */
  public void reloadScreens() {
    for (ScreenType type : EnumSet.allOf(ScreenType.class)) {
      loadScreen(type);
    }
    changeScreenTo(ScreenType.SETTINGS);
  }

  /**
   * Initializes a new game with the specified difficulty level.
   *
   * @param difficultyLevel The difficulty level of the new game.
   */
  public void initializeNewGameWithDifficulty(DifficultyLevel difficultyLevel) {
    initializeNewGame(difficultyLevel);
  }

  /**
   * Initializes a new game with the specified Sudoku file.
   *
   * @param sudokuFile The Sudoku file to load the game from.
   */
  public void initializeNewGameWithFile(final File sudokuFile) {
    initializeNewGame(sudokuFile);
  }

  /** Reloads the challenges screen. */
  public void reloadChallenges() {
    loadScreen(CHALLENGES);
  }

  private void initMusicPlayers() throws URISyntaxException {
    final Media mainMenuMusic =
        new Media(
            requireNonNull(getClass().getResource("/media/menu-music.mp3")).toURI().toString());
    final Media gameMenuMusic =
        new Media(
            requireNonNull(getClass().getResource("/media/game-music.mp3")).toURI().toString());
    mainMenuMediaPlayer = new MediaPlayer(mainMenuMusic);
    mainMenuMediaPlayer.setCycleCount(INDEFINITE);
    mainMenuMediaPlayer.play();
    gameMediaPlayer = new MediaPlayer(gameMenuMusic);
    gameMediaPlayer.setVolume(GAME_MUSIC_VOLUME);
    gameMediaPlayer.setCycleCount(INDEFINITE);
  }

  private void initializeNewGame(Object parameter) {
    ResourceBundle bundle =
        ResourceBundle.getBundle(
            "languages.MessagesBundle",
            new Locale(PropertiesHandler.getPropertyString(SETTINGS, LANGUAGE)));
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(SUDOKU.getFileName()), bundle);
      Parent loadScreen = loader.load();
      SudokuGameController sudokuGameController = loader.getController();
      sudokuGameController.setSudokuGui(this);

      if (parameter instanceof DifficultyLevel difficultyLevel) {
        sudokuGameController.createSudoku(difficultyLevel);
      } else if (parameter instanceof File file) {
        sudokuGameController.createSudoku(file);
      } else {
        throw new IllegalArgumentException(
            "Given parameter is not a compatible Sudoku initializer");
      }

      screens.put(SUDOKU, loadScreen);
      changeScreenTo(SUDOKU);
    } catch (IOException | InvalidFileFormatException | InvalidSudokuException e) {
      log.error(format("Error loading sudoku %s: %s", parameter, e.getMessage()));
      AlertBuilder.showAlert(
          new AlertOptions(
              ERROR,
              bundle.getString("sudoku_load_failed_title"),
              null,
              bundle.getString("sudoku_load_failed"),
              null,
              Collections.emptySet()));
    }
  }

  private void loadScreen(ScreenType type) {
    try {
      Locale locale;
      if (PropertiesHandler.getPropertyString(SETTINGS, LANGUAGE).isBlank()) {
        locale = Locale.getDefault();
      } else {
        locale = new Locale(PropertiesHandler.getPropertyString(SETTINGS, LANGUAGE));
      }
      ResourceBundle bundle = ResourceBundle.getBundle("languages.MessagesBundle", locale);
      FXMLLoader loader = new FXMLLoader(getClass().getResource(type.getFileName()), bundle);
      Parent loadScreen = loader.load();

      ControlledScreen controlledScreen = loader.getController();
      controlledScreen.setSudokuGui(this);

      screens.put(type, loadScreen);
    } catch (IOException e) {
      log.error(format("Error loading screen: %s", e.getMessage()));
    }
  }
}

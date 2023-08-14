package ch.zhaw.pm2.amongdigits.controller;

import static ch.zhaw.pm2.amongdigits.DifficultyLevel.BEGINNER;
import static ch.zhaw.pm2.amongdigits.DifficultyLevel.EASY;
import static ch.zhaw.pm2.amongdigits.DifficultyLevel.EXPERT;
import static ch.zhaw.pm2.amongdigits.DifficultyLevel.HARD;
import static ch.zhaw.pm2.amongdigits.DifficultyLevel.MEDIUM;
import static ch.zhaw.pm2.amongdigits.ScreenType.MAIN_MENU;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.EMPTY_GRID_CELL;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.GRID_SEPARATOR;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.SUDOKU_GRID_SIZE;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.VALID_FILE_ENDING;
import static ch.zhaw.pm2.amongdigits.utils.schema.SchemaTypes.SCHEMA_9X9;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

import ch.zhaw.pm2.amongdigits.ControlledScreen;
import ch.zhaw.pm2.amongdigits.DifficultyLevel;
import ch.zhaw.pm2.amongdigits.exception.InvalidFileFormatException;
import ch.zhaw.pm2.amongdigits.exception.InvalidSudokuException;
import ch.zhaw.pm2.amongdigits.upload.FileValidator;
import ch.zhaw.pm2.amongdigits.upload.SudokuFileLoader;
import ch.zhaw.pm2.amongdigits.utils.alert.AlertBuilder;
import ch.zhaw.pm2.amongdigits.utils.alert.AlertOptions;
import ch.zhaw.pm2.amongdigits.utils.sudoku.SudokuManager;
import java.io.File;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;

/**
 * The NewGameMenuController class is the controller for the new game menu screen. It provides the
 * user with options to start a new game with varying levels of difficulty, load a previously saved
 * game or return to the main menu.
 */
@Slf4j
public class NewGameMenuController extends ControlledScreen {

  private static final String[] EXAMPLE_GRID = {
    "9---8-3--",
    "---25-7--",
    "-2-3----4",
    "-94------",
    "---73-56-",
    "7-5-6-4--",
    "--78-39--",
    "--1-----3",
    "3-------2"
  };
  private static final String GRID_HELP_TEXT = String.join(lineSeparator(), EXAMPLE_GRID);

  private final Map<DifficultyLevel, Button> difficulties = new EnumMap<>(DifficultyLevel.class);
  @FXML private ResourceBundle resources;
  @FXML private AnchorPane rootPane;
  @FXML private Button newBeginnerSudoku;
  @FXML private Button newEasySudoku;
  @FXML private Button newMediumSudoku;
  @FXML private Button newHardSudoku;
  @FXML private Button newExpertSudoku;
  @FXML private Button loadSudoku;
  @FXML private Button mainMenu;
  private SudokuFileLoader sudokuFileLoader;
  private FileChooser fileChooser;

  /**
   * Initializes the NewGameMenuController. It creates the {@link SudokuFileLoader} and sets for
   * each difficulty the event to be fired when a new game of that difficulty should initiate.
   */
  @FXML
  public void initialize() {
    sudokuFileLoader =
        new SudokuFileLoader(
            new FileValidator(SUDOKU_GRID_SIZE, GRID_SEPARATOR, EMPTY_GRID_CELL),
            new SudokuManager(SCHEMA_9X9),
            resources);
    fileChooser = new FileChooser();
    initFileChooser();
    difficulties.put(BEGINNER, newBeginnerSudoku);
    difficulties.put(EASY, newEasySudoku);
    difficulties.put(MEDIUM, newMediumSudoku);
    difficulties.put(HARD, newHardSudoku);
    difficulties.put(EXPERT, newExpertSudoku);

    mainMenu.setOnAction(event -> getSudokuGui().changeScreenTo(MAIN_MENU));
    loadSudoku.setOnAction(this::uploadSudoku);
    difficulties.forEach(
        (key, value) ->
            value.setOnAction(event -> getSudokuGui().initializeNewGameWithDifficulty(key)));
  }

  private void initFileChooser() {
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Text Files", "*." + VALID_FILE_ENDING));
    fileChooser.setTitle(resources.getString("choose_file_title"));
  }

  private void uploadSudoku(ActionEvent event) {
    final File selectedFile = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
    if (selectedFile != null) {
      try {
        sudokuFileLoader.uploadSudoku(selectedFile);
        AlertBuilder.showAlert(
            new AlertOptions(
                INFORMATION,
                resources.getString("upload_success"),
                null,
                resources.getString("upload_success_message"),
                new Image("media/Upload.gif"),
                Collections.emptySet()));
        log.info("New sudoku " + selectedFile.getName() + " has been uploaded");
        getSudokuGui().reloadChallenges();
      } catch (final InvalidFileFormatException e) {
        AlertBuilder.showAlert(
            new AlertOptions(
                ERROR,
                resources.getString("upload_failed"),
                null,
                format(resources.getString("selected_file_invalid_exception"), e.getMessage()),
                new Image("media/UploadFailed.gif"),
                Collections.emptySet()));
        log.warn(
            "User tried to upload sudoku file with name %s but failed with exception %s"
                .formatted(selectedFile.getName(), e.getMessage()));
      } catch (InvalidSudokuException e) {
        AlertBuilder.showAlert(
            new AlertOptions(
                ERROR,
                resources.getString("upload_failed"),
                null,
                format(
                    resources.getString("selected_file_sudoku_invalid_exception"), e.getMessage()),
                new Image("media/UploadFailed.gif"),
                Collections.emptySet()));
        log.warn(
            "User tried to upload sudoku file with name %s but failed with exception %s"
                .formatted(selectedFile.getName(), e.getMessage()));
      }
    }
  }

  @FXML
  private void displayHelp() {
    AlertBuilder.showAlert(
        new AlertOptions(
            INFORMATION,
            resources.getString("upload_help_title"),
            null,
            resources.getString("upload_help") + lineSeparator() + GRID_HELP_TEXT,
            new Image("media/UploadHelp.gif"),
            Collections.emptySet()));
  }
}

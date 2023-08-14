package ch.zhaw.pm2.amongdigits.controller;

import static ch.zhaw.pm2.amongdigits.ChallengeType.PRE_GENERATED;
import static ch.zhaw.pm2.amongdigits.ChallengeType.USER_GENERATED;
import static ch.zhaw.pm2.amongdigits.ScreenType.MAIN_MENU;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.FILE_AREA_NAME_SEPARATOR;

import ch.zhaw.pm2.amongdigits.ControlledScreen;
import ch.zhaw.pm2.amongdigits.DifficultyLevel;
import ch.zhaw.pm2.amongdigits.model.ChallengesModel;
import java.io.File;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * The ChallengesController class is responsible for controlling the screen where users can select
 * and play pre-generated or user-generated Sudoku challenges.
 */
public class ChallengesController extends ControlledScreen {

  @FXML private ResourceBundle resources;
  @FXML private ListView<File> preGeneratedChallenges;
  @FXML private Button playPreGenerated;
  @FXML private ListView<File> userGeneratedChallenges;
  @FXML private Button playUserGenerated;
  @FXML private Button mainMenu;

  /**
   * Initializes the ChallengesController object by setting up the main menu button and loading
   * pre-generated and user-generated challenges.
   */
  @FXML
  public void initialize() {
    mainMenu.setOnAction(event -> getSudokuGui().changeScreenTo(MAIN_MENU));
    final ChallengesModel challengesModel = new ChallengesModel();
    challengesModel.load();
    preGeneratedChallenges.getItems().addAll(challengesModel.getChallenges().get(PRE_GENERATED));
    userGeneratedChallenges.getItems().addAll(challengesModel.getChallenges().get(USER_GENERATED));
    final FileCellFactory fileCellFactory = new FileCellFactory();
    preGeneratedChallenges.setCellFactory(fileCellFactory);
    userGeneratedChallenges.setCellFactory(fileCellFactory);
    playPreGenerated.setOnAction(event -> initializeOnPlay(preGeneratedChallenges));
    playUserGenerated.setOnAction(event -> initializeOnPlay(userGeneratedChallenges));
  }

  /** A callback function that sets up the display for a single file in the ListView. */
  public class FileCellFactory implements Callback<ListView<File>, ListCell<File>> {

    /**
     * Creates a new ListCell for a file.
     *
     * @param param The ListView that the cell belongs to.
     * @return The ListCell for the file.
     */
    @Override
    public ListCell<File> call(ListView<File> param) {
      return new ListCell<>() {
        /**
         * Updates the display for the cell.
         *
         * @param file The file to display.
         * @param empty Whether the cell is empty.
         */
        @Override
        public void updateItem(final File file, final boolean empty) {
          super.updateItem(file, empty);
          if (empty || file == null) {
            setText(null);
          } else {
            setText(resolveDisplayedFileName(file.getName()));
          }
        }
      };
    }

    private String resolveDisplayedFileName(final String fileName) {
      final DifficultyLevel difficultyLevel =
          DifficultyLevel.valueOf(
              fileName.substring(0, fileName.indexOf(FILE_AREA_NAME_SEPARATOR)));
      final String sudokuName =
          fileName.substring(
              fileName.indexOf(FILE_AREA_NAME_SEPARATOR) + 1,
              fileName.lastIndexOf(FILE_AREA_NAME_SEPARATOR));
      return resources.getString(difficultyLevel.getTranslationProperty()) + ": " + sudokuName;
    }
  }

  private void initializeOnPlay(final ListView<File> challenges) {
    final File selectedChallenge = challenges.getSelectionModel().getSelectedItem();
    if (selectedChallenge != null) {
      getSudokuGui().initializeNewGameWithFile(selectedChallenge);
    }
  }
}

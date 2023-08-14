package ch.zhaw.pm2.amongdigits.controller;

import static ch.zhaw.pm2.amongdigits.PropertyType.SETTINGS;
import static ch.zhaw.pm2.amongdigits.ScreenType.MAIN_MENU;
import static ch.zhaw.pm2.amongdigits.utils.PropertiesHandler.getPropertyString;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.CLOCK_FORMAT;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.INFINITY_SYMBOL;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.SECOND_MINUTE_THRESHOLD;
import static java.lang.Boolean.TRUE;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static javafx.scene.control.ButtonType.OK;
import static javafx.scene.input.KeyCode.BACK_SPACE;
import static javafx.scene.input.KeyCode.DELETE;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.LIGHTBLUE;
import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.TRANSPARENT;

import ch.zhaw.pm2.amongdigits.ControlledScreen;
import ch.zhaw.pm2.amongdigits.DifficultyLevel;
import ch.zhaw.pm2.amongdigits.exception.InvalidFileFormatException;
import ch.zhaw.pm2.amongdigits.exception.InvalidSudokuException;
import ch.zhaw.pm2.amongdigits.model.SudokuGameModel;
import ch.zhaw.pm2.amongdigits.utils.alert.AlertBuilder;
import ch.zhaw.pm2.amongdigits.utils.alert.AlertOptions;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.IntToDoubleFunction;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import lombok.extern.slf4j.Slf4j;

/**
 * The SudokuGameController class is the controller for the Sudoku game. It is responsible for
 * handling user input, updating the game state and GUI accordingly.
 */
@Slf4j
public class SudokuGameController extends ControlledScreen {

  private static final String REAL_TIME_FEEDBACK = "realtimeFeedback";
  private static final int SUDOKU_GRID_NUMBER = 9;
  private static final int SUDOKU_INNER_GRID = 3;
  private static final int FULL_SIZE = 1;
  private static final String REGEX_ONE_DIGIT = "[1-9]";
  private static final IntToDoubleFunction RESOLVE_TOP_LEFT_BORDER = val -> val == 0 ? 4 : 0;
  private static final IntToDoubleFunction RESOLVE_RIGHT_BOTTOM_BORDER =
      val -> (val + 1) % 3 == 0 ? 4 : 0;

  private final Label[][] sudokuFields;
  private final Label[][][][] pencilFields;

  private SudokuGameModel model;
  private Label activeLabel;

  @FXML private ResourceBundle resources;
  @FXML private AnchorPane rootPane;
  @FXML private GridPane sudokuGrid;
  @FXML private Label timeField;
  @FXML private Label maxTimeField;
  @FXML private Label mistakesField;
  @FXML private Label maxMistakesField;
  @FXML private ToggleButton pencilButton;
  @FXML private HBox timeBox;

  /**
   * This class controls the Sudoku game and initializes the game window. It creates the Sudoku grid
   * and populates it with values. It also sets up listeners for the timer, mistakes, and game
   * status.
   */
  public SudokuGameController() {
    sudokuFields = new Label[SUDOKU_GRID_NUMBER][SUDOKU_GRID_NUMBER];
    pencilFields =
        new Label[SUDOKU_GRID_NUMBER][SUDOKU_GRID_NUMBER][SUDOKU_INNER_GRID][SUDOKU_INNER_GRID];
  }

  /**
   * Initializes the game window by setting up the Sudoku grid, timer, mistakes, and game status.
   * Listens for key presses and sets up the game if the Sudoku board is null.
   */
  @FXML
  public void initialize() {
    model = new SudokuGameModel(resources);
    setUpSudokuGrid();

    mistakesField.textProperty().bind(model.getMistakesProperty().asString());
    if (getPropertyString(SETTINGS, "checkMistakes").equals("true")) {
      maxMistakesField.textProperty().bind(model.getMaxMistakesProperty().asString());
    } else {
      maxMistakesField.setText(INFINITY_SYMBOL);
    }

    if (getPropertyString(SETTINGS, REAL_TIME_FEEDBACK).equals("false")) {
      timeBox.setVisible(false);
    }

    model
        .getElapsedTimeProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              int minutes = (newValue.intValue() / SECOND_MINUTE_THRESHOLD);
              int seconds = (newValue.intValue() % SECOND_MINUTE_THRESHOLD);

              timeField.setText(format(CLOCK_FORMAT, minutes, seconds));
            });

    if (getPropertyString(SETTINGS, "checkTime").equals("true")) {
      model
          .getTimeLimitProperty()
          .addListener(
              (observable, oldValue, newValue) -> {
                int minutes = (newValue.intValue() / SECOND_MINUTE_THRESHOLD);
                int seconds = (newValue.intValue() % SECOND_MINUTE_THRESHOLD);

                maxTimeField.setText(format(CLOCK_FORMAT, minutes, seconds));
              });
    } else {
      maxTimeField.setText(INFINITY_SYMBOL);
    }

    model
        .isSolvedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (TRUE.equals(newValue)) {
                displaySolvedDialog();
              }
            });

    model
        .isLimitExceededProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (TRUE.equals(newValue)) {
                displayLostDialog();
              }
            });

    rootPane.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.F1) {
            displayHelp();
          }
        });
  }

  /**
   * Delegates the creation of a new sudoku based on the given {@link DifficultyLevel}
   *
   * @param difficultyLevel the {@link DifficultyLevel} for the Sudoku game to be created
   */
  public void createSudoku(DifficultyLevel difficultyLevel) {
    model.createSudoku(difficultyLevel);
    fillInitialSudoku();
    model.startGame();
  }

  /**
   * Delegates the creation of a new sudoku based on the given {@link File}
   *
   * @param sudokuFile the {@link File} which has the unsolved and solved grid of the Sudoku to be
   *     created
   */
  public void createSudoku(File sudokuFile)
      throws InvalidFileFormatException, InvalidSudokuException {
    model.createSudoku(sudokuFile);
    fillInitialSudoku();
    model.startGame();
  }

  private void fillInitialSudoku() {
    for (int row = 0; row < SUDOKU_GRID_NUMBER; row++) {
      for (int column = 0; column < SUDOKU_GRID_NUMBER; column++) {
        String numberValue =
            (model.getSudokuBoard().unsolvedGrid()[row][column] == 0)
                ? ""
                : valueOf(model.getSudokuBoard().unsolvedGrid()[row][column]);
        sudokuFields[row][column].setText(numberValue);
      }
    }
  }

  @FXML
  private void exit() {
    model.stopGame();
    getSudokuGui().changeScreenTo(MAIN_MENU);
  }

  @FXML
  private void displayHelp() {
    AlertBuilder.showAlert(
        new AlertOptions(
            AlertType.INFORMATION,
            resources.getString("help_title"),
            null,
            resources.getString("help_text"),
            new Image("media/Help.gif"),
            Collections.emptySet()));
  }

  @FXML
  private void displaySolvedDialog() {
    Optional<ButtonType> buttonType =
        AlertBuilder.showAlert(
            new AlertOptions(
                AlertType.INFORMATION,
                resources.getString("won_title"),
                resources.getString("won_text"),
                null,
                new Image("media/GameWon.gif"),
                Collections.emptySet()));
    if (buttonType.isPresent() && buttonType.get() == OK) {
      model.stopGame();
      getSudokuGui().changeScreenTo(MAIN_MENU);
    }
  }

  @FXML
  private void displayLostDialog() {
    Optional<ButtonType> buttonType =
        AlertBuilder.showAlert(
            new AlertOptions(
                AlertType.INFORMATION,
                resources.getString("lost_title"),
                resources.getString("lost_text"),
                null,
                new Image("media/GameLost.gif"),
                Set.of(
                    new ButtonType(resources.getString("retry_game"), ButtonBar.ButtonData.APPLY),
                    new ButtonType(resources.getString("main_menu"), OK.getButtonData()))));

    if (buttonType.isPresent()) {
      if (buttonType.get().getButtonData() == ButtonBar.ButtonData.APPLY) {
        model.stopGame();
        resetGame();
        model.startGame();

      } else {
        model.stopGame();
        getSudokuGui().changeScreenTo(MAIN_MENU);
      }
    }
  }

  private void resetGame() {
    deactivateLabel(activeLabel);

    for (int row = 0; row < SUDOKU_GRID_NUMBER; row++) {
      for (int col = 0; col < SUDOKU_GRID_NUMBER; col++) {
        sudokuFields[row][col].setText("");
        for (int subRow = 0; subRow < SUDOKU_INNER_GRID; subRow++) {
          for (int subCol = 0; subCol < SUDOKU_INNER_GRID; subCol++) {
            pencilFields[row][col][subRow][subCol].setText("");
          }
        }
      }
    }
    fillInitialSudoku();
  }

  @FXML
  private void switchInputMode() {
    if (activeLabel != null) {
      activeLabel.setOnKeyPressed(numKeyPressed(activeLabel));
    }
  }

  private void setUpSudokuGrid() {
    sudokuGrid.setAlignment(Pos.CENTER);
    populateGrid(sudokuGrid, SUDOKU_GRID_NUMBER);
    for (int row = 0; row < SUDOKU_GRID_NUMBER; row++) {
      for (int col = 0; col < SUDOKU_GRID_NUMBER; col++) {
        final GridPane subGridPane = new GridPane();
        initializeSubGrid(subGridPane, row, col);
        populateGrid(subGridPane, SUDOKU_INNER_GRID);
        addSubGridChildren(subGridPane, row, col);
        final Label numLabel = createNumLabel(subGridPane, row, col);
        sudokuFields[row][col] = numLabel;
        sudokuGrid.add(subGridPane, col, row);
        sudokuGrid.add(numLabel, col, row);
        GridPane.setHalignment(numLabel, HPos.CENTER);
      }
    }
  }

  private void populateGrid(final GridPane gridPane, final int dimension) {
    for (int i = 0; i < dimension; i++) {
      gridPane.addColumn(i);
      gridPane.addRow(i);
    }
  }

  private void initializeSubGrid(final GridPane subGridPane, final int row, final int col) {
    subGridPane.setId("subGrid-" + col + row);

    subGridPane.maxHeightProperty().bind(sudokuGrid.widthProperty().divide(SUDOKU_GRID_NUMBER));
    subGridPane.maxWidthProperty().bind(sudokuGrid.heightProperty().divide(SUDOKU_GRID_NUMBER));

    subGridPane
        .prefWidthProperty()
        .bind(
            Bindings.min(
                rootPane
                    .widthProperty()
                    .divide(SUDOKU_GRID_NUMBER)
                    .subtract(SUDOKU_INNER_GRID * subGridPane.getHgap()),
                rootPane
                    .heightProperty()
                    .divide(SUDOKU_GRID_NUMBER)
                    .subtract(SUDOKU_INNER_GRID * subGridPane.getVgap())));
    subGridPane.prefHeightProperty().bind(subGridPane.prefWidthProperty());
    subGridPane.setAlignment(Pos.CENTER);
    subGridPane
        .vgapProperty()
        .bind(
            Bindings.min(
                rootPane.heightProperty().divide(SUDOKU_GRID_NUMBER * SUDOKU_GRID_NUMBER * 2),
                rootPane.widthProperty().divide(SUDOKU_GRID_NUMBER * SUDOKU_GRID_NUMBER * 2)));
    subGridPane.hgapProperty().bind(subGridPane.vgapProperty());
  }

  private void addSubGridChildren(final GridPane subGridPane, final int row, final int col) {
    for (int subRow = 0; subRow < SUDOKU_INNER_GRID; subRow++) {
      for (int subCol = 0; subCol < SUDOKU_INNER_GRID; subCol++) {
        final Label subNumLabel = new Label();
        subNumLabel.setId("num" + row + col + "-pencilNum-" + subRow + subCol);
        subGridPane
            .widthProperty()
            .addListener(
                (obs, oldWidth, newWidth) ->
                    updateLabelFontSize(subNumLabel, subGridPane, SUDOKU_INNER_GRID));
        subGridPane
            .heightProperty()
            .addListener(
                (obs, oldHeight, newHeight) ->
                    updateLabelFontSize(subNumLabel, subGridPane, SUDOKU_INNER_GRID));
        pencilFields[row][col][subRow][subCol] = subNumLabel;
        subGridPane.add(subNumLabel, subCol, subRow);
        GridPane.setHalignment(subNumLabel, HPos.CENTER);
      }
    }
  }

  private Label createNumLabel(GridPane subGridPane, int row, int col) {
    final Label numLabel = new Label();
    numLabel.setId("num-" + row + "-" + col);
    numLabel.setStyle(defineBorder(row, col));
    numLabel.setAlignment(Pos.CENTER);
    numLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

    subGridPane
        .widthProperty()
        .addListener(
            (obs, oldWidth, newWidth) -> updateLabelFontSize(numLabel, subGridPane, FULL_SIZE));
    subGridPane
        .heightProperty()
        .addListener(
            (obs, oldHeight, newHeight) -> updateLabelFontSize(numLabel, subGridPane, FULL_SIZE));

    numLabel.setOnMouseClicked(event -> activateLabel(numLabel));
    return numLabel;
  }

  // Method to update the font size of the label based on the cell dimensions
  private void updateLabelFontSize(Label label, GridPane gridPane, int gridSize) {
    double cellWidth = gridPane.getWidth();
    double cellHeight = gridPane.getHeight();

    double fontSize =
        Math.min((cellWidth / label.getText().length() / gridSize), (cellHeight / (gridSize * 2)));
    label.setFont(Font.font(fontSize));
  }

  private String defineBorder(final int row, final int col) {
    return "-fx-border-width: "
        + RESOLVE_TOP_LEFT_BORDER.applyAsDouble(row)
        + " "
        + RESOLVE_RIGHT_BOTTOM_BORDER.applyAsDouble(col)
        + " "
        + RESOLVE_RIGHT_BOTTOM_BORDER.applyAsDouble(row)
        + " "
        + RESOLVE_TOP_LEFT_BORDER.applyAsDouble(col)
        + ";";
  }

  private void activateLabel(Label label) {
    // Only activate label if it is not pre-set
    if (!model.isDefaultNumber(
        getNumberLabelPosition(label)[0], getNumberLabelPosition(label)[1])) {

      if (activeLabel != null) {
        activeLabel.setBackground(Background.fill(TRANSPARENT));
        activeLabel.setOpacity(1);
        rootPane.getScene().setOnKeyPressed(null);
      }
      activeLabel = label;
      activeLabel.setBackground(Background.fill(LIGHTBLUE));
      activeLabel.setOpacity(0.5);
      rootPane.getScene().setOnKeyPressed(numKeyPressed(activeLabel));
    }
  }

  private void deactivateLabel(Label label) {
    if (label.equals(activeLabel)) {
      activeLabel = null;
    }

    label.setBackground(Background.fill(TRANSPARENT));
    label.setOnKeyPressed(null);
  }

  private EventHandler<KeyEvent> numKeyPressed(final Label label) {
    return event -> {
      if (event.getCode() == BACK_SPACE || event.getCode() == DELETE) {
        label.setText("");
        deactivateLabel(label);
        clearSubPencilLabels(label);
      } else if (event.getText().matches(REGEX_ONE_DIGIT)) {
        validateInputBasedOnPencilState(label, event);
      }
    };
  }

  private void validateInputBasedOnPencilState(Label label, KeyEvent event) {
    if (pencilButton.isSelected()) {
      processSubLabels(label, parseInt(event.getText()));
    } else {
      label.setText(event.getText());
      if (model.checkInput(
              Byte.parseByte(event.getText()),
              getNumberLabelPosition(label)[0],
              getNumberLabelPosition(label)[1])
          && getPropertyString(SETTINGS, REAL_TIME_FEEDBACK).equals("true")) {
        label.setTextFill(GREEN);
      } else if (getPropertyString(SETTINGS, REAL_TIME_FEEDBACK).equals("true")) {
        label.setTextFill(RED);
      }
      clearSubPencilLabels(label);
    }
  }

  private void clearSubPencilLabels(Label label) {
    Arrays.stream(getSubLabels(label))
        .flatMap(Arrays::stream)
        .forEach(subPencilLabel -> subPencilLabel.setText(""));
  }

  private void processSubLabels(final Label label, final int numKey) {
    final Label[][] subLabels = getSubLabels(label);
    label.setText("");
    int cellIndex = numKey - 1;
    int row = cellIndex / 3;
    int col = cellIndex % 3;
    subLabels[row][col].setText(subLabels[row][col].getText().isBlank() ? valueOf(numKey) : "");
  }

  private Label[][] getSubLabels(final Label parentLabel) {
    final int[] labelPositions = getNumberLabelPosition(parentLabel);
    return pencilFields[labelPositions[0]][labelPositions[1]].clone();
  }

  private int[] getNumberLabelPosition(final Label label) {
    final String[] labelPositions = label.getId().split("-");
    return new int[] {parseInt(labelPositions[1]), parseInt(labelPositions[2])};
  }
}

package ch.zhaw.pm2.amongdigits.model;

import static ch.zhaw.pm2.amongdigits.PropertyType.STATISTICS;
import static ch.zhaw.pm2.amongdigits.utils.PropertiesHandler.getPropertyString;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.EMPTY_CLOCK_FORMAT;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** The StatisticsModelTest class tests the StatisticsModel class. */
class StatisticsModelTest {
  StatisticsModel statisticsModel;

  /**
   * The setUp function is used to initialize the statisticsModel object before each test. This
   * ensures that each test starts with a fresh, empty model.
   */
  @BeforeEach
  void setUp() {
    statisticsModel = new StatisticsModel();
  }

  /**
   * The getEasyGameStartedPropertyProperty function returns the easyGameStartedProperty property.
   */
  @Test
  void getEasyGameStartedPropertyProperty() {
    assertEquals(
        getPropertyString(STATISTICS, "easyGameStarted"),
        statisticsModel.getEasyGameStartedPropertyProperty().getValue());
  }

  /** The getEasyGameWonPropertyProperty function returns the easyGameWonProperty property. */
  @Test
  void getEasyGameWonPropertyProperty() {
    assertEquals(
        getPropertyString(STATISTICS, "easyGameWon"),
        statisticsModel.getEasyGameWonPropertyProperty().getValue());
  }

  /**
   * The getEasyGameMistakesPropertyProperty function returns the easyGameMistakesProperty property.
   */
  @Test
  void getEasyGameMistakesPropertyProperty() {
    assertEquals(
        getPropertyString(STATISTICS, "easyGameMistakes"),
        statisticsModel.getEasyGameMistakesPropertyProperty().getValue());
  }

  /**
   * The getEasyGameTimePlayedPropertyProperty function returns the
   * easyGameTimePlayedPropertyProperty property of the statisticsModel.
   */
  @Test
  void getEasyGameTimePlayedPropertyProperty() {
    assertEquals(
        getPropertyString(STATISTICS, "easyGameTimePlayed"),
        statisticsModel.getEasyGameTimePlayedPropertyProperty().getValue());
  }

  /**
   * The getEasyGameBestTimePropertyProperty function returns the easyGameBestTimeProperty property.
   */
  @Test
  void getEasyGameBestTimePropertyProperty() {
    assertEquals(
        EMPTY_CLOCK_FORMAT, statisticsModel.getEasyGameBestTimePropertyProperty().getValue());
  }

  /**
   * The getMediumGameStartedPropertyProperty function returns the mediumGameStartedProperty
   * property.
   */
  @Test
  void getMediumGameStartedPropertyProperty() {
    assertEquals(
        getPropertyString(STATISTICS, "mediumGameStarted"),
        statisticsModel.getMediumGameStartedPropertyProperty().getValue());
  }

  /** The getMediumGameWonPropertyProperty function returns the mediumGameWonProperty property. */
  @Test
  void getMediumGameWonPropertyProperty() {
    assertEquals(
        getPropertyString(STATISTICS, "mediumGameWon"),
        statisticsModel.getMediumGameWonPropertyProperty().getValue());
  }

  /**
   * The getMediumGameMistakesPropertyProperty function returns the mediumGameMistakesProperty
   * property.
   */
  @Test
  void getMediumGameMistakesPropertyProperty() {
    assertEquals(
        getPropertyString(STATISTICS, "mediumGameMistakes"),
        statisticsModel.getMediumGameMistakesPropertyProperty().getValue());
  }

  /**
   * The getMediumGameTimePlayedPropertyProperty function returns the mediumGameTimePlayedProperty
   * property.
   */
  @Test
  void getMediumGameTimePlayedPropertyProperty() {
    assertEquals(
        getPropertyString(STATISTICS, "mediumGameTimePlayed"),
        statisticsModel.getMediumGameTimePlayedPropertyProperty().getValue());
  }

  /**
   * The getMediumGameBestTimePropertyProperty function returns the mediumGameBestTimeProperty
   * property.
   */
  @Test
  void getMediumGameBestTimePropertyProperty() {
    assertEquals(
        EMPTY_CLOCK_FORMAT, statisticsModel.getMediumGameBestTimePropertyProperty().getValue());
  }

  /**
   * The getHardGameStartedPropertyProperty function returns the hardGameStartedProperty property.
   */
  @Test
  void getHardGameStartedPropertyProperty() {
    assertEquals(
        getPropertyString(STATISTICS, "hardGameStarted"),
        statisticsModel.getHardGameStartedPropertyProperty().getValue());
  }

  /** The getHardGameWonPropertyProperty function returns the hardGameWonProperty property. */
  @Test
  void getHardGameWonPropertyProperty() {
    assertEquals(
        getPropertyString(STATISTICS, "hardGameWon"),
        statisticsModel.getHardGameWonPropertyProperty().getValue());
  }

  /**
   * The getHardGameMistakesPropertyProperty function returns the hardGameMistakesProperty property.
   */
  @Test
  void getHardGameMistakesPropertyProperty() {
    assertEquals(
        getPropertyString(STATISTICS, "hardGameMistakes"),
        statisticsModel.getHardGameMistakesPropertyProperty().getValue());
  }

  /**
   * The getHardGameTimePlayedPropertyProperty function returns the hardGameTimePlayedProperty
   * property.
   */
  @Test
  void getHardGameTimePlayedPropertyProperty() {
    assertEquals(
        getPropertyString(STATISTICS, "hardGameTimePlayed"),
        statisticsModel.getHardGameTimePlayedPropertyProperty().getValue());
  }

  /**
   * The getHardGameBestTimePropertyProperty function returns the hardGameBestTimeProperty property.
   */
  @Test
  void getHardGameBestTimePropertyProperty() {
    assertEquals(
        EMPTY_CLOCK_FORMAT, statisticsModel.getHardGameBestTimePropertyProperty().getValue());
  }
}

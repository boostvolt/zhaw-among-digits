package ch.zhaw.pm2.amongdigits.model;

import javafx.beans.property.SimpleStringProperty;
import java.util.Properties;
import static ch.zhaw.pm2.amongdigits.PropertyType.STATISTICS;
import static ch.zhaw.pm2.amongdigits.utils.PropertiesHandler.loadProperties;
import static ch.zhaw.pm2.amongdigits.utils.PropertiesHandler.storeProperties;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.CLOCK_FORMAT;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.EMPTY_CLOCK_FORMAT;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.SECOND_MINUTE_THRESHOLD;
import static java.lang.Integer.parseInt;

/** The StatisticsModel class is responsible for the statistics of the game. */
public class StatisticsModel {

  private final SimpleStringProperty beginnerGameStartedProperty = new SimpleStringProperty();
  private final SimpleStringProperty beginnerGameWonProperty = new SimpleStringProperty();
  private final SimpleStringProperty beginnerGameMistakesProperty = new SimpleStringProperty();
  private final SimpleStringProperty beginnerGameTimePlayedProperty = new SimpleStringProperty();
  private final SimpleStringProperty beginnerGameBestTimeProperty = new SimpleStringProperty();
  private final SimpleStringProperty easyGameStartedProperty = new SimpleStringProperty();
  private final SimpleStringProperty easyGameWonProperty = new SimpleStringProperty();
  private final SimpleStringProperty easyGameMistakesProperty = new SimpleStringProperty();
  private final SimpleStringProperty easyGameTimePlayedProperty = new SimpleStringProperty();
  private final SimpleStringProperty easyGameBestTimeProperty = new SimpleStringProperty();
  private final SimpleStringProperty mediumGameStartedProperty = new SimpleStringProperty();
  private final SimpleStringProperty mediumGameWonProperty = new SimpleStringProperty();
  private final SimpleStringProperty mediumGameMistakesProperty = new SimpleStringProperty();
  private final SimpleStringProperty mediumGameTimePlayedProperty = new SimpleStringProperty();
  private final SimpleStringProperty mediumGameBestTimeProperty = new SimpleStringProperty();
  private final SimpleStringProperty hardGameStartedProperty = new SimpleStringProperty();
  private final SimpleStringProperty hardGameWonProperty = new SimpleStringProperty();
  private final SimpleStringProperty hardGameMistakesProperty = new SimpleStringProperty();
  private final SimpleStringProperty hardGameTimePlayedProperty = new SimpleStringProperty();
  private final SimpleStringProperty hardGameBestTimeProperty = new SimpleStringProperty();
  private final SimpleStringProperty expertGameStartedProperty = new SimpleStringProperty();
  private final SimpleStringProperty expertGameWonProperty = new SimpleStringProperty();
  private final SimpleStringProperty expertGameMistakesProperty = new SimpleStringProperty();
  private final SimpleStringProperty expertGameTimePlayedProperty = new SimpleStringProperty();
  private final SimpleStringProperty expertGameBestTimeProperty = new SimpleStringProperty();

  public StatisticsModel() {
    updateStatistics();
  }

  private void updateStatistics() {
    Properties statistics = loadProperties(STATISTICS);
    beginnerGameMistakesProperty.set(statistics.getProperty("beginnerGameMistakes"));
    beginnerGameStartedProperty.set(statistics.getProperty("beginnerGameStarted"));
    beginnerGameBestTimeProperty.set(
        formatTime(parseInt(statistics.getProperty("beginnerGameBestTime"))));
    beginnerGameTimePlayedProperty.set(statistics.getProperty("beginnerGameTimePlayed"));
    beginnerGameWonProperty.set(statistics.getProperty("beginnerGameWon"));
    easyGameMistakesProperty.set(statistics.getProperty("easyGameMistakes"));
    easyGameStartedProperty.set(statistics.getProperty("easyGameStarted"));
    easyGameBestTimeProperty.set(formatTime(parseInt(statistics.getProperty("easyGameBestTime"))));
    easyGameTimePlayedProperty.set(statistics.getProperty("easyGameTimePlayed"));
    easyGameWonProperty.set(statistics.getProperty("easyGameWon"));
    mediumGameMistakesProperty.set(statistics.getProperty("mediumGameMistakes"));
    mediumGameStartedProperty.set(statistics.getProperty("mediumGameStarted"));
    mediumGameBestTimeProperty.set(
        formatTime(parseInt(statistics.getProperty("mediumGameBestTime"))));
    mediumGameTimePlayedProperty.set(statistics.getProperty("mediumGameTimePlayed"));
    mediumGameWonProperty.set(statistics.getProperty("mediumGameWon"));
    hardGameMistakesProperty.set(statistics.getProperty("hardGameMistakes"));
    hardGameStartedProperty.set(statistics.getProperty("hardGameStarted"));
    hardGameBestTimeProperty.set(formatTime(parseInt(statistics.getProperty("hardGameBestTime"))));
    hardGameTimePlayedProperty.set(statistics.getProperty("hardGameTimePlayed"));
    hardGameWonProperty.set(statistics.getProperty("hardGameWon"));
    expertGameMistakesProperty.set(statistics.getProperty("expertGameMistakes"));
    expertGameStartedProperty.set(statistics.getProperty("expertGameStarted"));
    expertGameBestTimeProperty.set(
        formatTime(parseInt(statistics.getProperty("expertGameBestTime"))));
    expertGameTimePlayedProperty.set(statistics.getProperty("expertGameTimePlayed"));
    expertGameWonProperty.set(statistics.getProperty("expertGameWon"));
    storeProperties(STATISTICS, statistics);
  }

  /**
   * The beginnerGameStartedPropertyProperty function returns the beginnerGameStartedProperty
   * property.
   */
  public SimpleStringProperty beginnerGameStartedPropertyProperty() {
    return beginnerGameStartedProperty;
  }

  /** The beginnerGameWonPropertyProperty function returns the beginnerGameWonProperty property. */
  public SimpleStringProperty beginnerGameWonPropertyProperty() {
    return beginnerGameWonProperty;
  }

  /**
   * The beginnerGameMistakesPropertyProperty function returns the beginnerGameMistakesProperty
   * property.
   */
  public SimpleStringProperty beginnerGameMistakesPropertyProperty() {
    return beginnerGameMistakesProperty;
  }

  /**
   * The beginnerGameTimePlayedPropertyProperty function returns the beginnerGameTimePlayedProperty
   * property.
   */
  public SimpleStringProperty beginnerGameTimePlayedPropertyProperty() {
    return beginnerGameTimePlayedProperty;
  }

  /**
   * The beginnerGameBestTimePropertyProperty function returns the beginnerGameBestTimeProperty
   * property.
   */
  public SimpleStringProperty beginnerGameBestTimePropertyProperty() {
    return beginnerGameBestTimeProperty;
  }

  /**
   * The getEasyGameStartedPropertyProperty function returns the easyGameStartedProperty property.
   */
  public SimpleStringProperty getEasyGameStartedPropertyProperty() {
    return easyGameStartedProperty;
  }

  /** The getEasyGameWonPropertyProperty function returns the easyGameWonProperty property. */
  public SimpleStringProperty getEasyGameWonPropertyProperty() {
    return easyGameWonProperty;
  }

  /**
   * The getEasyGameMistakesPropertyProperty function returns the easyGameMistakesProperty property.
   */
  public SimpleStringProperty getEasyGameMistakesPropertyProperty() {
    return easyGameMistakesProperty;
  }

  /**
   * The getEasyGameTimePlayedPropertyProperty function returns the easyGameTimePlayedProperty
   * property.
   */
  public SimpleStringProperty getEasyGameTimePlayedPropertyProperty() {
    return easyGameTimePlayedProperty;
  }

  /**
   * The getEasyGameBestTimePropertyProperty function returns the easyGameBestTimeProperty property.
   */
  public SimpleStringProperty getEasyGameBestTimePropertyProperty() {
    return easyGameBestTimeProperty;
  }

  /**
   * The getMediumGameStartedPropertyProperty function returns the mediumGameStartedProperty
   * property.
   */
  public SimpleStringProperty getMediumGameStartedPropertyProperty() {
    return mediumGameStartedProperty;
  }

  /** The getMediumGameWonPropertyProperty function returns the mediumGameWonProperty property. */
  public SimpleStringProperty getMediumGameWonPropertyProperty() {
    return mediumGameWonProperty;
  }

  /**
   * The getMediumGameMistakesPropertyProperty function returns the mediumGameMistakesProperty
   * property.
   */
  public SimpleStringProperty getMediumGameMistakesPropertyProperty() {
    return mediumGameMistakesProperty;
  }

  /**
   * The getMediumGameTimePlayedPropertyProperty function returns the mediumGameTimePlayedProperty
   * property.
   */
  public SimpleStringProperty getMediumGameTimePlayedPropertyProperty() {
    return mediumGameTimePlayedProperty;
  }

  /**
   * The getMediumGameBestTimePropertyProperty function returns the mediumGameBestTimeProperty
   * property.
   */
  public SimpleStringProperty getMediumGameBestTimePropertyProperty() {
    return mediumGameBestTimeProperty;
  }

  /**
   * The getHardGameStartedPropertyProperty function returns the hardGameStartedProperty property.
   */
  public SimpleStringProperty getHardGameStartedPropertyProperty() {
    return hardGameStartedProperty;
  }

  /** The getHardGameWonPropertyProperty function returns the hardGameWonProperty property. */
  public SimpleStringProperty getHardGameWonPropertyProperty() {
    return hardGameWonProperty;
  }

  /**
   * The getHardGameMistakesPropertyProperty function returns the hardGameMistakesProperty property.
   */
  public SimpleStringProperty getHardGameMistakesPropertyProperty() {
    return hardGameMistakesProperty;
  }

  /**
   * The getHardGameTimePlayedPropertyProperty function returns the hardGameTimePlayedProperty
   * property.
   */
  public SimpleStringProperty getHardGameTimePlayedPropertyProperty() {
    return hardGameTimePlayedProperty;
  }

  /**
   * The getHardGameBestTimePropertyProperty function returns the hardGameBestTimeProperty property.
   */
  public SimpleStringProperty getHardGameBestTimePropertyProperty() {
    return hardGameBestTimeProperty;
  }

  /**
   * The expertGameStartedPropertyProperty function returns the expertGameStartedProperty property.
   */
  public SimpleStringProperty expertGameStartedPropertyProperty() {
    return expertGameStartedProperty;
  }

  /** The expertGameWonPropertyProperty function returns the expertGameWonProperty property. */
  public SimpleStringProperty expertGameWonPropertyProperty() {
    return expertGameWonProperty;
  }

  /**
   * The expertGameMistakesPropertyProperty function returns the expertGameMistakesProperty
   * property.
   */
  public SimpleStringProperty expertGameMistakesPropertyProperty() {
    return expertGameMistakesProperty;
  }

  /**
   * The expertGameTimePlayedPropertyProperty function returns the expertGameTimePlayedProperty
   * property.
   */
  public SimpleStringProperty expertGameTimePlayedPropertyProperty() {
    return expertGameTimePlayedProperty;
  }

  /**
   * The expertGameBestTimePropertyProperty function returns the expertGameBestTimeProperty
   * property.
   */
  public SimpleStringProperty expertGameBestTimePropertyProperty() {
    return expertGameBestTimeProperty;
  }

  private String formatTime(int timeInSeconds) {
    if (timeInSeconds == 0) {
      return EMPTY_CLOCK_FORMAT;
    }
    int seconds = timeInSeconds % SECOND_MINUTE_THRESHOLD;
    int minutes = timeInSeconds / SECOND_MINUTE_THRESHOLD;
    return String.format(CLOCK_FORMAT, minutes, seconds);
  }
}

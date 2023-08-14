package ch.zhaw.pm2.amongdigits.model;

import static ch.zhaw.pm2.amongdigits.PropertyType.SETTINGS;
import static ch.zhaw.pm2.amongdigits.PropertyType.STATISTICS;
import static ch.zhaw.pm2.amongdigits.utils.PropertiesHandler.*;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.DISABLED_SYMBOL;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.ENABLED_SYMBOL;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.extern.slf4j.Slf4j;

/**
 * The SettingsModel class represents the model of the settings for the Sudoku game. It provides
 * methods to toggle different settings, update the corresponding properties and retrieve their
 * values. It also initializes the settings based on the stored configuration file.
 */
@Slf4j
public class SettingsModel {

  private static final String DARK_MODE_STRING = "darkMode";
  private static final String FALSE = "false";
  private static final String TRUE = "true";
  private final SimpleStringProperty darkModeProperty = new SimpleStringProperty();
  private final SimpleStringProperty checkMistakesProperty = new SimpleStringProperty();
  private final SimpleStringProperty checkTimeProperty = new SimpleStringProperty();
  private final SimpleStringProperty realtimeFeedbackProperty = new SimpleStringProperty();
  private final SimpleStringProperty darkModeValueProperty = new SimpleStringProperty();

  /**
   * Creates a new instance of SettingsModel and initializes the settings based on the stored
   * configuration file.
   */
  public SettingsModel() {
    initializeSettings();
  }

  /** Toggles the dark mode setting and updates the corresponding properties. */
  public void toggleDarkMode() {
    if (getSettingsPropertiesString(DARK_MODE_STRING).equals(FALSE)) {
      updateSettingsPropertiesString(DARK_MODE_STRING, TRUE);
    } else {
      updateSettingsPropertiesString(DARK_MODE_STRING, FALSE);
    }
    updateDarkModeProperties();
  }

  /** Toggles the check mistakes setting and updates the corresponding property. */
  public void toggleCheckMistakes() {
    String mistakes = "checkMistakes";
    if (getSettingsPropertiesString(mistakes).equals(TRUE)) {
      updateSettingsPropertiesString(mistakes, FALSE);
    } else {
      updateSettingsPropertiesString(mistakes, TRUE);
    }
    updateCheckMistakesProperty();
  }

  /** Toggles the check time setting. */
  public void toggleCheckTime() {
    String time = "checkTime";
    if (getSettingsPropertiesString(time).equals(TRUE)) {
      updateSettingsPropertiesString(time, FALSE);
    } else {
      updateSettingsPropertiesString(time, TRUE);
    }
    updateCheckTimeProperty();
  }

  /** Toggles the realtime feedback setting. */
  public void toggleRealtimeFeedback() {
    String realtimeFeedback = "realtimeFeedback";
    if (getSettingsPropertiesString(realtimeFeedback).equals(TRUE)) {
      updateSettingsPropertiesString(realtimeFeedback, FALSE);
    } else {
      updateSettingsPropertiesString(realtimeFeedback, TRUE);
    }
    updateRealtimeFeedbackProperty();
  }

  /** Resets the statistics. */
  public void resetStatistics() {
    try {
      Files.copy(
          Objects.requireNonNull(
              getClass().getResourceAsStream("/properties/defaultStatistics.properties")),
          new File(STATISTICS.getFileName()).toPath(),
          REPLACE_EXISTING);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  /**
   * Changes the language.
   *
   * @param newLanguage the new language
   */
  public void changeLanguage(String newLanguage) {
    String language = "language";
    updateSettingsPropertiesString(language, newLanguage);
  }

  /**
   * Gets the dark mode property.
   *
   * @return the dark mode property
   */
  public StringProperty getDarkModeProperty() {
    return darkModeProperty;
  }

  /**
   * Gets the check mistakes property.
   *
   * @return the check mistakes property
   */
  public StringProperty getCheckMistakesProperty() {
    return checkMistakesProperty;
  }

  /**
   * Gets the check time property.
   *
   * @return the check time property
   */
  public StringProperty getCheckTimeProperty() {
    return checkTimeProperty;
  }

  /**
   * Gets the realtime feedback property.
   *
   * @return the realtime feedback property
   */
  public StringProperty getRealtimeFeedbackProperty() {
    return realtimeFeedbackProperty;
  }

  /**
   * Gets the dark mode value property.
   *
   * @return the dark mode value property
   */
  public StringProperty getDarkModeValueProperty() {
    return darkModeValueProperty;
  }

  private void updateDarkModeProperties() {
    String cssFile;
    if (getSettingsPropertiesString(DARK_MODE_STRING).equals(TRUE)) {
      darkModeProperty.setValue(ENABLED_SYMBOL);
      cssFile = "css/darkMode.css";
    } else {
      darkModeProperty.setValue(DISABLED_SYMBOL);
      cssFile = "css/lightMode.css";
    }
    updatePropertyString(SETTINGS, "cssFileString", "/" + cssFile);
    darkModeValueProperty.setValue(cssFile);
  }

  private void updateCheckMistakesProperty() {
    if (getSettingsPropertiesString("checkMistakes").equals("true")) {
      checkMistakesProperty.setValue(ENABLED_SYMBOL);
    } else {
      checkMistakesProperty.setValue(DISABLED_SYMBOL);
    }
  }

  private void updateCheckTimeProperty() {
    if (getSettingsPropertiesString("checkTime").equals("true")) {
      checkTimeProperty.setValue(ENABLED_SYMBOL);
    } else {
      checkTimeProperty.setValue(DISABLED_SYMBOL);
    }
  }

  private void updateRealtimeFeedbackProperty() {
    if (getSettingsPropertiesString("realtimeFeedback").equals("true")) {
      realtimeFeedbackProperty.setValue(ENABLED_SYMBOL);
    } else {
      realtimeFeedbackProperty.setValue(DISABLED_SYMBOL);
    }
  }

  private void initializeSettings() {
    updateDarkModeProperties();
    updateCheckMistakesProperty();
    updateCheckTimeProperty();
    updateRealtimeFeedbackProperty();
  }

  private String getSettingsPropertiesString(String key) {
    return getPropertyString(SETTINGS, key);
  }

  private void updateSettingsPropertiesString(String key, String value) {
    updatePropertyString(SETTINGS, key, value);
  }
}

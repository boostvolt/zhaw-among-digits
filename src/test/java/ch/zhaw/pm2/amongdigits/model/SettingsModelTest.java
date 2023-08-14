package ch.zhaw.pm2.amongdigits.model;

import static ch.zhaw.pm2.amongdigits.PropertyType.SETTINGS;
import static ch.zhaw.pm2.amongdigits.utils.PropertiesHandler.getPropertyString;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.DISABLED_SYMBOL;
import static ch.zhaw.pm2.amongdigits.utils.SudokuConstants.ENABLED_SYMBOL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.beans.property.StringProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the SettingsModel class.
 *
 * <p>The SettingsModelTest class contains unit tests to verify the functionality of the
 * SettingsModel class. It tests the toggling of various settings, changing the language, and
 * retrieving properties related to settings.
 */
class SettingsModelTest {

  private SettingsModel settingsModel;

  /** Sets up the test environment before each test method is executed. */
  @BeforeEach
  void setUp() {
    settingsModel = new SettingsModel();
  }

  /** Tests toggling the dark mode setting and verifies that the value has changed. */
  @Test
  void toggleDarkMode() {
    String darkModeBefore = getPropertyString(SETTINGS, "darkMode");
    settingsModel.toggleDarkMode();
    assertNotEquals(darkModeBefore, getPropertyString(SETTINGS, "darkMode"));
  }

  /** Tests toggling the check mistakes setting and verifies that the value has changed. */
  @Test
  void toggleCheckMistakes() {
    String showMistakesBefore = getPropertyString(SETTINGS, "checkMistakes");
    settingsModel.toggleCheckMistakes();
    assertNotEquals(showMistakesBefore, getPropertyString(SETTINGS, "showMistakes"));
  }

  /** Tests toggling the check time setting and verifies that the value has changed. */
  @Test
  void toggleCheckTime() {
    String showTimeBefore = getPropertyString(SETTINGS, "checkTime");
    settingsModel.toggleCheckTime();
    assertNotEquals(showTimeBefore, getPropertyString(SETTINGS, "showTime"));
  }

  /** Tests toggling the realtime feedback setting and verifies that the value has changed. */
  @Test
  void toggleRealtimeFeedback() {
    String realtimeFeedbackBefore = getPropertyString(SETTINGS, "realtimeFeedback");
    settingsModel.toggleRealtimeFeedback();
    assertNotEquals(realtimeFeedbackBefore, getPropertyString(SETTINGS, "realtimeFeedback"));
  }

  /** Tests changing the language setting and verifies that the value has changed accordingly. */
  @Test
  void changeLanguage() {
    settingsModel.changeLanguage("de");
    assertEquals("de", getPropertyString(SETTINGS, "language"));
    settingsModel.changeLanguage("en");
    assertNotEquals("de", getPropertyString(SETTINGS, "language"));
  }

  /** Tests getting the dark mode property and verifies its value. */
  @Test
  void getDarkModeProperty() {
    if (getPropertyString(SETTINGS, "darkMode").equals("true")) {
      assertTrue(settingsModel.getDarkModeProperty().getValue().contains(ENABLED_SYMBOL));
    } else {
      assertTrue(settingsModel.getDarkModeProperty().getValue().contains(DISABLED_SYMBOL));
    }
  }

  /** Tests getting the check mistakes property and verifies its value. */
  @Test
  void getCheckMistakesProperty() {
    if (getPropertyString(SETTINGS, "checkMistakes").equals("true")) {
      assertTrue(settingsModel.getCheckMistakesProperty().getValue().contains(ENABLED_SYMBOL));
    } else {
      assertTrue(settingsModel.getCheckMistakesProperty().getValue().contains(DISABLED_SYMBOL));
    }
  }

  /** Tests getting the check time property and verifies its value. */
  @Test
  void getCheckTimeProperty() {
    if (getPropertyString(SETTINGS, "checkTime").equals("true")) {
      assertTrue(settingsModel.getCheckTimeProperty().getValue().contains(ENABLED_SYMBOL));
    } else {
      assertTrue(settingsModel.getCheckTimeProperty().getValue().contains(DISABLED_SYMBOL));
    }
  }

  /** Tests getting the realtime feedback property and verifies its value. */
  @Test
  void getRealtimeFeedbackProperty() {
    if (getPropertyString(SETTINGS, "realtimeFeedback").equals("true")) {
      assertTrue(settingsModel.getRealtimeFeedbackProperty().getValue().contains(ENABLED_SYMBOL));
    } else {
      assertTrue(settingsModel.getRealtimeFeedbackProperty().getValue().contains(DISABLED_SYMBOL));
    }
  }

  /**
   * Tests getting the dark mode value property and verifies its value after toggling the dark mode
   * setting.
   */
  @Test
  void getDarkModeValueProperty() {
    StringProperty darkModeValueProperty = settingsModel.getDarkModeValueProperty();
    for (int i = 0; i < 3; i++) {
      settingsModel.toggleDarkMode();
      if (getPropertyString(SETTINGS, "darkMode").equals("true")) {
        assertTrue(darkModeValueProperty.getValue().contains("darkMode"));
      } else {
        assertTrue(darkModeValueProperty.getValue().contains("lightMode"));
      }
    }
  }
}

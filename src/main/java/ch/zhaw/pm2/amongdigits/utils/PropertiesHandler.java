package ch.zhaw.pm2.amongdigits.utils;

import static java.lang.String.format;

import ch.zhaw.pm2.amongdigits.PropertyType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

/** Utility class for handling properties files. */
@Slf4j
public final class PropertiesHandler {

  private static final String FILE_NOT_FOUND = "File not found: %S";
  private static final String ERROR_READING_FILE = "Error reading File: %S";

  private PropertiesHandler() {
    throw new UnsupportedOperationException("Utility class");
  }

  /**
   * Retrieves the value of a specific property from the given property type.
   *
   * @param propertyType The type of the property file.
   * @param key The key of the property.
   * @return The value of the specified property, or null if not found.
   */
  public static String getPropertyString(PropertyType propertyType, String key) {
    return loadProperties(propertyType).getProperty(key);
  }

  /**
   * Updates the value of a specific property in the given property type.
   *
   * @param propertyType The type of the property file.
   * @param key The key of the property.
   * @param newValue The new value for the property.
   */
  public static void updatePropertyString(PropertyType propertyType, String key, String newValue) {
    testPropertiesSetup(propertyType);
    Properties updateProperty = loadProperties(propertyType);
    updateProperty.setProperty(key, newValue);
    storeProperties(propertyType, updateProperty);
  }

  /**
   * Stores the properties to the specified property file.
   *
   * @param propertyType The type of the property file.
   * @param property The properties to be stored.
   */
  public static void storeProperties(PropertyType propertyType, Properties property) {
    testPropertiesSetup(propertyType);
    try (FileWriter fileWriter = new FileWriter(propertyType.getFileName())) {
      property.store(fileWriter, null);
    } catch (FileNotFoundException e) {
      log.error(format(FILE_NOT_FOUND, e.getMessage()));
    } catch (IOException e) {
      log.error(format(ERROR_READING_FILE, e.getMessage()));
    }
  }

  /**
   * Loads the properties from the specified property file.
   *
   * @param propertyType The type of the property file.
   * @return The loaded properties.
   */
  public static Properties loadProperties(PropertyType propertyType) {
    testPropertiesSetup(propertyType);
    Properties loadedProperty = new Properties();
    try (FileInputStream fileInputStream = new FileInputStream(propertyType.getFileName())) {
      loadedProperty.load(fileInputStream);
    } catch (FileNotFoundException e) {
      log.error(format(FILE_NOT_FOUND, e.getMessage()));
    } catch (IOException e) {
      log.error(format(ERROR_READING_FILE, e.getMessage()));
    }
    return loadedProperty;
  }

  private static void testPropertiesSetup(PropertyType propertyType) {
    File settingsFile = new File(propertyType.getFileName());
    if (!settingsFile.exists()) {
      setupProperties(propertyType, settingsFile);
    }
  }

  private static void setupProperties(PropertyType propertyType, File settingsFile) {
    Properties property = new Properties();
    File defaultSettingsFile =
        new File((settingsFile.getParentFile() + "/default" + settingsFile.getName()));
    try (FileInputStream fileInputStream = new FileInputStream(defaultSettingsFile.toString());
        FileWriter fileWriter = new FileWriter(propertyType.getFileName())) {
      property.load(fileInputStream);
      property.store(fileWriter, null);
    } catch (FileNotFoundException e) {
      log.error(format(FILE_NOT_FOUND, e.getMessage()));
    } catch (IOException e) {
      log.error(format(ERROR_READING_FILE, e.getMessage()));
    }
  }
}

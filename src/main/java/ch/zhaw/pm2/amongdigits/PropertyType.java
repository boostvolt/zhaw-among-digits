package ch.zhaw.pm2.amongdigits;

/** An enumeration representing the types of property files. */
public enum PropertyType {
  /** The SETTINGS property type. */
  SETTINGS("src/main/resources/properties/settings.properties"),

  /** The STATISTICS property type. */
  STATISTICS("src/main/resources/properties/statistics.properties");

  private final String fileName;

  /**
   * Constructs a new PropertyType with the specified file name.
   *
   * @param fileName The file name associated with the property type.
   */
  PropertyType(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Returns the file name associated with the property type.
   *
   * @return The file name.
   */
  public String getFileName() {
    return fileName;
  }
}

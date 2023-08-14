package ch.zhaw.pm2.amongdigits.utils.schema;

import java.util.List;

/** This class provides a set of predefined {@link Schema} types. */
public final class SchemaTypes {

  /** A 9x9 {@link Schema} type. */
  public static final Schema SCHEMA_9X9 = new SchemaManager((byte) 0, (byte) 1, (byte) 9, 9, 3);

  private SchemaTypes() {
    throw new UnsupportedOperationException("Utility class");
  }

  /**
   * Returns a list of all available {@link Schema} types.
   *
   * @return a list of all available {@link Schema} types
   */
  public static List<Schema> getSchemaTypes() {
    return List.of(SCHEMA_9X9);
  }
}

package ch.zhaw.pm2.amongdigits.utils.schema;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** A set of tests for {@link SchemaManager}. */
class SchemaManagerTest {

  private static Stream<Arguments> allSchemas() {
    return SchemaTypes.getSchemaTypes().stream().map(Arguments::of);
  }

  /**
   * Tests that a bit mask with a value of 0 is valid for a given schema.
   *
   * @param schema the schema to test.
   */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidBitMaskWithZero(Schema schema) {
    assertTrue(schema.isBitMaskValid(0));
  }

  /**
   * Tests that a bit mask with a single bit set is valid for a given schema.
   *
   * @param schema the schema to test.
   */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidBitMaskWithSingleBit(Schema schema) {
    for (int i = schema.getMinimumValue(); i <= schema.getMaximumValue(); i++) {
      assertTrue(schema.isBitMaskValid(1 << i));
    }
  }

  /**
   * Tests that a bit mask with all bits set is valid for a given schema.
   *
   * @param schema the schema to test.
   */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidBitMaskWithAllBits(Schema schema) {
    int bitMask = 0;
    for (int i = schema.getMinimumValue(); i <= schema.getMaximumValue(); i++) {
      bitMask = bitMask | (1 << i);
    }
    assertTrue(schema.isBitMaskValid(bitMask));
  }

  /**
   * Tests that a bit mask with an invalid zero bit is not valid for a given schema.
   *
   * @param schema the schema to test.
   */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidBitMaskWithInvalidZeroBit(Schema schema) {
    assertFalse(schema.isBitMaskValid(1));
  }

  /**
   * Tests that a bit mask with a bit that exceeds the schema width is not valid for a given schema.
   *
   * @param schema the schema to test.
   */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidBitMaskWithInvalidWidthBit(Schema schema) {
    assertFalse(schema.isBitMaskValid(1 << schema.getWidth() + 1));
  }

  /**
   * Tests that row and column coordinates that are too big are not valid for a given schema.
   *
   * @param schema the schema to test.
   */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidCoordsWithRowAndColumnTooBig(Schema schema) {
    assertFalse(schema.areCoordsValid(schema.getWidth(), schema.getWidth()));
  }

  /**
   * Tests that a row coordinate that is too big is not valid for a given schema.
   *
   * @param schema the schema to test.
   */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidCoordsWithRowTooBig(Schema schema) {
    assertFalse(schema.areCoordsValid(schema.getWidth(), 0));
  }

  /** Parameterized tests for validating coordinates and values in a {@link Schema}. */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidCoordsWithColumnTooBig(Schema schema) {
    assertFalse(schema.areCoordsValid(0, schema.getWidth()));
  }

  /** Parameterized tests for validating coordinates and values in a {@link Schema}. */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidCoordsWithRowAndColumnTooSmall(Schema schema) {
    assertFalse(schema.areCoordsValid(-1, -1));
  }

  /** Parameterized tests for validating coordinates and values in a {@link Schema}. */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidCoordsWithRowTooSmall(Schema schema) {
    assertFalse(schema.areCoordsValid(-1, 0));
  }

  /** Parameterized tests for validating coordinates and values in a {@link Schema}. */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidCoordsWithColumnTooSmall(Schema schema) {
    assertFalse(schema.areCoordsValid(0, -1));
  }

  /** Parameterized tests for validating coordinates and values in a {@link Schema}. */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidCoordsWithRowAndColumnZero(Schema schema) {
    assertTrue(schema.areCoordsValid(0, 0));
  }

  /** Parameterized tests for validating coordinates and values in a {@link Schema}. */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidCoordsWithRowAndColumnMax(Schema schema) {
    assertTrue(schema.areCoordsValid(schema.getWidth() - 1, schema.getWidth() - 1));
  }

  /** Parameterized tests for validating coordinates and values in a {@link Schema}. */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidCoordsWithRowAndColumnMin(Schema schema) {
    assertTrue(schema.areCoordsValid(schema.getMinimumValue(), schema.getMinimumValue()));
  }

  /** Parameterized tests for validating coordinates and values in a {@link Schema}. */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidValueWithMax(Schema schema) {
    assertTrue(schema.isValueValid(schema.getMaximumValue()));
  }

  /** Parameterized tests for validating coordinates and values in a {@link Schema}. */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidValueWithMin(Schema schema) {
    assertTrue(schema.isValueValid(schema.getMinimumValue()));
  }

  /** Parameterized tests for validating coordinates and values in a {@link Schema}. */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidValueWithTooBig(Schema schema) {
    assertFalse(schema.isValueValid((byte) (schema.getMaximumValue() + 1)));
  }

  /** Parameterized tests for validating coordinates and values in a {@link Schema}. */
  @ParameterizedTest
  @MethodSource("allSchemas")
  void testValidValueWithTooSmall(Schema schema) {
    assertFalse(schema.isValueValid((byte) (schema.getUnsetValue() - 1)));
  }
}

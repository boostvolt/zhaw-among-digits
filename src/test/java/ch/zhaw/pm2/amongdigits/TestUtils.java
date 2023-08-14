package ch.zhaw.pm2.amongdigits;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The TestUtils class provides utility methods for converting a byte array to a sorted list of
 * integers.
 */
public class TestUtils {

  private TestUtils() {
    throw new UnsupportedOperationException("Utility class");
  }

  /**
   * Converts a byte array to a sorted list of integers.
   *
   * @param array the byte array to convert.
   * @return a sorted list of integers.
   */
  public static List<Integer> toIntList(byte[] array) {
    int[] intArray = toIntArray(array);

    return IntStream.of(intArray).boxed().sorted().collect(Collectors.toList());
  }

  /**
   * Converts a byte array to an array of integers.
   *
   * @param array the byte array to convert.
   * @return an array of integers.
   */
  private static int[] toIntArray(byte[] array) {
    int[] intArray = new int[array.length];
    for (int i = 0; i < array.length; i++) {
      intArray[i] = Byte.toUnsignedInt(array[i]);
    }

    return intArray;
  }
}

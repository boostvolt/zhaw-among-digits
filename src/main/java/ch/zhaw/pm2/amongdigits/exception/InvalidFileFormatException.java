package ch.zhaw.pm2.amongdigits.exception;

/** Exception thrown when attempting to read a file with an invalid format. */
public class InvalidFileFormatException extends Exception {

  /**
   * Constructs a new InvalidFileFormatException with the specified detail message.
   *
   * @param message the detail message.
   */
  public InvalidFileFormatException(final String message) {
    super(message);
  }

  /**
   * Constructs a new InvalidFileFormatException with the specified detail message and cause.
   *
   * @param message the detail message.
   * @param cause the cause of the exception.
   */
  public InvalidFileFormatException(final String message, final Throwable cause) {
    super(message, cause);
  }
}

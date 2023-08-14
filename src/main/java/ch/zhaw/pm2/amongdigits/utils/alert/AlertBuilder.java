package ch.zhaw.pm2.amongdigits.utils.alert;

import static ch.zhaw.pm2.amongdigits.PropertyType.SETTINGS;
import static ch.zhaw.pm2.amongdigits.utils.PropertiesHandler.getPropertyString;

import java.util.Objects;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;

/** This class provides a utility to build and show JavaFX Alert dialogs with custom options. */
public class AlertBuilder {

  private static final Integer DEFAULT_SIZE = 200;

  private AlertBuilder() {
    throw new UnsupportedOperationException("Utility class");
  }

  /**
   * Private constructor to prevent instantiation of this utility class.
   *
   * @throws UnsupportedOperationException if called
   */
  public static Optional<ButtonType> showAlert(AlertOptions alertRecord) {
    final Alert alert = new Alert(alertRecord.type());
    alert
        .getDialogPane()
        .getStylesheets()
        .add(
            Objects.requireNonNull(
                Objects.requireNonNull(
                        AlertBuilder.class.getResource(
                            getPropertyString(SETTINGS, "cssFileString")))
                    .toString()));
    alert.setTitle(alertRecord.title());
    alert.setHeaderText(alertRecord.header());
    alert.contentTextProperty().set(alertRecord.content());
    if (alertRecord.buttons() != null && !alertRecord.buttons().isEmpty()) {
      alert.getButtonTypes().setAll(alertRecord.buttons());
    }
    if (alertRecord.image() != null) {
      final ImageView imageView = new ImageView(alertRecord.image());
      imageView.setFitWidth(DEFAULT_SIZE);
      imageView.setFitHeight(DEFAULT_SIZE);
      alert.setGraphic(imageView);
    }
    return alert.showAndWait();
  }
}

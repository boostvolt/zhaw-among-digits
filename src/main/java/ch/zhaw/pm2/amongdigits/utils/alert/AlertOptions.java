package ch.zhaw.pm2.amongdigits.utils.alert;

import java.util.Set;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;

/**
 * A record that represents the options to be used when displaying an alert. The alert can have a
 * type, a title, a header, a content, an image, and a set of buttons.
 */
public record AlertOptions(
    Alert.AlertType type,
    String title,
    String header,
    String content,
    Image image,
    Set<ButtonType> buttons) {}

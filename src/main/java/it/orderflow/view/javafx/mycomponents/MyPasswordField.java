package it.orderflow.view.javafx.mycomponents;

import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class MyPasswordField extends VBox {

    PasswordField passwordField;
    Label floatingLabel;

    public MyPasswordField() {
        super();

        passwordField = new PasswordField();

        passwordField.setMinHeight(40);
        passwordField.setMaxWidth(Double.MAX_VALUE);

        floatingLabel = new Label("Password");

        floatingLabel.setTextFill(Color.GRAY);
        floatingLabel.setMouseTransparent(true);

        StackPane container = new StackPane();
        container.getChildren().addAll(passwordField, floatingLabel);
        StackPane.setAlignment(floatingLabel, Pos.CENTER_LEFT);
        floatingLabel.setTranslateX(10);

        passwordField.focusedProperty().addListener((obs, oldVal, isFocused) -> {
            moveLabel(floatingLabel, isFocused || !passwordField.getText().isEmpty());
            if (isFocused) {
                floatingLabel.setTextFill(Paint.valueOf("#006FFF"));
            } else {
                floatingLabel.setTextFill(Color.GRAY);
            }
        });

        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!passwordField.isFocused()) {
                moveLabel(floatingLabel, !newVal.isEmpty());
            }
        });

        this.getChildren().add(container);
    }

    private void moveLabel(Label label, boolean up) {
        if (up) {
            label.setTranslateY(-25);
        } else {
            label.setTranslateY(0);
        }
    }

    public String getText() {
        return passwordField.getText();
    }

    public StringProperty textProperty() {
        return passwordField.textProperty();
    }
}

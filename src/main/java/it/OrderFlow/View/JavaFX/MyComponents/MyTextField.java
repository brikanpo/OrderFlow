package it.OrderFlow.View.JavaFX.MyComponents;

import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class MyTextField extends VBox {

    TextField textField;
    Label floatingLabel;

    public MyTextField(String promptText) {
        super();

        textField = new TextField();

        textField.setMinHeight(40);
        textField.setMaxWidth(Double.MAX_VALUE);

        floatingLabel = new Label(promptText);

        floatingLabel.setTextFill(Color.GRAY);
        floatingLabel.setMouseTransparent(true);

        StackPane container = new StackPane();
        container.getChildren().addAll(textField, floatingLabel);
        StackPane.setAlignment(floatingLabel, Pos.CENTER_LEFT);
        floatingLabel.setTranslateX(10);

        textField.focusedProperty().addListener((obs, oldVal, isFocused) -> {
            moveLabel(floatingLabel, isFocused || !textField.getText().isEmpty());
            if (isFocused) {
                floatingLabel.setTextFill(Paint.valueOf("#006FFF"));
            } else {
                floatingLabel.setTextFill(Color.GRAY);
            }
        });

        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!textField.isFocused()) {
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
        return textField.getText();
    }

    public StringProperty textProperty() {
        return textField.textProperty();
    }
}

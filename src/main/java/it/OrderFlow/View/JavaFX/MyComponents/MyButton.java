package it.OrderFlow.View.JavaFX.MyComponents;

import javafx.scene.Cursor;
import javafx.scene.control.Button;

public class MyButton extends Button {

    public MyButton(String string) {
        super(string);
        this.setMinHeight(40);
        this.setMaxWidth(Double.MAX_VALUE);
        this.setCursor(Cursor.HAND);
        this.setStyle("-fx-background-color: #006FFF; " +
                "-fx-text-fill: white; -fx-font-weight: bold;");
        this.setOnMousePressed(event -> this.setStyle("-fx-background-color: #0078D4; " +
                "-fx-text-fill: white; -fx-font-weight: bold;"));
        this.setOnMouseReleased(event -> this.setStyle("-fx-background-color: #006FFF; " +
                "-fx-text-fill: white; -fx-font-weight: bold;"));
    }
}

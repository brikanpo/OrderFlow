package it.orderflow.view.javafx.mycomponents;

import javafx.scene.Cursor;
import javafx.scene.control.Button;

public class MyButton extends Button {

    public MyButton(String string) {
        super(string);
        this.setMinHeight(40);
        this.setMaxWidth(Double.MAX_VALUE);
        this.setCursor(Cursor.HAND);
        String style = "-fx-text-fill: white; -fx-font-weight: bold;";
        this.setStyle("-fx-background-color: #006FFF; " + style);
        this.setOnMousePressed(event -> this.setStyle("-fx-background-color: #0078D4; " + style));
        this.setOnMouseReleased(event -> this.setStyle("-fx-background-color: #006FFF; " + style));
    }
}

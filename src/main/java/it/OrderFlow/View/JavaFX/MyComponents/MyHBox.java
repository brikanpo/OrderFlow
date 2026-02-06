package it.OrderFlow.View.JavaFX.MyComponents;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class MyHBox extends HBox {

    public MyHBox() {
        super(15);
        this.setAlignment(Pos.CENTER);
        this.setMaxWidth(450);
        this.setStyle("-fx-background-color: #ffffff;");
    }
}

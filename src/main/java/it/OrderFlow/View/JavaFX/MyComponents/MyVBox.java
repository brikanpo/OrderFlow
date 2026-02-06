package it.OrderFlow.View.JavaFX.MyComponents;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class MyVBox extends VBox {

    public MyVBox() {
        super(20);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(40));
        this.setMaxWidth(450);
        this.setStyle("-fx-background-color: #ffffff;");
    }
}

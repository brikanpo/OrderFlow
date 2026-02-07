package it.orderflow.view.javafx.mycomponents;

import javafx.scene.control.Alert;

public class MySuccessMessage extends Alert {

    public MySuccessMessage(String message) {
        super(AlertType.INFORMATION);
        this.setTitle("Success");
        this.setHeaderText("Success");
        this.setContentText(message);
    }
}

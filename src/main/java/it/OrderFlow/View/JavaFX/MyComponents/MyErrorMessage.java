package it.OrderFlow.View.JavaFX.MyComponents;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MyErrorMessage extends Alert {

    public MyErrorMessage(Exception e) {
        super(AlertType.ERROR);
        this.setHeaderText("Something went wrong");
        this.setContentText(e.getMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        TextArea textArea = new TextArea(sw.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);

        this.getDialogPane().setExpandableContent(textArea);
    }
}

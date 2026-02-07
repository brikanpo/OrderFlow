package it.orderflow.view.javafx.mycomponents;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MyTitle extends Text {

    public MyTitle() {
        this(null);
    }

    public MyTitle(String title) {
        super(title);
        this.setFont(Font.font("System", FontWeight.BOLD, 26));
    }
}

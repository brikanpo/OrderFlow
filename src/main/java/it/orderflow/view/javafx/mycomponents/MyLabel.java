package it.orderflow.view.javafx.mycomponents;

import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class MyLabel extends Label {

    public MyLabel() {
        this(null);
    }

    public MyLabel(String text) {
        super(text);
        this.setWrapText(true);
        this.setMinHeight(Region.USE_PREF_SIZE);
    }
}

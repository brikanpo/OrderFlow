package it.OrderFlow.View.JavaFX.MyComponents;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class MyScene extends Scene {

    public MyScene(Parent parent, MyNavigationBar myNavigationBar) {
        super(MyContainer.getContainer(parent, myNavigationBar), 450, 600);
    }
}

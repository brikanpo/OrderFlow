package it.orderflow.view.javafx.mycomponents;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MyContainer {

    private MyContainer() {
        //This class should not be instantiated
    }

    public static BorderPane getContainer(Node node, MyNavigationBar myNavigationBar) {

        StackPane stackPane = new StackPane(node);
        stackPane.setStyle(node.getStyle());

        ScrollPane scrollPane = new ScrollPane(stackPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        BorderPane root = new BorderPane();
        root.setCenter(scrollPane);

        if (myNavigationBar != null) {
            StackPane stackPane1 = new StackPane(myNavigationBar);
            stackPane1.setStyle(node.getStyle());

            root.setBottom(stackPane1);

        }
        return root;
    }
}

package it.OrderFlow.View.JavaFX.MyComponents;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class MyNavigationBar extends MyHBox {

    private final MyButton logout;
    private final MyButton exit;
    private MyButton home;

    public MyNavigationBar() {
        super();
        this.setPadding(new Insets(40));

        home = new MyButton("Home");
        logout = new MyButton("Logout");
        exit = new MyButton("Exit");

        HBox.setHgrow(home, Priority.ALWAYS);
        HBox.setHgrow(logout, Priority.ALWAYS);
        HBox.setHgrow(exit, Priority.ALWAYS);

        this.getChildren().addAll(home, logout, exit);
    }

    public MyButton getHomeButton() {
        return this.home;
    }

    public MyButton getLogoutButton() {
        return this.logout;
    }

    public MyButton getExitButton() {
        return this.exit;
    }

    public void removeHomeButton() {
        if (this.getChildren().contains(this.home)) {
            this.getChildren().removeFirst();
            this.home = null;
        }
    }
}

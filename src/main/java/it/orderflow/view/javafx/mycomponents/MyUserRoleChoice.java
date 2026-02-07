package it.orderflow.view.javafx.mycomponents;

import it.orderflow.model.UserRole;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class MyUserRoleChoice extends MyHBox {

    private final ChoiceBox<String> userRoleChoice;

    public MyUserRoleChoice() {
        super();

        MyLabel userRoleLabel = new MyLabel("User role: ");

        userRoleChoice = new ChoiceBox<>();

        for (UserRole userRole : UserRole.values()) {
            userRoleChoice.getItems().add(userRole.toString());
        }

        HBox.setHgrow(userRoleLabel, Priority.ALWAYS);
        HBox.setHgrow(userRoleChoice, Priority.ALWAYS);

        this.getChildren().addAll(userRoleLabel, userRoleChoice);
    }

    public String getValue() {
        return this.userRoleChoice.getValue();
    }

    public ObjectProperty<String> valueProperty() {
        return this.userRoleChoice.valueProperty();
    }
}

package it.OrderFlow.View.JavaFX.MyComponents;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.List;

public class MyAttrCreationContainer extends MyVBox {

    private final List<MyTextField> attrNames = new ArrayList<>();
    private final List<MyTextField> attrValues = new ArrayList<>();

    public MyAttrCreationContainer(boolean type) {
        super();
        this.setPadding(new Insets(0));

        this.getChildren().add(this.createRow(type));
    }

    private MyHBox createRow(boolean type) {
        MyHBox row = new MyHBox();

        MyTextField attrName = new MyTextField("* Property name");
        attrNames.add(attrName);

        HBox.setHgrow(attrName, Priority.ALWAYS);

        MyButton addButton = new MyButton("+");
        addButton.setMaxWidth(20);
        MyButton removeButton = new MyButton("-");
        removeButton.setMaxWidth(20);

        addButton.setOnAction(e -> {
            int currentIndex = this.getChildren().indexOf(row);
            this.getChildren().add(currentIndex + 1, this.createRow(type));
        });
        removeButton.setOnAction(e -> {
            if (this.getChildren().size() > 1) {
                this.getChildren().remove(row);
            }
        });

        if (type) {
            MyTextField attrValue = new MyTextField("* Property value");
            attrValues.add(attrValue);

            HBox.setHgrow(attrValue, Priority.ALWAYS);

            row.getChildren().addAll(attrName, attrValue, addButton, removeButton);
        } else {
            row.getChildren().addAll(attrName, addButton, removeButton);
        }

        return row;
    }

    public List<String> getNames() {
        List<String> result = new ArrayList<>();
        for (MyTextField mtf : this.attrNames) {
            result.add(mtf.getText());
        }
        return result;
    }

    public List<String> getValues() {
        List<String> result = new ArrayList<>();
        for (MyTextField mtf : this.attrValues) {
            result.add(mtf.getText());
        }
        return result;
    }
}

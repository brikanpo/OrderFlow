package it.orderflow.view.javafx.mycomponents;

import javafx.geometry.Insets;

import java.util.ArrayList;
import java.util.List;

public class MyAttrValueFiller extends MyVBox {

    private final List<MyTextField> attrValues = new ArrayList<>();

    public MyAttrValueFiller(List<String> attrNames) {
        super();
        this.setPadding(new Insets(0));

        for (String str : attrNames) {
            MyTextField attrValue = new MyTextField(str);
            attrValues.add(attrValue);

            this.getChildren().add(attrValue);
        }
    }

    public List<String> getValues() {
        List<String> result = new ArrayList<>();
        for (MyTextField mtf : this.attrValues) {
            result.add(mtf.getText());
        }
        return result;
    }
}

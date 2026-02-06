package it.OrderFlow.View.JavaFX;

import it.OrderFlow.Beans.SupplierBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.JavaFX.MyComponents.*;
import it.OrderFlow.View.JavaFX.MyComponents.SpecificTableView.MyTableViewSupplier;
import it.OrderFlow.View.ManageSuppliersView;
import it.OrderFlow.View.ViewEvent;

import java.math.BigDecimal;
import java.util.List;

public class JavaFXManageSuppliersView extends JavaFXRootView implements ManageSuppliersView {

    private final MyTitle title;
    private final MyLabel infoLabel;
    private final MyButton confirmButton;
    private final MyButton backButton;
    private final MyNavigationBar myNavigationBar;

    private MyTextField nameField;
    private MyTextField emailField;
    private MyTextField phoneField;
    private MyTextField transportFeeField;

    public JavaFXManageSuppliersView() {
        super();

        title = new MyTitle();

        infoLabel = new MyLabel();

        confirmButton = new MyButton("Confirm");

        backButton = new MyButton("Back");

        myNavigationBar = new MyNavigationBar();

        myNavigationBar.getHomeButton().setOnAction(e -> this.notifyObservers(ViewEvent.HOME));
        myNavigationBar.getLogoutButton().setOnAction(e -> this.notifyObservers(ViewEvent.LOGOUT));
        myNavigationBar.getExitButton().setOnAction(e -> this.notifyObservers(ViewEvent.EXIT));
    }

    @Override
    public void displayManageSuppliers() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Manage suppliers");

            infoLabel.setText("What do you want to do?");

            MyButton addSupplierButton = new MyButton("Add supplier");

            MyButton changeSupplierInfoButton = new MyButton("Change supplier info");

            addSupplierButton.setOnAction(e -> this.notifyObservers(ViewEvent.ADD_SUPPLIER));
            changeSupplierInfoButton.setOnAction(e -> this.notifyObservers(ViewEvent.CHANGE_SUPPLIER_INFO));

            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.HOME));

            layout.getChildren().addAll(title, infoLabel, addSupplierButton, changeSupplierInfoButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayAddSupplier() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Add supplier");

            infoLabel.setText("Enter the new supplier's info. The fields with * are optional");

            nameField = new MyTextField("Name");
            emailField = new MyTextField("Email");
            phoneField = new MyTextField("* Phone");
            transportFeeField = new MyTextField("* Transport Fee");

            confirmButton.disableProperty().bind(nameField.textProperty().isEmpty().or(emailField.textProperty().isEmpty()));
            confirmButton.setOnAction(e -> this.notifyObservers(ViewEvent.SAVE_SUPPLIER));
            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.BACK));

            layout.getChildren().addAll(title, infoLabel, nameField, emailField, phoneField, transportFeeField,
                    confirmButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayChangeSupplierInfo(SupplierBean supplier) {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Change supplier info");

            infoLabel.setText("These are the selected supplier's info.\n"
                    + "Fill the fields you want to change");

            MyTableViewSupplier table = new MyTableViewSupplier(List.of(supplier));
            table.setSelectionModel(null);

            nameField = new MyTextField("Name");
            emailField = new MyTextField("Email");
            phoneField = new MyTextField("Phone");
            transportFeeField = new MyTextField("Transport Fee");

            confirmButton.disableProperty().unbind();
            confirmButton.setOnAction(e -> this.notifyObservers(ViewEvent.SAVE_SUPPLIER_INFO_CHANGE));
            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.BACK));

            layout.getChildren().addAll(title, infoLabel, table, nameField, emailField, phoneField, transportFeeField,
                    confirmButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public SupplierBean getSupplierBean() throws InvalidInputException {
        SupplierBean supplierBean = new SupplierBean();

        if (!this.nameField.getText().isBlank()) supplierBean.setName(this.nameField.getText());

        if (!this.emailField.getText().isBlank()) supplierBean.setEmail(this.emailField.getText());

        if (!this.phoneField.getText().isBlank()) supplierBean.setPhone(this.phoneField.getText());

        if (!this.transportFeeField.getText().isBlank()) {
            try {
                supplierBean.setTransportFee(new BigDecimal(this.transportFeeField.getText()));
            } catch (NumberFormatException e) {
                throw new InvalidInputException(InvalidInputException.InputType.PRICE);
            }
        }

        return supplierBean;
    }
}

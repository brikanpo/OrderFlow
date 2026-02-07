package it.orderflow.view.javafx;

import it.orderflow.beans.AddressBean;
import it.orderflow.beans.ClientBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.ManageClientsView;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.javafx.mycomponents.*;
import it.orderflow.view.javafx.mycomponents.specifictableview.MyTableViewClient;

import java.util.ArrayList;
import java.util.List;

public class JavaFXManageClientsView extends JavaFXRootView implements ManageClientsView {

    private final MyTitle title;
    private final MyLabel infoLabel;
    private final MyButton confirmButton;
    private final MyButton backButton;
    private final MyNavigationBar myNavigationBar;

    private MyTextField nameField;
    private MyTextField emailField;
    private MyTextField phoneField;
    private MyTextField addressField;
    private MyTextField capField;
    private MyTextField cityField;
    private MyTextField provinceField;

    public JavaFXManageClientsView() {
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
    public void displayManageClients() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Manage clients");

            infoLabel.setText("What do you want to do?");

            MyButton addClientButton = new MyButton("Add client");

            MyButton changeClientInfoButton = new MyButton("Change client info");

            addClientButton.setOnAction(e -> this.notifyObservers(ViewEvent.ADD_CLIENT));
            changeClientInfoButton.setOnAction(e -> this.notifyObservers(ViewEvent.CHANGE_CLIENT_INFO));

            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.HOME));

            layout.getChildren().addAll(title, infoLabel, addClientButton, changeClientInfoButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayAddClient() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Add client");

            infoLabel.setText("Enter the new client's info. The fields with * are optional");

            nameField = new MyTextField("Name");
            emailField = new MyTextField("Email");
            phoneField = new MyTextField("* Phone");
            addressField = new MyTextField("* Address");
            capField = new MyTextField("* CAP");
            cityField = new MyTextField("* City");
            provinceField = new MyTextField("* Province");

            confirmButton.disableProperty().bind(nameField.textProperty().isEmpty().or(emailField.textProperty().isEmpty()));
            confirmButton.setOnAction(e -> this.notifyObservers(ViewEvent.SAVE_CLIENT));
            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.BACK));

            layout.getChildren().addAll(title, infoLabel, nameField, emailField, phoneField, addressField, capField,
                    cityField, provinceField, confirmButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayChangeClientInfo(ClientBean client) {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Change client info");

            infoLabel.setText("These are the selected client's info.\n" +
                    "Fill the fields you want to change");

            MyTableViewClient table = new MyTableViewClient(new ArrayList<>(List.of(client)));
            table.setSelectionModel(null);

            nameField = new MyTextField("Name");
            emailField = new MyTextField("Email");
            phoneField = new MyTextField("Phone");
            addressField = new MyTextField("Address");
            capField = new MyTextField("CAP");
            cityField = new MyTextField("City");
            provinceField = new MyTextField("Province");

            confirmButton.disableProperty().unbind();
            confirmButton.setOnAction(e -> this.notifyObservers(ViewEvent.SAVE_CLIENT_INFO_CHANGE));
            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.BACK));

            layout.getChildren().addAll(title, infoLabel, table, nameField, emailField, phoneField, addressField,
                    capField, cityField, provinceField, confirmButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public ClientBean getClientBean() throws InvalidInputException {
        ClientBean clientBean = new ClientBean();

        if (!this.nameField.getText().isBlank()) clientBean.setName(this.nameField.getText());

        if (!this.emailField.getText().isBlank()) clientBean.setEmail(this.emailField.getText());

        if (!this.phoneField.getText().isBlank()) clientBean.setPhone(this.phoneField.getText());

        if (!this.addressField.getText().isBlank() && !this.capField.getText().isBlank() &&
                !this.cityField.getText().isBlank() && !this.provinceField.getText().isBlank()) {
            AddressBean addressBean = new AddressBean();
            addressBean.setAddress(this.addressField.getText());
            addressBean.setCap(this.capField.getText());
            addressBean.setCity(this.cityField.getText());
            addressBean.setProvince(this.provinceField.getText());
            clientBean.setAddressBean(addressBean);
        }

        return clientBean;
    }
}

package it.orderflow.view.javafx;

import it.orderflow.view.ManageOrdersView;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.javafx.mycomponents.*;

public class JavaFXManageOrdersView extends JavaFXRootView implements ManageOrdersView {

    private final MyTitle title;
    private final MyLabel infoLabel;
    private final MyButton backButton;
    private final MyNavigationBar myNavigationBar;

    public JavaFXManageOrdersView() {
        super();

        title = new MyTitle();

        infoLabel = new MyLabel("What do you want to do?");

        backButton = new MyButton("Back");

        myNavigationBar = new MyNavigationBar();

        myNavigationBar.getHomeButton().setOnAction(e -> this.notifyObservers(ViewEvent.HOME));
        myNavigationBar.getLogoutButton().setOnAction(e -> this.notifyObservers(ViewEvent.LOGOUT));
        myNavigationBar.getExitButton().setOnAction(e -> this.notifyObservers(ViewEvent.EXIT));
    }

    @Override
    public void displayManageOrders() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Manage orders");

            MyButton manageSuppliersOrdersButton = new MyButton("Manage suppliers orders");

            MyButton manageClientsOrdersButton = new MyButton("Manage clients orders");

            manageSuppliersOrdersButton.setOnAction(e -> this.notifyObservers(ViewEvent.MANAGE_SUPPLIERS_ORDERS));
            manageClientsOrdersButton.setOnAction(e -> this.notifyObservers(ViewEvent.MANAGE_CLIENTS_ORDERS));

            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.HOME));

            layout.getChildren().addAll(title, infoLabel, manageSuppliersOrdersButton, manageClientsOrdersButton,
                    backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayManageClientOrders() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Manage clients orders");

            MyButton addClientOrderButton = new MyButton("Add client order");

            MyButton prepareClientOrderButton = new MyButton("Prepare client order");

            MyButton closeClientOrderButton = new MyButton("Close client order");

            prepareClientOrderButton.setDisable(true);
            closeClientOrderButton.setDisable(true);

            addClientOrderButton.setOnAction(e -> this.notifyObservers(ViewEvent.ADD_CLIENT_ORDER));
            prepareClientOrderButton.setOnAction(e -> this.notifyObservers(ViewEvent.PREPARE_CLIENT_ORDER));
            closeClientOrderButton.setOnAction(e -> this.notifyObservers(ViewEvent.CLOSE_CLIENT_ORDER));

            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.BACK));

            layout.getChildren().addAll(title, infoLabel, addClientOrderButton, prepareClientOrderButton,
                    closeClientOrderButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayManageSuppliersOrders() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Manage suppliers orders");

            MyButton addSupplierOrderButton = new MyButton("Add supplier order");

            MyButton closeSupplierOrderButton = new MyButton("Close supplier order");

            addSupplierOrderButton.setDisable(true);
            closeSupplierOrderButton.setDisable(true);

            addSupplierOrderButton.setOnAction(e -> this.notifyObservers(ViewEvent.ADD_SUPPLIER_ORDER));
            closeSupplierOrderButton.setOnAction(e -> this.notifyObservers(ViewEvent.CLOSE_SUPPLIER_ORDER));

            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.BACK));

            layout.getChildren().addAll(title, infoLabel, addSupplierOrderButton, closeSupplierOrderButton,
                    backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }
}

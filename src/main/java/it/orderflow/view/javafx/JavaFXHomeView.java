package it.orderflow.view.javafx;

import it.orderflow.view.HomeView;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.javafx.mycomponents.*;

public class JavaFXHomeView extends JavaFXRootView implements HomeView {

    private final MyTitle title;
    private final MyLabel infoLabel;
    private final MyButton manageClientsButton;
    private final MyButton managePersonalInfoButton;
    private final MyNavigationBar myNavigationBar;

    public JavaFXHomeView() {
        super();

        title = new MyTitle("Home");

        infoLabel = new MyLabel();

        manageClientsButton = new MyButton("Manage clients");

        managePersonalInfoButton = new MyButton("Manage personal info");

        myNavigationBar = new MyNavigationBar();

        manageClientsButton.setOnAction(e -> this.notifyObservers(ViewEvent.MANAGE_CLIENTS));
        managePersonalInfoButton.setOnAction(e -> this.notifyObservers(ViewEvent.MANAGE_PERSONAL_INFO));
        myNavigationBar.removeHomeButton();
        myNavigationBar.getLogoutButton().setOnAction(e -> this.notifyObservers(ViewEvent.LOGOUT));
        myNavigationBar.getExitButton().setOnAction(e -> this.notifyObservers(ViewEvent.EXIT));
    }

    @Override
    public void displayManagerHome() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            infoLabel.setText("You are logged as Manager. What do you want to do?");

            MyButton manageEmployeeButton = new MyButton("Manage employee");

            MyButton manageSuppliersButton = new MyButton("Manage suppliers");

            MyButton manageArticlesButton = new MyButton("Manage articles");

            MyButton manageProductsButton = new MyButton("Manage products");

            MyButton manageOrdersButton = new MyButton("Manage orders");

            manageEmployeeButton.setOnAction(e -> this.notifyObservers(ViewEvent.MANAGE_EMPLOYEES));
            manageSuppliersButton.setOnAction(e -> this.notifyObservers(ViewEvent.MANAGE_SUPPLIERS));
            manageArticlesButton.setOnAction(e -> this.notifyObservers(ViewEvent.MANAGE_ARTICLES));
            manageProductsButton.setOnAction(e -> this.notifyObservers(ViewEvent.MANAGE_PRODUCTS));
            manageOrdersButton.setOnAction(e -> this.notifyObservers(ViewEvent.MANAGE_ORDERS));

            layout.getChildren().addAll(title, infoLabel, manageEmployeeButton, manageSuppliersButton,
                    manageClientsButton, manageArticlesButton, manageProductsButton, manageOrdersButton,
                    managePersonalInfoButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayRepresentativeHome() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            infoLabel.setText("You are logged as Representative. What do you want to do?");

            MyButton addClientOrderButton = new MyButton("Add client order");

            addClientOrderButton.setOnAction(e -> this.notifyObservers(ViewEvent.ADD_CLIENT_ORDER));

            layout.getChildren().addAll(title, infoLabel, manageClientsButton, addClientOrderButton,
                    managePersonalInfoButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayWarehouseWorkerHome() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            infoLabel.setText("You are logged as Warehouse worker. What do you want to do?");

            MyButton closeSupplierOrderButton = new MyButton("Close supplier order");

            MyButton prepareOrderButton = new MyButton("Prepare order");

            closeSupplierOrderButton.setDisable(true);
            prepareOrderButton.setDisable(true);

            closeSupplierOrderButton.setOnAction(e -> this.notifyObservers(ViewEvent.CLOSE_SUPPLIER_ORDER));
            prepareOrderButton.setOnAction(e -> this.notifyObservers(ViewEvent.PREPARE_CLIENT_ORDER));

            layout.getChildren().addAll(title, infoLabel, closeSupplierOrderButton, prepareOrderButton,
                    managePersonalInfoButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayDeliveryWorkerHome() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            infoLabel.setText("You are logged as Delivery worker. What do you want to do?");

            MyButton closeClientOrderButton = new MyButton("Close client order");

            closeClientOrderButton.setDisable(true);

            closeClientOrderButton.setOnAction(e -> this.notifyObservers(ViewEvent.CLOSE_CLIENT_ORDER));

            layout.getChildren().addAll(title, infoLabel, closeClientOrderButton, managePersonalInfoButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }
}

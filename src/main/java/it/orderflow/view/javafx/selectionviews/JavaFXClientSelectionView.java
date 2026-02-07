package it.orderflow.view.javafx.selectionviews;

import it.orderflow.beans.ClientBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.javafx.mycomponents.MyButton;
import it.orderflow.view.javafx.mycomponents.MyContainer;
import it.orderflow.view.javafx.mycomponents.MyNavigationBar;
import it.orderflow.view.javafx.mycomponents.MyVBox;
import it.orderflow.view.javafx.mycomponents.specifictableview.MyTableViewClient;
import it.orderflow.view.selectionviews.ClientSelectionView;

import java.util.List;

public class JavaFXClientSelectionView extends JavaFXAbstractSelectionView<ClientBean, MyTableViewClient>
        implements ClientSelectionView {

    private final boolean creationEnabled;

    public JavaFXClientSelectionView(String title, String message, boolean creationEnabled) {
        super(title, message);

        this.creationEnabled = creationEnabled;
    }

    @Override
    public void displayClients(List<ClientBean> clients, ViewEvent onBack) {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            table = new MyTableViewClient(clients);

            MyButton confirmButton = new MyButton("Confirm");

            MyButton backButton = new MyButton("Back");

            MyNavigationBar myNavigationBar = new MyNavigationBar();

            confirmButton.setOnAction(e -> this.onConfirmPressed(true));
            backButton.setOnAction(e -> this.notifyObservers(onBack));

            myNavigationBar.getHomeButton().setOnAction(e -> this.notifyObservers(ViewEvent.HOME));
            myNavigationBar.getLogoutButton().setOnAction(e -> this.notifyObservers(ViewEvent.LOGOUT));
            myNavigationBar.getExitButton().setOnAction(e -> this.notifyObservers(ViewEvent.EXIT));

            if (creationEnabled) {
                MyButton addNewClientButton = new MyButton("Add new client");
                addNewClientButton.setOnAction(e -> this.notifyObservers(ViewEvent.ADD_CLIENT));
                layout.getChildren().addAll(title, infoLabel, table, confirmButton, addNewClientButton, backButton);
            } else {
                layout.getChildren().addAll(title, infoLabel, table, confirmButton, backButton);
            }

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public ClientBean selectedClient() throws InvalidInputException {
        return this.selectedItem();
    }

    @Override
    protected MyTableViewClient createTable(List<ClientBean> items) {
        return null;
    }

    @Override
    protected void onConfirmPressed(boolean singleSelection) {
        this.notifyObservers(ViewEvent.SELECTED_CLIENT);
    }
}

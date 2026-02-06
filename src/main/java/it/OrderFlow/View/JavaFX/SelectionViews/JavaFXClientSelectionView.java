package it.OrderFlow.View.JavaFX.SelectionViews;

import it.OrderFlow.Beans.ClientBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.JavaFX.MyComponents.MyButton;
import it.OrderFlow.View.JavaFX.MyComponents.MyContainer;
import it.OrderFlow.View.JavaFX.MyComponents.MyNavigationBar;
import it.OrderFlow.View.JavaFX.MyComponents.MyVBox;
import it.OrderFlow.View.JavaFX.MyComponents.SpecificTableView.MyTableViewClient;
import it.OrderFlow.View.SelectionComponents.ClientSelectionView;
import it.OrderFlow.View.ViewEvent;

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

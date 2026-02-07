package it.orderflow.view.javafx.selectionviews;

import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.javafx.JavaFXRootView;
import it.orderflow.view.javafx.mycomponents.*;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public abstract class JavaFXAbstractSelectionView<T, V extends TableView<T>> extends JavaFXRootView {

    protected final MyTitle title;
    protected final MyLabel infoLabel;
    protected V table;

    protected JavaFXAbstractSelectionView(String title, String message) {
        super();

        this.title = new MyTitle(title);

        this.infoLabel = new MyLabel(message);
    }

    protected void displayItems(boolean singleSelection, List<T> items, ViewEvent onBack) {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            this.table = createTable(items);

            if (singleSelection) {
                this.table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            } else {
                this.table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            }

            MyButton confirmButton = new MyButton("Confirm");

            MyButton backButton = new MyButton("Back");

            confirmButton.setOnAction(e -> onConfirmPressed(singleSelection));
            backButton.setOnAction(e -> this.notifyObservers(onBack));

            MyNavigationBar myNavigationBar = createNavigationBar();

            layout.getChildren().addAll(title, infoLabel, table, confirmButton, backButton);
            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    protected T selectedItem() throws InvalidInputException {
        T bean;

        if (!this.table.getSelectionModel().isEmpty()) {
            bean = this.table.getSelectionModel().getSelectedItem();
        } else throw new InvalidInputException(InvalidInputException.InputType.SELECTION);

        this.table.getSelectionModel().clearSelection();

        return bean;
    }

    protected List<T> selectedItems() throws InvalidInputException {
        List<T> beanList;

        if (!this.table.getSelectionModel().isEmpty()) {
            beanList = new ArrayList<>(this.table.getSelectionModel().getSelectedItems());
        } else throw new InvalidInputException(InvalidInputException.InputType.SELECTION);

        this.table.getSelectionModel().clearSelection();

        return beanList;
    }

    protected abstract V createTable(List<T> items);

    protected abstract void onConfirmPressed(boolean singleSelection);

    private MyNavigationBar createNavigationBar() {
        MyNavigationBar navigationBar = new MyNavigationBar();

        navigationBar.getHomeButton().setOnAction(e -> this.notifyObservers(ViewEvent.HOME));
        navigationBar.getLogoutButton().setOnAction(e -> this.notifyObservers(ViewEvent.LOGOUT));
        navigationBar.getExitButton().setOnAction(e -> this.notifyObservers(ViewEvent.EXIT));

        return navigationBar;
    }
}

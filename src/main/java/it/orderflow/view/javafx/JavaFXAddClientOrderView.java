package it.orderflow.view.javafx;

import it.orderflow.beans.ClientOrderBean;
import it.orderflow.beans.ProductBean;
import it.orderflow.beans.ProductWithQuantityBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.AddClientOrderView;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.javafx.mycomponents.*;
import it.orderflow.view.javafx.mycomponents.specifictableview.MyTableViewClient;
import it.orderflow.view.javafx.mycomponents.specifictableview.MyTableViewProductWithQuantity;

import java.util.ArrayList;
import java.util.List;

public class JavaFXAddClientOrderView extends JavaFXRootView implements AddClientOrderView {

    private final MyTitle title;
    private final MyLabel infoLabel;
    private final MyButton confirmButton;
    private final MyButton backButton;
    private final MyNavigationBar myNavigationBar;

    private MyTextField numberOfOrders;
    private List<ProductWithQuantityBean> unavailableProducts;
    private List<ProductWithQuantityBean> pwqbList;

    public JavaFXAddClientOrderView() {
        super();

        title = new MyTitle("Add client order");

        infoLabel = new MyLabel();

        confirmButton = new MyButton("Confirm");

        backButton = new MyButton("Back");

        myNavigationBar = new MyNavigationBar();

        myNavigationBar.getHomeButton().setOnAction(e -> this.notifyObservers(ViewEvent.HOME));
        myNavigationBar.getLogoutButton().setOnAction(e -> this.notifyObservers(ViewEvent.LOGOUT));
        myNavigationBar.getExitButton().setOnAction(e -> this.notifyObservers(ViewEvent.EXIT));
    }

    @Override
    public void displayInsertionOptions() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            infoLabel.setText("How do you want to selects the products ordered?");

            numberOfOrders = new MyTextField("Number of past orders to consider");

            MyButton pastOrdersSelectionButton = new MyButton("Selection from past orders");

            MyButton manualSelectionButton = new MyButton("Manual selection");

            pastOrdersSelectionButton.disableProperty().bind(numberOfOrders.textProperty().isEmpty());
            pastOrdersSelectionButton.setOnAction(e -> this.notifyObservers(ViewEvent.SELECTED_NUMBER_OF_PAST_ORDERS));
            manualSelectionButton.setOnAction(e -> this.notifyObservers(ViewEvent.SELECTED_MANUAL_SELECTION));

            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.BACK));

            layout.getChildren().addAll(title, infoLabel, numberOfOrders, pastOrdersSelectionButton,
                    manualSelectionButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayQuantitySelection(List<ProductBean> products) {
        pwqbList = new ArrayList<>();
        for (ProductBean pb : products) {
            ProductWithQuantityBean pwqb = new ProductWithQuantityBean(pb);
            pwqbList.add(pwqb);
        }
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            infoLabel.setText("Selects the quantities of the products ordered");

            MyTableViewProductWithQuantity table = new MyTableViewProductWithQuantity(pwqbList, true);
            table.setSelectionModel(null);

            confirmButton.setOnAction(e -> this.notifyObservers(ViewEvent.SELECTED_PRODUCTS_QUANTITY));

            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.SELECT_PRODUCTS));

            layout.getChildren().addAll(title, infoLabel, table, confirmButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayUnavailableProducts(List<ProductWithQuantityBean> products) {
        unavailableProducts = products;
        pwqbList = new ArrayList<>();
        for (ProductWithQuantityBean pwqb : products) {
            pwqbList.add(new ProductWithQuantityBean(pwqb.getProductBean(), pwqb.getQuantity()));
        }
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            infoLabel.setText("Selects the quantities of the unavailable products you want to keep");

            MyTableViewProductWithQuantity table = new MyTableViewProductWithQuantity(pwqbList, true);
            table.setSelectionModel(null);

            confirmButton.setOnAction(e -> this.notifyObservers(ViewEvent.SELECTED_UNAVAILABLE_PRODUCTS_QUANTITY));

            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.SELECT_QUANTITY));

            layout.getChildren().addAll(title, infoLabel, table, confirmButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayClientOrderDetails(ClientOrderBean clientOrder) {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            infoLabel.setText("These are the client info");

            MyTableViewClient tableViewClient = new MyTableViewClient(List.of(clientOrder.getClientBean()));

            MyLabel infoLabel1 = new MyLabel("These are the products ordered");

            MyTableViewProductWithQuantity tableProductsOrdered = new MyTableViewProductWithQuantity(clientOrder.getProductsOrdered(), false);

            backButton.setText("Cancel operation");

            confirmButton.setOnAction(e -> this.notifyObservers(ViewEvent.SAVE_CLIENT_ORDER));
            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.HOME));

            layout.getChildren().addAll(title, infoLabel, tableViewClient, infoLabel1, tableProductsOrdered, confirmButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public int selectedNumberOfPastOrders() throws InvalidInputException {
        if (!this.numberOfOrders.getText().isBlank()) {
            try {
                return Integer.parseInt(this.numberOfOrders.getText());
            } catch (NumberFormatException e) {
                throw new InvalidInputException(InvalidInputException.InputType.QUANTITY_1_OR_HIGHER);
            }
        } else throw new InvalidInputException(InvalidInputException.InputType.QUANTITY_1_OR_HIGHER);
    }

    @Override
    public List<ProductWithQuantityBean> selectedProductsQuantity() throws InvalidInputException {
        List<ProductWithQuantityBean> productsOrdered = new ArrayList<>();

        for (ProductWithQuantityBean pwqb : this.pwqbList) {
            if (pwqb.getQuantity() != 0) {
                productsOrdered.add(pwqb);
            } else throw new InvalidInputException(InvalidInputException.InputType.QUANTITY_1_OR_HIGHER);
        }

        return productsOrdered;
    }

    @Override
    public List<ProductWithQuantityBean> selectedProductsToBeRemoved() throws InvalidInputException {
        List<ProductWithQuantityBean> productToBeRemoved = new ArrayList<>();

        for (int i = 0; i < this.unavailableProducts.size(); i++) {
            ProductWithQuantityBean unavailablePwqb = this.unavailableProducts.get(i);
            int quantityToRemove = unavailablePwqb.getQuantity() - this.pwqbList.get(i).getQuantity();
            if (quantityToRemove >= 0) {
                ProductWithQuantityBean newPwqb = new ProductWithQuantityBean(unavailablePwqb.getProductBean(), quantityToRemove);
                productToBeRemoved.add(newPwqb);
            } else throw new InvalidInputException(InvalidInputException.InputType.QUANTITY_SAME_OR_LOWER);
        }

        return productToBeRemoved;
    }
}

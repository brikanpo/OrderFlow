package it.orderflow.view.javafx.selectionviews;

import it.orderflow.beans.ProductBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.javafx.mycomponents.specifictableview.MyTableViewProduct;
import it.orderflow.view.selectionviews.ProductsSelectionView;

import java.util.List;

public class JavaFXProductsSelectionView extends JavaFXAbstractSelectionView<ProductBean, MyTableViewProduct>
        implements ProductsSelectionView {

    public JavaFXProductsSelectionView(String title, String message) {
        super(title, message);
    }

    @Override
    public void displayProducts(List<ProductBean> products, ViewEvent onBack) {
        this.displayItems(false, products, onBack);
    }

    @Override
    public List<ProductBean> selectedProducts() throws InvalidInputException {
        return this.selectedItems();
    }

    @Override
    protected MyTableViewProduct createTable(List<ProductBean> items) {
        return new MyTableViewProduct(items);
    }

    @Override
    protected void onConfirmPressed(boolean singleSelection) {
        this.notifyObservers(ViewEvent.SELECTED_PRODUCTS);
    }
}

package it.OrderFlow.View.JavaFX.SelectionViews;

import it.OrderFlow.Beans.ProductBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.JavaFX.MyComponents.SpecificTableView.MyTableViewProduct;
import it.OrderFlow.View.SelectionComponents.ProductsSelectionView;
import it.OrderFlow.View.ViewEvent;

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

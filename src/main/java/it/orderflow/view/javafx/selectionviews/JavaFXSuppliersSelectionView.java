package it.orderflow.view.javafx.selectionviews;

import it.orderflow.beans.SupplierBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.javafx.mycomponents.specifictableview.MyTableViewSupplier;
import it.orderflow.view.selectionviews.SupplierSelectionView;

import java.util.List;

public class JavaFXSuppliersSelectionView extends JavaFXAbstractSelectionView<SupplierBean, MyTableViewSupplier>
        implements SupplierSelectionView {

    public JavaFXSuppliersSelectionView(String title, String message) {
        super(title, message);
    }

    @Override
    public void displaySuppliers(List<SupplierBean> suppliers, ViewEvent onBack) {
        this.displayItems(true, suppliers, onBack);
    }

    @Override
    public SupplierBean selectedSupplier() throws InvalidInputException {
        return this.selectedItem();
    }

    @Override
    protected MyTableViewSupplier createTable(List<SupplierBean> items) {
        return new MyTableViewSupplier(items);
    }

    @Override
    protected void onConfirmPressed(boolean singleSelection) {
        this.notifyObservers(ViewEvent.SELECTED_SUPPLIER);
    }
}

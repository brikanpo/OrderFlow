package it.OrderFlow.View.JavaFX.SelectionViews;

import it.OrderFlow.Beans.SupplierBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.JavaFX.MyComponents.SpecificTableView.MyTableViewSupplier;
import it.OrderFlow.View.SelectionComponents.SupplierSelectionView;
import it.OrderFlow.View.ViewEvent;

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

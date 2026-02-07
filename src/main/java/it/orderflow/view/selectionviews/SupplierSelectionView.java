package it.orderflow.view.selectionviews;

import it.orderflow.beans.SupplierBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.BasicView;
import it.orderflow.view.ViewEvent;

import java.util.List;

public interface SupplierSelectionView extends BasicView {

    void displaySuppliers(List<SupplierBean> suppliers, ViewEvent onBack);

    SupplierBean selectedSupplier() throws InvalidInputException;
}

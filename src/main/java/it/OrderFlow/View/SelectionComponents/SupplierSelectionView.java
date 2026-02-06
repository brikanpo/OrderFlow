package it.OrderFlow.View.SelectionComponents;

import it.OrderFlow.Beans.SupplierBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.BasicView;
import it.OrderFlow.View.ViewEvent;

import java.util.List;

public interface SupplierSelectionView extends BasicView {

    void displaySuppliers(List<SupplierBean> suppliers, ViewEvent onBack);

    SupplierBean selectedSupplier() throws InvalidInputException;
}

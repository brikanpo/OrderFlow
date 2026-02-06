package it.OrderFlow.View;

import it.OrderFlow.Beans.SupplierBean;
import it.OrderFlow.Exceptions.InvalidInputException;

public interface ManageSuppliersView extends BasicView {

    void displayManageSuppliers();

    void displayAddSupplier();

    void displayChangeSupplierInfo(SupplierBean supplier);

    SupplierBean getSupplierBean() throws InvalidInputException;
}

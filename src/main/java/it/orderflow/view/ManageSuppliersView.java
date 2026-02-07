package it.orderflow.view;

import it.orderflow.beans.SupplierBean;
import it.orderflow.exceptions.InvalidInputException;

public interface ManageSuppliersView extends BasicView {

    void displayManageSuppliers();

    void displayAddSupplier();

    void displayChangeSupplierInfo(SupplierBean supplier);

    SupplierBean getSupplierBean() throws InvalidInputException;
}

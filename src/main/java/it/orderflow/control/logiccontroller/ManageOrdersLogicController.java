package it.orderflow.control.logiccontroller;

import it.orderflow.beans.EmployeeBean;
import it.orderflow.control.TransactionSafeController;
import it.orderflow.dao.EmployeeDAO;
import it.orderflow.dao.ProductDAO;
import it.orderflow.dao.SupplierDAO;
import it.orderflow.dao.SupplierOrderDAO;

public class ManageOrdersLogicController extends TransactionSafeController {

    private final SupplierDAO supplierDAO;
    private final ProductDAO productDAO;
    private final SupplierOrderDAO supplierOrderDAO;
    private final EmployeeDAO employeeDAO;
    private final EmployeeBean loggedEmployee;

    public ManageOrdersLogicController(SupplierDAO supplierDAO, ProductDAO productDAO,
                                       SupplierOrderDAO supplierOrderDAO, EmployeeDAO employeeDAO,
                                       EmployeeBean loggedEmployee) {
        super();
        this.supplierDAO = supplierDAO;
        this.productDAO = productDAO;
        this.supplierOrderDAO = supplierOrderDAO;
        this.employeeDAO = employeeDAO;
        this.loggedEmployee = loggedEmployee;
    }

    public SupplierDAO getSupplierDAO() {
        return this.supplierDAO;
    }

    public ProductDAO getProductDAO() {
        return this.productDAO;
    }

    public SupplierOrderDAO getSupplierOrderDAO() {
        return this.supplierOrderDAO;
    }

    public EmployeeDAO getEmployeeDAO() {
        return this.employeeDAO;
    }

    public EmployeeBean getLoggedEmployee() {
        return this.loggedEmployee;
    }

    //implements methods for adding suppliers orders
}

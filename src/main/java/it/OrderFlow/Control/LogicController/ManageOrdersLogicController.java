package it.OrderFlow.Control.LogicController;

import it.OrderFlow.Beans.EmployeeBean;
import it.OrderFlow.Control.TransactionSafeController;
import it.OrderFlow.DAO.EmployeeDAO;
import it.OrderFlow.DAO.ProductDAO;
import it.OrderFlow.DAO.SupplierDAO;
import it.OrderFlow.DAO.SupplierOrderDAO;

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

    //TODO implements methods for adding suppliers orders
}

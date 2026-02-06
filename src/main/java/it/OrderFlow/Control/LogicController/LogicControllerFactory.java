package it.OrderFlow.Control.LogicController;

import it.OrderFlow.Beans.EmployeeBean;
import it.OrderFlow.DAO.DAOFactory;

public class LogicControllerFactory {

    private final DAOFactory daoFactory;

    public LogicControllerFactory(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public AddClientOrderLogicController getAddClientOrderLogicController(EmployeeBean loggedEmployee) {
        return new AddClientOrderLogicController(daoFactory.getClientDAO(), daoFactory.getClientArticleDAO(),
                daoFactory.getProductInStockDAO(), daoFactory.getClientOrderDAO(), daoFactory.getEmployeeDAO(),
                loggedEmployee);
    }

    public CloseClientOrderLogicController getCloseClientOrderLogicController(EmployeeBean loggedEmployee) {
        return new CloseClientOrderLogicController(daoFactory.getProductInStockDAO(), daoFactory.getClientOrderDAO(),
                loggedEmployee);
    }

    public LoginLogicController getLoginLogicController() {
        return new LoginLogicController(daoFactory.getEmployeeDAO());
    }

    public ManageArticlesLogicController getManageArticlesLogicController() {
        return new ManageArticlesLogicController(daoFactory.getClientArticleDAO(),
                daoFactory.getSupplierArticleDAO(), daoFactory.getSupplierDAO());
    }

    public ManageClientsLogicController getManageClientsLogicController() {
        return new ManageClientsLogicController(daoFactory.getClientDAO());
    }

    public ManageEmployeesLogicController getManageEmployeesLogicController() {
        return new ManageEmployeesLogicController(daoFactory.getEmployeeDAO());
    }

    public ManagePersonalInfoLogicController getManagePersonalInfoLogicController(EmployeeBean loggedEmployee) {
        return new ManagePersonalInfoLogicController(daoFactory.getEmployeeDAO(), loggedEmployee);
    }

    public ManageOrdersLogicController getManageOrdersLogicController(EmployeeBean loggedEmployee) {
        return new ManageOrdersLogicController(daoFactory.getSupplierDAO(), daoFactory.getProductDAO(),
                daoFactory.getSupplierOrderDAO(), daoFactory.getEmployeeDAO(),
                loggedEmployee);
    }

    public ManageSuppliersLogicController getManageSuppliersLogicController() {
        return new ManageSuppliersLogicController(daoFactory.getSupplierDAO());
    }

    public ManageProductsLogicController getManageProductsLogicController() {
        return new ManageProductsLogicController(daoFactory.getClientArticleDAO(),
                daoFactory.getSupplierArticleDAO(), daoFactory.getProductDAO(), daoFactory.getProductInStockDAO());
    }
}

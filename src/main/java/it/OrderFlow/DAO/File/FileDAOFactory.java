package it.OrderFlow.DAO.File;

import it.OrderFlow.DAO.*;

public class FileDAOFactory extends DAOFactory {

    private static EmployeeDAO employeeDAO;

    @Override
    public EmployeeDAO getEmployeeDAO() {
        if (employeeDAO == null) employeeDAO = new FileEmployeeDAO();
        return employeeDAO;
    }

    @Override
    public SupplierDAO getSupplierDAO() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClientDAO getClientDAO() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SupplierArticleDAO getSupplierArticleDAO() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClientArticleDAO getClientArticleDAO() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductDAO getProductDAO() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductInStockDAO getProductInStockDAO() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SupplierOrderDAO getSupplierOrderDAO() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClientOrderDAO getClientOrderDAO() {
        throw new UnsupportedOperationException();
    }
}

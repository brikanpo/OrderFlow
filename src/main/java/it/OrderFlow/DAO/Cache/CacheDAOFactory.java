package it.OrderFlow.DAO.Cache;

import it.OrderFlow.DAO.*;

public class CacheDAOFactory extends DAOFactory {

    private static EmployeeDAO employeeDAO;
    private static SupplierDAO supplierDAO;
    private static ClientDAO clientDAO;
    private static SupplierArticleDAO supplierArticleDAO;
    private static ClientArticleDAO clientArticleDAO;
    private static ProductDAO productDAO;
    private static ProductInStockDAO productInStockDAO;
    private static SupplierOrderDAO supplierOrderDao;
    private static ClientOrderDAO clientOrderDAO;

    @Override
    public EmployeeDAO getEmployeeDAO() {
        if (employeeDAO == null) employeeDAO = new CacheEmployeeDAO();
        return employeeDAO;
    }

    @Override
    public SupplierDAO getSupplierDAO() {
        if (supplierDAO == null) supplierDAO = new CacheSupplierDAO();
        return supplierDAO;
    }

    @Override
    public ClientDAO getClientDAO() {
        if (clientDAO == null) clientDAO = new CacheClientDAO();
        return clientDAO;
    }

    @Override
    public SupplierArticleDAO getSupplierArticleDAO() {
        if (supplierArticleDAO == null) supplierArticleDAO = new CacheSupplierArticleDAO();
        return supplierArticleDAO;
    }

    @Override
    public ClientArticleDAO getClientArticleDAO() {
        if (clientArticleDAO == null) clientArticleDAO = new CacheClientArticleDAO();
        return clientArticleDAO;
    }

    @Override
    public ProductDAO getProductDAO() {
        if (productDAO == null) productDAO = new CacheProductDAO();
        return productDAO;
    }

    @Override
    public ProductInStockDAO getProductInStockDAO() {
        if (productInStockDAO == null) productInStockDAO = new CacheProductInStockDAO();
        return productInStockDAO;
    }

    @Override
    public SupplierOrderDAO getSupplierOrderDAO() {
        if (supplierOrderDao == null) supplierOrderDao = new CacheSupplierOrderDAO();
        return supplierOrderDao;
    }

    @Override
    public ClientOrderDAO getClientOrderDAO() {
        if (clientOrderDAO == null) clientOrderDAO = new CacheClientOrderDAO();
        return clientOrderDAO;
    }
}

package it.orderflow.dao.cache;

import it.orderflow.dao.*;

public class CacheDAOFactory extends DAOFactory {

    private EmployeeDAO employeeDAO;
    private SupplierDAO supplierDAO;
    private ClientDAO clientDAO;
    private SupplierArticleDAO supplierArticleDAO;
    private ClientArticleDAO clientArticleDAO;
    private ProductDAO productDAO;
    private ProductInStockDAO productInStockDAO;
    private SupplierOrderDAO supplierOrderDao;
    private ClientOrderDAO clientOrderDAO;

    private CacheDAOFactory() {
    }

    public static CacheDAOFactory getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final CacheDAOFactory INSTANCE = new CacheDAOFactory();
    }

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

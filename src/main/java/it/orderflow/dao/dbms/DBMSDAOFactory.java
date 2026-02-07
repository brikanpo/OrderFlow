package it.orderflow.dao.dbms;

import it.orderflow.dao.*;

public class DBMSDAOFactory extends DAOFactory {

    private EmployeeDAO employeeDAO;
    private SupplierDAO supplierDAO;
    private ClientDAO clientDAO;
    private SupplierArticleDAO supplierArticleDAO;
    private ClientArticleDAO clientArticleDAO;
    private ProductDAO productDAO;
    private ProductInStockDAO productInStockDAO;
    private SupplierOrderDAO supplierOrderDao;
    private ClientOrderDAO clientOrderDAO;

    private DBMSDAOFactory() {
    }

    public static DBMSDAOFactory getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DBMSDAOFactory INSTANCE = new DBMSDAOFactory();
    }

    @Override
    public EmployeeDAO getEmployeeDAO() {
        if (employeeDAO == null) employeeDAO = new DBMSEmployeeDAO();
        return employeeDAO;
    }

    @Override
    public SupplierDAO getSupplierDAO() {
        if (supplierDAO == null) supplierDAO = new DBMSSupplierDAO();
        return supplierDAO;
    }

    @Override
    public ClientDAO getClientDAO() {
        if (clientDAO == null) clientDAO = new DBMSClientDAO();
        return clientDAO;
    }

    @Override
    public SupplierArticleDAO getSupplierArticleDAO() {
        if (supplierArticleDAO == null) supplierArticleDAO = new DBMSSupplierArticleDAO();
        return supplierArticleDAO;
    }

    @Override
    public ClientArticleDAO getClientArticleDAO() {
        if (clientArticleDAO == null) clientArticleDAO = new DBMSClientArticleDAO();
        return clientArticleDAO;
    }

    @Override
    public ProductDAO getProductDAO() {
        if (productDAO == null) {
            productDAO = new DBMSProductDAO(this.getSupplierArticleDAO());
        }
        return productDAO;
    }

    @Override
    public ProductInStockDAO getProductInStockDAO() {
        if (productInStockDAO == null) productInStockDAO = new DBMSProductInStockDAO(this.getClientArticleDAO());
        return productInStockDAO;
    }

    @Override
    public SupplierOrderDAO getSupplierOrderDAO() {
        if (supplierOrderDao == null)
            supplierOrderDao = new DBMSSupplierOrderDAO(this.getSupplierDAO(), this.getProductDAO());
        return supplierOrderDao;
    }

    @Override
    public ClientOrderDAO getClientOrderDAO() {
        if (clientOrderDAO == null)
            clientOrderDAO = new DBMSClientOrderDAO(this.getClientDAO(), this.getProductInStockDAO());
        return clientOrderDAO;
    }
}

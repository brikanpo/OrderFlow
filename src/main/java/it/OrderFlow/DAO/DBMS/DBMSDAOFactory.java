package it.OrderFlow.DAO.DBMS;

import it.OrderFlow.DAO.*;

public class DBMSDAOFactory extends DAOFactory {

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

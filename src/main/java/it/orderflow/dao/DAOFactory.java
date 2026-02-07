package it.orderflow.dao;

import it.orderflow.dao.cache.CacheDAOFactory;
import it.orderflow.dao.dbms.DBMSDAOFactory;
import it.orderflow.dao.file.FileDAOFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class DAOFactory {

    private static final Map<DAOType, DAOFactory> factories = new HashMap<>();

    public synchronized static DAOFactory getFactory(DAOType type) {
        if (!factories.containsKey(type)) {
            DAOFactory factory = switch (type) {
                case DBMS -> DBMSDAOFactory.getInstance();
                case CACHE -> CacheDAOFactory.getInstance();
                case FILE -> FileDAOFactory.getInstance();
            };
            factories.put(type, factory);
        }
        return factories.get(type);
    }

    public abstract EmployeeDAO getEmployeeDAO();

    public abstract SupplierDAO getSupplierDAO();

    public abstract ClientDAO getClientDAO();

    public abstract SupplierArticleDAO getSupplierArticleDAO();

    public abstract ClientArticleDAO getClientArticleDAO();

    public abstract ProductDAO getProductDAO();

    public abstract ProductInStockDAO getProductInStockDAO();

    public abstract SupplierOrderDAO getSupplierOrderDAO();

    public abstract ClientOrderDAO getClientOrderDAO();
}

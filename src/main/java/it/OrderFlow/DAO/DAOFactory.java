package it.OrderFlow.DAO;

import it.OrderFlow.DAO.Cache.CacheDAOFactory;
import it.OrderFlow.DAO.DBMS.DBMSDAOFactory;
import it.OrderFlow.DAO.File.FileDAOFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class DAOFactory {

    private static final Map<DAOType, DAOFactory> factories = new HashMap<>();

    public synchronized static DAOFactory getFactory(DAOType type) {
        if (!factories.containsKey(type)) {
            DAOFactory factory = switch (type) {
                case DBMS -> new DBMSDAOFactory();
                case CACHE -> new CacheDAOFactory();
                case FILE -> new FileDAOFactory();
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

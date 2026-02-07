package it.orderflow.dao.dbms;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.orderflow.control.Statement;
import it.orderflow.dao.ProductDAO;
import it.orderflow.dao.SupplierDAO;
import it.orderflow.dao.SupplierOrderDAO;
import it.orderflow.exceptions.DatabaseException;
import it.orderflow.exceptions.EntityException;
import it.orderflow.model.*;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DBMSSupplierOrderDAO extends DBMSGeneralDAO<SupplierOrder> implements SupplierOrderDAO {

    private final Gson gson = new Gson();
    private final String tableName = "supplierOrder";

    private final DBMSSupplierDAO supplierDAO;
    private final DBMSProductDAO productDAO;

    public DBMSSupplierOrderDAO(SupplierDAO supplierDAO, ProductDAO productDAO) {
        super();
        this.supplierDAO = (DBMSSupplierDAO) supplierDAO;
        this.productDAO = (DBMSProductDAO) productDAO;
    }

    private UUID getSupplierOrderId(SupplierOrder supplierOrder) {
        return supplierOrder.getId();
    }

    private OrderState getSupplierOrderState(SupplierOrder supplierOrder) {
        return supplierOrder.getState();
    }

    private SupplierOrder copy(SupplierOrder supplierOrder) {
        return supplierOrder.copy();
    }

    private SupplierOrder findByIdFromCache(UUID id) {
        return this.findFromCache(id, this::getSupplierOrderId);
    }

    private List<SupplierOrder> findByStateFromCache(OrderState state) {
        return this.findMatchesFromCache(state, this::getSupplierOrderState);
    }

    private SupplierOrder findByIdFromPersistence(UUID id) throws DatabaseException {
        return this.findFromPersistence(this.tableName, "id", id,
                this::getSupplierOrder, EntityException.Entity.SUPPLIER_ORDER);
    }

    private List<SupplierOrder> findByStateFromPersistence(OrderState state) throws DatabaseException {
        return this.findMatchesFromPersistence(this.tableName, "orderState", state.toString(),
                this::getSupplierOrderList, EntityException.Entity.SUPPLIER_ORDER);
    }

    private SupplierOrder getSupplierOrder(ResultSet rs) throws DatabaseException {
        try {
            byte[] bytesId = rs.getBytes("id");
            LocalDateTime registrationDate = rs.getObject("registrationDate", LocalDateTime.class);
            String productsOrderedDBFormatJson = rs.getString("productsOrdered");
            String orderStateString = rs.getString("orderState");
            byte[] bytesSupplierId = rs.getBytes("supplierId");
            LocalDateTime deliveryDate = rs.getObject("deliveryDate", LocalDateTime.class);
            byte[] bytesWarehouseWorkerId = rs.getBytes("warehouseWorkerId");

            Type type = new TypeToken<HashMap<UUID, Integer>>() {
            }.getType();
            HashMap<UUID, Integer> productsOrderedDBFormat = gson.fromJson(productsOrderedDBFormatJson, type);
            ProductsWithQuantity productsOrdered = new ProductsWithQuantity();
            for (Map.Entry<UUID, Integer> entry : productsOrderedDBFormat.entrySet()) {
                Product product = this.productDAO.findProduct(entry.getKey());
                productsOrdered.add(new ProductWithQuantity(product, entry.getValue()));
            }

            Supplier supplier = this.supplierDAO.findSupplier(this.bytesToUUID(bytesSupplierId));

            return new SupplierOrder(this.bytesToUUID(bytesId), registrationDate, productsOrdered,
                    OrderState.getState(orderStateString), new SupplierOrderData(supplier, deliveryDate,
                    this.bytesToUUID(bytesWarehouseWorkerId)));
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_FROM, EntityException.Entity.SUPPLIER_ORDER, e);
        }
    }

    private List<SupplierOrder> getSupplierOrderList(ResultSet rs) throws DatabaseException {
        return this.getEntityList(rs, this::getSupplierOrder, EntityException.Entity.SUPPLIER_ORDER);
    }

    private Map<UUID, Integer> toFormatForDB(ProductsWithQuantity productsWithQuantity) {
        Map<UUID, Integer> result = new HashMap<>();
        for (ProductWithQuantity pwq : productsWithQuantity.getProductWithQuantityList()) {
            result.put(pwq.getProduct().getId(), pwq.getQuantity());
        }
        return result;
    }

    private void loadPreparedStatement(PreparedStatement pstmt, Statement.Type statementType, SupplierOrder supplierOrder)
            throws DatabaseException {
        try {
            switch (statementType) {
                case SAVE -> {
                    pstmt.setBytes(1, this.uuidToBytes(supplierOrder.getId()));
                    pstmt.setObject(2, supplierOrder.getRegistrationDate());
                    pstmt.setString(3, gson.toJson(this.toFormatForDB(supplierOrder.getProductsOrdered())));
                    pstmt.setString(4, supplierOrder.getState().toString());
                    pstmt.setBytes(5, this.uuidToBytes(supplierOrder.getSupplier().getId()));
                }
                case UPDATE -> {
                    pstmt.setObject(1, supplierOrder.getRegistrationDate());
                    pstmt.setString(2, gson.toJson(this.toFormatForDB(supplierOrder.getProductsOrdered())));
                    pstmt.setString(3, supplierOrder.getState().toString());
                    pstmt.setBytes(4, this.uuidToBytes(supplierOrder.getSupplier().getId()));
                    pstmt.setObject(5, supplierOrder.getDeliveryDate());
                    pstmt.setBytes(6, this.uuidToBytes(supplierOrder.getWarehouseWorkerId()));
                    pstmt.setBytes(7, this.uuidToBytes(supplierOrder.getId()));
                }
                case DELETE -> pstmt.setBytes(1, this.uuidToBytes(supplierOrder.getId()));
            }
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_TO, EntityException.Entity.SUPPLIER_ORDER);
        }
    }

    private void saveNewSupplierOrder(SupplierOrder supplierOrder) throws DatabaseException {
        this.saveNewEntity(supplierOrder, this::loadSupplierOrder, this::getSupplierOrderId, this::copy,
                "INSERT INTO " + this.tableName + " (id, registrationDate, productsOrdered, orderState, supplierId) VALUES (?,?,?,?,?);",
                this::loadPreparedStatement, EntityException.Entity.SUPPLIER_ORDER);
    }

    private void updateSupplierOrder(SupplierOrder supplierOrder) throws DatabaseException {
        this.updateEntity(supplierOrder, this::loadSupplierOrder, this::getSupplierOrderId,
                "UPDATE " + this.tableName + " SET registrationDate = ?, productsOrdered = ?, orderState = ?, supplierId = ?, deliveryDate = ?, warehouseWorkerId = ? WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.SUPPLIER_ORDER);
    }

    private void deleteSupplierOrder(SupplierOrder supplierOrder) throws DatabaseException {
        this.deleteEntity(supplierOrder, this::loadSupplierOrder, this::getSupplierOrderId,
                "DELETE FROM " + this.tableName + " WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.SUPPLIER_ORDER);
    }

    @Override
    public SupplierOrder loadSupplierOrder(UUID id) throws DatabaseException {
        return this.findSingleResult(id, this::findByIdFromCache, this::findByIdFromPersistence);
    }

    @Override
    public List<SupplierOrder> loadByState(OrderState state) throws DatabaseException {
        return this.findMultipleResults(state, this::getSupplierOrderId, this::findByStateFromCache, this::findByStateFromPersistence);
    }

    @Override
    public List<SupplierOrder> loadAll() throws DatabaseException {
        return this.loadAll(this.tableName, this::getSupplierOrder, EntityException.Entity.SUPPLIER_ORDER);
    }

    @Override
    public void executeTransaction(List<Statement<SupplierOrder>> statements) throws DatabaseException {
        this.executeTransaction(statements, this::saveNewSupplierOrder, this::updateSupplierOrder,
                this::deleteSupplierOrder);
    }

    @Override
    public void keepIntegrity(List<Statement<SupplierOrder>> statements) throws DatabaseException {
        this.keepIntegrity(statements, this::saveNewSupplierOrder, this::updateSupplierOrder,
                this::deleteSupplierOrder);
    }
}

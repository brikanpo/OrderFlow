package it.orderflow.dao.dbms;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.orderflow.control.Statement;
import it.orderflow.dao.ClientDAO;
import it.orderflow.dao.ClientOrderDAO;
import it.orderflow.dao.ProductInStockDAO;
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

public class DBMSClientOrderDAO extends DBMSGeneralDAO<ClientOrder> implements ClientOrderDAO {

    private final Gson gson = new Gson();
    private final String TABLE_NAME = "clientOrder";
    private final String ORDER_STATE = "orderState";
    private final DBMSClientDAO clientDAO;
    private final DBMSProductInStockDAO productInStockDAO;

    public DBMSClientOrderDAO(ClientDAO clientDAO, ProductInStockDAO productInStockDAO) {
        super();
        this.clientDAO = (DBMSClientDAO) clientDAO;
        this.productInStockDAO = (DBMSProductInStockDAO) productInStockDAO;
    }

    private UUID getClientOrderId(ClientOrder clientOrder) {
        return clientOrder.getId();
    }

    private UUID getClientOrderClientId(ClientOrder clientOrder) {
        return clientOrder.getClient().getId();
    }

    private OrderState getClientOrderState(ClientOrder clientOrder) {
        return clientOrder.getState();
    }

    private ClientOrder copy(ClientOrder clientOrder) {
        return clientOrder.copy();
    }

    private ClientOrder findByIdFromCache(UUID id) {
        return this.findFromCache(id, this::getClientOrderId);
    }

    private List<ClientOrder> findByStateFromCache(OrderState state) {
        return this.findMatchesFromCache(state, this::getClientOrderState);
    }

    private List<ClientOrder> findByStateAndClientIdFromCache(OrderState state, UUID clientId) {
        return this.findMatchesFromCache(state, this::getClientOrderState, clientId,
                this::getClientOrderClientId);
    }

    private ClientOrder findByIdFromPersistence(UUID id) throws DatabaseException {
        return this.findFromPersistence(this.TABLE_NAME, "id", id,
                this::getClientOrder, EntityException.Entity.CLIENT_ORDER);
    }

    private List<ClientOrder> findByStateFromPersistence(OrderState state) throws DatabaseException {
        return this.findMatchesFromPersistence(this.TABLE_NAME, this.ORDER_STATE, state,
                this::getClientOrderList, EntityException.Entity.CLIENT_ORDER);
    }

    private List<ClientOrder> findByStateAndClientIdFromPersistence(OrderState state, UUID clientId) throws DatabaseException {
        return this.findMatchesFromPersistence(this.TABLE_NAME, this.ORDER_STATE, state.toString(),
                "clientId", clientId, this::getClientOrderList, EntityException.Entity.CLIENT_ORDER);
    }

    private ClientOrder getClientOrder(ResultSet rs) throws DatabaseException {
        try {
            byte[] bytesId = rs.getBytes("id");
            LocalDateTime registrationDate = rs.getObject("registrationDate", LocalDateTime.class);
            String productsOrderedDBFormatJson = rs.getString("productsOrdered");
            String orderStateString = rs.getString(this.ORDER_STATE);
            byte[] bytesRepresentativeId = rs.getBytes("representativeId");
            byte[] bytesClientId = rs.getBytes("clientId");
            byte[] bytesWarehouseWorkerId = rs.getBytes("warehouseWorkerId");
            LocalDateTime deliveryDate = rs.getObject("deliveryDate", LocalDateTime.class);
            byte[] bytesDeliveryWorkerId = rs.getBytes("deliveryWorkerId");

            Type type = new TypeToken<HashMap<UUID, Integer>>() {
            }.getType();
            HashMap<UUID, Integer> productsOrderedDBFormat = gson.fromJson(productsOrderedDBFormatJson, type);
            ProductsWithQuantity productsOrdered = new ProductsWithQuantity();
            for (Map.Entry<UUID, Integer> entry : productsOrderedDBFormat.entrySet()) {
                ProductInStock productInStock = this.productInStockDAO.findProductInStock(entry.getKey());
                productsOrdered.add(new ProductWithQuantity(productInStock, entry.getValue()));
            }

            Client client = this.clientDAO.findClient(this.bytesToUUID(bytesClientId));

            return new ClientOrder(this.bytesToUUID(bytesId), registrationDate, productsOrdered,
                    OrderState.getState(orderStateString), this.bytesToUUID(bytesRepresentativeId),
                    client, this.bytesToUUID(bytesWarehouseWorkerId), deliveryDate,
                    this.bytesToUUID(bytesDeliveryWorkerId));
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_FROM, EntityException.Entity.CLIENT_ORDER, e);
        }
    }

    private List<ClientOrder> getClientOrderList(ResultSet rs) throws DatabaseException {
        return this.getEntityList(rs, this::getClientOrder, EntityException.Entity.CLIENT_ORDER);
    }

    private Map<UUID, Integer> toFormatForDB(ProductsWithQuantity productsWithQuantity) {
        Map<UUID, Integer> result = new HashMap<>();
        for (ProductWithQuantity pwq : productsWithQuantity.getProductWithQuantityList()) {
            result.put(pwq.getProduct().getId(), pwq.getQuantity());
        }
        return result;
    }

    private void loadPreparedStatement(PreparedStatement pstmt, Statement.Type statementType, ClientOrder clientOrder)
            throws DatabaseException {
        try {
            switch (statementType) {
                case SAVE -> {
                    pstmt.setBytes(1, this.uuidToBytes(clientOrder.getId()));
                    pstmt.setObject(2, clientOrder.getRegistrationDate());
                    pstmt.setString(3, gson.toJson(this.toFormatForDB(clientOrder.getProductsOrdered())));
                    pstmt.setString(4, clientOrder.getState().toString());
                    pstmt.setBytes(5, this.uuidToBytes(clientOrder.getRepresentativeId()));
                    pstmt.setBytes(6, this.uuidToBytes(clientOrder.getClient().getId()));

                }
                case UPDATE -> {
                    pstmt.setObject(1, clientOrder.getRegistrationDate());
                    pstmt.setString(2, gson.toJson(this.toFormatForDB(clientOrder.getProductsOrdered())));
                    pstmt.setString(3, clientOrder.getState().toString());
                    pstmt.setBytes(4, this.uuidToBytes(clientOrder.getRepresentativeId()));
                    pstmt.setBytes(5, this.uuidToBytes(clientOrder.getClient().getId()));
                    pstmt.setBytes(6, this.uuidToBytes(clientOrder.getWarehouseWorkerId()));
                    pstmt.setObject(7, clientOrder.getDeliveryDate());
                    pstmt.setBytes(8, this.uuidToBytes(clientOrder.getDeliveryWorkerId()));
                    pstmt.setBytes(9, this.uuidToBytes(clientOrder.getId()));
                }
                case DELETE -> pstmt.setBytes(1, this.uuidToBytes(clientOrder.getId()));
            }
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_TO, EntityException.Entity.CLIENT_ORDER);
        }
    }

    private void saveNewClientOrder(ClientOrder clientOrder) throws DatabaseException {
        this.saveNewEntity(clientOrder, this::loadClientOrder, this::getClientOrderId, this::copy,
                "INSERT INTO " + this.TABLE_NAME + " (id, registrationDate, productsOrdered, orderState, representativeId, " +
                        "clientId) VALUES (?,?,?,?,?,?);",
                this::loadPreparedStatement, EntityException.Entity.CLIENT_ORDER);
    }

    private void updateClientOrder(ClientOrder clientOrder) throws DatabaseException {
        this.updateEntity(clientOrder, this::loadClientOrder, this::getClientOrderId,
                "UPDATE " + this.TABLE_NAME + " SET registrationDate = ?, productsOrdered = ?, orderState = ?, " +
                        "representativeId = ?, clientId = ?, warehouseWorkerId = ?, deliveryDate = ?, deliveryWorkerId = ? WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.CLIENT_ORDER);
    }

    private void deleteClientOrder(ClientOrder clientOrder) throws DatabaseException {
        this.deleteEntity(clientOrder, this::loadClientOrder, this::getClientOrderId,
                "DELETE FROM " + this.TABLE_NAME + " WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.CLIENT_ORDER);
    }

    @Override
    public ClientOrder loadClientOrder(UUID id) throws DatabaseException {
        return this.findSingleResult(id, this::findByIdFromCache, this::findByIdFromPersistence);
    }

    @Override
    public List<ClientOrder> loadByState(OrderState state) throws DatabaseException {
        return this.findMultipleResults(state, this::getClientOrderId, this::findByStateFromCache, this::findByStateFromPersistence);
    }

    @Override
    public List<ClientOrder> loadByStateAndClientId(OrderState state, UUID clientId) throws DatabaseException {
        return this.findMultipleResults(state, clientId, this::getClientOrderId, this::findByStateAndClientIdFromCache,
                this::findByStateAndClientIdFromPersistence);
    }

    @Override
    public List<ClientOrder> loadAll() throws DatabaseException {
        return this.loadAll(this.TABLE_NAME, this::getClientOrder, EntityException.Entity.CLIENT_ORDER);
    }

    @Override
    public void executeTransaction(List<Statement<ClientOrder>> statements) throws DatabaseException {
        this.executeTransaction(statements, this::saveNewClientOrder, this::updateClientOrder,
                this::deleteClientOrder);
    }

    @Override
    public void keepIntegrity(List<Statement<ClientOrder>> statements) throws DatabaseException {
        this.keepIntegrity(statements, this::saveNewClientOrder, this::updateClientOrder,
                this::deleteClientOrder);
    }
}

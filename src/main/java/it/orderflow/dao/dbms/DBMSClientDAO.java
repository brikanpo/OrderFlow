package it.orderflow.dao.dbms;

import com.google.gson.Gson;
import it.orderflow.control.Statement;
import it.orderflow.dao.ClientDAO;
import it.orderflow.exceptions.DatabaseException;
import it.orderflow.exceptions.EntityException;
import it.orderflow.model.Address;
import it.orderflow.model.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DBMSClientDAO extends DBMSGeneralDAO<Client> implements ClientDAO {

    private final Gson gson = new Gson();
    private final String TABLE_NAME = "client";

    private UUID getClientId(Client client) {
        return client.getId();
    }

    private String getClientEmail(Client client) {
        return client.getEmail();
    }

    private Client copy(Client client) {
        return client.copy();
    }

    private Client findByIdFromCache(UUID id) {
        return this.findFromCache(id, this::getClientId);
    }

    private Client findByEmailFromCache(String email) {
        return this.findFromCache(email, this::getClientEmail);
    }

    private Client findByIdFromPersistence(UUID id) throws DatabaseException {
        return this.findFromPersistence(this.TABLE_NAME, "id", id,
                this::getClient, EntityException.Entity.CLIENT);
    }

    private Client findByEmailFromPersistence(String email) throws DatabaseException {
        return this.findFromPersistence(this.TABLE_NAME, "email", email,
                this::getClient, EntityException.Entity.CLIENT);
    }

    private Client getClient(ResultSet rs) throws DatabaseException {
        try {
            byte[] bytes = rs.getBytes("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            String addressJSON = rs.getString("address");

            Address address = gson.fromJson(addressJSON, Address.class);

            return new Client(this.bytesToUUID(bytes), name, email, phone, address);
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_FROM, EntityException.Entity.CLIENT, e);
        }
    }

    private void loadPreparedStatement(PreparedStatement pstmt, Statement.Type statementType, Client client)
            throws DatabaseException {
        try {
            switch (statementType) {
                case SAVE -> {
                    pstmt.setBytes(1, this.uuidToBytes(client.getId()));
                    pstmt.setString(2, client.getName());
                    pstmt.setString(3, client.getEmail());
                    pstmt.setString(4, client.getPhone());
                    pstmt.setString(5, gson.toJson(client.getAddress()));
                }
                case UPDATE -> {
                    pstmt.setString(1, client.getName());
                    pstmt.setString(2, client.getEmail());
                    pstmt.setString(3, client.getPhone());
                    pstmt.setString(4, gson.toJson(client.getAddress()));
                    pstmt.setBytes(5, this.uuidToBytes(client.getId()));
                }
                case DELETE -> pstmt.setBytes(1, this.uuidToBytes(client.getId()));
            }
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_TO, EntityException.Entity.CLIENT);
        }
    }

    private void saveNewClient(Client client) throws DatabaseException {
        this.saveNewEntity(client, this::loadClient, this::getClientEmail, this::copy,
                "INSERT INTO " + this.TABLE_NAME + " (id, name, email, phone, address) VALUES (?,?,?,?,?);",
                this::loadPreparedStatement, EntityException.Entity.CLIENT);
    }

    private void updateClient(Client client) throws DatabaseException {
        this.updateEntity(client, this::findClient, this::getClientId,
                "UPDATE " + this.TABLE_NAME + " SET name = ?, email = ?, phone = ?, address = ? WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.CLIENT);
    }

    private void deleteClient(Client client) throws DatabaseException {
        this.deleteEntity(client, this::findClient, this::getClientId,
                "DELETE FROM " + this.TABLE_NAME + " WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.CLIENT);
    }

    public Client findClient(UUID id) throws DatabaseException {
        return this.findSingleResult(id, this::findByIdFromCache, this::findByIdFromPersistence);
    }

    @Override
    public Client loadClient(String email) throws DatabaseException {
        return this.findSingleResult(email, this::findByEmailFromCache, this::findByEmailFromPersistence);
    }

    @Override
    public List<Client> loadAll() throws DatabaseException {
        return this.loadAll(this.TABLE_NAME, this::getClient, EntityException.Entity.CLIENT);
    }

    @Override
    public void executeTransaction(List<Statement<Client>> statements) throws DatabaseException {
        this.executeTransaction(statements, this::saveNewClient, this::updateClient, this::deleteClient);
    }

    @Override
    public void keepIntegrity(List<Statement<Client>> statements) throws DatabaseException {
        this.keepIntegrity(statements, this::saveNewClient, this::updateClient, this::deleteClient);
    }
}

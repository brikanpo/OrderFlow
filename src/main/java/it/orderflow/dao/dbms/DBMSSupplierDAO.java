package it.orderflow.dao.dbms;

import it.orderflow.control.Statement;
import it.orderflow.dao.SupplierDAO;
import it.orderflow.exceptions.DatabaseException;
import it.orderflow.exceptions.EntityException;
import it.orderflow.model.Supplier;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DBMSSupplierDAO extends DBMSGeneralDAO<Supplier> implements SupplierDAO {

    private final String tableName = "supplier";

    private UUID getSupplierId(Supplier supplier) {
        return supplier.getId();
    }

    private String getSupplierEmail(Supplier supplier) {
        return supplier.getEmail();
    }

    private Supplier copy(Supplier supplier) {
        return supplier.copy();
    }

    private Supplier findByIdFromCache(UUID id) {
        return this.findFromCache(id, this::getSupplierId);
    }

    private Supplier findByEmailFromCache(String email) {
        return this.findFromCache(email, this::getSupplierEmail);
    }

    private Supplier findByIdFromPersistence(UUID id) throws DatabaseException {
        return this.findFromPersistence(this.tableName, "id", id,
                this::getSupplier, EntityException.Entity.SUPPLIER);
    }

    private Supplier findByEmailFromPersistence(String email) throws DatabaseException {
        return this.findFromPersistence(this.tableName, "email", email,
                this::getSupplier, EntityException.Entity.SUPPLIER);
    }

    private Supplier getSupplier(ResultSet rs) throws DatabaseException {
        try {
            byte[] bytes = rs.getBytes("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            BigDecimal transportFee = rs.getBigDecimal("transportFee");

            return new Supplier(this.bytesToUUID(bytes), name, email, phone, transportFee);
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_FROM, EntityException.Entity.SUPPLIER, e);
        }
    }

    private void loadPreparedStatement(PreparedStatement pstmt, Statement.Type statementType, Supplier supplier)
            throws DatabaseException {
        try {
            switch (statementType) {
                case SAVE -> {
                    pstmt.setBytes(1, this.uuidToBytes(supplier.getId()));
                    pstmt.setString(2, supplier.getName());
                    pstmt.setString(3, supplier.getEmail());
                    pstmt.setString(4, supplier.getPhone());

                    BigDecimal transportFee = supplier.getTransportFee();
                    if (transportFee != null) pstmt.setBigDecimal(5, transportFee);
                    else pstmt.setString(5, null);
                }
                case UPDATE -> {
                    pstmt.setString(1, supplier.getName());
                    pstmt.setString(2, supplier.getEmail());
                    pstmt.setString(3, supplier.getPhone());

                    BigDecimal transportFee = supplier.getTransportFee();
                    if (transportFee != null) pstmt.setBigDecimal(4, transportFee);
                    else pstmt.setString(4, null);

                    pstmt.setBytes(5, this.uuidToBytes(supplier.getId()));
                }
                case DELETE -> pstmt.setBytes(1, this.uuidToBytes(supplier.getId()));
            }
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_TO, EntityException.Entity.SUPPLIER);
        }
    }

    private void saveNewSupplier(Supplier supplier) throws DatabaseException {
        this.saveNewEntity(supplier, this::loadSupplier, this::getSupplierEmail, this::copy,
                "INSERT INTO " + this.tableName + " (id, name, email, phone, transportFee) VALUES (?,?,?,?,?);",
                this::loadPreparedStatement, EntityException.Entity.SUPPLIER);
    }

    private void updateSupplier(Supplier supplier) throws DatabaseException {
        this.updateEntity(supplier, this::findSupplier, this::getSupplierId,
                "UPDATE " + this.tableName + " SET name = ?, email = ?, phone = ?, transportFee = ? WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.SUPPLIER);
    }

    private void deleteSupplier(Supplier supplier) throws DatabaseException {
        this.deleteEntity(supplier, this::findSupplier, this::getSupplierId,
                "DELETE FROM " + this.tableName + " WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.SUPPLIER);
    }

    public Supplier findSupplier(UUID id) throws DatabaseException {
        return this.findSingleResult(id, this::findByIdFromCache, this::findByIdFromPersistence);
    }

    @Override
    public Supplier loadSupplier(String email) throws DatabaseException {
        return this.findSingleResult(email, this::findByEmailFromCache, this::findByEmailFromPersistence);
    }

    @Override
    public List<Supplier> loadAll() throws DatabaseException {
        return this.loadAll(this.tableName, this::getSupplier, EntityException.Entity.SUPPLIER);
    }

    @Override
    public void executeTransaction(List<Statement<Supplier>> statements) throws DatabaseException {
        this.executeTransaction(statements, this::saveNewSupplier, this::updateSupplier, this::deleteSupplier);
    }

    @Override
    public void keepIntegrity(List<Statement<Supplier>> statements) throws DatabaseException {
        this.keepIntegrity(statements, this::saveNewSupplier, this::updateSupplier, this::deleteSupplier);
    }
}

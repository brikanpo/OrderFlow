package it.orderflow.dao.dbms;

import com.google.gson.Gson;
import it.orderflow.control.Statement;
import it.orderflow.dao.SupplierArticleDAO;
import it.orderflow.exceptions.DatabaseException;
import it.orderflow.exceptions.EntityException;
import it.orderflow.model.Attributes;
import it.orderflow.model.SupplierArticle;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DBMSSupplierArticleDAO extends DBMSGeneralDAO<SupplierArticle> implements SupplierArticleDAO {

    private final Gson gson = new Gson();
    private final String tableName = "supplierArticle";

    private UUID getSupplierArticleId(SupplierArticle supplierArticle) {
        return supplierArticle.getId();
    }

    private String getSupplierArticleName(SupplierArticle supplierArticle) {
        return supplierArticle.getName();
    }

    private SupplierArticle copy(SupplierArticle supplierArticle) {
        return supplierArticle.copy();
    }

    private SupplierArticle findByIdFromCache(UUID id) {
        return this.findFromCache(id, this::getSupplierArticleId);
    }

    private SupplierArticle findByNameFromCache(String name) {
        return this.findFromCache(name, this::getSupplierArticleName);
    }

    private SupplierArticle findByIdFromPersistence(UUID id) throws DatabaseException {
        return this.findFromPersistence(this.tableName, "id", id,
                this::getSupplierArticle, EntityException.Entity.SUPPLIER_ARTICLE);
    }

    private SupplierArticle findByNameFromPersistence(String name) throws DatabaseException {
        return this.findFromPersistence(this.tableName, "name", name,
                this::getSupplierArticle, EntityException.Entity.SUPPLIER_ARTICLE);
    }

    private SupplierArticle getSupplierArticle(ResultSet rs) throws DatabaseException {
        try {
            byte[] bytesId = rs.getBytes("id");
            String name = rs.getString("name");
            String category = rs.getString("category");
            String description = rs.getString("description");
            String articleAttributesJson = rs.getString("articleAttributes");
            String possibleAttributesJson = rs.getString("possibleAttributes");
            BigDecimal iva = rs.getBigDecimal("iva");
            byte[] bytesSupplierArticleId = rs.getBytes("supplierId");

            Attributes articleAttributes = gson.fromJson(articleAttributesJson, Attributes.class);
            Attributes possibleAttributes = gson.fromJson(possibleAttributesJson, Attributes.class);

            return new SupplierArticle(this.bytesToUUID(bytesId), name, category, description,
                    articleAttributes, possibleAttributes, iva, this.bytesToUUID(bytesSupplierArticleId));
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_FROM, EntityException.Entity.SUPPLIER_ARTICLE, e);
        }
    }

    private void loadPreparedStatement(PreparedStatement pstmt, Statement.Type statementType, SupplierArticle supplierArticle)
            throws DatabaseException {
        try {
            switch (statementType) {
                case SAVE -> {
                    pstmt.setBytes(1, this.uuidToBytes(supplierArticle.getId()));
                    pstmt.setString(2, supplierArticle.getName());
                    pstmt.setString(3, supplierArticle.getCategory());
                    pstmt.setString(4, supplierArticle.getDescription());
                    pstmt.setString(5, gson.toJson(supplierArticle.getArticleAttributes()));
                    pstmt.setString(6, gson.toJson(supplierArticle.getPossibleAttributes()));
                    pstmt.setBigDecimal(7, supplierArticle.getIva());
                    pstmt.setBytes(8, this.uuidToBytes(supplierArticle.getSupplierId()));
                }
                case UPDATE -> {
                    pstmt.setString(1, supplierArticle.getName());
                    pstmt.setString(2, supplierArticle.getCategory());
                    pstmt.setString(3, supplierArticle.getDescription());
                    pstmt.setString(4, gson.toJson(supplierArticle.getArticleAttributes()));
                    pstmt.setString(5, gson.toJson(supplierArticle.getPossibleAttributes()));
                    pstmt.setBigDecimal(6, supplierArticle.getIva());
                    pstmt.setBytes(7, this.uuidToBytes(supplierArticle.getSupplierId()));
                    pstmt.setBytes(8, this.uuidToBytes(supplierArticle.getId()));
                }
                case DELETE -> pstmt.setBytes(1, this.uuidToBytes(supplierArticle.getId()));
            }
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_TO, EntityException.Entity.SUPPLIER_ARTICLE);
        }
    }

    private void saveNewSupplierArticle(SupplierArticle supplierArticle) throws DatabaseException {
        this.saveNewEntity(supplierArticle, this::loadSupplierArticle, this::getSupplierArticleName, this::copy,
                "INSERT INTO " + this.tableName + " (id, name, category, description, articleAttributes, possibleAttributes, iva, supplierId) VALUES (?,?,?,?,?,?,?,?);",
                this::loadPreparedStatement, EntityException.Entity.SUPPLIER_ARTICLE);
    }

    private void updateSupplierArticle(SupplierArticle supplierArticle) throws DatabaseException {
        this.updateEntity(supplierArticle, this::findSupplierArticle, this::getSupplierArticleId,
                "UPDATE " + this.tableName + " SET name = ?, category = ?, description = ?, articleAttributes = ?, possibleAttributes = ?, iva = ?, supplierId = ? WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.SUPPLIER_ARTICLE);
    }

    private void deleteSupplierArticle(SupplierArticle supplierArticle) throws DatabaseException {
        this.deleteEntity(supplierArticle, this::findSupplierArticle, this::getSupplierArticleId,
                "DELETE FROM " + this.tableName + " WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.SUPPLIER_ARTICLE);
    }

    public SupplierArticle findSupplierArticle(UUID id) throws DatabaseException {
        return this.findSingleResult(id, this::findByIdFromCache, this::findByIdFromPersistence);
    }

    @Override
    public SupplierArticle loadSupplierArticle(String name) throws DatabaseException {
        return this.findSingleResult(name, this::findByNameFromCache, this::findByNameFromPersistence);
    }

    @Override
    public List<SupplierArticle> loadAll() throws DatabaseException {
        return this.loadAll(this.tableName, this::getSupplierArticle, EntityException.Entity.SUPPLIER_ARTICLE);
    }

    @Override
    public void executeTransaction(List<Statement<SupplierArticle>> statements) throws DatabaseException {
        this.executeTransaction(statements, this::saveNewSupplierArticle, this::updateSupplierArticle,
                this::deleteSupplierArticle);
    }

    @Override
    public void keepIntegrity(List<Statement<SupplierArticle>> statements) throws DatabaseException {
        this.keepIntegrity(statements, this::saveNewSupplierArticle, this::updateSupplierArticle,
                this::deleteSupplierArticle);
    }
}

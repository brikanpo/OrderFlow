package it.orderflow.dao.dbms;

import com.google.gson.Gson;
import it.orderflow.control.Statement;
import it.orderflow.dao.ClientArticleDAO;
import it.orderflow.exceptions.DatabaseException;
import it.orderflow.exceptions.EntityException;
import it.orderflow.model.ArticleData;
import it.orderflow.model.Attributes;
import it.orderflow.model.ClientArticle;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DBMSClientArticleDAO extends DBMSGeneralDAO<ClientArticle> implements ClientArticleDAO {

    private final Gson gson = new Gson();
    private final String tableName = "clientArticle";

    private UUID getClientArticleId(ClientArticle clientArticle) {
        return clientArticle.getId();
    }

    private String getClientArticleName(ClientArticle clientArticle) {
        return clientArticle.getName();
    }

    private ClientArticle copy(ClientArticle clientArticle) {
        return clientArticle.copy();
    }

    private ClientArticle findByIdFromCache(UUID id) {
        return this.findFromCache(id, this::getClientArticleId);
    }

    private ClientArticle findByNameFromCache(String name) {
        return this.findFromCache(name, this::getClientArticleName);
    }

    private ClientArticle findByIdFromPersistence(UUID id) throws DatabaseException {
        return this.findFromPersistence(this.tableName, "id", id,
                this::getClientArticle, EntityException.Entity.CLIENT_ARTICLE);
    }

    private ClientArticle findByNameFromPersistence(String name) throws DatabaseException {
        return this.findFromPersistence(this.tableName, "name", name,
                this::getClientArticle, EntityException.Entity.CLIENT_ARTICLE);
    }

    private ClientArticle getClientArticle(ResultSet rs) throws DatabaseException {
        try {
            byte[] bytesId = rs.getBytes("id");
            String name = rs.getString("name");
            String category = rs.getString("category");
            String description = rs.getString("description");
            String articleAttributesJson = rs.getString("articleAttributes");
            String possibleAttributesJson = rs.getString("possibleAttributes");
            BigDecimal iva = rs.getBigDecimal("iva");
            byte[] bytesSupplierArticleId = rs.getBytes("supplierArticleId");

            Attributes articleAttributes = gson.fromJson(articleAttributesJson, Attributes.class);
            Attributes possibleAttributes = gson.fromJson(possibleAttributesJson, Attributes.class);

            return new ClientArticle(this.bytesToUUID(bytesId), new ArticleData(name, category, description,
                    articleAttributes, possibleAttributes, iva), this.bytesToUUID(bytesSupplierArticleId));
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_FROM, EntityException.Entity.CLIENT_ARTICLE, e);
        }
    }

    private void loadPreparedStatement(PreparedStatement pstmt, Statement.Type statementType, ClientArticle clientArticle)
            throws DatabaseException {
        try {
            switch (statementType) {
                case SAVE -> {
                    pstmt.setBytes(1, this.uuidToBytes(clientArticle.getId()));
                    pstmt.setString(2, clientArticle.getName());
                    pstmt.setString(3, clientArticle.getCategory());
                    pstmt.setString(4, clientArticle.getDescription());
                    pstmt.setString(5, gson.toJson(clientArticle.getArticleAttributes()));
                    pstmt.setString(6, gson.toJson(clientArticle.getPossibleAttributes()));
                    pstmt.setBigDecimal(7, clientArticle.getIva());
                    pstmt.setBytes(8, this.uuidToBytes(clientArticle.getSupplierArticleId()));
                }
                case UPDATE -> {
                    pstmt.setString(1, clientArticle.getName());
                    pstmt.setString(2, clientArticle.getCategory());
                    pstmt.setString(3, clientArticle.getDescription());
                    pstmt.setString(4, gson.toJson(clientArticle.getArticleAttributes()));
                    pstmt.setString(5, gson.toJson(clientArticle.getPossibleAttributes()));
                    pstmt.setBigDecimal(6, clientArticle.getIva());
                    pstmt.setBytes(7, this.uuidToBytes(clientArticle.getSupplierArticleId()));
                    pstmt.setBytes(8, this.uuidToBytes(clientArticle.getId()));
                }
                case DELETE -> pstmt.setBytes(1, this.uuidToBytes(clientArticle.getId()));
            }
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_TO, EntityException.Entity.CLIENT_ARTICLE);
        }
    }

    private void saveNewClientArticle(ClientArticle clientArticle) throws DatabaseException {
        this.saveNewEntity(clientArticle, this::loadClientArticle, this::getClientArticleName, this::copy,
                "INSERT INTO " + this.tableName + " (id, name, category, description, articleAttributes, possibleAttributes, iva, supplierArticleId) VALUES (?,?,?,?,?,?,?,?);",
                this::loadPreparedStatement, EntityException.Entity.CLIENT_ARTICLE);
    }

    private void updateClientArticle(ClientArticle clientArticle) throws DatabaseException {
        this.updateEntity(clientArticle, this::findClientArticle, this::getClientArticleId,
                "UPDATE " + this.tableName + " SET name = ?, category = ?, description = ?, articleAttributes = ?, possibleAttributes = ?, iva = ?, supplierArticleId = ? WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.CLIENT_ARTICLE);
    }

    private void deleteClientArticle(ClientArticle clientArticle) throws DatabaseException {
        this.deleteEntity(clientArticle, this::findClientArticle, this::getClientArticleId,
                "DELETE FROM " + this.tableName + " WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.CLIENT_ARTICLE);
    }

    public ClientArticle findClientArticle(UUID id) throws DatabaseException {
        return this.findSingleResult(id, this::findByIdFromCache, this::findByIdFromPersistence);
    }

    @Override
    public ClientArticle loadClientArticle(String name) throws DatabaseException {
        return this.findSingleResult(name, this::findByNameFromCache, this::findByNameFromPersistence);
    }

    @Override
    public List<ClientArticle> loadAll() throws DatabaseException {
        return this.loadAll(this.tableName, this::getClientArticle, EntityException.Entity.CLIENT_ARTICLE);
    }

    @Override
    public void executeTransaction(List<Statement<ClientArticle>> statements) throws DatabaseException {
        this.executeTransaction(statements, this::saveNewClientArticle, this::updateClientArticle,
                this::deleteClientArticle);
    }

    @Override
    public void keepIntegrity(List<Statement<ClientArticle>> statements) throws DatabaseException {
        this.keepIntegrity(statements, this::saveNewClientArticle, this::updateClientArticle,
                this::deleteClientArticle);
    }
}

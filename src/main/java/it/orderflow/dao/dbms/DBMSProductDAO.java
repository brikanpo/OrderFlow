package it.orderflow.dao.dbms;

import com.google.gson.Gson;
import it.orderflow.control.Statement;
import it.orderflow.dao.ProductDAO;
import it.orderflow.dao.SupplierArticleDAO;
import it.orderflow.exceptions.DatabaseException;
import it.orderflow.exceptions.EntityException;
import it.orderflow.model.Article;
import it.orderflow.model.Attributes;
import it.orderflow.model.Product;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DBMSProductDAO extends DBMSGeneralDAO<Product> implements ProductDAO {

    private final Gson gson = new Gson();
    private final String TABLE_NAME = "supplierProduct";

    private final DBMSSupplierArticleDAO supplierArticleDAO;

    public DBMSProductDAO(SupplierArticleDAO supplierArticleDAO) {
        super();
        this.supplierArticleDAO = (DBMSSupplierArticleDAO) supplierArticleDAO;
    }

    private UUID getProductId(Product product) {
        return product.getId();
    }

    private String getProductCode(Product product) {
        return product.getCode();
    }

    private Product copy(Product product) {
        return product.copy();
    }

    private Product findByIdFromCache(UUID id) {
        return this.findFromCache(id, this::getProductId);
    }

    private Product findByCodeFromCache(String code) {
        return this.findFromCache(code, this::getProductCode);
    }

    private Product findByIdFromPersistence(UUID id) throws DatabaseException {
        return this.findFromPersistence(this.TABLE_NAME, "id", id,
                this::getProduct, EntityException.Entity.PRODUCT);
    }

    private Product findByCodeFromPersistence(String code) throws DatabaseException {
        return this.findFromPersistence(this.TABLE_NAME, "code", code,
                this::getProduct, EntityException.Entity.PRODUCT);
    }

    public Product findProduct(UUID id) throws DatabaseException {
        return this.findSingleResult(id, this::findByIdFromCache, this::findByIdFromPersistence);
    }

    private Product getProduct(ResultSet rs) throws DatabaseException {
        try {
            byte[] bytes = rs.getBytes("id");
            String code = rs.getString("code");
            byte[] bytesArticle = rs.getBytes("articleId");
            String productAttributesJson = rs.getString("productAttributes");
            BigDecimal price = rs.getBigDecimal("price");

            Article article = this.supplierArticleDAO.findSupplierArticle(this.bytesToUUID(bytesArticle));

            Attributes productAttributes = gson.fromJson(productAttributesJson, Attributes.class);

            return new Product(this.bytesToUUID(bytes), code, article, productAttributes, price);
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_FROM, EntityException.Entity.PRODUCT, e);
        }
    }

    private void loadPreparedStatement(PreparedStatement pstmt, Statement.Type statementType, Product product)
            throws DatabaseException {
        try {
            switch (statementType) {
                case SAVE -> {
                    pstmt.setBytes(1, this.uuidToBytes(product.getId()));
                    pstmt.setString(2, product.getCode());
                    pstmt.setBytes(3, this.uuidToBytes(product.getArticle().getId()));
                    pstmt.setString(4, gson.toJson(product.getProductAttributes()));
                    pstmt.setBigDecimal(5, product.getPrice());
                }
                case UPDATE -> {
                    pstmt.setString(1, product.getCode());
                    pstmt.setBytes(2, this.uuidToBytes(product.getArticle().getId()));
                    pstmt.setString(3, gson.toJson(product.getProductAttributes()));
                    pstmt.setBigDecimal(4, product.getPrice());
                    pstmt.setBytes(5, this.uuidToBytes(product.getId()));
                }
                case DELETE -> pstmt.setBytes(1, this.uuidToBytes(product.getId()));
            }
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_TO, EntityException.Entity.PRODUCT);
        }
    }

    private void saveNewProduct(Product product) throws DatabaseException {
        this.saveNewEntity(product, this::loadProduct, this::getProductCode, this::copy,
                "INSERT INTO " + this.TABLE_NAME + " (id, code, articleId, productAttributes, price) VALUES (?,?,?,?,?);",
                this::loadPreparedStatement, EntityException.Entity.PRODUCT);
    }

    private void updateProduct(Product product) throws DatabaseException {
        this.updateEntity(product, this::findProduct, this::getProductId,
                "UPDATE " + this.TABLE_NAME + " SET code = ?, articleId = ?, productAttributes = ?, price = ? WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.PRODUCT);
    }

    private void deleteProduct(Product product) throws DatabaseException {
        this.deleteEntity(product, this::findProduct, this::getProductId,
                "DELETE FROM " + this.TABLE_NAME + " WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.PRODUCT);
    }

    @Override
    public Product loadProduct(String email) throws DatabaseException {
        return this.findSingleResult(email, this::findByCodeFromCache, this::findByCodeFromPersistence);
    }

    @Override
    public List<Product> loadAll() throws DatabaseException {
        return this.loadAll(this.TABLE_NAME, this::getProduct, EntityException.Entity.PRODUCT);
    }

    @Override
    public void executeTransaction(List<Statement<Product>> statements) throws DatabaseException {
        this.executeTransaction(statements, this::saveNewProduct, this::updateProduct, this::deleteProduct);
    }

    @Override
    public void keepIntegrity(List<Statement<Product>> statements) throws DatabaseException {
        this.keepIntegrity(statements, this::saveNewProduct, this::updateProduct, this::deleteProduct);
    }
}

package it.orderflow.dao.dbms;

import com.google.gson.Gson;
import it.orderflow.control.Statement;
import it.orderflow.dao.ClientArticleDAO;
import it.orderflow.dao.ProductInStockDAO;
import it.orderflow.exceptions.DatabaseException;
import it.orderflow.exceptions.EntityException;
import it.orderflow.model.Attributes;
import it.orderflow.model.ClientArticle;
import it.orderflow.model.Product;
import it.orderflow.model.ProductInStock;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DBMSProductInStockDAO extends DBMSGeneralDAO<ProductInStock> implements ProductInStockDAO {

    private final Gson gson = new Gson();
    private final String TABLE_NAME = "productInStock";

    private final DBMSClientArticleDAO clientArticleDAO;

    public DBMSProductInStockDAO(ClientArticleDAO clientArticleDAO) {
        super();
        this.clientArticleDAO = (DBMSClientArticleDAO) clientArticleDAO;
    }

    private UUID getProductInStockId(ProductInStock product) {
        return product.getId();
    }

    private String getProductInStockCode(ProductInStock product) {
        return product.getCode();
    }

    private UUID getProductInStockArticleId(ProductInStock product) {
        return product.getArticle().getId();
    }

    private ProductInStock copy(ProductInStock product) {
        return product.copy();
    }

    private ProductInStock findByIdFromCache(UUID id) {
        return this.findFromCache(id, this::getProductInStockId);
    }

    private ProductInStock findByCodeFromCache(String code) {
        return this.findFromCache(code, this::getProductInStockCode);
    }

    private List<ProductInStock> findByArticleIdFromCache(UUID articleId) {
        return this.findMatchesFromCache(articleId, this::getProductInStockArticleId);
    }

    private ProductInStock findByIdFromPersistence(UUID id) throws DatabaseException {
        return this.findFromPersistence(this.TABLE_NAME, "id", id,
                this::getProductInStock, EntityException.Entity.PRODUCT_IN_STOCK);
    }

    private ProductInStock findByCodeFromPersistence(String code) throws DatabaseException {
        return this.findFromPersistence(this.TABLE_NAME, "code", code,
                this::getProductInStock, EntityException.Entity.PRODUCT_IN_STOCK);
    }

    private List<ProductInStock> findByArticleIdFromPersistence(UUID articleId) throws DatabaseException {
        return this.findMatchesFromPersistence(this.TABLE_NAME, "articleId",
                articleId, this::getProductInStockList, EntityException.Entity.PRODUCT_IN_STOCK);
    }

    public ProductInStock findProductInStock(UUID id) throws DatabaseException {
        return this.findSingleResult(id, this::findByIdFromCache, this::findByIdFromPersistence);
    }

    private ProductInStock getProductInStock(ResultSet rs) throws DatabaseException {
        try {
            byte[] bytes = rs.getBytes("id");
            String code = rs.getString("code");
            byte[] bytesArticle = rs.getBytes("articleId");
            String productAttributesJson = rs.getString("productAttributes");
            BigDecimal price = rs.getBigDecimal("price");
            int quantity = rs.getInt("quantity");
            int minimumStock = rs.getInt("minimumStock");
            int maximumStock = rs.getInt("maximumStock");
            int orderedProductsQuantity = rs.getInt("orderedProductsQuantity");

            ClientArticle clientArticle = this.clientArticleDAO.findClientArticle(this.bytesToUUID(bytesArticle));

            Attributes productAttributes = gson.fromJson(productAttributesJson, Attributes.class);

            return new ProductInStock(
                    new Product(this.bytesToUUID(bytes), code, clientArticle, productAttributes, price),
                    quantity, minimumStock, maximumStock, orderedProductsQuantity);
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_FROM, EntityException.Entity.PRODUCT_IN_STOCK, e);
        }
    }

    private List<ProductInStock> getProductInStockList(ResultSet rs) throws DatabaseException {
        return this.getEntityList(rs, this::getProductInStock, EntityException.Entity.PRODUCT_IN_STOCK);
    }

    private void loadPreparedStatement(PreparedStatement pstmt, Statement.Type statementType, ProductInStock product)
            throws DatabaseException {
        try {
            switch (statementType) {
                case SAVE -> {
                    pstmt.setBytes(1, this.uuidToBytes(product.getId()));
                    pstmt.setString(2, product.getCode());
                    pstmt.setBytes(3, this.uuidToBytes(product.getArticle().getId()));
                    pstmt.setString(4, gson.toJson(product.getProductAttributes()));
                    pstmt.setBigDecimal(5, product.getPrice());
                    pstmt.setInt(6, product.getQuantity());
                    pstmt.setInt(7, product.getMinimumStock());
                    pstmt.setInt(8, product.getMaximumStock());
                    pstmt.setInt(9, product.getOrderedProductsQuantity());
                }
                case UPDATE -> {
                    pstmt.setString(1, product.getCode());
                    pstmt.setBytes(2, this.uuidToBytes(product.getArticle().getId()));
                    pstmt.setString(3, gson.toJson(product.getProductAttributes()));
                    pstmt.setBigDecimal(4, product.getPrice());
                    pstmt.setInt(5, product.getQuantity());
                    pstmt.setInt(6, product.getMinimumStock());
                    pstmt.setInt(7, product.getMaximumStock());
                    pstmt.setInt(8, product.getOrderedProductsQuantity());
                    pstmt.setBytes(9, this.uuidToBytes(product.getId()));
                }
                case DELETE -> pstmt.setBytes(1, this.uuidToBytes(product.getId()));
            }
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_TO, EntityException.Entity.PRODUCT_IN_STOCK);
        }
    }

    private void saveNewProductInStock(ProductInStock product) throws DatabaseException {
        this.saveNewEntity(product, this::loadProductInStock, this::getProductInStockCode, this::copy,
                "INSERT INTO " + this.TABLE_NAME + " (id, code, articleId, productAttributes, price, quantity, " +
                        "minimumStock, maximumStock, orderedProductsQuantity) VALUES (?,?,?,?,?,?,?,?,?);",
                this::loadPreparedStatement, EntityException.Entity.PRODUCT_IN_STOCK);
    }

    private void updateProductInStock(ProductInStock product) throws DatabaseException {
        this.updateEntity(product, this::findProductInStock, this::getProductInStockId,
                "UPDATE " + this.TABLE_NAME + " SET code = ?, articleId = ?, productAttributes = ?, price = ?, " +
                        "quantity = ?, minimumStock = ?, maximumStock = ?, orderedProductsQuantity = ? WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.PRODUCT_IN_STOCK);
    }

    private void deleteProductInStock(ProductInStock product) throws DatabaseException {
        this.deleteEntity(product, this::findProductInStock, this::getProductInStockId,
                "DELETE FROM " + this.TABLE_NAME + " WHERE id = ?;",
                this::loadPreparedStatement, EntityException.Entity.PRODUCT_IN_STOCK);
    }

    @Override
    public ProductInStock loadProductInStock(String code) throws DatabaseException {
        return this.findSingleResult(code, this::findByCodeFromCache, this::findByCodeFromPersistence);
    }

    @Override
    public List<ProductInStock> loadByArticleName(String articleName) throws DatabaseException {
        UUID articleId = this.clientArticleDAO.loadClientArticle(articleName).getId();
        return this.findMultipleResults(articleId, this::getProductInStockId, this::findByArticleIdFromCache, this::findByArticleIdFromPersistence);
    }

    @Override
    public List<ProductInStock> loadAll() throws DatabaseException {
        return this.loadAll(this.TABLE_NAME, this::getProductInStock, EntityException.Entity.PRODUCT_IN_STOCK);
    }

    @Override
    public void executeTransaction(List<Statement<ProductInStock>> statements) throws DatabaseException {
        this.executeTransaction(statements, this::saveNewProductInStock, this::updateProductInStock,
                this::deleteProductInStock);
    }

    @Override
    public void keepIntegrity(List<Statement<ProductInStock>> statements) throws DatabaseException {
        this.keepIntegrity(statements, this::saveNewProductInStock, this::updateProductInStock,
                this::deleteProductInStock);
    }
}

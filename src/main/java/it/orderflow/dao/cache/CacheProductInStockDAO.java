package it.orderflow.dao.cache;

import it.orderflow.control.Statement;
import it.orderflow.dao.ProductInStockDAO;
import it.orderflow.exceptions.CacheIntegrityException;
import it.orderflow.model.ProductInStock;

import java.util.List;
import java.util.UUID;

public class CacheProductInStockDAO extends CacheGeneralDAO<ProductInStock> implements ProductInStockDAO {

    private UUID getProductInStockId(ProductInStock product) {
        return product.getId();
    }

    private String getProductInStockCode(ProductInStock product) {
        return product.getCode();
    }

    private String getProductInStockArticleName(ProductInStock product) {
        return product.getArticle().getName();
    }

    private ProductInStock copy(ProductInStock product) {
        return product.clone();
    }

    private ProductInStock findProductInStock(UUID id) {
        return this.findFromCache(id, this::getProductInStockId);
    }

    private void saveNewProductInStock(ProductInStock product) {
        this.saveNewEntity(product, this::loadProductInStock, this::getProductInStockCode, this::copy);
    }

    private void updateProductInStock(ProductInStock product) {
        this.updateEntity(product, this::findProductInStock, this::getProductInStockId);
    }

    private void deleteProductInStock(ProductInStock product) {
        this.deleteEntity(product, this::findProductInStock, this::getProductInStockId);
    }

    @Override
    public ProductInStock loadProductInStock(String code) {
        return this.findFromCache(code, this::getProductInStockCode);
    }

    @Override
    public List<ProductInStock> loadByArticleName(String articleName) {
        return this.findMatchesFromCache(articleName, this::getProductInStockArticleName);
    }

    @Override
    public List<ProductInStock> loadAll() {
        return List.copyOf(this.getCache());
    }

    @Override
    public void executeTransaction(List<Statement<ProductInStock>> statements) throws CacheIntegrityException {
        this.executeTransaction(statements, this::saveNewProductInStock, this::updateProductInStock,
                this::deleteProductInStock);
    }

    @Override
    public void keepIntegrity(List<Statement<ProductInStock>> statements) throws CacheIntegrityException {
        this.keepIntegrity(statements, this::saveNewProductInStock, this::updateProductInStock,
                this::deleteProductInStock);
    }
}

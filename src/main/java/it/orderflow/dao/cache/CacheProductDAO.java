package it.orderflow.dao.cache;

import it.orderflow.control.Statement;
import it.orderflow.dao.ProductDAO;
import it.orderflow.exceptions.CacheIntegrityException;
import it.orderflow.model.Product;

import java.util.List;
import java.util.UUID;

public class CacheProductDAO extends CacheGeneralDAO<Product> implements ProductDAO {

    private UUID getProductId(Product product) {
        return product.getId();
    }

    private String getProductCode(Product product) {
        return product.getCode();
    }

    private Product copy(Product product) {
        return product.clone();
    }

    private Product findProduct(UUID id) {
        return this.findFromCache(id, this::getProductId);
    }

    private void saveNewProduct(Product product) {
        this.saveNewEntity(product, this::loadProduct, this::getProductCode, this::copy);
    }

    private void updateProduct(Product product) {
        this.updateEntity(product, this::findProduct, this::getProductId);
    }

    private void deleteProduct(Product product) {
        this.deleteEntity(product, this::findProduct, this::getProductId);
    }

    @Override
    public Product loadProduct(String code) {
        return this.findFromCache(code, this::getProductCode);
    }

    @Override
    public List<Product> loadAll() {
        return List.copyOf(this.getCache());
    }

    @Override
    public void executeTransaction(List<Statement<Product>> statements) throws CacheIntegrityException {
        this.executeTransaction(statements, this::saveNewProduct, this::updateProduct,
                this::deleteProduct);
    }

    @Override
    public void keepIntegrity(List<Statement<Product>> statements) throws CacheIntegrityException {
        this.keepIntegrity(statements, this::saveNewProduct, this::updateProduct,
                this::deleteProduct);
    }
}

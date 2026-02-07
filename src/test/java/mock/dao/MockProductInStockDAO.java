package mock.dao;

import it.orderflow.control.Statement;
import it.orderflow.dao.ProductInStockDAO;
import it.orderflow.model.ProductInStock;
import mock.model.MockEntity;

import java.util.ArrayList;
import java.util.List;

public class MockProductInStockDAO implements ProductInStockDAO {

    private List<ProductInStock> mockCache;

    public MockProductInStockDAO() {
        mockCache = new ArrayList<>(List.of(
                new MockEntity().getMockProductInStock()));
    }

    @Override
    public ProductInStock loadProductInStock(String code) {
        for (ProductInStock productInStock : mockCache) {
            if (productInStock.getCode().equals(code)) return productInStock;
        }
        return null;
    }

    @Override
    public List<ProductInStock> loadByArticleName(String articleName) {
        List<ProductInStock> result = new ArrayList<>();
        for (ProductInStock productInStock : mockCache) {
            if (productInStock.getArticle().getName().equals(articleName)) result.add(productInStock);
        }
        return result;
    }

    @Override
    public List<ProductInStock> loadAll() {
        return List.copyOf(mockCache);
    }

    @Override
    public void executeTransaction(List<Statement<ProductInStock>> statements) {
        for (Statement<ProductInStock> statement : statements) {
            switch (statement.getStatementType()) {
                case SAVE -> mockCache.add(statement.getNewEntity());
                case UPDATE -> {
                    ProductInStock oldEntity = statement.getOldEntity();
                    mockCache.set(mockCache.indexOf(oldEntity), statement.getNewEntity());
                }
                case DELETE -> mockCache.remove(statement.getNewEntity());
            }
        }
    }

    @Override
    public void keepIntegrity(List<Statement<ProductInStock>> statements) {
        //is not called because save, update and delete do not raise exceptions
    }
}

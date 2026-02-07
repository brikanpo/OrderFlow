package it.orderflow.model;

import mock.model.MockEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestProductInStock {

    private final MockEntity me = new MockEntity();
    private Product mockProduct;

    @BeforeEach
    void setUp() {
        mockProduct = me.getMockClientProduct();
    }

    @Test
    void testGetAvailableStock() {
        ProductInStock product = new ProductInStock(mockProduct, 10, 5, 20, 3);
        assertEquals(7, product.getAvailableStock());
    }

    @Test
    void testGetAvailableStockCheckNoNegatives() {
        ProductInStock product = new ProductInStock(mockProduct, 2, 5, 20, 10);
        assertEquals(0, product.getAvailableStock());
    }

    @Test
    void testIsUnderMinimumStock() {
        ProductInStock product = new ProductInStock(mockProduct, 10, 5, 20, 8);
        assertTrue(product.isUnderMinimumStock());
    }

    @Test
    void testGetQuantityToOrder() {
        ProductInStock product = new ProductInStock(mockProduct, 10, 5, 20, 2);
        assertEquals(0, product.getQuantityToOrder());
    }

    @Test
    void testGetQuantityToOrderWhenUnderMinimumStock() {
        ProductInStock product = new ProductInStock(mockProduct, 10, 5, 20, 6);
        assertEquals(16, product.getQuantityToOrder());
    }

    @Test
    void testGetQuantityToOrderWhenOrderedBiggerThanQuantity() {
        ProductInStock product = new ProductInStock(mockProduct, 10, 5, 20, 15);
        assertEquals(25, product.getQuantityToOrder());
    }

    @Test
    void testSoldOrderedProductCheckQuantity() {
        ProductInStock product = new ProductInStock(mockProduct, 10, 5, 20, 2);
        product.soldOrderedProduct(2);
        assertEquals(8, product.getQuantity());
    }

    @Test
    void testSoldOrderedProductCheckOrderedQuantity() {
        ProductInStock product = new ProductInStock(mockProduct, 10, 5, 20, 2);
        product.soldOrderedProduct(2);
        assertEquals(0, product.getOrderedProductsQuantity());
    }

    @Test
    void testRemoveCheckNoBelowZero() {
        ProductInStock product = new ProductInStock(mockProduct, 5, 2, 10);
        product.remove(10);
        assertEquals(0, product.getQuantity());
    }
}

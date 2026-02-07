package it.orderflow.model;

import mock.model.MockEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestProductsWithQuantity {

    private final MockEntity me = new MockEntity();
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product(UUID.randomUUID(), "code1", me.getMockClientArticle(),
                me.getMockProductAttributes(), new BigDecimal("10.00"));
        product2 = new Product(UUID.randomUUID(), "code2", me.getMockClientArticle(),
                me.getMockProductAttributes(), new BigDecimal("20.00"));
    }

    @Test
    void testAddCheckAddProductNotPresent() {
        ProductsWithQuantity productsWithQuantity = new ProductsWithQuantity();
        ProductWithQuantity pwq = new ProductWithQuantity(product1, 5);

        productsWithQuantity.add(pwq);

        assertEquals(5, productsWithQuantity.get("code1").getQuantity());
    }

    @Test
    void testAddCheckAddProductPresent() {
        ProductsWithQuantity productsWithQuantity = new ProductsWithQuantity();
        ProductWithQuantity pwq = new ProductWithQuantity(product1, 5);
        productsWithQuantity.setProductWithQuantityList(new ArrayList<>(List.of(pwq)));

        ProductWithQuantity pwq1 = new ProductWithQuantity(product1, 5);
        productsWithQuantity.add(pwq1);

        assertEquals(10, productsWithQuantity.get("code1").getQuantity());
    }

    @Test
    void testAddCheckAddDifferentProducts() {
        ProductsWithQuantity productsWithQuantity = new ProductsWithQuantity();
        ProductWithQuantity pwq = new ProductWithQuantity(product1, 5);
        productsWithQuantity.setProductWithQuantityList(new ArrayList<>(List.of(pwq)));

        ProductWithQuantity pwq1 = new ProductWithQuantity(product2, 5);
        productsWithQuantity.add(pwq1);

        assertEquals(2, productsWithQuantity.getProductWithQuantityList().size());
    }

    @Test
    void testAddProducts() {
        ProductsWithQuantity productsWithQuantity = new ProductsWithQuantity();
        ProductWithQuantity pwq = new ProductWithQuantity(product1, 5);
        ProductWithQuantity pwq1 = new ProductWithQuantity(product2, 5);
        productsWithQuantity.setProductWithQuantityList(new ArrayList<>(List.of(pwq, pwq1)));

        ProductsWithQuantity tempProductsWithQuantity = new ProductsWithQuantity();
        tempProductsWithQuantity.setProductWithQuantityList(new ArrayList<>(List.of(pwq, pwq1)));

        productsWithQuantity.addProducts(tempProductsWithQuantity);

        assertEquals(2, productsWithQuantity.getProductWithQuantityList().size());
    }

    @Test
    void testGenerateTotal() {
        ProductsWithQuantity productsWithQuantity = new ProductsWithQuantity();
        ProductWithQuantity pwq = new ProductWithQuantity(product1, 1);
        ProductWithQuantity pwq1 = new ProductWithQuantity(product2, 1);
        productsWithQuantity.setProductWithQuantityList(new ArrayList<>(List.of(pwq, pwq1)));

        assertEquals(0, productsWithQuantity.generateTotal().compareTo(new BigDecimal("36.60")));
    }

    @Test
    void testRemoveCheckNoProductsPresent() {
        ProductsWithQuantity productsWithQuantity = new ProductsWithQuantity();
        ProductWithQuantity pwq = new ProductWithQuantity(product1, 5);

        productsWithQuantity.remove(pwq);

        assertEquals(0, productsWithQuantity.getProductWithQuantityList().size());
    }

    @Test
    void testRemoveCheckProductPresent() {
        ProductsWithQuantity productsWithQuantity = new ProductsWithQuantity();
        ProductWithQuantity pwq = new ProductWithQuantity(product1, 5);
        productsWithQuantity.setProductWithQuantityList(new ArrayList<>(List.of(pwq)));

        ProductWithQuantity pwq1 = new ProductWithQuantity(product1, 3);
        productsWithQuantity.remove(pwq1);

        assertEquals(2, productsWithQuantity.get("code1").getQuantity());
    }

    @Test
    void testRemoveCheckQuantityBiggerThanActual() {
        ProductsWithQuantity productsWithQuantity = new ProductsWithQuantity();
        ProductWithQuantity pwq = new ProductWithQuantity(product1, 5);
        productsWithQuantity.setProductWithQuantityList(new ArrayList<>(List.of(pwq)));

        ProductWithQuantity pwq1 = new ProductWithQuantity(product1, 10);
        productsWithQuantity.remove(pwq1);

        assertEquals(0, productsWithQuantity.getProductWithQuantityList().size());
    }

    @Test
    void removeProducts() {
        ProductsWithQuantity productsWithQuantity = new ProductsWithQuantity();
        ProductWithQuantity pwq = new ProductWithQuantity(product1, 5);
        ProductWithQuantity pwq1 = new ProductWithQuantity(product2, 5);
        productsWithQuantity.setProductWithQuantityList(new ArrayList<>(List.of(pwq, pwq1)));

        ProductsWithQuantity tempProductsWithQuantity = new ProductsWithQuantity();
        pwq = new ProductWithQuantity(product1, 2);
        pwq1 = new ProductWithQuantity(product2, 5);
        tempProductsWithQuantity.setProductWithQuantityList(new ArrayList<>(List.of(pwq, pwq1)));

        productsWithQuantity.removeProducts(tempProductsWithQuantity);

        assertEquals(1, productsWithQuantity.getProductWithQuantityList().size());
    }
}
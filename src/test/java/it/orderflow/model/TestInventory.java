package it.orderflow.model;

import mock.model.MockEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TestInventory {

    private Inventory inventory;
    private ProductInStock p1;
    private ProductInStock p2;

    private final MockEntity me = new MockEntity();
    private final ClientArticle clientArticle = me.getMockClientArticle();

    @BeforeEach
    void setUp() {
        p1 = new ProductInStock(new Product(UUID.randomUUID(), "code1", clientArticle, new Attributes(List.of("Color"), List.of("Red")), new BigDecimal("25.00")),
                10, 5, 20, 0);
        p2 = new ProductInStock(new Product(UUID.randomUUID(), "code2", clientArticle, new Attributes(List.of("Color"), List.of("Red")), new BigDecimal("20.00")),
                10, 5, 20, 6);

        inventory = new Inventory(new ArrayList<>(List.of(p1, p2)));
    }

    @Test
    void testConstructorCheckCopy() {
        List<ProductInStock> list = new ArrayList<>();
        ProductInStock p = new ProductInStock(new Product(UUID.randomUUID(), "code3", clientArticle, new Attributes(List.of("Color"), List.of("Red")), new BigDecimal("20.00")),
                10, 5, 20, 0);
        list.add(p);
        Inventory inv = new Inventory(list);

        assertNotSame(p, inv.get("code3"));
    }

    @Test
    void testGetProductByCode() {
        assertEquals("code1", inventory.get("code1").getCode());
    }

    @Test
    void testGetProductByCodeCheckNull() {
        assertNull(inventory.get("NOT_PRESENT"));
    }

    @Test
    void testAddOrderedProducts() {
        ProductsWithQuantity ordered = new ProductsWithQuantity();
        ordered.add(new ProductWithQuantity(p1, 2));

        inventory.addOrderedProducts(ordered);

        assertEquals(2, inventory.get("code1").getOrderedProductsQuantity());
    }

    @Test
    void testGetProductsToOrder() {
        ProductsWithQuantity toOrder = inventory.getProductsToOrder();

        assertEquals(1, toOrder.getProductWithQuantityList().size());
    }

    @Test
    void testRemoveOrderedProducts() {
        ProductsWithQuantity ordered = new ProductsWithQuantity();
        ordered.add(new ProductWithQuantity(p2, 3));

        inventory.removeOrderedProducts(ordered);

        assertEquals(3, inventory.get("code2").getOrderedProductsQuantity());
    }

    @Test
    void testRestock() {
        ProductsWithQuantity restockList = new ProductsWithQuantity();
        restockList.add(new ProductWithQuantity(p1, 5));

        inventory.restock(restockList);

        assertEquals(15, inventory.get("code1").getQuantity());
    }

    @Test
    void testSoldOrderedProducts() {
        ProductsWithQuantity restockList = new ProductsWithQuantity();
        restockList.add(new ProductWithQuantity(p2, 6));

        inventory.soldOrderedProducts(restockList);

        assertEquals(0, inventory.get("code2").getOrderedProductsQuantity());
    }
}
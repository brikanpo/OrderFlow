package it.OrderFlow.Model;

import java.util.List;

public class Inventory {

    private final List<ProductInStock> inventory;

    public Inventory(List<ProductInStock> inventory) {
        this.inventory = inventory.stream().map(ProductInStock::clone).toList();
    }

    public List<ProductInStock> getInventory() {
        return this.inventory;
    }

    public ProductInStock get(String code) {
        for (ProductInStock productInStock : this.getInventory()) {
            if (productInStock.getCode().equals(code)) return productInStock;
        }
        return null;
    }

    public void addOrderedProducts(ProductsWithQuantity products) {
        for (ProductWithQuantity prod : products.getProducts()) {
            this.get(prod.getCode()).addOrderedQuantity(prod.getQuantity());
        }
    }

    public ProductsWithQuantity getProductsToOrder() {
        ProductsWithQuantity result = new ProductsWithQuantity();
        for (ProductInStock prod : this.getInventory()) {
            if (prod.getQuantityToOrder() > 0) {
                result.add(new ProductWithQuantity(prod, prod.getQuantityToOrder()));
            }
        }
        return result;
    }

    public void removeOrderedProducts(ProductsWithQuantity products) {
        for (ProductWithQuantity prod : products.getProducts()) {
            this.get(prod.getCode()).removeOrderedProduct(prod.getQuantity());
        }
    }

    public void restock(ProductsWithQuantity products) {
        for (ProductWithQuantity prod : products.getProducts()) {
            this.get(prod.getCode()).add(prod.getQuantity());
        }
    }

    public void soldOrderedProducts(ProductsWithQuantity products) {
        for (ProductWithQuantity prod : products.getProducts()) {
            this.get(prod.getCode()).soldOrderedProduct(prod.getQuantity());
        }
    }
}

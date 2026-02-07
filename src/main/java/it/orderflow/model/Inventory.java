package it.orderflow.model;

import java.util.List;

public class Inventory {

    private final List<ProductInStock> productInStockList;

    public Inventory(List<ProductInStock> productInStockList) {
        this.productInStockList = productInStockList.stream().map(ProductInStock::copy).toList();
    }

    public List<ProductInStock> getProductInStockList() {
        return this.productInStockList;
    }

    public ProductInStock get(String code) {
        for (ProductInStock productInStock : this.getProductInStockList()) {
            if (productInStock.getCode().equals(code)) return productInStock;
        }
        return null;
    }

    public void addOrderedProducts(ProductsWithQuantity products) {
        for (ProductWithQuantity prod : products.getProductWithQuantityList()) {
            this.get(prod.getCode()).addOrderedQuantity(prod.getQuantity());
        }
    }

    public ProductsWithQuantity getProductsToOrder() {
        ProductsWithQuantity result = new ProductsWithQuantity();
        for (ProductInStock prod : this.getProductInStockList()) {
            if (prod.getQuantityToOrder() > 0) {
                result.add(new ProductWithQuantity(prod, prod.getQuantityToOrder()));
            }
        }
        return result;
    }

    public void removeOrderedProducts(ProductsWithQuantity products) {
        for (ProductWithQuantity prod : products.getProductWithQuantityList()) {
            this.get(prod.getCode()).removeOrderedProduct(prod.getQuantity());
        }
    }

    public void restock(ProductsWithQuantity products) {
        for (ProductWithQuantity prod : products.getProductWithQuantityList()) {
            this.get(prod.getCode()).add(prod.getQuantity());
        }
    }

    public void soldOrderedProducts(ProductsWithQuantity products) {
        for (ProductWithQuantity prod : products.getProductWithQuantityList()) {
            this.get(prod.getCode()).soldOrderedProduct(prod.getQuantity());
        }
    }
}

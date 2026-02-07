package it.orderflow.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductsWithQuantity {

    private List<ProductWithQuantity> productWithQuantityList;

    public ProductsWithQuantity() {
        this.productWithQuantityList = new ArrayList<>();
    }

    public List<ProductWithQuantity> getProductWithQuantityList() {
        return this.productWithQuantityList;
    }

    public void setProductWithQuantityList(List<ProductWithQuantity> productWithQuantityList) {
        this.productWithQuantityList = productWithQuantityList;
    }

    public ProductWithQuantity get(String code) {
        for (ProductWithQuantity product : this.getProductWithQuantityList()) {
            if (product.getCode().equals(code)) {
                return product;
            }
        }
        return null;
    }

    public void add(ProductWithQuantity product) {
        ProductWithQuantity prod = this.get(product.getCode());
        int quantity = product.getQuantity();
        if (quantity > 0) {
            if (prod != null) {
                prod.add(quantity);
            } else {
                this.getProductWithQuantityList().add(product);
            }
        }
    }

    public void addProducts(ProductsWithQuantity products) {
        for (ProductWithQuantity prod : products.getProductWithQuantityList()) {
            this.add(prod);
        }
    }

    public BigDecimal generateTotal() {
        BigDecimal result = new BigDecimal(0);
        for (ProductWithQuantity prod : this.getProductWithQuantityList()) {
            result = result.add(prod.generateTotal());
        }
        return result;
    }

    public void remove(ProductWithQuantity product) {
        ProductWithQuantity prod = this.get(product.getCode());
        if (prod != null) {
            prod.remove(product.getQuantity());
            if (prod.isEmpty()) {
                this.getProductWithQuantityList().remove(prod);
            }
        }
    }

    public void removeProducts(ProductsWithQuantity products) {
        for (ProductWithQuantity prod : products.getProductWithQuantityList()) {
            this.remove(prod);
        }
    }

    public ProductsWithQuantity copy() {
        ProductsWithQuantity productsWithQuantity = new ProductsWithQuantity();
        productsWithQuantity.setProductWithQuantityList(this.getProductWithQuantityList().stream()
                .map(ProductWithQuantity::copy)
                .collect(Collectors.toCollection(ArrayList::new)));
        return productsWithQuantity;
    }
}

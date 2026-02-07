package it.orderflow.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductsWithQuantity implements Cloneable {

    private List<ProductWithQuantity> products;

    public ProductsWithQuantity() {
        this.products = new ArrayList<>();
    }

    public List<ProductWithQuantity> getProducts() {
        return this.products;
    }

    public void setProducts(List<ProductWithQuantity> products) {
        this.products = products;
    }

    public ProductWithQuantity get(String code) {
        for (ProductWithQuantity product : this.getProducts()) {
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
                this.getProducts().add(product);
            }
        }
    }

    public void addProducts(ProductsWithQuantity products) {
        for (ProductWithQuantity prod : products.getProducts()) {
            this.add(prod);
        }
    }

    public BigDecimal generateTotal() {
        BigDecimal result = new BigDecimal(0);
        for (ProductWithQuantity prod : this.getProducts()) {
            result = result.add(prod.generateTotal());
        }
        return result;
    }

    public void remove(ProductWithQuantity product) {
        ProductWithQuantity prod = this.get(product.getCode());
        if (prod != null) {
            prod.remove(product.getQuantity());
            if (prod.isEmpty()) {
                this.getProducts().remove(prod);
            }
        }
    }

    public void removeProducts(ProductsWithQuantity products) {
        for (ProductWithQuantity prod : products.getProducts()) {
            this.remove(prod);
        }
    }

    @Override
    public ProductsWithQuantity clone() {
        try {
            ProductsWithQuantity clone = (ProductsWithQuantity) super.clone();
            clone.products = this.products.stream().map(ProductWithQuantity::clone).toList();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

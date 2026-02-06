package it.OrderFlow.Model;

import java.math.BigDecimal;

public class ProductWithQuantity implements Cloneable {

    private Product product;
    private int quantity;

    public ProductWithQuantity(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void add(int quantity) {
        this.setQuantity(this.getQuantity() + quantity);
    }

    public BigDecimal generateTotal() {
        return this.getProduct().generateIvaPrice().multiply(new BigDecimal(this.getQuantity()));
    }

    public String getCode() {
        return this.getProduct().getCode();
    }

    public boolean isEmpty() {
        return this.getQuantity() == 0;
    }

    public void remove(int quantity) {
        this.setQuantity(Math.max(this.getQuantity() - quantity, 0));
    }

    @Override
    public ProductWithQuantity clone() {
        try {
            ProductWithQuantity clone = (ProductWithQuantity) super.clone();
            clone.product = this.product.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

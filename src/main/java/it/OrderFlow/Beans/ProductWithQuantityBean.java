package it.OrderFlow.Beans;

import it.OrderFlow.Model.ProductWithQuantity;

public class ProductWithQuantityBean {

    private ProductBean productBean;
    private int quantity;

    public ProductWithQuantityBean() {
    }

    public ProductWithQuantityBean(ProductWithQuantity product) {
        this.productBean = new ProductBean(product.getProduct());
        this.quantity = product.getQuantity();
    }

    public ProductWithQuantityBean(ProductBean product) {
        this.productBean = product;
        this.quantity = 0;
    }

    public ProductWithQuantityBean(ProductBean product, int quantity) {
        this.productBean = product;
        this.quantity = quantity;
    }

    public ProductBean getProductBean() {
        return this.productBean;
    }

    public void setProductBean(ProductBean productBean) {
        this.productBean = productBean;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

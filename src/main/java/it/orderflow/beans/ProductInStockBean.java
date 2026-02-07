package it.orderflow.beans;

import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.model.Attributes;
import it.orderflow.model.ProductInStock;

import java.math.BigDecimal;

public class ProductInStockBean {

    private String code;
    private String articleName;
    private Attributes productAttributes;
    private BigDecimal price;
    private BigDecimal articleIva;
    private Integer quantity;
    private Integer minimumStock;
    private Integer maximumStock;
    private int orderedProductsQuantity;

    public ProductInStockBean() {
    }

    public ProductInStockBean(ProductInStock product) {
        this.code = product.getCode();
        this.articleName = product.getArticle().getName();
        this.productAttributes = product.getProductAttributes();
        this.price = product.getPrice();
        this.articleIva = product.getArticle().getIva();
        this.quantity = product.getQuantity();
        this.minimumStock = product.getQuantity();
        this.maximumStock = product.getMaximumStock();
        this.orderedProductsQuantity = product.getOrderedProductsQuantity();
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getArticleName() {
        return this.articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public Attributes getProductAttributes() {
        return this.productAttributes;
    }

    public void setProductAttributes(Attributes productAttributes) {
        this.productAttributes = productAttributes;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) throws InvalidInputException {
        if (price.compareTo(new BigDecimal("0.00")) > 0) {
            throw new InvalidInputException(InvalidInputException.InputType.PRICE);
        }
        this.price = price;
    }

    public BigDecimal getArticleIva() {
        return this.articleIva;
    }

    public void setArticleIva(BigDecimal articleIva) {
        this.articleIva = articleIva;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) throws InvalidInputException {
        if (quantity < 0) {
            throw new InvalidInputException(InvalidInputException.InputType.QUANTITY_0_OR_HIGHER);
        }
        this.quantity = quantity;
    }

    public Integer getMinimumStock() {
        return this.minimumStock;
    }

    public void setMinimumStock(Integer minimumStock) throws InvalidInputException {
        if (minimumStock < 0) {
            throw new InvalidInputException(InvalidInputException.InputType.QUANTITY_0_OR_HIGHER);
        }
        this.minimumStock = minimumStock;
    }

    public Integer getMaximumStock() {
        return this.maximumStock;
    }

    public void setMaximumStock(Integer maximumStock) throws InvalidInputException {
        if (maximumStock < 0) {
            throw new InvalidInputException(InvalidInputException.InputType.QUANTITY_0_OR_HIGHER);
        }
        this.maximumStock = maximumStock;
    }

    public Integer getOrderedProductsQuantity() {
        return this.orderedProductsQuantity;
    }

    public void setOrderedProductsQuantity(int orderedProductsQuantity) {
        this.orderedProductsQuantity = orderedProductsQuantity;
    }
}

package it.OrderFlow.Beans;

import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.Model.Attributes;
import it.OrderFlow.Model.Product;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductBean {

    private UUID id;
    private String code;
    private String articleName;
    private Attributes productAttributes;
    private BigDecimal price;
    private BigDecimal articleIva;

    public ProductBean() {
    }

    public ProductBean(Product product) {
        this.code = product.getCode();
        this.articleName = product.getArticle().getName();
        this.productAttributes = product.getProductAttributes();
        this.price = product.getPrice();
        this.articleIva = product.getArticle().getIva();
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}

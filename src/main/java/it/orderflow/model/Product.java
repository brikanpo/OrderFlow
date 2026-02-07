package it.orderflow.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Product implements Cloneable {

    private UUID id;
    private String code;
    private Article article;
    private Attributes productAttributes;
    private BigDecimal price;

    public Product(Article article, Attributes productAttributes, BigDecimal price) {
        this(UUID.randomUUID(), null, article, productAttributes, price);
        this.setCode(this.generateCode());
    }

    public Product(UUID id, String code, Article article, Attributes productAttributes, BigDecimal price) {
        this.setId(id);
        this.setCode(code);
        this.setArticle(article);
        this.setProductAttributes(productAttributes);
        this.setPrice(price);
    }

    public UUID getId() {
        return this.id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    private void setCode(String code) {
        this.code = code;
    }

    public Article getArticle() {
        return this.article;
    }

    private void setArticle(Article article) {
        this.article = article;
    }

    public Attributes getProductAttributes() {
        return this.productAttributes;
    }

    private void setProductAttributes(Attributes productAttributes) {
        this.productAttributes = productAttributes;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    private void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String generateCode() {
        StringBuilder result = new StringBuilder(this.getArticle().getName());
        for (Object o : this.getProductAttributes().getAttributesValues()) {
            result.append("-").append(o.toString().trim());
        }
        return result.toString();
    }

    public BigDecimal generateIvaPrice() {
        return this.getPrice().add(this.getPrice().multiply(this.getArticle().getIva()));
    }

    @Override
    public Product clone() {
        try {
            Product clone = (Product) super.clone();
            clone.productAttributes = this.productAttributes.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

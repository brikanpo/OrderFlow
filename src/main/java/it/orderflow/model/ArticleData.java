package it.orderflow.model;

import java.math.BigDecimal;

public class ArticleData {

    private String name;
    private String category;
    private String description;
    private Attributes articleAttributes;
    private Attributes possibleAttributes;
    private BigDecimal iva;

    public ArticleData(String name, String category) {
        this(name, category, null, new Attributes(), new Attributes(),
                new BigDecimal("0.22"));
    }

    public ArticleData(String name, String category, String description, Attributes articleAttributes,
                       Attributes possibleAttributes, BigDecimal iva) {
        this.setName(name);
        this.setCategory(category);
        this.setDescription(description);
        this.setArticleAttributes(articleAttributes);
        this.setPossibleAttributes(possibleAttributes);
        this.setIva(iva);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Attributes getArticleAttributes() {
        return this.articleAttributes;
    }

    public void setArticleAttributes(Attributes articleAttributes) {
        this.articleAttributes = articleAttributes;
    }

    public Attributes getPossibleAttributes() {
        return this.possibleAttributes;
    }

    public void setPossibleAttributes(Attributes possibleAttributes) {
        this.possibleAttributes = possibleAttributes;
    }

    public BigDecimal getIva() {
        return this.iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public ArticleData copy() {
        return new ArticleData(this.getName(), this.getCategory(), this.getDescription(),
                this.getArticleAttributes().copy(), this.getPossibleAttributes().copy(), this.getIva());
    }
}

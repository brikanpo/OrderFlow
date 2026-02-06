package it.OrderFlow.Model;

import java.math.BigDecimal;
import java.util.UUID;

public class Article {

    private UUID id;
    private String name;
    private String category;
    private String description;
    private Attributes articleAttributes;
    private Attributes possibleAttributes;
    private BigDecimal iva;

    public Article(String name, String category) {
        this(UUID.randomUUID(), name, category, null, new Attributes(), new Attributes(),
                new BigDecimal("0.22"));
    }

    public Article(UUID id, String name, String category, String description, Attributes articleAttributes,
                   Attributes possibleAttributes, BigDecimal iva) {
        this.setId(id);
        this.setName(name);
        this.setCategory(category);
        this.setDescription(description);
        this.setArticleAttributes(articleAttributes);
        this.setPossibleAttributes(possibleAttributes);
        this.setIva(iva);
    }

    public UUID getId() {
        return this.id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return this.category;
    }

    private void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return this.description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public Attributes getArticleAttributes() {
        return this.articleAttributes;
    }

    private void setArticleAttributes(Attributes articleAttributes) {
        this.articleAttributes = articleAttributes;
    }

    public Attributes getPossibleAttributes() {
        return this.possibleAttributes;
    }

    private void setPossibleAttributes(Attributes possibleAttributes) {
        this.possibleAttributes = possibleAttributes;
    }

    public BigDecimal getIva() {
        return this.iva;
    }

    private void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public void changeDescription(String description) {
        this.setDescription(description);
    }

    public void changeArticleAttributes(Attributes articleAttributes) {
        this.setArticleAttributes(articleAttributes);
    }

    public void changePossibleAttributes(Attributes possibleAttributes) {
        this.setPossibleAttributes(possibleAttributes);
    }

    public void changeIva(BigDecimal iva) {
        this.setIva(iva);
    }
}

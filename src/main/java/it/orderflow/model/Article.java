package it.orderflow.model;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class Article {

    private UUID id;
    private ArticleData articleData;

    protected Article(String name, String category) {
        this(UUID.randomUUID(), new ArticleData(name, category));
    }

    protected Article(UUID id, ArticleData articleData) {
        this.setId(id);
        this.setArticleData(articleData);
    }

    public UUID getId() {
        return this.id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public ArticleData getArticleData() {
        return this.articleData;
    }

    public void setArticleData(ArticleData articleData) {
        this.articleData = articleData;
    }

    public void changeDescription(String description) {
        this.getArticleData().setDescription(description);
    }

    public void changeArticleAttributes(Attributes articleAttributes) {
        this.getArticleData().setArticleAttributes(articleAttributes);
    }

    public void changePossibleAttributes(Attributes possibleAttributes) {
        this.getArticleData().setPossibleAttributes(possibleAttributes);
    }

    public void changeIva(BigDecimal iva) {
        this.getArticleData().setIva(iva);
    }

    public String getName() {
        return this.getArticleData().getName();
    }

    public String getCategory() {
        return this.getArticleData().getCategory();
    }

    public String getDescription() {
        return this.getArticleData().getDescription();
    }

    public Attributes getArticleAttributes() {
        return this.getArticleData().getArticleAttributes();
    }

    public Attributes getPossibleAttributes() {
        return this.getArticleData().getPossibleAttributes();
    }

    public BigDecimal getIva() {
        return this.getArticleData().getIva();
    }

    public abstract Article copy();
}

package it.orderflow.model;

import java.util.UUID;

public class ClientArticle extends Article {

    private UUID supplierArticleId;

    public ClientArticle(String name, String category, UUID supplierArticleId) {
        super(name, category);
        this.setSupplierArticleId(supplierArticleId);
    }

    public ClientArticle(UUID id, ArticleData articleData, UUID supplierArticleId) {
        super(id, articleData);
        this.setSupplierArticleId(supplierArticleId);
    }

    public UUID getSupplierArticleId() {
        return this.supplierArticleId;
    }

    private void setSupplierArticleId(UUID supplierArticleId) {
        this.supplierArticleId = supplierArticleId;
    }

    @Override
    public ClientArticle copy() {
        return new ClientArticle(this.getId(), this.getArticleData().copy(), this.getSupplierArticleId());
    }
}

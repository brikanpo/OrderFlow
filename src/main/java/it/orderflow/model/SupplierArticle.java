package it.orderflow.model;

import java.util.UUID;

public class SupplierArticle extends Article {

    private UUID supplierId;

    public SupplierArticle(String name, String category, UUID supplierId) {
        super(name, category);
        this.setSupplierId(supplierId);
    }

    public SupplierArticle(UUID id, ArticleData articleData, UUID supplierId) {
        super(id, articleData);
        this.setSupplierId(supplierId);
    }

    public UUID getSupplierId() {
        return this.supplierId;
    }

    private void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public SupplierArticle copy() {
        return new SupplierArticle(this.getId(), this.getArticleData().copy(), this.getSupplierId());
    }
}

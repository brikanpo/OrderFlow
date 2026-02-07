package it.orderflow.model;

import java.math.BigDecimal;
import java.util.UUID;

public class ClientArticle extends Article {

    private UUID supplierArticleId;

    public ClientArticle(String name, String category, UUID supplierArticleId) {
        super(name, category);
        this.setSupplierArticleId(supplierArticleId);
    }

    public ClientArticle(UUID id, String name, String category, String description, Attributes articleAttributes,
                         Attributes possibleAttributes, BigDecimal iva, UUID supplierArticleId) {
        super(id, name, category, description, articleAttributes, possibleAttributes, iva);
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
        return new ClientArticle(this.getId(), this.getName(), this.getCategory(), this.getDescription(),
                this.getArticleAttributes().copy(), this.getPossibleAttributes().copy(), this.getIva(),
                this.getSupplierArticleId());
    }
}

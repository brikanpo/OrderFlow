package it.orderflow.model;

import java.math.BigDecimal;
import java.util.UUID;

public class ClientArticle extends Article implements Cloneable {

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
    public ClientArticle clone() {
        try {
            return (ClientArticle) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

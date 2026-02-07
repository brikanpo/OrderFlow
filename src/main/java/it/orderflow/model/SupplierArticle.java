package it.orderflow.model;

import java.math.BigDecimal;
import java.util.UUID;

public class SupplierArticle extends Article implements Cloneable {

    private UUID supplierId;

    public SupplierArticle(String name, String category, UUID supplierId) {
        super(name, category);
        this.setSupplierId(supplierId);
    }

    public SupplierArticle(UUID id, String name, String category, String description, Attributes articleAttributes,
                           Attributes possibleAttributes, BigDecimal iva, UUID supplierId) {
        super(id, name, category, description, articleAttributes, possibleAttributes, iva);
        this.setSupplierId(supplierId);
    }

    public UUID getSupplierId() {
        return this.supplierId;
    }

    private void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public SupplierArticle clone() {
        try {
            return (SupplierArticle) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

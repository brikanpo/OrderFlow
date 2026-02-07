package it.orderflow.beans;

import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.model.Attributes;
import it.orderflow.model.ClientArticle;
import it.orderflow.model.SupplierArticle;

import java.math.BigDecimal;
import java.util.UUID;

public class ArticleBean {

    private UUID id;
    private String name;
    private String category;
    private String description;
    private Attributes articleAttributes;
    private Attributes possibleAttributes;
    private BigDecimal iva;
    private UUID supplierArticleId;
    private UUID supplierId;

    public ArticleBean() {
    }

    public ArticleBean(ClientArticle clientArticle) {
        this.id = clientArticle.getId();
        this.name = clientArticle.getName();
        this.category = clientArticle.getCategory();
        this.description = clientArticle.getDescription();
        this.articleAttributes = clientArticle.getArticleAttributes();
        this.possibleAttributes = clientArticle.getPossibleAttributes();
        this.iva = clientArticle.getIva();
        this.supplierArticleId = clientArticle.getSupplierArticleId();
    }

    public ArticleBean(SupplierArticle supplierArticle) {
        this.id = supplierArticle.getId();
        this.name = supplierArticle.getName();
        this.category = supplierArticle.getCategory();
        this.description = supplierArticle.getDescription();
        this.articleAttributes = supplierArticle.getArticleAttributes();
        this.possibleAttributes = supplierArticle.getPossibleAttributes();
        this.iva = supplierArticle.getIva();
        this.supplierId = supplierArticle.getSupplierId();
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) throws InvalidInputException {
        if (name.isBlank()) {
            throw new InvalidInputException(InvalidInputException.InputType.BLANK);
        }
        this.name = name;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) throws InvalidInputException {
        if (category.isBlank()) {
            throw new InvalidInputException(InvalidInputException.InputType.BLANK);
        }
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

    public void setIva(BigDecimal iva) throws InvalidInputException {
        if (iva.compareTo(new BigDecimal("0.00")) > 0 && iva.compareTo(new BigDecimal("1.00")) < 0) {
            this.iva = iva;
        } else throw new InvalidInputException(InvalidInputException.InputType.PERCENTAGE);
    }

    public UUID getSupplierArticleId() {
        return this.supplierArticleId;
    }

    public void setSupplierArticleId(UUID supplierArticleId) {
        this.supplierArticleId = supplierArticleId;
    }

    public UUID getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }
}

package it.orderflow.model;

public class ProductInStock extends Product {

    private int quantity;
    private int minimumStock;
    private int maximumStock;
    private int orderedProductsQuantity;

    public ProductInStock(Product product, int quantity, int minimumStock, int maximumStock) {
        this(product, quantity, minimumStock, maximumStock, 0);
    }

    public ProductInStock(Product product, int quantity, int minimumStock, int maximumStock,
                          int orderedProductsQuantity) {
        super(product.getId(), product.getCode(), product.getArticle(), product.getProductAttributes(), product.getPrice());
        this.setQuantity(quantity);
        this.setMinimumStock(minimumStock);
        this.setMaximumStock(maximumStock);
        this.setOrderedProductsQuantity(orderedProductsQuantity);
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinimumStock() {
        return this.minimumStock;
    }

    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
    }

    public int getMaximumStock() {
        return this.maximumStock;
    }

    public void setMaximumStock(int targetStock) {
        this.maximumStock = targetStock;
    }

    public int getOrderedProductsQuantity() {
        return this.orderedProductsQuantity;
    }

    public void setOrderedProductsQuantity(int orderedProductsQuantity) {
        this.orderedProductsQuantity = orderedProductsQuantity;
    }

    public void add(int quantity) {
        this.setQuantity(this.getQuantity() + quantity);
    }

    public void addOrderedQuantity(int quantity) {
        this.setOrderedProductsQuantity(this.getOrderedProductsQuantity() + quantity);
    }

    public int getAvailableStock() {
        return Math.max(this.getQuantity() - this.getOrderedProductsQuantity(), 0);
    }

    public int getQuantityToOrder() {
        if (this.isUnderMinimumStock()) {
            if (this.getOrderedProductsQuantity() >= this.getQuantity()) {
                return this.getMaximumStock() - this.getQuantity() + this.getOrderedProductsQuantity();
            } else {
                return this.getMaximumStock() - (this.getQuantity() - this.getOrderedProductsQuantity());
            }
        } else return 0;
    }

    public boolean isUnderMinimumStock() {
        return this.getAvailableStock() < this.getMinimumStock();
    }

    public void remove(int quantity) {
        this.setQuantity(Math.max(this.getQuantity() - quantity, 0));
    }

    public void removeOrderedProduct(int quantity) {
        this.setOrderedProductsQuantity(this.getOrderedProductsQuantity() - quantity);
    }

    public void soldOrderedProduct(int quantity) {
        this.removeOrderedProduct(quantity);
        this.remove(quantity);
    }

    @Override
    public ProductInStock clone() {
        return (ProductInStock) super.clone();
    }
}

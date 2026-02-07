package it.orderflow.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Order {

    private UUID id;
    private LocalDateTime registrationDate;
    private ProductsWithQuantity productsOrdered;
    private OrderState state;

    protected Order(ProductsWithQuantity productsOrdered) {
        this(UUID.randomUUID(), LocalDateTime.now(), productsOrdered, OrderState.WAITING);
    }

    protected Order(UUID id, LocalDateTime registrationDate, ProductsWithQuantity productsOrdered, OrderState state) {
        this.setId(id);
        this.setRegistrationDate(registrationDate);
        this.setProductsOrdered(productsOrdered);
        this.setState(state);
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public ProductsWithQuantity getProductsOrdered() {
        return this.productsOrdered;
    }

    public void setProductsOrdered(ProductsWithQuantity productsOrdered) {
        this.productsOrdered = productsOrdered;
    }

    public OrderState getState() {
        return this.state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public void addProducts(ProductsWithQuantity products) {
        this.changeState(OrderState.WAITING);
        this.getProductsOrdered().addProducts(products);
    }

    public void removeProducts(ProductsWithQuantity products) {
        this.changeState(OrderState.WAITING);
        this.getProductsOrdered().removeProducts(products);
    }

    public abstract BigDecimal generateTotal();

    protected abstract void changeState(OrderState state);

    public abstract Order copy();
}

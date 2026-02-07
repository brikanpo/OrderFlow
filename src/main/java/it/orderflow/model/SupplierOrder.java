package it.orderflow.model;

import it.orderflow.exceptions.InvalidStateException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class SupplierOrder extends Order {

    private Supplier supplier;
    private LocalDateTime deliveryDate;
    private UUID warehouseWorkerId;

    public SupplierOrder(ProductsWithQuantity productsOrdered, Supplier supplier) {
        super(productsOrdered);
        this.setSupplier(supplier);
    }

    public SupplierOrder(UUID id, LocalDateTime registrationDate, ProductsWithQuantity productsOrdered, OrderState state,
                         Supplier supplier, LocalDateTime deliveryDate, UUID warehouseWorkerId) {
        super(id, registrationDate, productsOrdered, state);
        this.setSupplier(supplier);
        this.setDeliveryDate(deliveryDate);
        this.setWarehouseWorkerId(warehouseWorkerId);
    }

    public Supplier getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public LocalDateTime getDeliveryDate() {
        return this.deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public UUID getWarehouseWorkerId() {
        return this.warehouseWorkerId;
    }

    public void setWarehouseWorkerId(UUID warehouseWorkerId) {
        this.warehouseWorkerId = warehouseWorkerId;
    }

    public void closeSupplierOrder(UUID warehouseWorkerId) {
        this.changeState(OrderState.CLOSED);
        this.setDeliveryDate(LocalDateTime.now());
        this.setWarehouseWorkerId(warehouseWorkerId);
    }

    @Override
    protected void changeState(OrderState state) {
        switch (state) {
            case WAITING -> {
                if (this.getState() != OrderState.WAITING) {
                    throw new InvalidStateException(InvalidStateException.StateType.TO_WAITING);
                }
            }
            case READY -> throw new InvalidStateException(InvalidStateException.StateType.TO_READY);
            case CLOSED -> {
                switch (this.getState()) {
                    case WAITING -> this.setState(state);
                    case READY, CLOSED -> throw new InvalidStateException(InvalidStateException.StateType.TO_CLOSED);
                }
            }
        }
    }

    @Override
    public BigDecimal generateTotal() {
        return this.getProductsOrdered().generateTotal().add(this.getSupplier().getTransportFee());
    }

    @Override
    public SupplierOrder clone() {
        return (SupplierOrder) super.clone();
    }
}

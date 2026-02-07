package it.orderflow.model;

import it.orderflow.exceptions.InvalidStateException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class SupplierOrder extends Order {

    private SupplierOrderData supplierOrderData;

    public SupplierOrder(ProductsWithQuantity productsOrdered, Supplier supplier) {
        super(productsOrdered);
        this.setSupplierOrderData(new SupplierOrderData(supplier));
    }

    public SupplierOrder(UUID id, LocalDateTime registrationDate, ProductsWithQuantity productsOrdered, OrderState state,
                         SupplierOrderData supplierOrderData) {
        super(id, registrationDate, productsOrdered, state);
        this.setSupplierOrderData(supplierOrderData);
    }

    public SupplierOrderData getSupplierOrderData() {
        return this.supplierOrderData;
    }

    public void setSupplierOrderData(SupplierOrderData supplierOrderData) {
        this.supplierOrderData = supplierOrderData;
    }

    public Supplier getSupplier() {
        return this.getSupplierOrderData().getSupplier();
    }

    public void setSupplier(Supplier supplier) {
        this.getSupplierOrderData().setSupplier(supplier);
    }

    public LocalDateTime getDeliveryDate() {
        return this.getSupplierOrderData().getDeliveryDate();
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.getSupplierOrderData().setDeliveryDate(deliveryDate);
    }

    public UUID getWarehouseWorkerId() {
        return this.getSupplierOrderData().getWarehouseWorkerId();
    }

    public void setWarehouseWorkerId(UUID warehouseWorkerId) {
        this.getSupplierOrderData().setWarehouseWorkerId(warehouseWorkerId);
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
    public SupplierOrder copy() {
        return new SupplierOrder(this.getId(), this.getRegistrationDate(), this.getProductsOrdered().copy(),
                this.getState(), this.getSupplierOrderData().copy());
    }
}

package it.orderflow.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class SupplierOrderData {

    private Supplier supplier;
    private LocalDateTime deliveryDate;
    private UUID warehouseWorkerId;

    public SupplierOrderData(Supplier supplier) {
        this(supplier, null, null);
    }

    public SupplierOrderData(Supplier supplier, LocalDateTime deliveryDate, UUID warehouseWorkerId) {
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

    public SupplierOrderData copy() {
        return new SupplierOrderData(this.getSupplier().copy(), this.getDeliveryDate(), this.getWarehouseWorkerId());
    }
}

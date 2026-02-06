package it.OrderFlow.Beans;

import it.OrderFlow.Model.OrderState;
import it.OrderFlow.Model.ProductsWithQuantity;
import it.OrderFlow.Model.SupplierOrder;

import java.time.LocalDateTime;
import java.util.UUID;

public class SupplierOrderBean {

    private UUID id;
    private LocalDateTime registrationDate;
    private ProductsWithQuantity productsOrdered;
    private OrderState orderState;
    private SupplierBean supplierBean;
    private LocalDateTime deliveryDate;
    private UUID warehouseWorkerId;

    public SupplierOrderBean() {
    }

    public SupplierOrderBean(SupplierOrder supplierOrder) {
        this.id = supplierOrder.getId();
        this.registrationDate = supplierOrder.getRegistrationDate();
        this.productsOrdered = supplierOrder.getProductsOrdered();
        this.supplierBean = new SupplierBean(supplierOrder.getSupplier());
        this.deliveryDate = supplierOrder.getDeliveryDate();
        this.warehouseWorkerId = supplierOrder.getWarehouseWorkerId();
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

    public OrderState getOrderState() {
        return this.orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public SupplierBean getSupplierBean() {
        return this.supplierBean;
    }

    public void setSupplierBean(SupplierBean supplierBean) {
        this.supplierBean = supplierBean;
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
}

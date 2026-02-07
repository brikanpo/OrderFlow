package it.orderflow.beans;

import it.orderflow.model.ClientOrder;
import it.orderflow.model.ProductWithQuantity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientOrderBean {

    private UUID id;
    private LocalDateTime registrationDate;
    private List<ProductWithQuantityBean> productsOrdered;
    private UUID representativeId;
    private ClientBean clientBean;
    private UUID warehouseWorkerId;
    private LocalDateTime deliveryDate;
    private UUID deliveryWorkerId;

    public ClientOrderBean() {
    }

    public ClientOrderBean(ClientOrder clientOrder) {
        this.id = clientOrder.getId();
        this.registrationDate = clientOrder.getRegistrationDate();
        List<ProductWithQuantityBean> products = new ArrayList<>();
        for (ProductWithQuantity product : clientOrder.getProductsOrdered().getProducts()) {
            products.add(new ProductWithQuantityBean(product));
        }
        this.productsOrdered = products;
        this.representativeId = clientOrder.getRepresentativeId();
        this.clientBean = new ClientBean(clientOrder.getClient());
        this.warehouseWorkerId = clientOrder.getWarehouseWorkerId();
        this.deliveryDate = clientOrder.getDeliveryDate();
        this.deliveryWorkerId = clientOrder.getDeliveryWorkerId();
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

    public List<ProductWithQuantityBean> getProductsOrdered() {
        return this.productsOrdered;
    }

    public void setProductsOrdered(List<ProductWithQuantityBean> productsOrdered) {
        this.productsOrdered = productsOrdered;
    }

    public UUID getRepresentativeId() {
        return this.representativeId;
    }

    public void setRepresentativeId(UUID representativeId) {
        this.representativeId = representativeId;
    }

    public ClientBean getClientBean() {
        return this.clientBean;
    }

    public void setClientBean(ClientBean clientBean) {
        this.clientBean = clientBean;
    }

    public UUID getWarehouseWorkerId() {
        return this.warehouseWorkerId;
    }

    public void setWarehouseWorkerId(UUID warehouseWorkerId) {
        this.warehouseWorkerId = warehouseWorkerId;
    }

    public LocalDateTime getDeliveryDate() {
        return this.deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public UUID getDeliveryWorkerId() {
        return this.deliveryWorkerId;
    }

    public void setDeliveryWorkerId(UUID deliveryWorkerId) {
        this.deliveryWorkerId = deliveryWorkerId;
    }
}

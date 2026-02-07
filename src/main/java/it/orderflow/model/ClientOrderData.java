package it.orderflow.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class ClientOrderData {

    private UUID representativeId;
    private Client client;
    private UUID warehouseWorkerId;
    private LocalDateTime deliveryDate;
    private UUID deliveryWorkerId;

    public ClientOrderData(UUID representativeId, Client client) {
        this(representativeId, client, null, null, null);
    }

    public ClientOrderData(UUID representativeId, Client client, UUID warehouseWorkerId,
                           LocalDateTime deliveryDate, UUID deliveryWorkerId) {
        this.setRepresentativeId(representativeId);
        this.setClient(client);
        this.setWarehouseWorkerId(warehouseWorkerId);
        this.setDeliveryDate(deliveryDate);
        this.setDeliveryWorkerId(deliveryWorkerId);
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public UUID getRepresentativeId() {
        return this.representativeId;
    }

    public void setRepresentativeId(UUID representativeId) {
        this.representativeId = representativeId;
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

    public ClientOrderData copy() {
        return new ClientOrderData(this.getRepresentativeId(), this.getClient().copy(), this.getWarehouseWorkerId(),
                this.getDeliveryDate(), this.getDeliveryWorkerId());
    }
}

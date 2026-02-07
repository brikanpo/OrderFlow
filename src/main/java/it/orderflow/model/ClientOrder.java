package it.orderflow.model;

import it.orderflow.exceptions.InvalidStateException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ClientOrder extends Order {

    private UUID representativeId;
    private Client client;
    private UUID warehouseWorkerId;
    private LocalDateTime deliveryDate;
    private UUID deliveryWorkerId;

    public ClientOrder(ProductsWithQuantity productsOrdered, UUID representativeId, Client client) {
        super(productsOrdered);
        this.setRepresentativeId(representativeId);
        this.setClient(client);
    }

    public ClientOrder(UUID id, LocalDateTime registrationDate, ProductsWithQuantity productsOrdered,
                       OrderState state, UUID representativeId, Client client, UUID warehouseWorkerId,
                       LocalDateTime deliveryDate, UUID deliveryWorkerId) {
        super(id, registrationDate, productsOrdered, state);
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

    public void prepareClientOrder(UUID warehouseWorkerId) {
        this.changeState(OrderState.READY);
        this.setWarehouseWorkerId(warehouseWorkerId);
    }

    public void closeClientOrder(UUID deliveryWorkerId) {
        this.changeState(OrderState.CLOSED);
        this.setDeliveryDate(LocalDateTime.now());
        this.setDeliveryWorkerId(deliveryWorkerId);
    }

    @Override
    protected void changeState(OrderState state) {
        switch (state) {
            case WAITING -> {
                if (this.getState() != OrderState.WAITING) {
                    throw new InvalidStateException(InvalidStateException.StateType.TO_WAITING);
                }
            }
            case READY -> {
                switch (this.getState()) {
                    case WAITING -> this.setState(state);
                    case READY, CLOSED -> throw new InvalidStateException(InvalidStateException.StateType.TO_READY);
                }
            }
            case CLOSED -> {
                switch (this.getState()) {
                    case READY -> this.setState(state);
                    case WAITING, CLOSED -> throw new InvalidStateException(InvalidStateException.StateType.TO_CLOSED);
                }
            }
        }
    }

    @Override
    public BigDecimal generateTotal() {
        return this.getProductsOrdered().generateTotal();
    }

    @Override
    public ClientOrder copy() {
        return new ClientOrder(this.getId(), this.getRegistrationDate(), this.getProductsOrdered().copy(),
                this.getState(), this.getRepresentativeId(), this.getClient().copy(), this.getWarehouseWorkerId(),
                this.getDeliveryDate(), this.getDeliveryWorkerId());
    }
}

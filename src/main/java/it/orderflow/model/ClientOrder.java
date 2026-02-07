package it.orderflow.model;

import it.orderflow.exceptions.InvalidStateException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ClientOrder extends Order {

    private ClientOrderData clientOrderData;

    public ClientOrder(ProductsWithQuantity productsOrdered, UUID representativeId, Client client) {
        super(productsOrdered);
        this.setClientOrderData(new ClientOrderData(representativeId, client));
    }

    public ClientOrder(UUID id, LocalDateTime registrationDate, ProductsWithQuantity productsOrdered,
                       OrderState state, ClientOrderData clientOrderData) {
        super(id, registrationDate, productsOrdered, state);
        this.setClientOrderData(clientOrderData);
    }

    public ClientOrderData getClientOrderData() {
        return this.clientOrderData;
    }

    public void setClientOrderData(ClientOrderData clientOrderData) {
        this.clientOrderData = clientOrderData;
    }

    public Client getClient() {
        return this.getClientOrderData().getClient();
    }

    public void setClient(Client client) {
        this.getClientOrderData().setClient(client);
    }

    public UUID getRepresentativeId() {
        return this.getClientOrderData().getRepresentativeId();
    }

    public void setRepresentativeId(UUID representativeId) {
        this.getClientOrderData().setRepresentativeId(representativeId);
    }

    public UUID getWarehouseWorkerId() {
        return this.getClientOrderData().getWarehouseWorkerId();
    }

    public void setWarehouseWorkerId(UUID warehouseWorkerId) {
        this.getClientOrderData().setWarehouseWorkerId(warehouseWorkerId);
    }

    public LocalDateTime getDeliveryDate() {
        return this.getClientOrderData().getDeliveryDate();
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.getClientOrderData().setDeliveryDate(deliveryDate);
    }

    public UUID getDeliveryWorkerId() {
        return this.getClientOrderData().getDeliveryWorkerId();
    }

    public void setDeliveryWorkerId(UUID deliveryWorkerId) {
        this.getClientOrderData().setDeliveryWorkerId(deliveryWorkerId);
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
                this.getState(), this.getClientOrderData().copy());
    }
}

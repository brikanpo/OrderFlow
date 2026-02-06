package it.OrderFlow.Model;

import Mock.Model.MockEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestClientOrders {

    private final MockEntity me = new MockEntity();
    private ClientOrders clientOrders;
    private ClientOrder order1;
    private ClientOrder order2;

    @BeforeEach
    void setUp() {

        order1 = new ClientOrder(UUID.randomUUID(),
                LocalDateTime.of(2026, 1, 10, 12, 0),
                me.getMockClientProductsWithQuantity(),
                OrderState.CLOSED,
                me.getMockRepresentative().getId(),
                me.getMockClient(),
                me.getMockWarehouseWorker().getId(),
                LocalDateTime.of(2026, 1, 12, 12, 0),
                me.getMockDeliveryWorker().getId());

        order2 = new ClientOrder(UUID.randomUUID(),
                LocalDateTime.of(2026, 1, 13, 12, 0),
                me.getMockClientProductsWithQuantity(),
                OrderState.CLOSED,
                me.getMockRepresentative().getId(),
                me.getMockClient(),
                me.getMockWarehouseWorker().getId(),
                LocalDateTime.of(2026, 1, 14, 12, 0),
                me.getMockDeliveryWorker().getId());

        clientOrders = new ClientOrders(new ArrayList<>(List.of(order1, order2)));
    }

    @Test
    void testGetAllProductsOrdered() {
        ProductsWithQuantity pwq = clientOrders.getAllProductsOrdered();

        assertEquals(1, pwq.getProducts().size());
    }

    @Test
    void testSortByRegistrationDateCheckAscending() {
        clientOrders.sortByRegistrationDate(true);

        assertEquals(order1.getRegistrationDate(),
                clientOrders.getOrders().getFirst().getRegistrationDate());
    }

    @Test
    void testSortByRegistrationDateCheckDescending() {
        clientOrders.sortByRegistrationDate(false);

        assertEquals(order2.getRegistrationDate(),
                clientOrders.getOrders().getFirst().getRegistrationDate());
    }

    @Test
    void testGetClientPastOrders() {
        ClientOrders pastClientOrders = clientOrders.getPastOrders(1);

        assertEquals(1, pastClientOrders.getOrders().size());
    }

    @Test
    void testGetClientPastOrdersCheckNegativeNumber() {
        ClientOrders pastClientOrders = clientOrders.getPastOrders(-1);

        assertEquals(0, pastClientOrders.getOrders().size());
    }

    @Test
    void testGetClientPastOrdersCheckBiggerNumber() {
        ClientOrders pastClientOrders = clientOrders.getPastOrders(10);

        assertEquals(2, pastClientOrders.getOrders().size());
    }
}
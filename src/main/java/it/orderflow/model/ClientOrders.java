package it.orderflow.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClientOrders {

    private final List<ClientOrder> orders;

    public ClientOrders(List<ClientOrder> orders) {
        this.orders = orders.stream().map(ClientOrder::clone).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<ClientOrder> getOrders() {
        return this.orders;
    }

    public ProductsWithQuantity getAllProductsOrdered() {
        ProductsWithQuantity result = new ProductsWithQuantity();
        for (ClientOrder clientOrder : this.getOrders()) {
            result.addProducts(clientOrder.getProductsOrdered());
        }
        return result;
    }

    public void sortByRegistrationDate(boolean ascending) {
        if (ascending) {
            this.getOrders().sort(Comparator.comparing(ClientOrder::getRegistrationDate));
        } else {
            this.getOrders().sort(Comparator.comparing(ClientOrder::getRegistrationDate).reversed());
        }
    }

    public ClientOrders getPastOrders(int numberOfOrders) {
        this.sortByRegistrationDate(false);
        return new ClientOrders(this.getOrders().subList(0, Math.max(0, Math.min(numberOfOrders, this.getOrders().size()))));
    }
}

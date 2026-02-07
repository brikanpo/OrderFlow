package it.orderflow.view.cli;

import it.orderflow.view.ManageOrdersView;
import it.orderflow.view.ViewEvent;

public class CLIManageOrdersView extends CLIRootView implements ManageOrdersView {

    @Override
    public void displayManageOrders() {
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            this.printTitle("Manage orders");
            System.out.println("What do you want to do?");
            System.out.println("1) Manage suppliers orders");
            System.out.println("2) Manage clients orders");
            System.out.println("3) Home");
            System.out.println("4) Logout");
            System.out.println("5) Exit");
            System.out.print("Type the number of the option chosen: ");
            switch (scanner.nextLine()) {
                case "1" -> this.notifyObservers(ViewEvent.MANAGE_SUPPLIERS_ORDERS);
                case "2" -> this.notifyObservers(ViewEvent.MANAGE_CLIENTS_ORDERS);
                case "3" -> this.notifyObservers(ViewEvent.HOME);
                case "4" -> this.notifyObservers(ViewEvent.LOGOUT);
                case "5" -> this.notifyObservers(ViewEvent.EXIT);
                default -> {
                    continue;
                }
            }
            chosenAnOption = true;
        }
    }

    @Override
    public void displayManageClientOrders() {
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            this.printTitle("Manage clients orders");
            System.out.println("What do you want to do?");
            System.out.println("1) Add client order");
            System.out.println("2) Prepare client order (NOT IMPLEMENTED)");
            System.out.println("3) Close client order (NOT IMPLEMENTED)");
            System.out.println("4) Back");
            System.out.println("5) Home");
            System.out.println("6) Logout");
            System.out.println("7) Exit");
            System.out.print("Type the number of the option chosen: ");
            switch (scanner.nextLine()) {
                case "1" -> this.notifyObservers(ViewEvent.ADD_CLIENT_ORDER);
                case "4" -> this.notifyObservers(ViewEvent.BACK);
                case "5" -> this.notifyObservers(ViewEvent.HOME);
                case "6" -> this.notifyObservers(ViewEvent.LOGOUT);
                case "7" -> this.notifyObservers(ViewEvent.EXIT);
                default -> {
                    continue;
                }
            }
            chosenAnOption = true;
        }
    }

    @Override
    public void displayManageSuppliersOrders() {
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            this.printTitle("Manage suppliers orders");
            System.out.println("What do you want to do?");
            System.out.println("1) Add supplier order (NOT IMPLEMENTED)");
            System.out.println("2) Close supplier order (NOT IMPLEMENTED)");
            System.out.println("3) Back");
            System.out.println("4) Home");
            System.out.println("5) Logout");
            System.out.println("6) Exit");
            System.out.print("Type the number of the option chosen: ");
            switch (scanner.nextLine()) {
                case "3" -> this.notifyObservers(ViewEvent.BACK);
                case "4" -> this.notifyObservers(ViewEvent.HOME);
                case "5" -> this.notifyObservers(ViewEvent.LOGOUT);
                case "6" -> this.notifyObservers(ViewEvent.EXIT);
                default -> {
                    continue;
                }
            }
            chosenAnOption = true;
        }
    }
}

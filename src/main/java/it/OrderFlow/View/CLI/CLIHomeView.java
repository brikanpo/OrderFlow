package it.OrderFlow.View.CLI;

import it.OrderFlow.View.HomeView;
import it.OrderFlow.View.ViewEvent;

public class CLIHomeView extends CLIRootView implements HomeView {

    public CLIHomeView() {
        super();
    }

    @Override
    public void displayManagerHome() {
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            this.printTitle("Home");
            System.out.println("You are logged in as a Manager");
            System.out.println("What do you want to do?");
            System.out.println("1) Manage employees");
            System.out.println("2) Manage suppliers");
            System.out.println("3) Manage clients");
            System.out.println("4) Manage articles");
            System.out.println("5) Manage products");
            System.out.println("6) Manage orders");
            System.out.println("7) Manage personal info");
            System.out.println("8) Logout");
            System.out.println("9) Exit");
            System.out.print("Type the number of the option chosen: ");
            switch (scanner.nextLine()) {
                case "1" -> this.notifyObservers(ViewEvent.MANAGE_EMPLOYEES);
                case "2" -> this.notifyObservers(ViewEvent.MANAGE_SUPPLIERS);
                case "3" -> this.notifyObservers(ViewEvent.MANAGE_CLIENTS);
                case "4" -> this.notifyObservers(ViewEvent.MANAGE_ARTICLES);
                case "5" -> this.notifyObservers(ViewEvent.MANAGE_PRODUCTS);
                case "6" -> this.notifyObservers(ViewEvent.MANAGE_ORDERS);
                case "7" -> this.notifyObservers(ViewEvent.MANAGE_PERSONAL_INFO);
                case "8" -> this.notifyObservers(ViewEvent.LOGOUT);
                case "9" -> this.notifyObservers(ViewEvent.EXIT);
                default -> {
                    continue;
                }
            }
            chosenAnOption = true;
        }
    }

    @Override
    public void displayRepresentativeHome() {
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            this.printTitle("Home");
            System.out.println("You are logged in as a Representative");
            System.out.println("What do you want to do?");
            System.out.println("1) Manage clients");
            System.out.println("2) Add client order");
            System.out.println("3) Manage personal info");
            System.out.println("4) Logout");
            System.out.println("5) Exit");
            System.out.print("Type the number of the option chosen: ");
            switch (scanner.nextLine()) {
                case "1" -> this.notifyObservers(ViewEvent.MANAGE_CLIENTS);
                case "2" -> this.notifyObservers(ViewEvent.ADD_CLIENT_ORDER);
                case "3" -> this.notifyObservers(ViewEvent.MANAGE_PERSONAL_INFO);
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
    public void displayWarehouseWorkerHome() {
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            this.printTitle("Home");
            System.out.println("You are logged in as a Warehouse worker");
            System.out.println("What do you want to do?");
            System.out.println("1) Prepare client order (NOT IMPLEMENTED)");
            System.out.println("2) Close supplier order (NOT IMPLEMENTED)");
            System.out.println("3) Manage personal info");
            System.out.println("4) Logout");
            System.out.println("5) Exit");
            System.out.print("Type the number of the option chosen: ");
            switch (scanner.nextLine()) {
                case "3" -> this.notifyObservers(ViewEvent.MANAGE_PERSONAL_INFO);
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
    public void displayDeliveryWorkerHome() {
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            this.printTitle("Home");
            System.out.println("You are logged in as a Delivery worker");
            System.out.println("What do you want to do?");
            System.out.println("1) Close client order (NOT IMPLEMENTED)");
            System.out.println("2) Manage personal info");
            System.out.println("3) Logout");
            System.out.println("4) Exit");
            System.out.print("Type the number of the option chosen: ");
            switch (scanner.nextLine()) {
                case "2" -> this.notifyObservers(ViewEvent.MANAGE_PERSONAL_INFO);
                case "3" -> this.notifyObservers(ViewEvent.LOGOUT);
                case "4" -> this.notifyObservers(ViewEvent.EXIT);
                default -> {
                    continue;
                }
            }
            chosenAnOption = true;
        }
    }
}

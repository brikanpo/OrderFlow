package it.OrderFlow.View.CLI;

import it.OrderFlow.Beans.*;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.AddClientOrderView;
import it.OrderFlow.View.ViewEvent;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class CLIAddClientOrderView extends CLIRootView implements AddClientOrderView {

    private String number;
    private List<ProductBean> productsList;
    private List<String> quantitiesProductsList;
    private List<ProductWithQuantityBean> unavailableProductsList;
    private List<String> quantitiesUnavailableProductsList;

    private String insertQuantity(String message) {
        String number;
        do {
            System.out.print(message);
            number = this.scanner.nextLine();
            try {
                Integer.parseInt(number);
            } catch (NumberFormatException e) {
                number = null;
            }
        } while (number == null);

        return number;
    }

    @Override
    public void displayInsertionOptions() {
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            this.printTitle("Add client order");
            System.out.println("How do you want to select the products to order?");
            System.out.println("1) From this client past orders");
            System.out.println("2) From the articles list");
            System.out.print("Type the number of the option chosen or 'back' to go back: ");
            String input = this.scanner.nextLine();
            if (input.equals("back")) {
                this.notifyObservers(ViewEvent.BACK);
            } else {
                switch (input) {
                    case "1" -> {
                        this.number = this.insertQuantity("Type the number of past orders to consider: ");
                        this.notifyObservers(ViewEvent.SELECTED_NUMBER_OF_PAST_ORDERS);
                    }
                    case "2" -> this.notifyObservers(ViewEvent.SELECTED_MANUAL_SELECTION);
                    default -> {
                        continue;
                    }
                }
            }

            chosenAnOption = true;
        }
    }

    @Override
    public void displayQuantitySelection(List<ProductBean> products) {
        this.number = null;
        this.productsList = products;
        this.quantitiesProductsList = new ArrayList<>();
        this.printTitle("Add client order");
        System.out.println("Inserts the quantity to order next to every product selected");
        for (ProductBean pb : products) {
            this.quantitiesProductsList.add(this.insertQuantity(pb.getArticleName() + " " + pb.getCode() + " : "));
        }
        this.notifyObservers(ViewEvent.SELECTED_PRODUCTS_QUANTITY);
    }

    @Override
    public void displayUnavailableProducts(List<ProductWithQuantityBean> products) {
        this.quantitiesProductsList = null;
        this.unavailableProductsList = products;
        this.printTitle("Add client order");
        System.out.println("These are the unavailable products");
        System.out.printf("%-2s) | %-25s | %-100s | %-12s |%n",
                "N°", "Article Name", "Code", "Quantity");
        for (int i = 0; i < products.size(); i++) {
            ProductWithQuantityBean pwqb = products.get(i);
            ProductBean pb = pwqb.getProductBean();

            System.out.printf("%-2s) | %-25s | %-100s | %-12s |%n",
                    (i + 1), pb.getArticleName(), pb.getCode(), pwqb.getQuantity());
        }

        this.quantitiesUnavailableProductsList = new ArrayList<>();
        System.out.println("Inserts the quantity to remove next to every unavailable product");
        for (ProductWithQuantityBean pwqb : products) {
            ProductBean pb = pwqb.getProductBean();
            this.quantitiesUnavailableProductsList.add(this.insertQuantity(pb.getArticleName() + " " + pb.getCode() + " : "));
        }

        this.notifyObservers(ViewEvent.SELECTED_UNAVAILABLE_PRODUCTS_QUANTITY);
    }

    @Override
    public void displayClientOrderDetails(ClientOrderBean clientOrder) {
        this.quantitiesUnavailableProductsList = null;
        this.printTitle("Add client order");

        ClientBean cb = clientOrder.getClientBean();
        System.out.println("These are the client info");
        System.out.printf("%-25s | %-35s | %-10s | %-50s | %-6s | %-30s | %-8s |%n",
                "Name", "Email", "Phone", "Address", "CAP", "City", "Province");

        String phone = cb.getPhone();
        if (phone == null) phone = "";

        AddressBean ab = cb.getAddressBean();

        if (ab != null) {
            System.out.printf("%-25s | %-35s | %-10s | %-50s | %-6s | %-30s | %-8s |%n",
                    cb.getName(), cb.getEmail(), phone, ab.getAddress(), ab.getCap(), ab.getCity(), ab.getProvince());
        } else {
            System.out.printf("%-25s | %-35s | %-10s | %-50s | %-6s | %-30s | %-8s |%n",
                    cb.getName(), cb.getEmail(), phone, "", "", "", "");
        }

        List<ProductWithQuantityBean> productsOrdered = clientOrder.getProductsOrdered();
        System.out.println("These are the products ordered");
        System.out.printf("%-2s) | %-25s | %-100s | %-10s | %-8s | %-12s |%n",
                "N°", "Article Name", "Code", "Price", "IVA", "Quantity");
        for (int i = 0; i < productsOrdered.size(); i++) {
            ProductWithQuantityBean pwqb = productsOrdered.get(i);
            ProductBean pb = pwqb.getProductBean();

            String price = NumberFormat.getCurrencyInstance().format(pb.getPrice());

            String iva = NumberFormat.getPercentInstance().format(pb.getArticleIva());

            System.out.printf("%-2s) | %-25s | %-100s | %-10s | %-8s | %-12s |%n",
                    (i + 1), pb.getArticleName(), pb.getCode(), price, iva, pwqb.getQuantity());
        }

        this.confirmOperation(ViewEvent.SAVE_CLIENT_ORDER, ViewEvent.HOME);
    }

    @Override
    public int selectedNumberOfPastOrders() throws InvalidInputException {
        int number = Integer.parseInt(this.number);

        if (number >= 1) {

            return number;

        } else throw new InvalidInputException(InvalidInputException.InputType.QUANTITY_1_OR_HIGHER);
    }

    @Override
    public List<ProductWithQuantityBean> selectedProductsQuantity() throws InvalidInputException {
        List<ProductWithQuantityBean> pwqbList = new ArrayList<>();

        for (int i = 0; i < this.productsList.size(); i++) {
            ProductWithQuantityBean pwqb = new ProductWithQuantityBean();
            pwqb.setProductBean(this.productsList.get(i));

            int number = Integer.parseInt(this.quantitiesProductsList.get(i));

            if (number >= 1) {

                pwqb.setQuantity(number);
                pwqbList.add(pwqb);

            } else throw new InvalidInputException(InvalidInputException.InputType.QUANTITY_1_OR_HIGHER);
        }

        return pwqbList;
    }

    @Override
    public List<ProductWithQuantityBean> selectedProductsToBeRemoved() throws InvalidInputException {
        List<ProductWithQuantityBean> pwqbList = new ArrayList<>();

        for (int i = 0; i < this.unavailableProductsList.size(); i++) {
            ProductWithQuantityBean pwqb = this.unavailableProductsList.get(i);

            int number = Integer.parseInt(this.quantitiesUnavailableProductsList.get(i));

            if (number >= 0) {

                pwqb.setQuantity(number);
                pwqbList.add(pwqb);

            } else throw new InvalidInputException(InvalidInputException.InputType.QUANTITY_0_OR_HIGHER);
        }

        return pwqbList;
    }

    @Override
    public void displayErrorMessage(Exception e) {
        super.displayErrorMessage(e);
        if (this.number != null) {
            this.displayInsertionOptions();
        } else if (this.quantitiesProductsList != null) {
            this.displayQuantitySelection(this.productsList);
        } else if (this.quantitiesUnavailableProductsList != null) {
            this.displayUnavailableProducts(this.unavailableProductsList);
        }
    }
}

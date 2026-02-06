package it.OrderFlow.View.CLI;

import it.OrderFlow.Beans.ProductBean;
import it.OrderFlow.Beans.ProductInStockBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.Model.Attributes;
import it.OrderFlow.View.ManageProductsView;
import it.OrderFlow.View.ViewEvent;

import java.math.BigDecimal;
import java.util.List;

public class CLIManageProductsView extends CLIRootView implements ManageProductsView {

    private List<String> attributesIds;
    private Attributes productAttributes;
    private String price;
    private String quantity;
    private String minimumStock;
    private String maximumStock;

    private void insertAttributesValues() {
        String attributesValue;
        this.productAttributes = new Attributes();
        for (String name : this.attributesIds) {
            do {
                System.out.print("Insert attribute " + name + " value: ");
                attributesValue = scanner.nextLine();
            } while (attributesValue.isBlank());
            this.productAttributes.addAttributeIdAndValue(name, attributesValue);
        }
    }

    private void insertProductInfo(ViewEvent event) {
        String type;
        if (event == ViewEvent.SAVE_SUPPLIER_PRODUCT) {
            type = "supplier product";
        } else {
            type = "product in inventory";
        }
        System.out.println("If this " + type + " has some attributes that varies between products you have to insert a value for each of them ");
        this.insertAttributesValues();
        System.out.print("Insert " + type + " price: ");
        this.price = scanner.nextLine();
        if (event == ViewEvent.SAVE_PRODUCT_IN_INVENTORY) {
            System.out.print("Insert quantity present in inventory: ");
            this.quantity = scanner.nextLine();
            System.out.print("Insert the minimum stock you want to keep in inventory: ");
            this.minimumStock = scanner.nextLine();
            System.out.print("Insert the maximum stock you want to keep in inventory: ");
            this.maximumStock = scanner.nextLine();
        }
    }

    @Override
    public void displayManageProducts() {
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            this.printTitle("Manage products");
            System.out.println("What do you want to do?");
            System.out.println("1) Add new supplier product");
            System.out.println("2) Add new product in inventory");
            System.out.println("3) Back");
            System.out.println("4) Home");
            System.out.println("5) Logout");
            System.out.println("6) Exit");
            System.out.print("Type the number of the option chosen: ");
            switch (this.scanner.nextLine()) {
                case "1" -> this.notifyObservers(ViewEvent.ADD_SUPPLIER_PRODUCT);
                case "2" -> this.notifyObservers(ViewEvent.ADD_PRODUCT_IN_INVENTORY);
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

    @Override
    public void displayAddSupplierProduct(List<String> attributesIds) {
        this.attributesIds = attributesIds;
        this.printTitle("Add supplier product");
        System.out.println("Insert the supplier product info.");
        this.insertProductInfo(ViewEvent.SAVE_SUPPLIER_PRODUCT);
        this.confirmOperation(ViewEvent.SAVE_SUPPLIER_PRODUCT, ViewEvent.BACK);
    }

    @Override
    public void displayAddProductInStock(List<String> attributesIds) {
        this.attributesIds = attributesIds;
        this.printTitle("Add product in inventory");
        System.out.println("Insert the product in inventory info.");
        this.insertProductInfo(ViewEvent.SAVE_PRODUCT_IN_INVENTORY);
        this.confirmOperation(ViewEvent.SAVE_PRODUCT_IN_INVENTORY, ViewEvent.BACK);
    }

    @Override
    public ProductBean getProductBean() throws InvalidInputException {
        ProductBean productBean = new ProductBean();

        productBean.setProductAttributes(this.productAttributes);

        try {
            productBean.setPrice(new BigDecimal(this.price));
        } catch (NumberFormatException e) {
            throw new InvalidInputException(InvalidInputException.InputType.PRICE);
        }

        return productBean;
    }

    @Override
    public ProductInStockBean getProductInStockBean() throws InvalidInputException {
        ProductInStockBean productInStockBean = new ProductInStockBean();

        productInStockBean.setProductAttributes(this.productAttributes);

        try {
            productInStockBean.setPrice(new BigDecimal(this.price));
        } catch (NumberFormatException e) {
            throw new InvalidInputException(InvalidInputException.InputType.PRICE);
        }

        try {
            productInStockBean.setQuantity(Integer.parseInt(this.quantity));
        } catch (NumberFormatException e) {
            throw new InvalidInputException(InvalidInputException.InputType.QUANTITY_0_OR_HIGHER);
        }

        try {
            productInStockBean.setMinimumStock(Integer.parseInt(this.minimumStock));
        } catch (NumberFormatException e) {
            throw new InvalidInputException(InvalidInputException.InputType.QUANTITY_0_OR_HIGHER);
        }

        try {
            productInStockBean.setMaximumStock(Integer.parseInt(this.maximumStock));
        } catch (NumberFormatException e) {
            throw new InvalidInputException(InvalidInputException.InputType.QUANTITY_0_OR_HIGHER);
        }

        return productInStockBean;
    }

    @Override
    public void displayErrorMessage(Exception e) {
        super.displayErrorMessage(e);
        this.displayManageProducts();
    }
}

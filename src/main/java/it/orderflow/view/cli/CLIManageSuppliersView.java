package it.orderflow.view.cli;

import it.orderflow.beans.SupplierBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.ManageSuppliersView;
import it.orderflow.view.ViewEvent;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class CLIManageSuppliersView extends CLIRootView implements ManageSuppliersView {

    private String name;
    private String email;
    private String phone;
    private String transportFee;

    private void insertSupplierInfo() {
        System.out.print("Insert supplier name: ");
        this.name = scanner.nextLine();
        System.out.print("Insert supplier email: ");
        this.email = scanner.nextLine();
        System.out.println("The following fields are optional. To ignore leave blank");
        System.out.print("Insert supplier phone: ");
        this.phone = scanner.nextLine();
        System.out.print("Insert supplier transport fee: ");
        this.transportFee = scanner.nextLine();

    }

    @Override
    public void displayManageSuppliers() {
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            this.printTitle("Manage suppliers");
            System.out.println("What do you want to do?");
            System.out.println("1) Add supplier");
            System.out.println("2) Change supplier info");
            System.out.println("3) Home");
            System.out.println("4) Logout");
            System.out.println("5) Exit");
            System.out.print("Type the number of the option chosen: ");
            switch (this.scanner.nextLine()) {
                case "1" -> this.notifyObservers(ViewEvent.ADD_SUPPLIER);
                case "2" -> this.notifyObservers(ViewEvent.CHANGE_SUPPLIER_INFO);
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
    public void displayAddSupplier() {
        this.printTitle("Add supplier");
        this.insertSupplierInfo();
        this.confirmOperation(ViewEvent.SAVE_SUPPLIER, ViewEvent.BACK);
    }

    @Override
    public void displayChangeSupplierInfo(SupplierBean supplier) {
        this.printTitle("Change supplier info");
        System.out.println("These are the selected supplier's info");
        System.out.printf("%-25s | %-35s | %-10s | %-13s |%n",
                "Name", "Email", "Phone", "Transport Fee");

        String phone = supplier.getPhone();
        if (phone == null) phone = "";

        BigDecimal transportFee = supplier.getTransportFee();
        String transportFeeStr = "";
        if (transportFee != null) transportFeeStr = NumberFormat.getCurrencyInstance().format(transportFee);

        System.out.printf("%-25s | %-35s | %-10s | %-12s |%n",
                supplier.getName(), supplier.getEmail(), phone, transportFeeStr);

        System.out.println("If you don't want to change something leave the field blank.");
        this.insertSupplierInfo();
        this.confirmOperation(ViewEvent.SAVE_SUPPLIER_INFO_CHANGE, ViewEvent.BACK);
    }

    @Override
    public SupplierBean getSupplierBean() throws InvalidInputException {
        SupplierBean supplierBean = new SupplierBean();

        if (!this.name.isBlank()) supplierBean.setName(this.name);

        if (!this.email.isBlank()) supplierBean.setEmail(this.email);

        if (!this.phone.isBlank()) supplierBean.setPhone(this.phone);

        if (!this.transportFee.isBlank()) {
            try {
                supplierBean.setTransportFee(new BigDecimal(this.transportFee));
            } catch (NumberFormatException e) {
                throw new InvalidInputException(InvalidInputException.InputType.PRICE);
            }
        }

        return supplierBean;
    }

    @Override
    public void displayErrorMessage(Exception e) {
        super.displayErrorMessage(e);
        this.displayManageSuppliers();
    }
}

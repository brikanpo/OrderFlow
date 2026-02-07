package it.orderflow.view.cli;

import it.orderflow.beans.AddressBean;
import it.orderflow.beans.ClientBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.ManageClientsView;
import it.orderflow.view.ViewEvent;

public class CLIManageClientsView extends CLIRootView implements ManageClientsView {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String cap;
    private String city;
    private String province;
    private ClientBean client;

    public CLIManageClientsView() {
        super();
    }

    private void insertClientInfo() {
        System.out.print("Insert client name: ");
        this.name = scanner.nextLine();
        System.out.print("Insert client email: ");
        this.email = scanner.nextLine();
        System.out.println("The following fields are optional. To ignore leave blank");
        System.out.print("Insert client phone: ");
        this.phone = scanner.nextLine();
        System.out.print("Insert client address: ");
        this.address = scanner.nextLine();
        System.out.print("Insert client cap: ");
        this.cap = scanner.nextLine();
        System.out.print("Insert client city: ");
        this.city = scanner.nextLine();
        System.out.print("Insert client province: ");
        this.province = scanner.nextLine();
    }

    @Override
    public void displayManageClients() {
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            this.printTitle("Manage clients");
            System.out.println("What do you want to do?");
            System.out.println("1) Add client");
            System.out.println("2) Change client info");
            System.out.println("3) Home");
            System.out.println("4) Logout");
            System.out.println("5) Exit");
            System.out.print("Type the number of the option chosen: ");
            switch (this.scanner.nextLine()) {
                case "1" -> this.notifyObservers(ViewEvent.ADD_CLIENT);
                case "2" -> this.notifyObservers(ViewEvent.CHANGE_CLIENT_INFO);
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
    public void displayAddClient() {
        this.client = new ClientBean();
        this.printTitle("Add client");
        this.insertClientInfo();
        this.confirmOperation(ViewEvent.SAVE_CLIENT, ViewEvent.BACK);
    }

    @Override
    public void displayChangeClientInfo(ClientBean clientBean) {
        this.client = clientBean;
        this.printTitle("Change client");
        System.out.println("These are the selected client's info");
        System.out.printf("%-25s | %-35s | %-10s | %-50s | %-6s | %-30s | %-7s |%n",
                "Name", "Email", "Phone", "Address", "CAP", "City", "Province");

        String tempPhone = clientBean.getPhone();
        if (tempPhone == null) tempPhone = "";

        AddressBean ab = clientBean.getAddressBean();

        if (ab != null) {
            System.out.printf("%-25s | %-35s | %-10s | %-50s | %-6s | %-30s | %-8s |%n",
                    clientBean.getName(), clientBean.getEmail(), tempPhone,
                    ab.getStreetAddress(), ab.getCap(), ab.getCity(), ab.getProvince());
        } else {
            System.out.printf("%-25s | %-35s | %-10s | %-50s | %-6s | %-30s | %-8s |%n",
                    clientBean.getName(), clientBean.getEmail(), tempPhone, "", "", "", "");
        }

        System.out.println("If you don't want to change something leave the field blank.");
        this.insertClientInfo();
        this.confirmOperation(ViewEvent.SAVE_CLIENT_INFO_CHANGE, ViewEvent.BACK);
    }

    @Override
    public ClientBean getClientBean() throws InvalidInputException {

        if (!this.name.isBlank()) this.client.setName(this.name);

        if (!this.email.isBlank()) this.client.setEmail(this.email);

        if (!this.phone.isBlank()) this.client.setPhone(this.phone);

        if (!this.address.isBlank() && !this.cap.isBlank() && !this.city.isBlank() && !this.province.isBlank()) {
            AddressBean addressBean = new AddressBean();
            addressBean.setStreetAddress(this.address);
            addressBean.setCap(this.cap);
            addressBean.setCity(this.city);
            addressBean.setProvince(this.province);
            this.client.setAddressBean(addressBean);
        } else throw new InvalidInputException(InvalidInputException.InputType.ADDRESS);

        return this.client;
    }

    @Override
    public void displayErrorMessage(Exception e) {
        super.displayErrorMessage(e);
        this.displayManageClients();
    }
}

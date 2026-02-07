package it.orderflow.view.cli.selectionviews;

import it.orderflow.beans.AddressBean;
import it.orderflow.beans.ClientBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.selectionviews.ClientSelectionView;

import java.util.List;

public class CLIClientSelectionView extends CLIAbstractSelectionView<ClientBean> implements ClientSelectionView {

    private final String title;
    private final String message;
    private final boolean creationEnabled;

    public CLIClientSelectionView(String title, String message, boolean creationEnabled) {
        super();
        this.title = title;
        this.message = message;
        this.creationEnabled = creationEnabled;
    }

    @Override
    public void displayClients(List<ClientBean> clients, ViewEvent onBack) {
        this.items = clients;
        this.onBack = onBack;
        System.out.println(this.title);
        System.out.println(this.message);
        System.out.printf("%-2s) | %-25s | %-35s | %-10s | %-50s | %-6s | %-30s | %-8s |%n",
                "N°", "Name", "Email", "Phone", "Address", "CAP", "City", "Province");
        for (int i = 0; i < clients.size(); i++) {
            ClientBean cb = clients.get(i);

            String phone = cb.getPhone();
            if (phone == null) phone = "";

            AddressBean ab = cb.getAddressBean();

            if (ab != null) {
                System.out.printf("%-2d) | %-25s | %-35s | %-10s | %-50s | %-6s | %-30s | %-8s |%n",
                        (i + 1), cb.getName(), cb.getEmail(), phone, ab.getAddress(), ab.getCap(), ab.getCity(), ab.getProvince());
            } else {
                System.out.printf("%-2d) | %-25s | %-35s | %-10s | %-50s | %-6s | %-30s | %-8s |%n",
                        (i + 1), cb.getName(), cb.getEmail(), phone, "", "", "", "");
            }
        }
        if (this.creationEnabled) {
            System.out.print("Type the number of the client chosen, 'new client' to add a new client or 'back' to go back: ");
            this.index = this.scanner.nextLine();

            if (this.index.equals("new client")) this.notifyObservers(ViewEvent.ADD_CLIENT);
            else if (this.index.equals("back")) this.notifyObservers(onBack);
            else this.notifyObservers(ViewEvent.SELECTED_CLIENT);
        } else {
            System.out.print("Type the number of the client chosen or 'back' to go back: ");
            this.index = this.scanner.nextLine();
            if (this.index.equals("back")) this.notifyObservers(onBack);
            else this.notifyObservers(ViewEvent.SELECTED_CLIENT);
        }
    }

    @Override
    public ClientBean selectedClient() throws InvalidInputException {
        return this.selectedItem();
    }

    @Override
    public void displayErrorMessage(Exception e) {
        super.displayErrorMessage(e);
        this.displayClients(this.items, this.onBack);
    }

    @Override
    protected void printHeader() {

    }

    @Override
    protected void printRow(int position, ClientBean bean) {

    }

    @Override
    protected void printSelectionMessage(boolean singleSelection) {

    }

    @Override
    protected void onItemSelected(boolean singleSelection) {

    }
}

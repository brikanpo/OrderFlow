package it.OrderFlow.View.CLI.SelectionViews;

import it.OrderFlow.Beans.ClientOrderBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.SelectionComponents.ClientOrderSelectionView;
import it.OrderFlow.View.ViewEvent;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CLIClientOrderSelectionView extends CLIAbstractSelectionView<ClientOrderBean> implements ClientOrderSelectionView {

    private final String title;
    private final String message;

    public CLIClientOrderSelectionView(String title, String message) {
        super();
        this.title = title;
        this.message = message;
    }

    @Override
    public void displayClientsOrders(List<ClientOrderBean> clientsOrders, ViewEvent onBack) {
        this.displayItems(true, clientsOrders, this.title, this.message, onBack);
    }

    @Override
    public ClientOrderBean selectedClientOrder() throws InvalidInputException {
        return this.selectedItem();
    }

    @Override
    public void displayErrorMessage(Exception e) {
        super.displayErrorMessage(e);
        this.displayClientsOrders(this.items, this.onBack);
    }

    @Override
    protected void printHeader() {
        System.out.printf("%-2s) | %-17s | %-25s |%n", "N°", "Registration Date", "Client Name");
    }

    @Override
    protected void printRow(int position, ClientOrderBean bean) {
        System.out.printf("%-2d) | %-17s | %-25s |%n",
                position, bean.getRegistrationDate().format(DateTimeFormatter.ISO_LOCAL_DATE), bean.getClientBean().getName());
    }

    @Override
    protected void printSelectionMessage(boolean singleSelection) {
        System.out.print("Type the number of the client order chosen or 'back' to go back: ");
    }

    @Override
    protected void onItemSelected(boolean singleSelection) {
        this.notifyObservers(ViewEvent.SELECTED_CLIENT_ORDER);
    }
}

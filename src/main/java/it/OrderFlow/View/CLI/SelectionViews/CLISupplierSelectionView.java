package it.OrderFlow.View.CLI.SelectionViews;

import it.OrderFlow.Beans.SupplierBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.SelectionComponents.SupplierSelectionView;
import it.OrderFlow.View.ViewEvent;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

public class CLISupplierSelectionView extends CLIAbstractSelectionView<SupplierBean> implements SupplierSelectionView {

    private final String title;
    private final String message;

    public CLISupplierSelectionView(String title, String message) {
        super();
        this.title = title;
        this.message = message;
    }

    @Override
    public void displaySuppliers(List<SupplierBean> suppliers, ViewEvent onBack) {
        this.displayItems(true, suppliers, this.title, this.message, onBack);
    }

    @Override
    public SupplierBean selectedSupplier() throws InvalidInputException {
        return this.selectedItem();
    }

    @Override
    public void displayErrorMessage(Exception e) {
        super.displayErrorMessage(e);
        this.displaySuppliers(this.items, this.onBack);
    }

    @Override
    protected void printHeader() {
        System.out.printf("%-2s) | %-25s | %-35s | %-10s | %-12s |%n",
                "N°", "Name", "Email", "Phone", "Transport Fee");
    }

    @Override
    protected void printRow(int position, SupplierBean bean) {
        String phone = bean.getPhone();
        if (phone == null) phone = "";

        BigDecimal transportFee = bean.getTransportFee();
        String transportFeeStr = "";
        if (transportFee != null) transportFeeStr = NumberFormat.getCurrencyInstance().format(transportFee);

        System.out.printf("%-2d) | %-25s | %-35s | %-10s | %-12s |%n",
                position, bean.getName(), bean.getEmail(), phone, transportFeeStr);
    }

    @Override
    protected void printSelectionMessage(boolean singleSelection) {
        System.out.print("Type the number of the supplier chosen or 'back' to go back: ");
    }

    @Override
    protected void onItemSelected(boolean singleSelection) {
        this.notifyObservers(ViewEvent.SELECTED_SUPPLIER);
    }
}

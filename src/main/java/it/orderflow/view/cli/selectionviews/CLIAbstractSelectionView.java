package it.orderflow.view.cli.selectionviews;

import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.cli.CLIRootView;

import java.util.ArrayList;
import java.util.List;

public abstract class CLIAbstractSelectionView<T> extends CLIRootView {

    protected boolean singleSelection;
    protected List<T> items;
    protected ViewEvent onBack;
    protected String index;
    protected String indexes;

    protected void displayItems(boolean singleSelection, List<T> items, String title, String message, ViewEvent onBack) {
        this.singleSelection = singleSelection;
        this.onBack = onBack;
        this.items = items;
        this.printTitle(title);
        System.out.println(message);

        this.printHeader();
        for (int i = 0; i < items.size(); i++) {
            this.printRow(i + 1, items.get(i));
        }

        this.printSelectionMessage(singleSelection);

        String input = this.scanner.nextLine();

        if (input.equals("back")) {
            this.notifyObservers(onBack);
        } else {
            if (singleSelection) {
                this.index = input;
            } else {
                this.indexes = input;
            }

            this.onItemSelected(singleSelection);
        }
    }

    protected T selectedItem() throws InvalidInputException {
        try {
            return this.items.get(Integer.parseInt(index) - 1);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new InvalidInputException(InvalidInputException.InputType.SELECTION, e);
        } finally {
            this.index = null;
        }
    }

    protected List<T> selectedItems() throws InvalidInputException {
        try {
            List<T> itemsList = new ArrayList<>();

            String[] indexList = this.indexes.split(",");

            for (String tempIndex : indexList) {
                itemsList.add(this.items.get(Integer.parseInt(tempIndex) - 1));
            }

            return itemsList;
        } catch (Exception e) {
            throw new InvalidInputException(InvalidInputException.InputType.SELECTION, e);
        } finally {
            this.indexes = null;
        }
    }

    protected abstract void printHeader();

    protected abstract void printRow(int position, T bean);

    protected abstract void printSelectionMessage(boolean singleSelection);

    protected abstract void onItemSelected(boolean singleSelection);
}

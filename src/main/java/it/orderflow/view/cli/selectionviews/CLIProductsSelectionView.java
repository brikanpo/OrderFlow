package it.orderflow.view.cli.selectionviews;

import it.orderflow.beans.ProductBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.selectionviews.ProductsSelectionView;

import java.text.NumberFormat;
import java.util.List;

public class CLIProductsSelectionView extends CLIAbstractSelectionView<ProductBean> implements ProductsSelectionView {

    private final String title;
    private final String message;

    public CLIProductsSelectionView(String title, String message) {
        super();
        this.title = title;
        this.message = message;
    }

    @Override
    public void displayProducts(List<ProductBean> products, ViewEvent onBack) {
        this.displayItems(false, products, this.title, this.message, onBack);
    }

    @Override
    public List<ProductBean> selectedProducts() throws InvalidInputException {
        return this.selectedItems();
    }

    @Override
    public void displayErrorMessage(Exception e) {
        super.displayErrorMessage(e);
        this.displayProducts(this.items, this.onBack);
    }

    @Override
    protected void printHeader() {
        System.out.printf("%-2s) | %-25s | %-100s | %-12s | %-6s |%n",
                "N°", "Article Name", "Code", "Price", "IVA");
    }

    @Override
    protected void printRow(int position, ProductBean bean) {
        String price = NumberFormat.getCurrencyInstance().format(bean.getPrice());

        String iva = NumberFormat.getPercentInstance().format(bean.getArticleIva());

        System.out.printf("%-2s) | %-25s | %-100s | %-12s | %-6s |%n",
                position, bean.getArticleName(), bean.getCode(), price, iva);
    }

    @Override
    protected void printSelectionMessage(boolean singleSelection) {
        System.out.print("Type the number of the products chosen separated by commas(e.g, 1,2,4) or 'back' to go back: ");
    }

    @Override
    protected void onItemSelected(boolean singleSelection) {
        this.notifyObservers(ViewEvent.SELECTED_PRODUCTS);
    }
}

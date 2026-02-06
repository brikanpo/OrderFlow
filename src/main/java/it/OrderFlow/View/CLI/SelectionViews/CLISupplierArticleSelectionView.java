package it.OrderFlow.View.CLI.SelectionViews;

import it.OrderFlow.Beans.ArticleBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.SelectionComponents.SupplierArticleSelectionView;
import it.OrderFlow.View.ViewEvent;

import java.text.NumberFormat;
import java.util.List;

public class CLISupplierArticleSelectionView extends CLIAbstractSelectionView<ArticleBean> implements SupplierArticleSelectionView {

    private final String title;
    private final String message;

    public CLISupplierArticleSelectionView(String title, String message) {
        super();
        this.title = title;
        this.message = message;
    }

    @Override
    public void displaySupplierArticles(List<ArticleBean> supplierArticles, ViewEvent onBack) {
        this.displayItems(true, supplierArticles, this.title, this.message, onBack);
    }

    @Override
    public ArticleBean selectedSupplierArticle() throws InvalidInputException {
        return this.selectedItem();
    }

    @Override
    public void displayErrorMessage(Exception e) {
        super.displayErrorMessage(e);
        this.displaySupplierArticles(this.items, this.onBack);
    }

    @Override
    protected void printHeader() {
        System.out.printf("%-2s) | %-25s | %-25s | %-200s | %-6s |%n",
                "N°", "Name", "Category", "Description", "IVA");
    }

    @Override
    protected void printRow(int position, ArticleBean bean) {
        String description = bean.getDescription();
        if (description == null) description = "";

        String iva = NumberFormat.getPercentInstance().format(bean.getIva());

        System.out.printf("%-2s) | %-25s | %-25s | %-200s | %-6s |%n",
                position, bean.getName(), bean.getCategory(), description, iva);

    }

    @Override
    protected void printSelectionMessage(boolean singleSelection) {
        System.out.print("Type the number of the supplier article chosen or 'back' to go back: ");
    }

    @Override
    protected void onItemSelected(boolean singleSelection) {
        this.notifyObservers(ViewEvent.SELECTED_SUPPLIER_ARTICLE);
    }
}

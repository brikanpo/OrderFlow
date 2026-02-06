package it.OrderFlow.View.CLI.SelectionViews;

import it.OrderFlow.Beans.ArticleBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.SelectionComponents.ClientArticleSelectionView;
import it.OrderFlow.View.ViewEvent;

import java.text.NumberFormat;
import java.util.List;

public class CLIClientArticleSelectionView extends CLIAbstractSelectionView<ArticleBean> implements ClientArticleSelectionView {

    private final String title;
    private final String message;

    public CLIClientArticleSelectionView(String title, String message) {
        super();
        this.title = title;
        this.message = message;
    }

    @Override
    public void displayClientArticles(boolean singleSelection, List<ArticleBean> clientArticles, ViewEvent onBack) {
        this.displayItems(singleSelection, clientArticles, this.title, this.message, onBack);
    }

    @Override
    public ArticleBean selectedClientArticle() throws InvalidInputException {
        return this.selectedItem();
    }

    @Override
    public List<ArticleBean> selectedClientArticles() throws InvalidInputException {
        return this.selectedItems();
    }

    @Override
    public void displayErrorMessage(Exception e) {
        super.displayErrorMessage(e);
        this.displayClientArticles(this.singleSelection, this.items, this.onBack);
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
        if (singleSelection) System.out.print("Type the number of the client article chosen or 'back' to go back: ");
        else
            System.out.print("Type the number of the client articles chosen separated by commas(e.g, 1,2,4) or 'back' to go back: ");
    }

    @Override
    protected void onItemSelected(boolean singleSelection) {
        if (singleSelection) this.notifyObservers(ViewEvent.SELECTED_CLIENT_ARTICLE);
        else this.notifyObservers(ViewEvent.SELECTED_CLIENT_ARTICLES);
    }
}

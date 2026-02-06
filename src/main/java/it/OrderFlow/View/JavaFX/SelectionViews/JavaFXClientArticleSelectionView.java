package it.OrderFlow.View.JavaFX.SelectionViews;

import it.OrderFlow.Beans.ArticleBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.JavaFX.MyComponents.SpecificTableView.MyTableViewArticle;
import it.OrderFlow.View.SelectionComponents.ClientArticleSelectionView;
import it.OrderFlow.View.ViewEvent;

import java.util.List;

public class JavaFXClientArticleSelectionView extends JavaFXAbstractSelectionView<ArticleBean, MyTableViewArticle>
        implements ClientArticleSelectionView {

    public JavaFXClientArticleSelectionView(String title, String message) {
        super(title, message);
    }

    @Override
    public void displayClientArticles(boolean singleSelection, List<ArticleBean> clientArticles, ViewEvent onBack) {
        this.displayItems(singleSelection, clientArticles, onBack);
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
    protected MyTableViewArticle createTable(List<ArticleBean> items) {
        return new MyTableViewArticle(items);
    }

    @Override
    protected void onConfirmPressed(boolean singleSelection) {
        if (singleSelection) this.notifyObservers(ViewEvent.SELECTED_CLIENT_ARTICLE);
        else this.notifyObservers(ViewEvent.SELECTED_CLIENT_ARTICLES);
    }
}

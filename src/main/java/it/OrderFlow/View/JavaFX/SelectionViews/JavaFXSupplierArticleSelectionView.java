package it.OrderFlow.View.JavaFX.SelectionViews;

import it.OrderFlow.Beans.ArticleBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.JavaFX.MyComponents.SpecificTableView.MyTableViewArticle;
import it.OrderFlow.View.SelectionComponents.SupplierArticleSelectionView;
import it.OrderFlow.View.ViewEvent;

import java.util.List;

public class JavaFXSupplierArticleSelectionView extends JavaFXAbstractSelectionView<ArticleBean, MyTableViewArticle>
        implements SupplierArticleSelectionView {

    public JavaFXSupplierArticleSelectionView(String title, String message) {
        super(title, message);
    }

    @Override
    public void displaySupplierArticles(List<ArticleBean> supplierArticles, ViewEvent onBack) {
        this.displayItems(true, supplierArticles, onBack);
    }

    @Override
    public ArticleBean selectedSupplierArticle() throws InvalidInputException {
        return this.selectedItem();
    }

    @Override
    protected MyTableViewArticle createTable(List<ArticleBean> items) {
        return new MyTableViewArticle(items);
    }

    @Override
    protected void onConfirmPressed(boolean singleSelection) {
        this.notifyObservers(ViewEvent.SELECTED_SUPPLIER_ARTICLE);
    }
}

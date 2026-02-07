package it.orderflow.view.javafx.selectionviews;

import it.orderflow.beans.ArticleBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.javafx.mycomponents.specifictableview.MyTableViewArticle;
import it.orderflow.view.selectionviews.SupplierArticleSelectionView;

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

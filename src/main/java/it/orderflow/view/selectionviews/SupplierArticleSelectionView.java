package it.orderflow.view.selectionviews;

import it.orderflow.beans.ArticleBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.BasicView;
import it.orderflow.view.ViewEvent;

import java.util.List;

public interface SupplierArticleSelectionView extends BasicView {

    void displaySupplierArticles(List<ArticleBean> supplierArticles, ViewEvent onBack);

    ArticleBean selectedSupplierArticle() throws InvalidInputException;
}

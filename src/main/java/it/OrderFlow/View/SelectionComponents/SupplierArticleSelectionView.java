package it.OrderFlow.View.SelectionComponents;

import it.OrderFlow.Beans.ArticleBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.BasicView;
import it.OrderFlow.View.ViewEvent;

import java.util.List;

public interface SupplierArticleSelectionView extends BasicView {

    void displaySupplierArticles(List<ArticleBean> supplierArticles, ViewEvent onBack);

    ArticleBean selectedSupplierArticle() throws InvalidInputException;
}

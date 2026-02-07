package it.orderflow.view.selectionviews;

import it.orderflow.beans.ArticleBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.BasicView;
import it.orderflow.view.ViewEvent;

import java.util.List;

public interface ClientArticleSelectionView extends BasicView {

    void displayClientArticles(boolean singleSelection, List<ArticleBean> clientArticles, ViewEvent onBack);

    ArticleBean selectedClientArticle() throws InvalidInputException;

    List<ArticleBean> selectedClientArticles() throws InvalidInputException;
}

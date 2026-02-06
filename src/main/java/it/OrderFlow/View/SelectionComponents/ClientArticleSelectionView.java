package it.OrderFlow.View.SelectionComponents;

import it.OrderFlow.Beans.ArticleBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.BasicView;
import it.OrderFlow.View.ViewEvent;

import java.util.List;

public interface ClientArticleSelectionView extends BasicView {

    void displayClientArticles(boolean singleSelection, List<ArticleBean> clientArticles, ViewEvent onBack);

    ArticleBean selectedClientArticle() throws InvalidInputException;

    List<ArticleBean> selectedClientArticles() throws InvalidInputException;
}

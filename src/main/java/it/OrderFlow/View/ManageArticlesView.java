package it.OrderFlow.View;

import it.OrderFlow.Beans.ArticleBean;
import it.OrderFlow.Exceptions.InvalidInputException;

public interface ManageArticlesView extends BasicView {

    void displayManageArticles();

    void displayAddSupplierArticle();

    void displayAddClientArticle();

    ArticleBean getSupplierArticleBean() throws InvalidInputException;

    ArticleBean getClientArticleBean() throws InvalidInputException;
}

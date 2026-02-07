package it.orderflow.view;

import it.orderflow.beans.ArticleBean;
import it.orderflow.exceptions.InvalidInputException;

public interface ManageArticlesView extends BasicView {

    void displayManageArticles();

    void displayAddSupplierArticle();

    void displayAddClientArticle();

    ArticleBean getSupplierArticleBean() throws InvalidInputException;

    ArticleBean getClientArticleBean() throws InvalidInputException;
}

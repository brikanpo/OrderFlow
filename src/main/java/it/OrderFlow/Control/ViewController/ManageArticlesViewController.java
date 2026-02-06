package it.OrderFlow.Control.ViewController;

import it.OrderFlow.Beans.ArticleBean;
import it.OrderFlow.Beans.SupplierBean;
import it.OrderFlow.Control.ControllerEvent;
import it.OrderFlow.Control.LogicController.ManageArticlesLogicController;
import it.OrderFlow.View.ManageArticlesView;
import it.OrderFlow.View.SelectionComponents.SupplierArticleSelectionView;
import it.OrderFlow.View.SelectionComponents.SupplierSelectionView;
import it.OrderFlow.View.ViewEvent;

import java.util.List;

public class ManageArticlesViewController extends RootViewController {

    private ManageArticlesView view;
    private SupplierSelectionView supplierSelectionView;
    private SupplierArticleSelectionView supplierArticleSelectionView;
    private ManageArticlesLogicController logicController;

    public ManageArticlesViewController(ManageArticlesView view,
                                        SupplierSelectionView supplierSelectionView,
                                        SupplierArticleSelectionView supplierArticleSelectionView,
                                        ManageArticlesLogicController logicController) {
        super();
        this.setView(view);
        this.setSupplierSelectionView(supplierSelectionView);
        this.setSupplierArticleSelectionView(supplierArticleSelectionView);
        this.setLogicController(logicController);
    }

    public ManageArticlesView getView() {
        return this.view;
    }

    public void setView(ManageArticlesView view) {
        this.view = view;
        this.view.addObserver(this);
    }

    public SupplierSelectionView getSupplierSelectionView() {
        return this.supplierSelectionView;
    }

    public void setSupplierSelectionView(SupplierSelectionView supplierSelectionView) {
        this.supplierSelectionView = supplierSelectionView;
        this.supplierSelectionView.addObserver(this);
    }

    public SupplierArticleSelectionView getSupplierArticleSelectionView() {
        return this.supplierArticleSelectionView;
    }

    public void setSupplierArticleSelectionView(SupplierArticleSelectionView supplierArticleSelectionView) {
        this.supplierArticleSelectionView = supplierArticleSelectionView;
        this.supplierArticleSelectionView.addObserver(this);
    }

    public ManageArticlesLogicController getLogicController() {
        return this.logicController;
    }

    public void setLogicController(ManageArticlesLogicController logicController) {
        this.logicController = logicController;
    }

    @Override
    public void start() {
        this.getView().displayManageArticles();
    }

    @Override
    public void onEvent(ViewEvent event) {
        switch (event) {
            case ADD_SUPPLIER_ARTICLE -> {
                try {
                    List<SupplierBean> suppliersBeans = this.getLogicController().getSuppliersList();

                    this.getSupplierSelectionView().displaySuppliers(suppliersBeans, ViewEvent.BACK);
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case SELECTED_SUPPLIER -> {
                try {
                    SupplierBean supplierBean = this.getSupplierSelectionView().selectedSupplier();

                    this.getLogicController().addSelectedSupplier(supplierBean);

                    this.getView().displayAddSupplierArticle();
                } catch (Exception e) {
                    this.getSupplierSelectionView().displayErrorMessage(e);
                }
            }
            case SAVE_SUPPLIER_ARTICLE -> {
                try {
                    ArticleBean supplierArticleBean = this.getView().getSupplierArticleBean();

                    this.getLogicController().saveNewSupplierArticle(supplierArticleBean);

                    this.getView().displaySuccessMessage("Operation add supplier article successful");
                    this.start();
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case ADD_CLIENT_ARTICLE -> {
                try {
                    List<ArticleBean> supplierArticlesBeans = this.getLogicController().getSupplierArticlesList();

                    this.getSupplierArticleSelectionView().displaySupplierArticles(supplierArticlesBeans, ViewEvent.BACK);
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case SELECTED_SUPPLIER_ARTICLE -> {
                try {
                    ArticleBean articleBean = this.getSupplierArticleSelectionView().selectedSupplierArticle();

                    this.getLogicController().addSelectedSupplierArticle(articleBean);

                    this.getView().displayAddClientArticle();
                } catch (Exception e) {
                    this.getSupplierArticleSelectionView().displayErrorMessage(e);
                }
            }
            case SAVE_CLIENT_ARTICLE -> {
                try {
                    ArticleBean clientArticleBean = this.getView().getClientArticleBean();

                    this.getLogicController().saveNewClientArticle(clientArticleBean);

                    this.getView().displaySuccessMessage("Operation add client article successful");
                    this.start();
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case BACK -> this.start();
            case HOME -> this.notifyObservers(ControllerEvent.HOME);
            case LOGOUT -> this.notifyObservers(ControllerEvent.LOGOUT);
            case EXIT -> {
                this.getView().close();
                this.notifyObservers(ControllerEvent.EXIT);
            }
        }
    }
}

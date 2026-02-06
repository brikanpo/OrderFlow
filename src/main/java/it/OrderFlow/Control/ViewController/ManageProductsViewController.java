package it.OrderFlow.Control.ViewController;

import it.OrderFlow.Beans.ArticleBean;
import it.OrderFlow.Beans.ProductBean;
import it.OrderFlow.Beans.ProductInStockBean;
import it.OrderFlow.Control.ControllerEvent;
import it.OrderFlow.Control.LogicController.ManageProductsLogicController;
import it.OrderFlow.View.ManageProductsView;
import it.OrderFlow.View.SelectionComponents.ClientArticleSelectionView;
import it.OrderFlow.View.SelectionComponents.SupplierArticleSelectionView;
import it.OrderFlow.View.ViewEvent;

import java.util.List;

public class ManageProductsViewController extends RootViewController {

    private ManageProductsView view;
    private SupplierArticleSelectionView supplierArticleSelectionView;
    private ClientArticleSelectionView clientArticleSelectionView;
    private ManageProductsLogicController logicController;

    public ManageProductsViewController(ManageProductsView view,
                                        SupplierArticleSelectionView supplierArticleSelectionView,
                                        ClientArticleSelectionView clientArticleSelectionView,
                                        ManageProductsLogicController logicController) {
        super();
        this.setView(view);
        this.setSupplierArticleSelectionView(supplierArticleSelectionView);
        this.setClientArticlesSelectionView(clientArticleSelectionView);
        this.setLogicController(logicController);
    }

    public ManageProductsView getView() {
        return this.view;
    }

    public void setView(ManageProductsView view) {
        this.view = view;
        this.view.addObserver(this);
    }

    public SupplierArticleSelectionView getSupplierArticleSelectionView() {
        return this.supplierArticleSelectionView;
    }

    public void setSupplierArticleSelectionView(SupplierArticleSelectionView supplierArticleSelectionView) {
        this.supplierArticleSelectionView = supplierArticleSelectionView;
        this.supplierArticleSelectionView.addObserver(this);
    }

    public ClientArticleSelectionView getClientArticlesSelectionView() {
        return this.clientArticleSelectionView;
    }

    public void setClientArticlesSelectionView(ClientArticleSelectionView clientArticleSelectionView) {
        this.clientArticleSelectionView = clientArticleSelectionView;
        this.clientArticleSelectionView.addObserver(this);
    }

    public ManageProductsLogicController getLogicController() {
        return this.logicController;
    }

    public void setLogicController(ManageProductsLogicController logicController) {
        this.logicController = logicController;
    }

    @Override
    public void start() {
        this.getView().displayManageProducts();
    }

    @Override
    public void onEvent(ViewEvent event) {
        switch (event) {
            case ADD_SUPPLIER_PRODUCT -> {
                try {
                    List<ArticleBean> supplierArticlesBeans = this.getLogicController().getSupplierArticlesList();

                    this.getSupplierArticleSelectionView().displaySupplierArticles(supplierArticlesBeans, ViewEvent.BACK);
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case SELECTED_SUPPLIER_ARTICLE -> {
                try {
                    ArticleBean supplierArticleBean = this.getSupplierArticleSelectionView().selectedSupplierArticle();

                    this.getLogicController().addSelectedSupplierArticle(supplierArticleBean);

                    this.getView().displayAddSupplierProduct(supplierArticleBean.getPossibleAttributes().getAttributesId());
                } catch (Exception e) {
                    this.getSupplierArticleSelectionView().displayErrorMessage(e);
                }
            }
            case SAVE_SUPPLIER_PRODUCT -> {
                try {
                    ProductBean productBean = this.getView().getProductBean();

                    this.getLogicController().saveNewProduct(productBean);

                    this.getView().displaySuccessMessage("Operation add supplier product successful");
                    this.start();
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case ADD_PRODUCT_IN_INVENTORY -> {
                try {
                    List<ArticleBean> clientArticleBeans = this.getLogicController().getClientArticleList();

                    this.getClientArticlesSelectionView().displayClientArticles(true, clientArticleBeans, ViewEvent.BACK);
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case SELECTED_CLIENT_ARTICLE -> {
                try {
                    ArticleBean clientArticleBean = this.getClientArticlesSelectionView().selectedClientArticle();

                    this.getLogicController().addSelectedClientArticle(clientArticleBean);

                    this.getView().displayAddProductInStock(clientArticleBean.getPossibleAttributes().getAttributesId());
                } catch (Exception e) {
                    this.getClientArticlesSelectionView().displayErrorMessage(e);
                }
            }
            case SAVE_PRODUCT_IN_INVENTORY -> {
                try {
                    ProductInStockBean productInStockBean = this.getView().getProductInStockBean();

                    this.getLogicController().saveNewProductInStock(productInStockBean);

                    this.getView().displaySuccessMessage("Operation add product in inventory successful");
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

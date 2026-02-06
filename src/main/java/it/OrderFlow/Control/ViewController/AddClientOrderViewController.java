package it.OrderFlow.Control.ViewController;

import it.OrderFlow.Beans.ArticleBean;
import it.OrderFlow.Beans.ClientBean;
import it.OrderFlow.Beans.ProductBean;
import it.OrderFlow.Beans.ProductWithQuantityBean;
import it.OrderFlow.Control.ControllerEvent;
import it.OrderFlow.Control.LogicController.AddClientOrderLogicController;
import it.OrderFlow.View.AddClientOrderView;
import it.OrderFlow.View.SelectionComponents.ClientArticleSelectionView;
import it.OrderFlow.View.SelectionComponents.ClientSelectionView;
import it.OrderFlow.View.SelectionComponents.ProductsSelectionView;
import it.OrderFlow.View.ViewEvent;

import java.util.List;

public class AddClientOrderViewController extends RootViewController {

    private AddClientOrderView view;
    private ClientSelectionView clientSelectionView;
    private ProductsSelectionView productsSelectionView;
    private ClientArticleSelectionView clientArticleSelectionView;
    private AddClientOrderLogicController logicController;

    public AddClientOrderViewController(AddClientOrderView view, ClientSelectionView clientSelectionView,
                                        ProductsSelectionView productsSelectionView,
                                        ClientArticleSelectionView clientArticleSelectionView,
                                        AddClientOrderLogicController logicController) {
        super();
        this.setView(view);
        this.setClientSelectionView(clientSelectionView);
        this.setProductsSelectionView(productsSelectionView);
        this.setClientArticlesSelectionView(clientArticleSelectionView);
        this.setLogicController(logicController);
    }

    public AddClientOrderView getView() {
        return this.view;
    }

    public void setView(AddClientOrderView view) {
        this.view = view;
        this.view.addObserver(this);
    }

    public ClientSelectionView getClientSelectionView() {
        return this.clientSelectionView;
    }

    public void setClientSelectionView(ClientSelectionView clientSelectionView) {
        this.clientSelectionView = clientSelectionView;
        this.clientSelectionView.addObserver(this);
    }

    public ProductsSelectionView getProductsSelectionView() {
        return this.productsSelectionView;
    }

    public void setProductsSelectionView(ProductsSelectionView productsSelectionView) {
        this.productsSelectionView = productsSelectionView;
        this.productsSelectionView.addObserver(this);
    }

    public ClientArticleSelectionView getClientArticlesSelectionView() {
        return this.clientArticleSelectionView;
    }

    public void setClientArticlesSelectionView(ClientArticleSelectionView clientArticleSelectionView) {
        this.clientArticleSelectionView = clientArticleSelectionView;
        this.clientArticleSelectionView.addObserver(this);
    }

    public AddClientOrderLogicController getLogicController() {
        return this.logicController;
    }

    public void setLogicController(AddClientOrderLogicController logicController) {
        this.logicController = logicController;
    }

    private void selectInsertOptions() {
        this.getView().displayInsertionOptions();
    }

    private void selectPastProductsOrdered() {
        try {
            List<ProductBean> clientProducts = this.getLogicController().getPastClientOrdersProductsList();

            this.getProductsSelectionView().displayProducts(clientProducts, ViewEvent.SELECT_INSERT_OPTIONS);
        } catch (Exception e) {
            this.getView().displayErrorMessage(e);
        }
    }

    private void selectProducts() {
        try {
            List<ProductBean> clientProducts = this.getLogicController().getClientProductsListByArticleName();

            this.getProductsSelectionView().displayProducts(clientProducts, ViewEvent.SELECTED_MANUAL_SELECTION);
        } catch (Exception e) {
            this.getClientArticlesSelectionView().displayErrorMessage(e);
        }
    }

    private void selectQuantity() {
        List<ProductBean> products = this.getLogicController().getSelectedProducts();

        this.getView().displayQuantitySelection(products);
    }

    private void selectUnavailableProductsQuantity() {
        try {
            List<ProductWithQuantityBean> unavailableProducts = this.getLogicController().getUnavailableProducts();

            if (unavailableProducts.isEmpty()) {
                this.getView().displayClientOrderDetails(this.getLogicController().getTempClientOrderBean());
            } else {
                this.getView().displayUnavailableProducts(unavailableProducts);
            }
        } catch (Exception e) {
            this.getView().displayErrorMessage(e);
        }
    }

    @Override
    public void start() {
        try {
            List<ClientBean> clients = this.getLogicController().getClientsList();

            this.getClientSelectionView().displayClients(clients, ViewEvent.HOME);
        } catch (Exception e) {
            this.getView().displayErrorMessage(e);
        }
    }

    @Override
    public void onEvent(ViewEvent viewEvent) {
        switch (viewEvent) {
            case ADD_CLIENT -> this.notifyObservers(ControllerEvent.ADD_CLIENT);
            case SELECTED_CLIENT -> {
                try {
                    ClientBean selectedClient = this.getClientSelectionView().selectedClient();

                    this.getLogicController().addSelectedClient(selectedClient);

                    this.selectInsertOptions();
                } catch (Exception e) {
                    this.getClientSelectionView().displayErrorMessage(e);
                }
            }
            case SELECT_INSERT_OPTIONS -> this.selectInsertOptions();
            case SELECTED_NUMBER_OF_PAST_ORDERS -> {
                try {
                    int numberOfPastOrders = this.getView().selectedNumberOfPastOrders();

                    this.getLogicController().addSelectedNumberOfPastOrders(numberOfPastOrders);

                    this.selectPastProductsOrdered();
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case SELECT_PRODUCTS -> {
                if (this.getLogicController().isInManualSelectionState()) {
                    this.selectProducts();
                } else {
                    this.selectPastProductsOrdered();
                }
            }
            case SELECTED_MANUAL_SELECTION -> {
                try {
                    List<ArticleBean> clientArticles = this.getLogicController().getClientArticlesList();

                    this.getClientArticlesSelectionView().displayClientArticles(false, clientArticles, ViewEvent.SELECT_INSERT_OPTIONS);
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case SELECTED_CLIENT_ARTICLES -> {
                try {
                    List<ArticleBean> clientArticles = this.getClientArticlesSelectionView().selectedClientArticles();

                    this.getLogicController().addSelectedClientArticles(clientArticles);

                    this.selectProducts();
                } catch (Exception e) {
                    this.getClientArticlesSelectionView().displayErrorMessage(e);
                }
            }
            case SELECTED_PRODUCTS -> {
                try {
                    List<ProductBean> products = this.getProductsSelectionView().selectedProducts();

                    this.getLogicController().addSelectedProducts(products);

                    this.selectQuantity();
                } catch (Exception e) {
                    this.getProductsSelectionView().displayErrorMessage(e);
                }
            }
            case SELECT_QUANTITY -> this.selectQuantity();
            case SELECTED_PRODUCTS_QUANTITY -> {
                try {
                    List<ProductWithQuantityBean> productsWithQuantity = this.getView().selectedProductsQuantity();

                    this.getLogicController().addSelectedProductsWithQuantity(productsWithQuantity);

                    this.selectUnavailableProductsQuantity();
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case SELECT_UNAVAILABLE_PRODUCTS_QUANTITY -> this.selectUnavailableProductsQuantity();
            case SELECTED_UNAVAILABLE_PRODUCTS_QUANTITY -> {
                try {
                    List<ProductWithQuantityBean> productsWithQuantity = this.getView().selectedProductsToBeRemoved();

                    this.getLogicController().removeSelectedProductsWithQuantity(productsWithQuantity);

                    this.getView().displayClientOrderDetails(this.getLogicController().getTempClientOrderBean());
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case SAVE_CLIENT_ORDER -> {
                try {
                    this.getLogicController().saveNewClientOrder();

                    this.getView().displaySuccessMessage("Operation add client order successful");

                    this.notifyObservers(ControllerEvent.HOME);
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

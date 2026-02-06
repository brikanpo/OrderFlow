package it.OrderFlow.View.JavaFX;

import it.OrderFlow.View.*;
import it.OrderFlow.View.JavaFX.SelectionViews.*;
import it.OrderFlow.View.SelectionComponents.*;

public class JavaFXViewsFactory extends ViewsFactory {

    @Override
    public AddClientOrderView getAddClientOrderView() {
        return new JavaFXAddClientOrderView();
    }

    @Override
    public ClientOrderSelectionView getClientOrderSelectionView(String title, String message) {
        return new JavaFXClientOrderSelectionView(title, message);
    }

    @Override
    public ClientSelectionView getClientSelectionView(String title, String message, boolean creationEnabled) {
        return new JavaFXClientSelectionView(title, message, creationEnabled);
    }

    @Override
    public CloseClientOrderView getCloseClientOrderView() {
        throw new UnsupportedOperationException();//TODO return new JavaFXCloseClientOrderView();
    }

    @Override
    public ClientArticleSelectionView getClientArticleSelectionView(String title, String message) {
        return new JavaFXClientArticleSelectionView(title, message);
    }

    @Override
    public LoginView getLoginView() {
        return new JavaFXLoginView();
    }

    @Override
    public HomeView getHomeView() {
        return new JavaFXHomeView();
    }

    @Override
    public ManageEmployeesView getManageEmployeesView() {
        return new JavaFXManageEmployeesView();
    }

    @Override
    public ManageSuppliersView getManageSuppliersView() {
        return new JavaFXManageSuppliersView();
    }

    @Override
    public ManageClientsView getManageClientsView() {
        return new JavaFXManageClientsView();
    }

    @Override
    public ManageArticlesView getManageArticlesView() {
        return new JavaFXManageArticlesView();
    }

    @Override
    public ManageOrdersView getManageOrdersView() {
        return new JavaFXManageOrdersView();
    }

    @Override
    public ManagePersonalInfoView getManagePersonalInfoView() {
        return new JavaFXManagePersonalInfoView();
    }

    @Override
    public ManageProductsView getManageProductsView() {
        return new JavaFXManageProductsView();
    }

    @Override
    public ProductsSelectionView getProductsSelectionView(String title, String message) {
        return new JavaFXProductsSelectionView(title, message);
    }

    @Override
    public SupplierArticleSelectionView getSupplierArticleSelectionView(String title, String message) {
        return new JavaFXSupplierArticleSelectionView(title, message);
    }

    @Override
    public SupplierSelectionView getSupplierSelectionView(String title, String message) {
        return new JavaFXSuppliersSelectionView(title, message);
    }
}

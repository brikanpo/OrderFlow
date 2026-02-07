package it.orderflow.view.cli;

import it.orderflow.view.*;
import it.orderflow.view.cli.selectionviews.*;
import it.orderflow.view.selectionviews.*;

public class CLIViewsFactory extends ViewsFactory {

    @Override
    public AddClientOrderView getAddClientOrderView() {
        return new CLIAddClientOrderView();
    }

    @Override
    public ClientOrderSelectionView getClientOrderSelectionView(String title, String message) {
        return new CLIClientOrderSelectionView(title, message);
    }

    @Override
    public ClientSelectionView getClientSelectionView(String title, String message, boolean creationEnabled) {
        return new CLIClientSelectionView(title, message, creationEnabled);
    }

    @Override
    public CloseClientOrderView getCloseClientOrderView() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClientArticleSelectionView getClientArticleSelectionView(String title, String message) {
        return new CLIClientArticleSelectionView(title, message);
    }

    @Override
    public LoginView getLoginView() {
        return new CLILoginView();
    }

    @Override
    public HomeView getHomeView() {
        return new CLIHomeView();
    }

    @Override
    public ManageEmployeesView getManageEmployeesView() {
        return new CLIManageEmployeesView();
    }

    @Override
    public ManageSuppliersView getManageSuppliersView() {
        return new CLIManageSuppliersView();
    }

    @Override
    public ManageClientsView getManageClientsView() {
        return new CLIManageClientsView();
    }

    @Override
    public ManageArticlesView getManageArticlesView() {
        return new CLIManageArticlesView();
    }

    @Override
    public ManageOrdersView getManageOrdersView() {
        return new CLIManageOrdersView();
    }

    @Override
    public ManagePersonalInfoView getManagePersonalInfoView() {
        return new CLIManagePersonalInfoView();
    }

    @Override
    public ManageProductsView getManageProductsView() {
        return new CLIManageProductsView();
    }

    @Override
    public ProductsSelectionView getProductsSelectionView(String title, String message) {
        return new CLIProductsSelectionView(title, message);
    }

    @Override
    public SupplierArticleSelectionView getSupplierArticleSelectionView(String title, String message) {
        return new CLISupplierArticleSelectionView(title, message);
    }

    @Override
    public SupplierSelectionView getSupplierSelectionView(String title, String message) {
        return new CLISupplierSelectionView(title, message);
    }
}

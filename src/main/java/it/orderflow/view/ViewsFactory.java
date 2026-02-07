package it.orderflow.view;

import it.orderflow.view.cli.CLIViewsFactory;
import it.orderflow.view.javafx.JavaFXViewsFactory;
import it.orderflow.view.selectionviews.*;

import java.util.HashMap;
import java.util.Map;

public abstract class ViewsFactory {

    private static final Map<ViewType, ViewsFactory> factories = new HashMap<>();

    public synchronized static ViewsFactory getFactory(ViewType type) {
        if (!factories.containsKey(type)) {
            ViewsFactory factory = switch (type) {
                case CLI -> new CLIViewsFactory();
                case JAVAFX -> new JavaFXViewsFactory();
            };
            factories.put(type, factory);
        }
        return factories.get(type);
    }

    public abstract AddClientOrderView getAddClientOrderView();

    public abstract ClientOrderSelectionView getClientOrderSelectionView(String title, String message);

    public abstract ClientSelectionView getClientSelectionView(String title, String message, boolean creationEnabled);

    public abstract CloseClientOrderView getCloseClientOrderView();

    public abstract ClientArticleSelectionView getClientArticleSelectionView(String title, String message);

    public abstract LoginView getLoginView();

    public abstract HomeView getHomeView();

    public abstract ManageEmployeesView getManageEmployeesView();

    public abstract ManageSuppliersView getManageSuppliersView();

    public abstract ManageClientsView getManageClientsView();

    public abstract ManageArticlesView getManageArticlesView();

    public abstract ManageOrdersView getManageOrdersView();

    public abstract ManagePersonalInfoView getManagePersonalInfoView();

    public abstract ManageProductsView getManageProductsView();

    public abstract ProductsSelectionView getProductsSelectionView(String title, String message);

    public abstract SupplierArticleSelectionView getSupplierArticleSelectionView(String title, String message);

    public abstract SupplierSelectionView getSupplierSelectionView(String title, String message);
}

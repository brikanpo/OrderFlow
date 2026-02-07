package it.orderflow.control.viewcontroller;

import it.orderflow.beans.EmployeeBean;
import it.orderflow.control.logiccontroller.LogicControllerFactory;
import it.orderflow.view.ViewsFactory;

public class ViewControllerFactory {

    private final ViewsFactory viewsFactory;
    private final LogicControllerFactory logicControllerFactory;

    public ViewControllerFactory(ViewsFactory viewsFactory, LogicControllerFactory logicControllerFactory) {
        this.viewsFactory = viewsFactory;
        this.logicControllerFactory = logicControllerFactory;
    }

    public AddClientOrderViewController getAddClientOrderViewController(EmployeeBean loggedEmployee) {
        String title = "Add client order";
        return new AddClientOrderViewController(viewsFactory.getAddClientOrderView(),
                viewsFactory.getClientSelectionView(title,
                        "Choose which client is associated with this new order", true),
                viewsFactory.getProductsSelectionView(title,
                        "Choose which products you want to add to your new order"),
                viewsFactory.getClientArticleSelectionView(title,
                        "Choose which articles you want to add to your new order"),
                logicControllerFactory.getAddClientOrderLogicController(loggedEmployee));
    }

    public CloseClientOrderViewController getCloseClientOrderViewController(EmployeeBean loggedEmployee) {
        return new CloseClientOrderViewController(viewsFactory.getCloseClientOrderView(),
                viewsFactory.getClientOrderSelectionView("Close client order",
                        "Choose which order you want to close"),
                logicControllerFactory.getCloseClientOrderLogicController(loggedEmployee));
    }

    public HomeViewController getHomeViewController(EmployeeBean loggedEmployee) {
        return new HomeViewController(viewsFactory.getHomeView(), loggedEmployee);
    }

    public LoginViewController getLoginViewController() {
        return new LoginViewController(viewsFactory.getLoginView(), logicControllerFactory.getLoginLogicController());
    }

    public ManageArticlesViewController getManageArticlesViewController() {
        return new ManageArticlesViewController(viewsFactory.getManageArticlesView(),
                viewsFactory.getSupplierSelectionView("Add supplier article",
                        "Choose which supplier is associated with this new supplier article"),
                viewsFactory.getSupplierArticleSelectionView("Add client article",
                        "Choose which supplier article is associated with this new client article"),
                logicControllerFactory.getManageArticlesLogicController());
    }

    public ManageClientsViewController getManageClientsViewController() {
        return new ManageClientsViewController(viewsFactory.getManageClientsView(),
                viewsFactory.getClientSelectionView("Change client info",
                        "Choose which client's info you want to change", false),
                logicControllerFactory.getManageClientsLogicController());
    }

    public ManageEmployeesViewController getManageEmployeesViewController() {
        return new ManageEmployeesViewController(viewsFactory.getManageEmployeesView(),
                logicControllerFactory.getManageEmployeesLogicController());
    }

    public ManageOrdersViewController getManageOrdersViewController(EmployeeBean loggedEmployee) {
        return new ManageOrdersViewController(viewsFactory.getManageOrdersView(),
                logicControllerFactory.getManageOrdersLogicController(loggedEmployee));
    }

    public ManagePersonalInfoViewController getManagePersonalInfoViewController(EmployeeBean loggedEmployee) {
        return new ManagePersonalInfoViewController(viewsFactory.getManagePersonalInfoView(),
                logicControllerFactory.getManagePersonalInfoLogicController(loggedEmployee));
    }

    public ManageProductsViewController getManageProductsViewController() {
        return new ManageProductsViewController(viewsFactory.getManageProductsView(),
                viewsFactory.getSupplierArticleSelectionView("Add supplier product",
                        "Choose which supplier article is associated with this new supplier product"),
                viewsFactory.getClientArticleSelectionView("Add product in inventory",
                        "Choose which client article is associated with this new product in inventory"),
                logicControllerFactory.getManageProductsLogicController());
    }

    public ManageSuppliersViewController getManageSuppliersViewController() {
        return new ManageSuppliersViewController(viewsFactory.getManageSuppliersView(),
                viewsFactory.getSupplierSelectionView("Change supplier info",
                        "Choose which supplier's info you want to change"),
                logicControllerFactory.getManageSuppliersLogicController());
    }
}

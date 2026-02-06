package it.OrderFlow.Control;

import it.OrderFlow.Beans.EmployeeBean;
import it.OrderFlow.Control.LogicController.LogicControllerFactory;
import it.OrderFlow.Control.ViewController.ManageClientsViewController;
import it.OrderFlow.Control.ViewController.ManagePersonalInfoViewController;
import it.OrderFlow.Control.ViewController.RootViewController;
import it.OrderFlow.Control.ViewController.ViewControllerFactory;
import it.OrderFlow.DAO.DAOFactory;
import it.OrderFlow.DAO.DAOType;
import it.OrderFlow.DAO.DBMS.DBMSConnectionPool;
import it.OrderFlow.View.ViewType;
import it.OrderFlow.View.ViewsFactory;

public class AppController implements ControllerResultObserver {

    private final ViewControllerFactory viewControllerFactory;
    private EmployeeBean loggedEmployee;
    public AppController(ViewType viewType, DAOType daoType) {
        this.viewControllerFactory = new ViewControllerFactory(ViewsFactory.getFactory(viewType),
                new LogicControllerFactory(DAOFactory.getFactory(daoType)));
    }

    public EmployeeBean getLoggedEmployee() {
        return this.loggedEmployee;
    }

    public void setLoggedEmployee(EmployeeBean loggedEmployee) {
        this.loggedEmployee = loggedEmployee;
    }

    public void start() {
        this.startController(ControllerType.LOGIN);
    }

    public RootViewController getViewController(ControllerType controllerType) {
        RootViewController controller = switch (controllerType) {
            case ADD_CLIENT_ORDER ->
                    this.viewControllerFactory.getAddClientOrderViewController(this.getLoggedEmployee());
            case CLOSE_CLIENT_ORDER ->
                    this.viewControllerFactory.getCloseClientOrderViewController(this.getLoggedEmployee());
            case HOME -> this.viewControllerFactory.getHomeViewController(this.getLoggedEmployee());
            case LOGIN -> this.viewControllerFactory.getLoginViewController();
            case MANAGE_ARTICLES -> this.viewControllerFactory.getManageArticlesViewController();
            case MANAGE_CLIENTS -> this.viewControllerFactory.getManageClientsViewController();
            case MANAGE_EMPLOYEES -> this.viewControllerFactory.getManageEmployeesViewController();
            case MANAGE_ORDERS -> this.viewControllerFactory.getManageOrdersViewController(this.getLoggedEmployee());
            case MANAGE_PERSONAL_INFO ->
                    this.viewControllerFactory.getManagePersonalInfoViewController(this.getLoggedEmployee());
            case MANAGE_PRODUCTS -> this.viewControllerFactory.getManageProductsViewController();
            case MANAGE_SUPPLIERS -> this.viewControllerFactory.getManageSuppliersViewController();
        };
        controller.addObserver(this);
        return controller;
    }

    public void startController(ControllerType controllerType) {
        this.getViewController(controllerType).start();
    }

    @Override
    public void onEvent(ControllerEvent event) {
        switch (event) {
            case ADD_CLIENT -> {
                ManageClientsViewController manageClientsViewController =
                        (ManageClientsViewController) this.getViewController(ControllerType.MANAGE_CLIENTS);
                manageClientsViewController.addClient();
            }
            case ADD_CLIENT_ORDER -> this.startController(ControllerType.ADD_CLIENT_ORDER);
            case CLOSE_CLIENT_ORDER -> this.startController(ControllerType.CLOSE_CLIENT_ORDER);
            case HOME -> this.startController(ControllerType.HOME);
            case MANAGE_ARTICLES -> this.startController(ControllerType.MANAGE_ARTICLES);
            case MANAGE_CLIENTS -> this.startController(ControllerType.MANAGE_CLIENTS);
            case MANAGE_EMPLOYEES -> this.startController(ControllerType.MANAGE_EMPLOYEES);
            case MANAGE_ORDERS -> this.startController(ControllerType.MANAGE_ORDERS);
            case MANAGE_PERSONAL_INFO -> this.startController(ControllerType.MANAGE_PERSONAL_INFO);
            case MANAGE_PRODUCTS -> this.startController(ControllerType.MANAGE_PRODUCTS);
            case MANAGE_SUPPLIERS -> this.startController(ControllerType.MANAGE_SUPPLIERS);
            case LOGOUT -> {
                this.setLoggedEmployee(null);
                this.start();
            }
            case EXIT -> {
                if (DBMSConnectionPool.isOpen()) DBMSConnectionPool.getInstance().closeAll();
                System.exit(0);
            }
        }
    }

    @Override
    public <T> void onResult(ControllerEvent event, T result) {
        switch (event) {
            case PASSWORD_CHANGE_REQUIRED -> {
                this.setLoggedEmployee((EmployeeBean) result);
                ManagePersonalInfoViewController managePersonalInfoController =
                        (ManagePersonalInfoViewController) this.getViewController(ControllerType.MANAGE_PERSONAL_INFO);
                managePersonalInfoController.changePassword(true);
            }
            case SUCCESS_LOGIN -> {
                this.setLoggedEmployee((EmployeeBean) result);
                this.startController(ControllerType.HOME);
            }
            case SUCCESS_EMPLOYEE_UPDATED -> this.setLoggedEmployee((EmployeeBean) result);
        }
    }

    public enum ControllerType {
        ADD_CLIENT_ORDER,
        CLOSE_CLIENT_ORDER,
        HOME,
        LOGIN,
        MANAGE_ARTICLES,
        MANAGE_EMPLOYEES,
        MANAGE_CLIENTS,
        MANAGE_ORDERS,
        MANAGE_PERSONAL_INFO,
        MANAGE_PRODUCTS,
        MANAGE_SUPPLIERS,
    }
}

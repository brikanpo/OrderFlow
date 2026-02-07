package it.orderflow.control.logiccontroller;

import it.orderflow.beans.*;
import it.orderflow.control.Statement;
import it.orderflow.control.TransactionSafeController;
import it.orderflow.dao.*;
import it.orderflow.exceptions.EmailNotSentException;
import it.orderflow.exceptions.PersistenceException;
import it.orderflow.model.*;
import it.orderflow.services.EmailSenderService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddClientOrderLogicController extends TransactionSafeController {

    private final ClientDAO clientDAO;
    private final ClientArticleDAO clientArticleDAO;
    private final ProductInStockDAO productInStockDAO;
    private final ClientOrderDAO clientOrderDAO;
    private final EmployeeDAO employeeDAO;
    private final EmployeeBean loggedEmployee;
    private int numberOfPastOrders;
    private List<ArticleBean> clientArticleBeanList;
    private List<ProductBean> productBeanList;
    private ClientOrderBean tempClientOrderBean;

    public AddClientOrderLogicController(ClientDAO clientDAO, ClientArticleDAO clientArticleDAO,
                                         ProductInStockDAO productInStockDAO, ClientOrderDAO clientOrderDAO,
                                         EmployeeDAO employeeDAO, EmployeeBean loggedEmployee) {
        super();
        this.clientDAO = clientDAO;
        this.clientArticleDAO = clientArticleDAO;
        this.productInStockDAO = productInStockDAO;
        this.clientOrderDAO = clientOrderDAO;
        this.employeeDAO = employeeDAO;
        this.loggedEmployee = loggedEmployee;
        this.tempClientOrderBean = new ClientOrderBean();
    }

    public ClientDAO getClientDAO() {
        return this.clientDAO;
    }

    public ClientArticleDAO getClientArticleDAO() {
        return this.clientArticleDAO;
    }

    public ProductInStockDAO getProductInStockDAO() {
        return this.productInStockDAO;
    }

    public ClientOrderDAO getClientOrderDAO() {
        return this.clientOrderDAO;
    }

    public EmployeeDAO getEmployeeDAO() {
        return this.employeeDAO;
    }

    public EmployeeBean getLoggedEmployee() {
        return this.loggedEmployee;
    }

    public int getNumberOfPastOrders() {
        return this.numberOfPastOrders;
    }

    public void setNumberOfPastOrders(int numberOfPastOrders) {
        this.numberOfPastOrders = numberOfPastOrders;
    }

    public List<ArticleBean> getClientArticleBeanList() {
        return this.clientArticleBeanList;
    }

    public void setClientArticleBeanList(List<ArticleBean> clientArticleBeanList) {
        this.clientArticleBeanList = clientArticleBeanList;
    }

    public List<ProductBean> getProductBeanList() {
        return this.productBeanList;
    }

    public void setProductBeanList(List<ProductBean> productBeanList) {
        this.productBeanList = productBeanList;
    }

    public ClientOrderBean getTempClientOrderBean() {
        return this.tempClientOrderBean;
    }

    public void setTempClientOrderBean(ClientOrderBean tempClientOrderBean) {
        this.tempClientOrderBean = tempClientOrderBean;
    }

    public List<ClientBean> getClientsList() throws PersistenceException {
        List<Client> clients = this.getClientDAO().loadAll();

        return clients.stream()
                .map(ClientBean::new)
                .toList();
    }

    public void addSelectedClient(ClientBean selectedClient) {
        this.getTempClientOrderBean().setClientBean(selectedClient);
    }

    public List<ArticleBean> getClientArticlesList() throws PersistenceException {
        List<ClientArticle> clientArticles = this.getClientArticleDAO().loadAll();

        return clientArticles.stream()
                .map(ArticleBean::new)
                .toList();
    }

    public void addSelectedClientArticles(List<ArticleBean> clientArticles) {
        this.setNumberOfPastOrders(0);
        this.setClientArticleBeanList(clientArticles);
    }

    public List<ProductBean> getClientProductsListByArticleName() throws PersistenceException {
        List<ProductInStock> productInStocks = new ArrayList<>();
        for (ArticleBean clientArticleBean : this.getClientArticleBeanList()) {
            List<ProductInStock> prods = this.getProductInStockDAO().loadByArticleName(clientArticleBean.getName());
            productInStocks.addAll(prods);
        }

        return productInStocks.stream()
                .map(ProductBean::new)
                .toList();
    }

    public void addSelectedProducts(List<ProductBean> products) {
        this.setProductBeanList(products);
    }

    public List<ProductBean> getSelectedProducts() {
        return this.getProductBeanList();
    }

    public boolean isInManualSelectionState() {
        return this.getNumberOfPastOrders() == 0;
    }

    public void addSelectedNumberOfPastOrders(int numberOfPastOrders) {
        this.setNumberOfPastOrders(numberOfPastOrders);
    }

    public List<ProductBean> getPastClientOrdersProductsList() throws PersistenceException {
        List<ClientOrder> closedClientOrders = this.getClientOrderDAO().loadByStateAndClientId(OrderState.CLOSED, this.getTempClientOrderBean().getClientBean().getId());

        ClientOrders pastClientOrders = new ClientOrders(closedClientOrders).getPastOrders(this.getNumberOfPastOrders());

        ProductsWithQuantity productsWithQuantity = pastClientOrders.getAllProductsOrdered();

        return productsWithQuantity.getProductWithQuantityList().stream()
                .map(productWithQuantity -> new ProductBean(productWithQuantity.getProduct()))
                .toList();
    }

    public void addSelectedProductsWithQuantity(List<ProductWithQuantityBean> productsWithQuantity) {
        this.getTempClientOrderBean().setProductsOrdered(productsWithQuantity);
    }

    public List<ProductWithQuantityBean> getUnavailableProducts() throws PersistenceException {
        List<ProductWithQuantityBean> result = new ArrayList<>();
        for (ProductWithQuantityBean productWithQuantityBean : this.getTempClientOrderBean().getProductsOrdered()) {
            ProductInStock targetProductInStock = this.getProductInStockDAO().loadProductInStock(productWithQuantityBean.getProductBean().getCode());

            if (targetProductInStock.getAvailableStock() < productWithQuantityBean.getQuantity()) {
                result.add(new ProductWithQuantityBean(new ProductWithQuantity(
                        targetProductInStock,
                        productWithQuantityBean.getQuantity() - targetProductInStock.getAvailableStock())));
            }
        }
        return result;
    }

    public void removeSelectedProductsWithQuantity(List<ProductWithQuantityBean> productsToBeRemovedBeanList)
            throws PersistenceException {
        List<ProductWithQuantityBean> productsOrderedBeanList = this.getTempClientOrderBean().getProductsOrdered();

        ProductsWithQuantity productsOrdered = new ProductsWithQuantity();
        for (ProductWithQuantityBean productWithQuantityBean : productsOrderedBeanList) {
            productsOrdered.add(new ProductWithQuantity(this.getProductInStockDAO().loadProductInStock(productWithQuantityBean.getProductBean().getCode()),
                    productWithQuantityBean.getQuantity()));
        }

        ProductsWithQuantity productsToBeRemoved = new ProductsWithQuantity();
        for (ProductWithQuantityBean productWithQuantityBean : productsToBeRemovedBeanList) {
            productsToBeRemoved.add(new ProductWithQuantity(
                    this.getProductInStockDAO().loadProductInStock(productWithQuantityBean.getProductBean().getCode()),
                    productWithQuantityBean.getQuantity()));
        }

        productsOrdered.removeProducts(productsToBeRemoved);

        List<ProductWithQuantityBean> productWithQuantityBeanList = productsOrdered.getProductWithQuantityList().stream()
                .map(ProductWithQuantityBean::new)
                .toList();

        this.getTempClientOrderBean().setProductsOrdered(productWithQuantityBeanList);
    }

    public void saveNewClientOrder() throws EmailNotSentException, PersistenceException {
        ProductsWithQuantity productsOrdered = new ProductsWithQuantity();
        for (ProductWithQuantityBean productWithQuantityBean : this.getTempClientOrderBean().getProductsOrdered()) {
            productsOrdered.add(new ProductWithQuantity(this.getProductInStockDAO().loadProductInStock(productWithQuantityBean.getProductBean().getCode()),
                    productWithQuantityBean.getQuantity()));
        }

        Client client = this.getClientDAO().loadClient(this.getTempClientOrderBean().getClientBean().getEmail());

        ClientOrder clientOrder = new ClientOrder(productsOrdered, this.getLoggedEmployee().getId(), client);

        //set the quantity of products in inventory as ordered
        List<ProductInStock> productsInStock = new ArrayList<>();
        for (ProductWithQuantity productWithQuantity : clientOrder.getProductsOrdered().getProductWithQuantityList()) {
            productsInStock.add(this.getProductInStockDAO().loadProductInStock(productWithQuantity.getCode()));
        }
        Inventory inventory = new Inventory(productsInStock);
        inventory.addOrderedProducts(clientOrder.getProductsOrdered());

        this.startOperation();
        this.addStatement(this.getClientOrderDAO(), new Statement<>(List.of(clientOrder), Statement.Type.SAVE));
        for (int i = 0; i < productsInStock.size(); i++) {
            ProductInStock oldVersion = productsInStock.get(i);
            ProductInStock newVersion = inventory.getProductInStockList().get(i);
            this.addStatement(this.getProductInStockDAO(), new Statement<>(List.of(newVersion, oldVersion), Statement.Type.UPDATE));
        }
        this.endOperation();

        //Notifications
        List<Employee> warehouseWorkers = this.getEmployeeDAO().loadByRole(UserRole.WAREHOUSE_WORKER);
        if (!warehouseWorkers.isEmpty()) {
            EmailSenderService emailSender = new EmailSenderService();
            for (Employee employee : warehouseWorkers) {
                emailSender.sendEmailNotification(EmailSenderService.EmailType.NEW_CLIENT_ORDER,
                        employee.getEmail(), clientOrder.getRegistrationDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
                                + " - " + clientOrder.getClient().getName());
            }
        }

        this.setTempClientOrderBean(new ClientOrderBean());
    }
}

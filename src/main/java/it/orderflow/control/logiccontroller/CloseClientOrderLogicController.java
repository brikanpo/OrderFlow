package it.orderflow.control.logiccontroller;

import it.orderflow.beans.ClientOrderBean;
import it.orderflow.beans.EmployeeBean;
import it.orderflow.control.Statement;
import it.orderflow.control.TransactionSafeController;
import it.orderflow.dao.ClientOrderDAO;
import it.orderflow.dao.ProductInStockDAO;
import it.orderflow.exceptions.EntityException;
import it.orderflow.exceptions.EntityNotFoundException;
import it.orderflow.exceptions.PersistenceException;
import it.orderflow.model.*;

import java.util.ArrayList;
import java.util.List;

public class CloseClientOrderLogicController extends TransactionSafeController {

    private final ProductInStockDAO productInStockDAO;
    private final ClientOrderDAO clientOrderDAO;
    private final EmployeeBean loggedEmployee;
    private ClientOrderBean tempClientOrderBean;

    public CloseClientOrderLogicController(ProductInStockDAO productInStockDAO, ClientOrderDAO clientOrderDAO,
                                           EmployeeBean loggedEmployee) {
        super();
        this.productInStockDAO = productInStockDAO;
        this.clientOrderDAO = clientOrderDAO;
        this.loggedEmployee = loggedEmployee;
    }

    public ProductInStockDAO getProductInStockDAO() {
        return this.productInStockDAO;
    }

    public ClientOrderDAO getClientOrderDAO() {
        return this.clientOrderDAO;
    }

    public EmployeeBean getLoggedEmployee() {
        return this.loggedEmployee;
    }

    public ClientOrderBean getTempClientOrderBean() {
        return this.tempClientOrderBean;
    }

    public void setTempClientOrderBean(ClientOrderBean tempClientOrderBean) {
        this.tempClientOrderBean = tempClientOrderBean;
    }

    public List<ClientOrderBean> getReadyOrdersList() throws PersistenceException {
        List<ClientOrder> readyClientOrders = this.getClientOrderDAO().loadByState(OrderState.READY);

        List<ClientOrderBean> clientOrderBeans = new ArrayList<>();
        for (ClientOrder readyClientOrder : readyClientOrders) {
            ClientOrderBean clientOrderBean = new ClientOrderBean(readyClientOrder);
            clientOrderBeans.add(clientOrderBean);
        }
        return clientOrderBeans;
    }

    public void addSelectedOrder(ClientOrderBean readyOrder) {
        this.setTempClientOrderBean(readyOrder);
    }

    public void closeClientOrder() throws EntityNotFoundException, PersistenceException {
        ClientOrder oldClientOrder = this.getClientOrderDAO().loadClientOrder(this.getTempClientOrderBean().getId());
        if (oldClientOrder != null) {
            ClientOrder updatedClientOrder = oldClientOrder.copy();

            updatedClientOrder.closeClientOrder(this.getLoggedEmployee().getId());

            //remove from inventory products in order
            List<ProductInStock> productsInStock = new ArrayList<>();
            for (ProductWithQuantity productWithQuantity : updatedClientOrder.getProductsOrdered().getProductWithQuantityList()) {
                productsInStock.add(this.getProductInStockDAO().loadProductInStock(productWithQuantity.getCode()));
            }
            Inventory inventory = new Inventory(productsInStock);
            inventory.soldOrderedProducts(updatedClientOrder.getProductsOrdered());

            this.startOperation();
            this.addStatement(this.getClientOrderDAO(), new Statement<>(List.of(updatedClientOrder, oldClientOrder), Statement.Type.UPDATE));
            for (int i = 0; i < productsInStock.size(); i++) {
                ProductInStock oldVersion = productsInStock.get(i);
                ProductInStock newVersion = inventory.getProductInStockList().get(i);
                this.addStatement(this.getProductInStockDAO(), new Statement<>(List.of(newVersion, oldVersion), Statement.Type.UPDATE));
            }
            this.endOperation();

            //send email to client with invoice

        } else throw new EntityNotFoundException(EntityException.Entity.CLIENT_ORDER);
    }
}

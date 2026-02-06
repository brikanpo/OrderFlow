package it.OrderFlow.Control.LogicController;

import Mock.DAO.*;
import Mock.Model.MockEntity;
import it.OrderFlow.Beans.*;
import it.OrderFlow.DAO.*;
import it.OrderFlow.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestAddClientOrderLogicController {

    private final MockEntity me = new MockEntity();
    private ClientDAO clientDAO;
    private ClientArticleDAO clientArticleDAO;
    private ProductInStockDAO productInStockDAO;
    private ClientOrderDAO clientOrderDAO;
    private AddClientOrderLogicController logicController;

    @BeforeEach
    void setUp() {
        clientDAO = new MockClientDAO();
        clientArticleDAO = new MockClientArticleDAO();
        productInStockDAO = new MockProductInStockDAO();
        clientOrderDAO = new MockClientOrderDAO();
        EmployeeDAO employeeDAO = new MockEmployeeDAO();
        EmployeeBean loggedEmployee = new EmployeeBean(me.getMockRepresentative());

        logicController = new AddClientOrderLogicController(clientDAO, clientArticleDAO, productInStockDAO,
                clientOrderDAO, employeeDAO, loggedEmployee);
    }

    @Test
    void testGetClientsList() throws Exception {
        List<ClientBean> clientBeanList = logicController.getClientsList();

        assertEquals(clientDAO.loadAll().size(), clientBeanList.size());
    }

    @Test
    void testGetClientArticlesList() throws Exception {
        List<ArticleBean> articleBeanList = logicController.getClientArticlesList();

        assertEquals(clientArticleDAO.loadAll().size(), articleBeanList.size());
    }

    @Test
    void getClientProductsListByArticleName() throws Exception {
        logicController.addSelectedClientArticles(List.of(new ArticleBean(me.getMockClientArticle())));

        List<ProductBean> productBeanList = logicController.getClientProductsListByArticleName();

        assertEquals(productInStockDAO.loadByArticleName("clientArticle1").size(), productBeanList.size());
    }

    @Test
    void getPastClientOrdersProductsList() throws Exception {
        List<ClientOrder> clientOrderList = clientOrderDAO.loadAll();

        Client client = clientOrderList.getFirst().getClient();

        logicController.addSelectedClient(new ClientBean(client));

        logicController.addSelectedNumberOfPastOrders(1);

        List<ProductBean> productBeanList = logicController.getPastClientOrdersProductsList();

        ProductsWithQuantity productsWithQuantity = new ProductsWithQuantity();
        for (ClientOrder clientOrder : clientOrderList) {
            productsWithQuantity.addProducts(clientOrder.getProductsOrdered());
        }

        assertEquals(productsWithQuantity.getProducts().size(), productBeanList.size());
    }

    @Test
    void testGetUnavailableProducts() throws Exception {

        logicController.addSelectedProductsWithQuantity(List.of(new ProductWithQuantityBean(new ProductWithQuantity(me.getMockClientProduct(), 15))));

        List<ProductWithQuantityBean> pwqbList = logicController.getUnavailableProducts();

        assertEquals(5, pwqbList.getFirst().getQuantity());
    }

    @Test
    void testGetUnavailableProductsCheckNoUnavailableProducts() throws Exception {

        logicController.addSelectedProductsWithQuantity(List.of(new ProductWithQuantityBean(new ProductWithQuantity(me.getMockClientProduct(), 3))));

        List<ProductWithQuantityBean> pwqbList = logicController.getUnavailableProducts();

        assertTrue(pwqbList.isEmpty());
    }

    @Test
    void testRemoveSelectedProductsWithQuantity() throws Exception {
        logicController.addSelectedProductsWithQuantity(
                List.of(new ProductWithQuantityBean(new ProductWithQuantity(me.getMockClientProduct(), 15))));

        logicController.removeSelectedProductsWithQuantity(
                List.of(new ProductWithQuantityBean(new ProductWithQuantity(me.getMockClientProduct(), 5))));

        List<ProductWithQuantityBean> pwqbList = logicController.getTempClientOrderBean().getProductsOrdered();

        assertEquals(10, pwqbList.getFirst().getQuantity());
    }

    @Test
    void testSaveNewClientOrder() throws Exception {
        logicController.addSelectedClient(new ClientBean(me.getMockClient()));
        logicController.addSelectedProductsWithQuantity(
                List.of(new ProductWithQuantityBean(me.getMockClientProductWithQuantity())));

        logicController.saveNewClientOrder();

        assertEquals(1, clientOrderDAO.loadByState(OrderState.WAITING).size());
    }

    @Test
    void testSaveNewClientOrderCheckUpdatedProductOrderedQuantity() throws Exception {
        logicController.addSelectedClient(new ClientBean(me.getMockClient()));
        logicController.addSelectedProductsWithQuantity(
                List.of(new ProductWithQuantityBean(me.getMockClientProductWithQuantity())));

        logicController.saveNewClientOrder();

        ProductInStock productInStock = productInStockDAO.loadProductInStock("codeClientProduct1");

        assertEquals(me.getMockClientProductWithQuantity().getQuantity(), productInStock.getOrderedProductsQuantity());
    }
}
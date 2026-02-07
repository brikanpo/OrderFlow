package it.orderflow.control.logiccontroller;

import it.orderflow.beans.ArticleBean;
import it.orderflow.beans.ProductBean;
import it.orderflow.beans.ProductInStockBean;
import it.orderflow.control.Statement;
import it.orderflow.control.TransactionSafeController;
import it.orderflow.dao.ClientArticleDAO;
import it.orderflow.dao.ProductDAO;
import it.orderflow.dao.ProductInStockDAO;
import it.orderflow.dao.SupplierArticleDAO;
import it.orderflow.exceptions.AlreadyInUseException;
import it.orderflow.exceptions.EntityException;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.exceptions.PersistenceException;
import it.orderflow.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ManageProductsLogicController extends TransactionSafeController {

    private final ClientArticleDAO clientArticleDAO;
    private final SupplierArticleDAO supplierArticleDAO;
    private final ProductDAO productDAO;
    private final ProductInStockDAO productInStockDAO;
    private ProductBean tempSupplierProductBean;
    private ProductInStockBean tempProductInStockBean;

    public ManageProductsLogicController(ClientArticleDAO clientArticleDAO,
                                         SupplierArticleDAO supplierArticleDAO,
                                         ProductDAO productDAO,
                                         ProductInStockDAO productInStockDAO) {
        super();
        this.clientArticleDAO = clientArticleDAO;
        this.supplierArticleDAO = supplierArticleDAO;
        this.productDAO = productDAO;
        this.productInStockDAO = productInStockDAO;
        this.setTempSupplierProductBean(new ProductBean());
        this.setTempProductInStockBean(new ProductInStockBean());
    }

    public ClientArticleDAO getClientArticleDAO() {
        return this.clientArticleDAO;
    }

    public SupplierArticleDAO getSupplierArticleDAO() {
        return this.supplierArticleDAO;
    }

    public ProductDAO getProductDAO() {
        return this.productDAO;
    }

    public ProductInStockDAO getProductInStockDAO() {
        return this.productInStockDAO;
    }

    public ProductBean getTempSupplierProductBean() {
        return this.tempSupplierProductBean;
    }

    public void setTempSupplierProductBean(ProductBean tempSupplierProductBean) {
        this.tempSupplierProductBean = tempSupplierProductBean;
    }

    public ProductInStockBean getTempProductInStockBean() {
        return this.tempProductInStockBean;
    }

    public void setTempProductInStockBean(ProductInStockBean tempProductInStockBean) {
        this.tempProductInStockBean = tempProductInStockBean;
    }

    public List<ArticleBean> getSupplierArticlesList() throws PersistenceException {
        List<SupplierArticle> supplierArticles = this.getSupplierArticleDAO().loadAll();

        return supplierArticles.stream()
                .map(ArticleBean::new)
                .toList();
    }

    public void addSelectedSupplierArticle(ArticleBean supplierArticleBean) {
        this.getTempSupplierProductBean().setArticleName(supplierArticleBean.getName());
    }

    public void saveNewProduct(ProductBean productBean)
            throws AlreadyInUseException, InvalidInputException, PersistenceException {
        Product targetProduct = this.getProductDAO().loadProduct(productBean.getCode());
        if (targetProduct == null) {

            Attributes productAttributes = productBean.getProductAttributes();
            BigDecimal price = productBean.getPrice();
            if (productAttributes == null || price == null) {
                throw new InvalidInputException(InvalidInputException.InputType.BLANK);
            }
            SupplierArticle targetArticle = this.getSupplierArticleDAO().loadSupplierArticle(productBean.getArticleName());

            Product tempProduct = new Product(targetArticle, productAttributes, price);

            this.startOperation();
            this.addStatement(this.getProductDAO(), new Statement<>(List.of(tempProduct), Statement.Type.SAVE));
            this.endOperation();

            this.setTempSupplierProductBean(new ProductBean());

        } else throw new AlreadyInUseException(EntityException.Entity.PRODUCT, AlreadyInUseException.Param.CODE);
    }

    public List<ArticleBean> getClientArticleList() throws PersistenceException {
        List<ClientArticle> clientArticles = this.getClientArticleDAO().loadAll();

        List<ArticleBean> clientArticleBeans = new ArrayList<>();
        for (ClientArticle clientArticle : clientArticles) {
            ArticleBean clientArticleBean = new ArticleBean(clientArticle);
            clientArticleBeans.add(clientArticleBean);
        }
        return clientArticleBeans;
    }

    public void addSelectedClientArticle(ArticleBean clientArticleBean) {
        this.getTempProductInStockBean().setArticleName(clientArticleBean.getName());
    }

    public void saveNewProductInStock(ProductInStockBean productInStockBean)
            throws AlreadyInUseException, InvalidInputException, PersistenceException {
        ProductInStock targetProductInStock = this.getProductInStockDAO().loadProductInStock(productInStockBean.getCode());
        if (targetProductInStock == null) {

            Attributes productAttributes = productInStockBean.getProductAttributes();
            BigDecimal price = productInStockBean.getPrice();
            Integer quantity = productInStockBean.getQuantity();
            Integer minimumStock = productInStockBean.getMinimumStock();
            Integer maximumStock = productInStockBean.getMaximumStock();
            if (productAttributes == null || price == null || quantity == null || minimumStock == null
                    || maximumStock == null) {
                throw new InvalidInputException(InvalidInputException.InputType.BLANK);
            }
            ClientArticle targetArticle = this.getClientArticleDAO().loadClientArticle(productInStockBean.getArticleName());
            ProductInStock tempProductInStock = new ProductInStock(new Product(targetArticle, productAttributes, price),
                    quantity, minimumStock, maximumStock);

            this.startOperation();
            this.addStatement(this.getProductInStockDAO(), new Statement<>(List.of(tempProductInStock), Statement.Type.SAVE));
            this.endOperation();

            this.setTempProductInStockBean(new ProductInStockBean());

        } else
            throw new AlreadyInUseException(EntityException.Entity.PRODUCT_IN_STOCK, AlreadyInUseException.Param.CODE);
    }
}

package it.orderflow.control.logiccontroller;

import it.orderflow.beans.ArticleBean;
import it.orderflow.beans.SupplierBean;
import it.orderflow.control.Statement;
import it.orderflow.control.TransactionSafeController;
import it.orderflow.dao.ClientArticleDAO;
import it.orderflow.dao.SupplierArticleDAO;
import it.orderflow.dao.SupplierDAO;
import it.orderflow.exceptions.AlreadyInUseException;
import it.orderflow.exceptions.EntityException;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.exceptions.PersistenceException;
import it.orderflow.model.*;

import java.math.BigDecimal;
import java.util.List;

public class ManageArticlesLogicController extends TransactionSafeController {

    private final ClientArticleDAO clientArticleDAO;
    private final SupplierArticleDAO supplierArticleDAO;
    private final SupplierDAO supplierDAO;
    private ArticleBean tempSupplierArticleBean;
    private ArticleBean tempClientArticleBean;

    public ManageArticlesLogicController(ClientArticleDAO clientArticleDAO,
                                         SupplierArticleDAO supplierArticleDAO,
                                         SupplierDAO supplierDAO) {
        this.clientArticleDAO = clientArticleDAO;
        this.supplierArticleDAO = supplierArticleDAO;
        this.supplierDAO = supplierDAO;
        this.tempSupplierArticleBean = new ArticleBean();
        this.tempClientArticleBean = new ArticleBean();
    }

    public ClientArticleDAO getClientArticleDAO() {
        return this.clientArticleDAO;
    }

    public SupplierArticleDAO getSupplierArticleDAO() {
        return this.supplierArticleDAO;
    }

    public SupplierDAO getSupplierDAO() {
        return this.supplierDAO;
    }

    public ArticleBean getTempSupplierArticleBean() {
        return this.tempSupplierArticleBean;
    }

    public void setTempSupplierArticleBean(ArticleBean tempSupplierArticleBean) {
        this.tempSupplierArticleBean = tempSupplierArticleBean;
    }

    public ArticleBean getTempClientArticleBean() {
        return this.tempClientArticleBean;
    }

    public void setTempClientArticleBean(ArticleBean tempClientArticleBean) {
        this.tempClientArticleBean = tempClientArticleBean;
    }

    public List<SupplierBean> getSuppliersList() throws PersistenceException {
        List<Supplier> suppliers = this.getSupplierDAO().loadAll();

        return suppliers.stream()
                .map(SupplierBean::new)
                .toList();
    }

    public void addSelectedSupplier(SupplierBean supplierBean) {
        this.getTempSupplierArticleBean().setSupplierId(supplierBean.getId());
    }

    public void saveNewSupplierArticle(ArticleBean supplierArticleBean)
            throws AlreadyInUseException, InvalidInputException, PersistenceException {
        SupplierArticle targetSupplierArticle = this.getSupplierArticleDAO().loadSupplierArticle(supplierArticleBean.getName());
        if (targetSupplierArticle == null) {

            String name = supplierArticleBean.getName();
            String category = supplierArticleBean.getCategory();
            if (name == null || category == null) {
                throw new InvalidInputException(InvalidInputException.InputType.BLANK);
            }
            SupplierArticle newSupplierArticle = new SupplierArticle(name, category,
                    this.getTempSupplierArticleBean().getSupplierId());

            this.setOptionalParameters(supplierArticleBean, newSupplierArticle);

            this.startOperation();
            this.addStatement(this.getSupplierArticleDAO(), new Statement<>(List.of(newSupplierArticle), Statement.Type.SAVE));
            this.endOperation();

            this.setTempSupplierArticleBean(new ArticleBean());

        } else
            throw new AlreadyInUseException(EntityException.Entity.SUPPLIER_ARTICLE, AlreadyInUseException.Param.NAME);
    }

    public List<ArticleBean> getSupplierArticlesList() throws PersistenceException {
        List<SupplierArticle> supplierArticles = this.getSupplierArticleDAO().loadAll();

        return supplierArticles.stream()
                .map(ArticleBean::new)
                .toList();
    }

    public void addSelectedSupplierArticle(ArticleBean articleBean) {
        this.getTempClientArticleBean().setSupplierArticleId(articleBean.getId());
    }

    public void saveNewClientArticle(ArticleBean clientArticleBean)
            throws AlreadyInUseException, InvalidInputException, PersistenceException {
        ClientArticle targetClientArticle = this.getClientArticleDAO().loadClientArticle(clientArticleBean.getName());
        if (targetClientArticle == null) {

            String name = clientArticleBean.getName();
            String category = clientArticleBean.getCategory();
            if (name == null || category == null) {
                throw new InvalidInputException(InvalidInputException.InputType.BLANK);
            }
            ClientArticle newClientArticle = new ClientArticle(name, category,
                    clientArticleBean.getSupplierArticleId());

            this.setOptionalParameters(clientArticleBean, newClientArticle);

            this.startOperation();
            this.addStatement(this.getClientArticleDAO(), new Statement<>(List.of(newClientArticle), Statement.Type.SAVE));
            this.endOperation();

            this.setTempClientArticleBean(new ArticleBean());

        } else throw new AlreadyInUseException(EntityException.Entity.CLIENT_ARTICLE, AlreadyInUseException.Param.NAME);
    }

    private void setOptionalParameters(ArticleBean articleBean, Article article) {
        String description = articleBean.getDescription();
        if (description != null) article.changeDescription(description);

        Attributes articleAttributes = articleBean.getArticleAttributes();
        if (articleAttributes != null) article.changeArticleAttributes(articleAttributes);

        Attributes possibleAttributes = articleBean.getPossibleAttributes();
        if (possibleAttributes != null) article.changePossibleAttributes(possibleAttributes);

        BigDecimal iva = articleBean.getIva();
        if (iva != null) article.changeIva(iva);
    }
}

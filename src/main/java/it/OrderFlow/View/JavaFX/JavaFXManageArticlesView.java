package it.OrderFlow.View.JavaFX;

import it.OrderFlow.Beans.ArticleBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.Model.Attributes;
import it.OrderFlow.View.JavaFX.MyComponents.*;
import it.OrderFlow.View.ManageArticlesView;
import it.OrderFlow.View.ViewEvent;

import java.math.BigDecimal;
import java.util.List;

public class JavaFXManageArticlesView extends JavaFXRootView implements ManageArticlesView {

    private final MyTitle title;
    private final MyLabel infoLabel;
    private final MyButton confirmButton;
    private final MyButton backButton;
    private final MyNavigationBar myNavigationBar;

    private MyTextField nameField;
    private MyTextField categoryField;
    private MyTextField descriptionField;
    private MyAttrCreationContainer articleAttributesContainer;
    private MyAttrCreationContainer possibleAttributesContainer;
    private MyTextField ivaField;

    public JavaFXManageArticlesView() {
        super();

        title = new MyTitle();

        infoLabel = new MyLabel();

        confirmButton = new MyButton("Confirm");

        backButton = new MyButton("Back");

        myNavigationBar = new MyNavigationBar();

        myNavigationBar.getHomeButton().setOnAction(e -> this.notifyObservers(ViewEvent.HOME));
        myNavigationBar.getLogoutButton().setOnAction(e -> this.notifyObservers(ViewEvent.LOGOUT));
        myNavigationBar.getExitButton().setOnAction(e -> this.notifyObservers(ViewEvent.EXIT));
    }

    @Override
    public void displayManageArticles() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Manage articles");

            infoLabel.setText("What do you want to do?");

            MyButton addSupplierArticleButton = new MyButton("Add supplier article");

            MyButton addClientArticleButton = new MyButton("Add client article");

            addSupplierArticleButton.setOnAction(e -> this.notifyObservers(ViewEvent.ADD_SUPPLIER_ARTICLE));
            addClientArticleButton.setOnAction(e -> this.notifyObservers(ViewEvent.ADD_CLIENT_ARTICLE));

            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.HOME));

            layout.getChildren().addAll(title, infoLabel, addSupplierArticleButton, addClientArticleButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayAddSupplierArticle() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Add supplier article");

            infoLabel.setText("Insert the new article info. The fields with * are optional");

            nameField = new MyTextField("Name");
            categoryField = new MyTextField("Category");
            descriptionField = new MyTextField("* Description");
            articleAttributesContainer = new MyAttrCreationContainer(true);
            possibleAttributesContainer = new MyAttrCreationContainer(false);
            ivaField = new MyTextField("* Iva (default 0.22)");

            confirmButton.disableProperty().bind(nameField.textProperty().isEmpty()
                    .or(categoryField.textProperty().isEmpty()));
            confirmButton.setOnAction(e -> this.notifyObservers(ViewEvent.SAVE_SUPPLIER_ARTICLE));
            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.BACK));

            layout.getChildren().addAll(title, infoLabel, nameField, categoryField, descriptionField,
                    articleAttributesContainer, possibleAttributesContainer, ivaField, confirmButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayAddClientArticle() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Add client article");

            infoLabel.setText("Insert the new article info. The fields with * are optional");

            nameField = new MyTextField("Name");
            categoryField = new MyTextField("Category");
            descriptionField = new MyTextField("* Description");
            articleAttributesContainer = new MyAttrCreationContainer(true);
            possibleAttributesContainer = new MyAttrCreationContainer(false);
            ivaField = new MyTextField("* Iva (default 0.22)");

            confirmButton.disableProperty().bind(nameField.textProperty().isEmpty()
                    .or(categoryField.textProperty().isEmpty()));
            confirmButton.setOnAction(e -> this.notifyObservers(ViewEvent.SAVE_CLIENT_ARTICLE));
            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.BACK));

            layout.getChildren().addAll(title, infoLabel, nameField, categoryField, descriptionField,
                    articleAttributesContainer, possibleAttributesContainer, ivaField, confirmButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public ArticleBean getSupplierArticleBean() throws InvalidInputException {
        ArticleBean supplierArticleBean = new ArticleBean();

        if (!this.nameField.getText().isBlank()) supplierArticleBean.setName(this.nameField.getText());

        if (!this.categoryField.getText().isBlank()) supplierArticleBean.setCategory(this.categoryField.getText());

        if (!this.descriptionField.getText().isBlank())
            supplierArticleBean.setDescription(this.descriptionField.getText());

        if (!this.articleAttributesContainer.getNames().isEmpty()) {

            List<String> attrNames = this.articleAttributesContainer.getNames();
            List<String> attrValue = this.articleAttributesContainer.getValues();

            Attributes attributes = new Attributes();

            for (int i = 0; i < attrNames.size(); i++) {
                if (!attrNames.get(i).isBlank() && !attrValue.get(i).isBlank()) {
                    attributes.addAttributeIdAndValue(attrNames.get(i), attrValue.get(i));
                }
            }
            supplierArticleBean.setArticleAttributes(attributes);
        }

        if (!this.possibleAttributesContainer.getNames().isEmpty()) {

            List<String> attrNames = this.possibleAttributesContainer.getNames();

            Attributes attributes = new Attributes();

            for (String attrName : attrNames) {
                if (!attrName.isBlank()) {
                    attributes.addAttributeId(attrName);
                }
            }
            supplierArticleBean.setPossibleAttributes(attributes);
        }

        if (!this.ivaField.getText().isBlank()) {
            try {
                BigDecimal iva = new BigDecimal(this.ivaField.getText());
                supplierArticleBean.setIva(iva);
            } catch (NumberFormatException e) {
                throw new InvalidInputException(InvalidInputException.InputType.PERCENTAGE);
            }
        }

        return supplierArticleBean;
    }

    @Override
    public ArticleBean getClientArticleBean() throws InvalidInputException {
        ArticleBean clientArticleBean = new ArticleBean();

        if (!this.nameField.getText().isBlank()) clientArticleBean.setName(this.nameField.getText());

        if (!this.categoryField.getText().isBlank()) clientArticleBean.setCategory(this.categoryField.getText());

        if (!this.descriptionField.getText().isBlank())
            clientArticleBean.setDescription(this.descriptionField.getText());

        if (!this.articleAttributesContainer.getNames().isEmpty()) {

            List<String> attrNames = this.articleAttributesContainer.getNames();
            List<String> attrValue = this.articleAttributesContainer.getValues();

            Attributes attributes = new Attributes();

            for (int i = 0; i < attrNames.size(); i++) {
                if (!attrNames.get(i).isBlank() && !attrValue.get(i).isBlank()) {
                    attributes.addAttributeIdAndValue(attrNames.get(i), attrValue.get(i));
                }
            }
            clientArticleBean.setArticleAttributes(attributes);
        }

        if (!this.possibleAttributesContainer.getNames().isEmpty()) {

            List<String> attrNames = this.possibleAttributesContainer.getNames();

            Attributes attributes = new Attributes();

            for (String attrName : attrNames) {
                if (!attrName.isBlank()) {
                    attributes.addAttributeId(attrName);
                }
            }
            clientArticleBean.setPossibleAttributes(attributes);
        }

        if (!this.ivaField.getText().isBlank()) {
            try {
                BigDecimal iva = new BigDecimal(this.ivaField.getText());
                clientArticleBean.setIva(iva);
            } catch (NumberFormatException e) {
                throw new InvalidInputException(InvalidInputException.InputType.PERCENTAGE);
            }
        }

        return clientArticleBean;
    }
}

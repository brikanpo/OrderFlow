package it.orderflow.view.cli;

import it.orderflow.beans.ArticleBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.model.Attributes;
import it.orderflow.view.ManageArticlesView;
import it.orderflow.view.ViewEvent;

import java.math.BigDecimal;

public class CLIManageArticlesView extends CLIRootView implements ManageArticlesView {

    private String name;
    private String category;
    private String description;
    private Attributes articleAttributes;
    private Attributes possibleAttributes;
    private String iva;

    private void insertPairAttributeNameAndValue() {
        String attributeName;
        String attributeValue;
        this.articleAttributes = new Attributes();
        boolean keepInserting = true;
        while (keepInserting) {
            System.out.println("You can keep inserting new pairs of attribute name and value till you leave both blank ");
            System.out.print("Insert attribute name: ");
            attributeName = scanner.nextLine();
            System.out.print("Insert attribute value: ");
            attributeValue = scanner.nextLine();

            if (attributeName.isBlank() && attributeValue.isBlank()) {
                keepInserting = false;
            } else if (attributeName.isBlank() || attributeValue.isBlank()) {
                continue;
            }

            this.articleAttributes.addAttributeIdAndValue(attributeName, attributeValue);
        }
    }

    private void insertPossibleAttributesNames() {
        String attributeName;
        this.possibleAttributes = new Attributes();
        while (true) {
            System.out.println("You can keep inserting a new attribute name till you leave the field blank ");
            System.out.print("Insert attribute name: ");
            attributeName = scanner.nextLine();

            if (attributeName.isBlank()) {
                break;
            }

            this.possibleAttributes.addAttributeId(attributeName);
        }
    }

    private void insertArticleInfo(ViewEvent event) {
        String type;
        String insert = "Insert ";

        if (event == ViewEvent.SAVE_SUPPLIER_ARTICLE) {
            type = "supplier";
        } else {
            type = "client";
        }

        System.out.print(insert + type + " article name: ");
        this.name = scanner.nextLine();
        System.out.print(insert + type + " article category: ");
        this.category = scanner.nextLine();
        System.out.println("The following fields are optional. To ignore leave blank");
        System.out.print(insert + type + " article description: ");
        this.description = scanner.nextLine();
        System.out.println("You can choose to insert the name of an attribute and its value for this article " +
                "(e.g., name: material, value: wood)");
        this.insertPairAttributeNameAndValue();
        System.out.println("You can choose to insert the name of an attribute that can differ between products of this article " +
                "(e.g., name: size)");
        this.insertPossibleAttributesNames();
        System.out.print(insert + type + " article iva (default 0.22): ");
        this.iva = scanner.nextLine();
    }

    @Override
    public void displayManageArticles() {
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            this.printTitle("Manage articles");
            System.out.println("What do you want to do?");
            System.out.println("1) Add new supplier article");
            System.out.println("2) Add new client article");
            System.out.println("3) Back");
            System.out.println("4) Home");
            System.out.println("5) Logout");
            System.out.println("6) Exit");
            System.out.print("Type the number of the option chosen: ");
            switch (this.scanner.nextLine()) {
                case "1" -> this.notifyObservers(ViewEvent.ADD_SUPPLIER_ARTICLE);
                case "2" -> this.notifyObservers(ViewEvent.ADD_CLIENT_ARTICLE);
                case "3" -> this.notifyObservers(ViewEvent.BACK);
                case "4" -> this.notifyObservers(ViewEvent.HOME);
                case "5" -> this.notifyObservers(ViewEvent.LOGOUT);
                case "6" -> this.notifyObservers(ViewEvent.EXIT);
                default -> {
                    continue;
                }
            }
            chosenAnOption = true;
        }
    }

    @Override
    public void displayAddSupplierArticle() {
        this.printTitle("Add supplier article");
        System.out.println("Insert the supplier article info.");
        this.insertArticleInfo(ViewEvent.SAVE_SUPPLIER_ARTICLE);
        this.confirmOperation(ViewEvent.SAVE_SUPPLIER_ARTICLE, ViewEvent.BACK);
    }

    @Override
    public void displayAddClientArticle() {
        this.printTitle("Add client article");
        System.out.println("Insert the client article info.");
        this.insertArticleInfo(ViewEvent.SAVE_CLIENT_ARTICLE);
        this.confirmOperation(ViewEvent.SAVE_CLIENT_ARTICLE, ViewEvent.BACK);
    }

    @Override
    public ArticleBean getSupplierArticleBean() throws InvalidInputException {
        ArticleBean supplierArticleBean = new ArticleBean();

        supplierArticleBean.setName(this.name);
        supplierArticleBean.setCategory(this.category);

        if (!this.description.isBlank()) supplierArticleBean.setDescription(this.description);

        supplierArticleBean.setArticleAttributes(this.articleAttributes);
        supplierArticleBean.setPossibleAttributes(this.possibleAttributes);

        if (!this.iva.isBlank()) {
            try {
                supplierArticleBean.setIva(new BigDecimal(this.iva));
            } catch (NumberFormatException e) {
                throw new InvalidInputException(InvalidInputException.InputType.PRICE);
            }
        }

        return supplierArticleBean;
    }

    @Override
    public ArticleBean getClientArticleBean() throws InvalidInputException {
        ArticleBean clientArticleBean = new ArticleBean();

        clientArticleBean.setName(this.name);
        clientArticleBean.setCategory(this.category);

        if (!this.description.isBlank()) clientArticleBean.setDescription(this.description);

        clientArticleBean.setArticleAttributes(this.articleAttributes);
        clientArticleBean.setPossibleAttributes(this.possibleAttributes);

        if (!this.iva.isBlank()) {
            try {
                clientArticleBean.setIva(new BigDecimal(this.iva));
            } catch (NumberFormatException e) {
                throw new InvalidInputException(InvalidInputException.InputType.PRICE);
            }
        }

        return clientArticleBean;
    }

    @Override
    public void displayErrorMessage(Exception e) {
        super.displayErrorMessage(e);
        this.displayManageArticles();
    }
}

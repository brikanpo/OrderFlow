package it.orderflow.view.javafx;

import it.orderflow.beans.ProductBean;
import it.orderflow.beans.ProductInStockBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.model.Attributes;
import it.orderflow.view.ManageProductsView;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.javafx.mycomponents.*;

import java.math.BigDecimal;
import java.util.List;

public class JavaFXManageProductsView extends JavaFXRootView implements ManageProductsView {

    private final MyTitle title;
    private final MyLabel infoLabel;
    private final MyButton confirmButton;
    private final MyButton backButton;
    private final MyNavigationBar myNavigationBar;

    private MyAttrValueFiller productAttributesContainer;
    private MyTextField priceField;
    private MyTextField quantityField;
    private MyTextField minimumStockField;
    private MyTextField maximumStockField;

    private List<String> attributesIds;

    public JavaFXManageProductsView() {
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
    public void displayManageProducts() {
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Manage products");

            infoLabel.setText("What do you want to do?");

            MyButton addSupplierProductButton = new MyButton("Add supplier product");

            MyButton addProductInInventoryButton = new MyButton("Add product in inventory");

            addSupplierProductButton.setOnAction(e -> this.notifyObservers(ViewEvent.ADD_SUPPLIER_PRODUCT));
            addProductInInventoryButton.setOnAction(e -> this.notifyObservers(ViewEvent.ADD_PRODUCT_IN_INVENTORY));

            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.HOME));

            layout.getChildren().addAll(title, infoLabel, addSupplierProductButton, addProductInInventoryButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayAddSupplierProduct(List<String> attributesIds) {
        this.attributesIds = attributesIds;
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Add supplier product");

            infoLabel.setText("Insert the new product info");

            productAttributesContainer = new MyAttrValueFiller(attributesIds);

            priceField = new MyTextField("Price");

            confirmButton.setOnAction(e -> this.notifyObservers(ViewEvent.SAVE_SUPPLIER_PRODUCT));
            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.BACK));

            layout.getChildren().addAll(title, infoLabel, productAttributesContainer, priceField,
                    confirmButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public void displayAddProductInStock(List<String> attributesIds) {
        this.attributesIds = attributesIds;
        this.runOnFXThread(() -> {
            MyVBox layout = new MyVBox();

            title.setText("Add product in inventory");

            infoLabel.setText("Insert the new product info");

            productAttributesContainer = new MyAttrValueFiller(attributesIds);

            priceField = new MyTextField("Price");

            quantityField = new MyTextField("Quantity");

            minimumStockField = new MyTextField("Minimum stock");

            maximumStockField = new MyTextField("Maximum stock");

            confirmButton.setOnAction(e -> this.notifyObservers(ViewEvent.SAVE_SUPPLIER_PRODUCT));
            backButton.setOnAction(e -> this.notifyObservers(ViewEvent.BACK));

            layout.getChildren().addAll(title, infoLabel, productAttributesContainer, priceField,
                    quantityField, minimumStockField, maximumStockField, confirmButton, backButton);

            stage.getScene().setRoot(MyContainer.getContainer(layout, myNavigationBar));
            stage.show();
        });
    }

    @Override
    public ProductBean getProductBean() throws InvalidInputException {
        ProductBean productBean = new ProductBean();

        if (!this.productAttributesContainer.getValues().isEmpty()) {

            List<String> attrValue = this.productAttributesContainer.getValues();

            Attributes attributes = new Attributes();

            for (int i = 0; i < this.attributesIds.size(); i++) {
                if (!this.attributesIds.get(i).isBlank() && !attrValue.get(i).isBlank()) {
                    attributes.addAttributeIdAndValue(this.attributesIds.get(i), attrValue.get(i));
                }
            }
            productBean.setProductAttributes(attributes);
        }

        if (!this.priceField.getText().isBlank()) {
            try {
                BigDecimal iva = new BigDecimal(this.priceField.getText());
                productBean.setPrice(iva);
            } catch (NumberFormatException e) {
                throw new InvalidInputException(InvalidInputException.InputType.PRICE);
            }
        }

        return productBean;
    }

    private void setProductAttributes(ProductInStockBean productInStockBean) {
        if (!this.productAttributesContainer.getValues().isEmpty()) {

            List<String> attrValue = this.productAttributesContainer.getValues();

            Attributes attributes = new Attributes();

            for (int i = 0; i < this.attributesIds.size(); i++) {
                if (!this.attributesIds.get(i).isBlank() && !attrValue.get(i).isBlank()) {
                    attributes.addAttributeIdAndValue(this.attributesIds.get(i), attrValue.get(i));
                }
            }
            productInStockBean.setProductAttributes(attributes);
        }
    }

    @Override
    public ProductInStockBean getProductInStockBean() throws InvalidInputException {
        ProductInStockBean productInStockBean = new ProductInStockBean();

        this.setProductAttributes(productInStockBean);

        if (!this.priceField.getText().isBlank()) {
            try {
                BigDecimal iva = new BigDecimal(this.priceField.getText());
                productInStockBean.setPrice(iva);
            } catch (NumberFormatException e) {
                throw new InvalidInputException(InvalidInputException.InputType.PRICE);
            }
        }

        if (!this.quantityField.getText().isBlank()) {
            try {
                int quantity = Integer.parseInt(this.quantityField.getText());
                productInStockBean.setQuantity(quantity);
            } catch (NumberFormatException e) {
                throw new InvalidInputException(InvalidInputException.InputType.QUANTITY_0_OR_HIGHER);
            }
        }

        if (!this.minimumStockField.getText().isBlank()) {
            try {
                int minimumStock = Integer.parseInt(this.minimumStockField.getText());
                productInStockBean.setMinimumStock(minimumStock);
            } catch (NumberFormatException e) {
                throw new InvalidInputException(InvalidInputException.InputType.QUANTITY_0_OR_HIGHER);
            }
        }

        if (!this.maximumStockField.getText().isBlank()) {
            try {
                int maximumStock = Integer.parseInt(this.maximumStockField.getText());
                productInStockBean.setMaximumStock(maximumStock);
            } catch (NumberFormatException e) {
                throw new InvalidInputException(InvalidInputException.InputType.QUANTITY_0_OR_HIGHER);
            }
        }

        return productInStockBean;
    }
}

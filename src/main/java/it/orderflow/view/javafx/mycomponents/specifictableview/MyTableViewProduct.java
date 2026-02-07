package it.orderflow.view.javafx.mycomponents.specifictableview;

import it.orderflow.beans.ProductBean;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.util.List;

public class MyTableViewProduct extends TableView<ProductBean> {

    public MyTableViewProduct(List<ProductBean> productBeanList) {
        super();
        this.setMinHeight(200);
        this.setItems(FXCollections.observableArrayList(productBeanList));

        TableColumn<ProductBean, String> tableColumnArticleName = new TableColumn<>("Article Name");
        tableColumnArticleName.setCellValueFactory(new PropertyValueFactory<>("articleName"));

        TableColumn<ProductBean, String> tableColumnCode = new TableColumn<>("Code");
        tableColumnCode.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn<ProductBean, String> tableColumnPrice = new TableColumn<>("Price");
        tableColumnPrice.setCellValueFactory(data -> {
            ProductBean pb = data.getValue();
            return new SimpleStringProperty(NumberFormat.getCurrencyInstance().format(pb.getPrice()));
        });

        TableColumn<ProductBean, String> tableColumnIva = new TableColumn<>("IVA");
        tableColumnIva.setCellValueFactory(data -> {
            ProductBean pb = data.getValue();
            return new SimpleStringProperty(NumberFormat.getPercentInstance().format(pb.getArticleIva()));
        });

        this.getColumns().add(tableColumnArticleName);
        this.getColumns().add(tableColumnCode);
        this.getColumns().add(tableColumnPrice);
        this.getColumns().add(tableColumnIva);
    }
}

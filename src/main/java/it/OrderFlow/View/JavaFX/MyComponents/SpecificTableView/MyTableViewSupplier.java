package it.OrderFlow.View.JavaFX.MyComponents.SpecificTableView;

import it.OrderFlow.Beans.SupplierBean;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.util.List;

public class MyTableViewSupplier extends TableView<SupplierBean> {

    public MyTableViewSupplier(List<SupplierBean> supplierBeanList) {
        super();
        this.setMinHeight(200);
        this.setItems(FXCollections.observableArrayList(supplierBeanList));

        TableColumn<SupplierBean, String> tableColumnName = new TableColumn<>("Name");
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<SupplierBean, String> tableColumnEmail = new TableColumn<>("Email");
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<SupplierBean, String> tableColumnPhone = new TableColumn<>("Phone");
        tableColumnPhone.setCellValueFactory(data -> {
            SupplierBean sb = data.getValue();
            if (sb.getPhone() != null) {
                return new SimpleStringProperty(sb.getPhone());
            } else return new SimpleStringProperty("");
        });

        TableColumn<SupplierBean, String> tableColumnTransportFee = new TableColumn<>("Transport Fee");
        tableColumnTransportFee.setCellValueFactory(data -> {
            SupplierBean sb = data.getValue();
            if (sb.getTransportFee() != null) {
                return new SimpleStringProperty(NumberFormat.getCurrencyInstance().format(sb.getTransportFee()));
            } else return new SimpleStringProperty("");
        });

        this.getColumns().add(tableColumnName);
        this.getColumns().add(tableColumnEmail);
        this.getColumns().add(tableColumnPhone);
        this.getColumns().add(tableColumnTransportFee);
    }
}

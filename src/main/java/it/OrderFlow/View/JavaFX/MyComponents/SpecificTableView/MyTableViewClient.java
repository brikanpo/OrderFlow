package it.OrderFlow.View.JavaFX.MyComponents.SpecificTableView;

import it.OrderFlow.Beans.ClientBean;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class MyTableViewClient extends TableView<ClientBean> {

    public MyTableViewClient(List<ClientBean> clientBeanList) {
        super();
        this.setMinHeight(200);
        this.setItems(FXCollections.observableArrayList(clientBeanList));

        TableColumn<ClientBean, String> tableColumnName = new TableColumn<>("Name");
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<ClientBean, String> tableColumnEmail = new TableColumn<>("Email");
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<ClientBean, String> tableColumnPhone = new TableColumn<>("Phone");
        tableColumnPhone.setCellValueFactory(data -> {
            ClientBean cb = data.getValue();
            if (cb.getPhone() != null) {
                return new SimpleStringProperty(cb.getPhone());
            } else return new SimpleStringProperty("");
        });

        TableColumn<ClientBean, String> tableColumnAddress = new TableColumn<>("Address");

        TableColumn<ClientBean, String> tableColumn_Address = new TableColumn<>("Address");
        tableColumn_Address.setCellValueFactory(data -> {
            ClientBean cb = data.getValue();
            if (cb.getAddressBean() != null) {
                return new SimpleStringProperty(cb.getAddressBean().getAddress());
            } else return new SimpleStringProperty("");
        });

        TableColumn<ClientBean, String> tableColumn_Cap = new TableColumn<>("CAP");
        tableColumn_Cap.setCellValueFactory(data -> {
            ClientBean cb = data.getValue();
            if (cb.getAddressBean() != null) {
                return new SimpleStringProperty(cb.getAddressBean().getCap());
            } else return new SimpleStringProperty("");
        });

        TableColumn<ClientBean, String> tableColumn_City = new TableColumn<>("City");
        tableColumn_City.setCellValueFactory(data -> {
            ClientBean cb = data.getValue();
            if (cb.getAddressBean() != null) {
                return new SimpleStringProperty(cb.getAddressBean().getCity());
            } else return new SimpleStringProperty("");
        });

        TableColumn<ClientBean, String> tableColumn_Province = new TableColumn<>("Province");
        tableColumn_Province.setCellValueFactory(data -> {
            ClientBean cb = data.getValue();
            if (cb.getAddressBean() != null) {
                return new SimpleStringProperty(cb.getAddressBean().getProvince());
            } else return new SimpleStringProperty("");
        });

        tableColumnAddress.getColumns().add(tableColumn_Address);
        tableColumnAddress.getColumns().add(tableColumn_Cap);
        tableColumnAddress.getColumns().add(tableColumn_City);
        tableColumnAddress.getColumns().add(tableColumn_Province);

        this.getColumns().add(tableColumnName);
        this.getColumns().add(tableColumnEmail);
        this.getColumns().add(tableColumnPhone);
        this.getColumns().add(tableColumnAddress);
    }
}

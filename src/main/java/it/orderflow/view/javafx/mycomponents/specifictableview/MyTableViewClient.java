package it.orderflow.view.javafx.mycomponents.specifictableview;

import it.orderflow.beans.ClientBean;
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

        TableColumn<ClientBean, String> tableColumnStreetAddress = new TableColumn<>("Street Address");
        tableColumnStreetAddress.setCellValueFactory(data -> {
            ClientBean cb = data.getValue();
            if (cb.getAddressBean() != null) {
                return new SimpleStringProperty(cb.getAddressBean().getStreetAddress());
            } else return new SimpleStringProperty("");
        });

        TableColumn<ClientBean, String> tableColumnCap = new TableColumn<>("CAP");
        tableColumnCap.setCellValueFactory(data -> {
            ClientBean cb = data.getValue();
            if (cb.getAddressBean() != null) {
                return new SimpleStringProperty(cb.getAddressBean().getCap());
            } else return new SimpleStringProperty("");
        });

        TableColumn<ClientBean, String> tableColumnCity = new TableColumn<>("City");
        tableColumnCity.setCellValueFactory(data -> {
            ClientBean cb = data.getValue();
            if (cb.getAddressBean() != null) {
                return new SimpleStringProperty(cb.getAddressBean().getCity());
            } else return new SimpleStringProperty("");
        });

        TableColumn<ClientBean, String> tableColumnProvince = new TableColumn<>("Province");
        tableColumnProvince.setCellValueFactory(data -> {
            ClientBean cb = data.getValue();
            if (cb.getAddressBean() != null) {
                return new SimpleStringProperty(cb.getAddressBean().getProvince());
            } else return new SimpleStringProperty("");
        });

        tableColumnAddress.getColumns().add(tableColumnStreetAddress);
        tableColumnAddress.getColumns().add(tableColumnCap);
        tableColumnAddress.getColumns().add(tableColumnCity);
        tableColumnAddress.getColumns().add(tableColumnProvince);

        this.getColumns().add(tableColumnName);
        this.getColumns().add(tableColumnEmail);
        this.getColumns().add(tableColumnPhone);
        this.getColumns().add(tableColumnAddress);
    }
}

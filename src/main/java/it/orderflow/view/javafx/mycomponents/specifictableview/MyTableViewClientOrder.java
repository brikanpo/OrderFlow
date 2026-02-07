package it.orderflow.view.javafx.mycomponents.specifictableview;

import it.orderflow.beans.ClientOrderBean;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyTableViewClientOrder extends TableView<ClientOrderBean> {

    public MyTableViewClientOrder(List<ClientOrderBean> clientOrderBeanList) {
        super();
        this.setMinHeight(200);
        this.setItems(FXCollections.observableArrayList(clientOrderBeanList));

        TableColumn<ClientOrderBean, String> tableColumnRegistrationDate = new TableColumn<>("Registration Date");
        tableColumnRegistrationDate.setCellValueFactory(data -> {
            ClientOrderBean cob = data.getValue();
            return new SimpleStringProperty(cob.getRegistrationDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        });

        TableColumn<ClientOrderBean, String> tableColumnClientName = new TableColumn<>("Client Name");
        tableColumnClientName.setCellValueFactory(data -> {
            ClientOrderBean cob = data.getValue();
            return new SimpleStringProperty(cob.getClientBean().getName());
        });

        this.getColumns().add(tableColumnRegistrationDate);
        this.getColumns().add(tableColumnClientName);
    }
}

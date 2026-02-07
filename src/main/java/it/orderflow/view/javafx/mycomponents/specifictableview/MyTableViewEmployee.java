package it.orderflow.view.javafx.mycomponents.specifictableview;

import it.orderflow.beans.EmployeeBean;
import it.orderflow.model.UserRole;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class MyTableViewEmployee extends TableView<EmployeeBean> {

    public MyTableViewEmployee(List<EmployeeBean> employeeBeanList) {
        this.setMinHeight(200);
        this.setItems(FXCollections.observableArrayList(employeeBeanList));

        TableColumn<EmployeeBean, String> tableColumnName = new TableColumn<>("Name");
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<EmployeeBean, String> tableColumnEmail = new TableColumn<>("Email");
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<EmployeeBean, UserRole> tableColumnRole = new TableColumn<>("Role");
        tableColumnRole.setCellValueFactory(new PropertyValueFactory<>("userRole"));

        this.getColumns().add(tableColumnName);
        this.getColumns().add(tableColumnEmail);
        this.getColumns().add(tableColumnRole);
    }
}

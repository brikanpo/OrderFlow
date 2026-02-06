package it.OrderFlow.View.JavaFX.MyComponents.SpecificTableView;

import it.OrderFlow.Beans.ProductBean;
import it.OrderFlow.Beans.ProductWithQuantityBean;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.util.List;

public class MyTableViewProductWithQuantity extends TableView<ProductWithQuantityBean> {

    public MyTableViewProductWithQuantity(List<ProductWithQuantityBean> pwqbList, boolean editable) {
        super();
        this.setMinHeight(200);
        this.setItems(FXCollections.observableArrayList(pwqbList));

        TableColumn<ProductWithQuantityBean, String> tableColumnArticleName = new TableColumn<>("Article Name");
        tableColumnArticleName.setCellValueFactory(data -> {
            ProductBean pb = data.getValue().getProductBean();
            return new SimpleStringProperty(pb.getArticleName());
        });

        TableColumn<ProductWithQuantityBean, String> tableColumnCode = new TableColumn<>("Code");
        tableColumnCode.setCellValueFactory(data -> {
            ProductBean pb = data.getValue().getProductBean();
            return new SimpleStringProperty(pb.getCode());
        });

        TableColumn<ProductWithQuantityBean, String> tableColumnPrice = new TableColumn<>("Price");
        tableColumnPrice.setCellValueFactory(data -> {
            ProductBean pb = data.getValue().getProductBean();
            return new SimpleStringProperty(NumberFormat.getCurrencyInstance().format(pb.getPrice()));
        });

        TableColumn<ProductWithQuantityBean, String> tableColumnIva = new TableColumn<>("IVA");
        tableColumnIva.setCellValueFactory(data -> {
            ProductBean pb = data.getValue().getProductBean();
            return new SimpleStringProperty(NumberFormat.getPercentInstance().format(pb.getArticleIva()));
        });

        TableColumn<ProductWithQuantityBean, Integer> tableColumnQuantity = new TableColumn<>("Quantity");
        if (editable) {
            tableColumnQuantity.setCellFactory(column -> new TableCell<>() {
                private final Spinner<Integer> spinner = new Spinner<>(0, 100000, 0);

                {
                    spinner.valueProperty().addListener((obs, oldVal, newVal) -> {
                        if (getTableRow() != null && getTableRow().getItem() != null) {
                            getTableRow().getItem().setQuantity(newVal);
                        }
                    });
                    spinner.setEditable(true);
                    spinner.setMaxWidth(80);
                }

                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        spinner.getValueFactory().setValue(item);
                        setGraphic(spinner);
                    }
                }
            });
        }
        tableColumnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        this.getColumns().add(tableColumnArticleName);
        this.getColumns().add(tableColumnCode);
        this.getColumns().add(tableColumnPrice);
        this.getColumns().add(tableColumnIva);
        this.getColumns().add(tableColumnQuantity);
    }
}

package it.OrderFlow.View.JavaFX.MyComponents.SpecificTableView;

import it.OrderFlow.Beans.ArticleBean;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.util.List;

public class MyTableViewArticle extends TableView<ArticleBean> {

    public MyTableViewArticle(List<ArticleBean> articleBeanList) {
        super();
        this.setMinHeight(200);
        this.setItems(FXCollections.observableArrayList(articleBeanList));

        TableColumn<ArticleBean, String> tableColumnName = new TableColumn<>("Name");
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<ArticleBean, String> tableColumnCategory = new TableColumn<>("Category");
        tableColumnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<ArticleBean, String> tableColumnDescription = new TableColumn<>("Description");
        tableColumnDescription.setCellValueFactory(data -> {
            ArticleBean cab = data.getValue();
            if (cab.getDescription() != null) {
                return new SimpleStringProperty(cab.getDescription());
            } else {
                return new SimpleStringProperty("");
            }
        });

        TableColumn<ArticleBean, String> tableColumnIva = new TableColumn<>("IVA");
        tableColumnIva.setCellValueFactory(data -> {
            ArticleBean cab = data.getValue();
            return new SimpleStringProperty(NumberFormat.getPercentInstance().format(cab.getIva()));
        });

        this.getColumns().add(tableColumnName);
        this.getColumns().add(tableColumnCategory);
        this.getColumns().add(tableColumnDescription);
        this.getColumns().add(tableColumnIva);
    }
}

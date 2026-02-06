module it.OrderFlow {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires mysql.connector.j;
    requires com.google.api.client;
    requires google.api.client;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.auth;
    requires com.google.api.client.json.gson;
    requires com.google.api.services.gmail;
    requires jakarta.mail;
    requires org.apache.commons.codec;
    requires jdk.httpserver;
    requires com.google.gson;
    requires org.checkerframework.checker.qual;

    opens it.OrderFlow.View.JavaFX to javafx.fxml, javafx.graphics;
    opens it.OrderFlow.Beans to javafx.base;

    opens it.OrderFlow.Model to com.google.gson;

    exports it.OrderFlow;
    opens it.OrderFlow.View to javafx.fxml, javafx.graphics;
    opens it.OrderFlow.View.JavaFX.SelectionViews to javafx.fxml, javafx.graphics;
}
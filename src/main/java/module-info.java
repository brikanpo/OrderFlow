module it.orderflow {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires mysql.connector.j;

    requires com.google.api.client;
    requires com.google.api.client.auth;
    requires com.google.api.client.json.gson;
    requires com.google.api.services.gmail;

    requires jakarta.mail;
    requires com.google.gson;
    requires jdk.httpserver;
    requires google.api.client;
    requires org.apache.commons.codec;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires org.checkerframework.checker.qual;

    opens it.orderflow.view.javafx to javafx.fxml, javafx.graphics;
    opens it.orderflow.beans to javafx.base, com.google.gson;
    opens it.orderflow.model to com.google.gson;
    opens it.orderflow.view to javafx.fxml, javafx.graphics;
    opens it.orderflow.view.javafx.selectionviews to javafx.fxml, javafx.graphics;

    exports it.orderflow;
}
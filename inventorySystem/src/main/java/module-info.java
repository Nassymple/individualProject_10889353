module com.example.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires javafx.base;


    opens com.example.inventorysystem to javafx.fxml;
    exports com.example.inventorysystem;
}
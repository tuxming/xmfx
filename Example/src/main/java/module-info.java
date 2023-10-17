module com.xm2013.example.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.xm2013.BaseUI;

    opens com.xm2013.example.example to javafx.fxml,  javafx.graphic;
    exports com.xm2013.example.example;
    exports com.xm2013.example.test;
    opens com.xm2013.example.test to javafx.fxml, javafx.graphic;
}
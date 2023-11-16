module com.xm2013.template.template {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.xm2013.BaseUI;
    requires java.desktop;
    requires javafx.swing;


    opens com.xm2013.template.template to javafx.fxml;
    exports com.xm2013.template.template;
}
module com.xm2013.BaseUI {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.xml;
    requires javafx.base;

    opens com.xm2013.jfx.control.ui to javafx.fxml,  javafx.graphics;
    opens com.xm2013.jfx.borderless to javafx.fxml, javafx.graphics;
    exports com.xm2013.jfx.control.ui;
    exports com.xm2013.jfx.borderless;
    exports com.xm2013.jfx.common;
    exports com.xm2013.jfx.control.svg;
    exports com.xm2013.jfx.component.eventbus;
    exports com.xm2013.jfx.control.button;
    exports com.xm2013.jfx.control.base;
    exports com.xm2013.jfx.control.icon;
    exports com.xm2013.jfx.control.button.animate;
    exports com.xm2013.jfx.control.textfield;
    exports com.xm2013.jfx.control.label;
    exports com.xm2013.jfx.control.checkbox;
    exports com.xm2013.jfx.control.dropdown;
    exports com.xm2013.jfx.control.animate;
    exports com.xm2013.jfx.container;
    exports com.xm2013.jfx.control.scroll;
    exports com.xm2013.jfx.control.selector;
    exports com.xm2013.jfx.control.data;
    exports com.xm2013.jfx.control.date;
    exports com.xm2013.jfx.control.pager;
    exports com.xm2013.jfx.control.listview;
    exports com.xm2013.jfx.control.treeview;
    exports com.xm2013.jfx.control.tableview;
}
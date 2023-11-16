package com.xm2013.template.template;

import com.xm2013.jfx.common.XmMenu;
import com.xm2013.jfx.control.treeview.XmTreeView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;

public class SideBar extends BasePane{

    private final SidebarBottomPane bottomPane;
    private LogoPane logoPane;
    private XmTreeView<XmMenu> menuView;

    public SideBar(ImageView background, BooleanProperty expand, DoubleProperty radius) {
        super(background, radius);

        logoPane = new LogoPane();
        logoPane.prefWidthProperty().bind(widthProperty());

        menuView = MenuBuilder.createMenu(expand);
        menuView.prefWidthProperty().bind(widthProperty());

        bottomPane = new SidebarBottomPane(expand);
        bottomPane.prefWidthProperty().bind(widthProperty());

        getChildren().addAll(logoPane, menuView, bottomPane);
    }

    @Override
    protected void layoutChildren() {
        double width = prefWidth(-1), height = getHeight();
        double logoPaneHeight = logoPane.prefHeight(-1), logoPaneX = 0, logoPaneY = 0;
        double bottomHeight = bottomPane.prefHeight(-1), bottomWidth = bottomPane.prefWidth(-1);
        double menuX = 0, menuY = logoPaneHeight, menuHeight = height - logoPaneHeight - bottomHeight;
        double bottomY = logoPaneHeight + menuHeight;

        layoutInArea(logoPane, logoPaneX, logoPaneY, width, logoPaneHeight, -1, HPos.CENTER, VPos.CENTER);
        layoutInArea(bottomPane, 0, bottomY, width, bottomHeight, -1, HPos.CENTER, VPos.CENTER);
        layoutInArea(menuView, menuX, menuY, width, menuHeight, -1, HPos.CENTER, VPos.CENTER);

    }
}

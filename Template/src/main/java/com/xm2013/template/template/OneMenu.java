package com.xm2013.template.template;

import com.xm2013.jfx.common.XmMenu;
import com.xm2013.jfx.control.icon.XmIcon;
import com.xm2013.jfx.control.icon.XmSVGIcon;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public class OneMenu implements XmMenu {
    private XmIcon icon = null;
    private String label = null;

    public OneMenu(XmIcon icon, String label) {
        this.icon = icon;
        this.label = label;
    }

    @Override
    public XmIcon getIcon() {
        return icon;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Node getPage() {
        return null;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public XmMenu getParent() {
        return null;
    }

    @Override
    public ObservableList<? extends XmMenu> getChildren() {
        return null;
    }
}

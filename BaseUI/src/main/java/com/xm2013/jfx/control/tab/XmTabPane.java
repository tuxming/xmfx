/*
 * MIT License
 *
 * Copyright (c) 2023 tuxming@sina.com / wechat: t5x5m5
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package com.xm2013.jfx.control.tab;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.XmControl;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Skin;

import java.util.List;

public class XmTabPane extends XmControl {
    private static String DEFAULT_CLASS = "xm-tab-pane";

    public XmTabPane(){
        this(null);
    }

    public XmTabPane(List<XmTab> tabs){
        if(tabs!=null){
            this.tabs.setAll(tabs);
        }
        this.init();
    }

    private void init() {
        getStylesheets().add(FxKit.USER_AGENT_STYLESHEET);
        getStyleClass().add(DEFAULT_CLASS);
        setAccessibleRole(AccessibleRole.TAB_PANE);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new XmTabPaneSkin(this);
    }


    /**
     * 要显示的tab
     */
    private ObservableList<XmTab> tabs = FXCollections.observableArrayList();
    public ObservableList<XmTab> getTabs() {
        return tabs;
    }
    public void setTabs(XmTab ...tab) {
        this.tabs.setAll(tab);
    }
    public void setTabs(List<XmTab> tabs){
        this.tabs.setAll(tabs);
    }
    public void addTabs(List<XmTab> tabs){
        this.tabs.addAll(tabs);
    }
    public void addTab(XmTab tab){
        this.tabs.add(tab);
    }

    /**
     * tab头的显示宽度
     */
    private DoubleProperty tabMaxWidth;
    public double getTabMaxWidth() {
        return tabMaxWidthProperty().get();
    }
    public DoubleProperty tabMaxWidthProperty() {
        if(tabMaxWidth == null){
            tabMaxWidth = new SimpleDoubleProperty(300);
        }
        return tabMaxWidth;
    }
    public void setTabMaxWidth(double tabMaxWidth) {
        this.tabMaxWidthProperty().set(tabMaxWidth);
    }

    /**
     * 设置选中tab
     */
    private ObjectProperty<XmTab> selected = new SimpleObjectProperty<>(null);
    public ObjectProperty<XmTab> selectedProperty(){
        return selected;
    }
    public void select(XmTab tab){
        selected.set(tab);
    }
    public XmTab getSelected(){
        return selectedProperty().get();
    }

    
}

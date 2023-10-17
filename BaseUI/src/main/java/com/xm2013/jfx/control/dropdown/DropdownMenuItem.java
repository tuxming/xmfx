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
package com.xm2013.jfx.control.dropdown;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Paint;

/**
 * 下拉菜单的数据定义
 */
public class DropdownMenuItem {
    private DropdownMenuItem parent;
    private ObservableList<DropdownMenuItem> children = FXCollections.observableArrayList();
    private String labelName; //显示名
    private Node icon; //图标

    private Paint textColor;  //文本的颜色，这里可以单独设置
    private Paint selectColor; //选中的背景颜色，这里可以单独设置
    private BooleanProperty disable = new SimpleBooleanProperty(false);  //是否禁用

    public DropdownMenuItem getParent() {
        return parent;
    }

    public void setParent(DropdownMenuItem parent) {
        this.parent = parent;
    }

    public ObservableList<DropdownMenuItem> getChildren() {
        return children;
    }

    public void setChildren(ObservableList<DropdownMenuItem> children) {
        this.children = children;
    }

    public Paint getTextColor() {
        return textColor;
    }

    //单独设置每一行选中的颜色，如果没有设置选中的颜色，则根据主题颜色走
    public void setTextColor(Paint textColor) {
        this.textColor = textColor;
    }

    public Paint getSelectColor() {
        return selectColor;
    }

    public void setSelectColor(Paint selectColor) {
        this.selectColor = selectColor;
    }

    public DropdownMenuItem() {
    }

    public DropdownMenuItem(String labelName) {
        this.labelName = labelName;
    }

    public DropdownMenuItem(String labelName, boolean disable) {
        this.labelName = labelName;
        this.disable.set(disable);
    }

    public DropdownMenuItem(String labelName, Paint textColor, Paint selectColor) {
        this.labelName = labelName;
        this.textColor = textColor;
        this.selectColor = selectColor;
    }

    public DropdownMenuItem(String labelName, Paint textColor, Paint selectColor, boolean disable) {
        this.labelName = labelName;
        this.textColor = textColor;
        this.selectColor = selectColor;
        this.disable.set(disable);
    }

    public DropdownMenuItem(String labelName, Node icon) {
        this.labelName = labelName;
        this.icon = icon;
    }

    public DropdownMenuItem(String labelName, Paint textColor, boolean disable) {
        this.labelName = labelName;
        this.textColor = textColor;
        this.disable.set(disable);
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public boolean isDisable() {
        return disable.get();
    }
    public BooleanProperty disableProperty() {
        return disable;
    }
    public void setDisable(boolean disable) {
        this.disable.set(disable);
    }

    public Node getIcon() {
        return icon;
    }

    public void setIcon(Node icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "DropdownMenuItem{" +
                "labelName='" + labelName + '\'' +
                ", parent=" + (parent!=null?parent.getLabelName():null) +
                ", icon='" + icon + '\'' +
                ", children=" + children +
                '}';
    }
}

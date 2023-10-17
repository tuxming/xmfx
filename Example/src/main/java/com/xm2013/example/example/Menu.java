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
package com.xm2013.example.example;

import com.xm2013.jfx.common.XmMenu;
import com.xm2013.jfx.control.icon.XmFontIcon;
import com.xm2013.jfx.control.icon.XmIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.List;

public class Menu implements XmMenu {
    private String icon;
    private String label;
    private String pageClass;
    private Integer id;

    private XmFontIcon iconNode = null;
    private Node page;

    private Menu parent;
    private ObservableList<Menu> children = FXCollections.observableArrayList();

    public Menu(){}

    public Menu(String icon, String label, Integer id) {
        this(icon, label, null, id, null);
    }

    public Menu(String icon, String label, String pageClass, Integer id) {
        this(icon, label, pageClass, id, null);
    }

    public Menu(String icon, String label, String pageClass, Integer id, Menu parent) {
        this.icon = icon;
        this.label = label;
        this.pageClass = pageClass;
        this.id = id;
        this.parent = parent;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPageClass() {
        return pageClass;
    }

    public void setPageClass(String pageClass) {
        this.pageClass = pageClass;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public void setChildren(ObservableList<Menu> children) {
        this.children = children;
    }

    public void addAllChildren(Menu ...menu){
        this.children.addAll(List.of(menu));
    }

    @Override
    public XmIcon getIcon() {
        if(iconNode==null){
            iconNode = new XmFontIcon(icon);
        }
        return iconNode;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Node getPage() {
        if(page == null){
            if(pageClass!=null){
                try{
                    Class<?> clazz = Class.forName(pageClass);
                    page = (Node) clazz.getDeclaredConstructor().newInstance();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                page = new Pane(new Label(label));
            }
        }
        return page;
    }

    @Override
    public String getKey() {
        return id+"";
    }

    @Override
    public XmMenu getParent() {
        return parent;
    }

    @Override
    public ObservableList<? extends XmMenu> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "label='" + label + '\'' +
                '}';
    }
}

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
package com.xm2013.jfx.control.selector;

import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * 下拉框的数据结构的默认实现，你可以使用任意对象，如果是tree则需要实现SelectorItem接口
 */
public class SelectorItemBase implements SelectorItem {

    protected ObjectProperty<String> label = new SimpleObjectProperty<>(null);
    protected SelectorItem parent;
    protected ObservableList<SelectorItem> children = FXCollections.observableArrayList();

    protected Node icon;

    public void setIcon(Node icon) {
        this.icon = icon;
    }

    @Override
    public String getLabel() {
        return label.get();
    }

    @Override
    public Node getIcon() {
        return this.icon;
    }

    @Override
    public SelectorItem getParent() {
        return parent;
    }

    @Override
    public ObservableList<SelectorItem> getChildren() {
        return children;
    }

    @Override
    public ColorType getSelectedColorType() {
        return ColorType.success();
    }

    @Override
    public HueType getSelectedHueType() {
        return HueType.LIGHT;
    }

    @Override
    public ColorType getSelectedColorTypeInList() {
        return ColorType.danger();
    }

}

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
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * 这个是下拉选中的默认设置值，通过实现这个接口，可以实现一些设置，具体的看说明
 */
public interface SelectorItem {

    /**
     * 获取选中后显示的文本
     * @return String
     */
    String getLabel();

    /**
     * @return Node
     */
    Node getIcon();

    /**
     * 获取父节点，SelectorType为TREE的时候，需要实现这个接口
     * @return SelectorItem
     */
    SelectorItem getParent();

    /**
     * 获取子节点，SelectorType为TREE的时候，需要实现这个接口
     * @return ObservableList
     */
    ObservableList<SelectorItem> getChildren();

    /**
     * 设置选中值的颜色， 选中后显示时的颜色类型，如果没设置则跟随主题色
     * @return ColorType
     */
    ColorType getSelectedColorType();

    /**
     * 设置选中值的色调， 选中后显示时的色调类型，如果没设置则跟随主题色调
     * @return HueType
     */
    HueType getSelectedHueType();

    /**
     * 设置在列表中选中后的显示颜色
     * @return ColorType
     */
    ColorType getSelectedColorTypeInList();

}

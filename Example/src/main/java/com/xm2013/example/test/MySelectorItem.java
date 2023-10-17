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
package com.xm2013.example.test;

import com.xm2013.jfx.control.selector.SelectorItemBase;
import javafx.scene.Node;

public class MySelectorItem extends SelectorItemBase {

    public MySelectorItem(){};

    public MySelectorItem(String label){
        super.label.set(label);
    }

    public MySelectorItem(String label, MySelectorItem parent){
        super.label.set(label);
        super.parent = parent;
    }

    public MySelectorItem(String label,Node icon){
        super.label.set(label);
        super.icon = icon;
    }

    public MySelectorItem(String label,Node icon,  MySelectorItem parent){
        super.label.set(label);
        super.parent = parent;
        super.icon = icon;
    }

    public MySelectorItem(String label, MySelectorItem parent, MySelectorItem ...children){
        super.label.set(label);
        super.parent = parent;
        super.children.setAll(children);
    }

    public MySelectorItem setLabel(String label){
        super.label.set(label);
        return this;
    }

    public MySelectorItem setParent(MySelectorItem parent){
        super.parent = parent;
        return this;
    }

    public MySelectorItem addAllChildren(MySelectorItem ...item){
        super.children.addAll(item);
        return this;
    }

    public MySelectorItem setAllChildren(MySelectorItem ...item){
        super.children.setAll(item);
        return this;
    }

}

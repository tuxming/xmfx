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
package com.xm2013.jfx.control.icon;

import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;

public class XmImgIconSkin extends SkinBase<XmImgIcon> {

    private XmImgIcon control;
    private ImageView view;

    public XmImgIconSkin(XmImgIcon control) {
        super(control);
        this.control = control;

        this.view = new ImageView();
        this.view.imageProperty().bind(control.imageProperty());
        this.view.fitWidthProperty().bind(control.sizeProperty());
        this.view.fitHeightProperty().bind(control.sizeProperty());

        this.getChildren().add(view);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

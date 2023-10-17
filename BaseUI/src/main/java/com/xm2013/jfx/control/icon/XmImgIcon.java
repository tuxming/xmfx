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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;

/**
 * 图片图标，这里的图片一般是正方形，如果不是正方形，可能会变形，并且不能做任何处理
 */
public class XmImgIcon extends XmIcon{
    @Override
    public XmIcon clone() {
        return null;
    }

    public XmImgIcon(){
        super();
    }

    public XmImgIcon(String path){
        super();
        this.setSize(48);
        loadImage(path);
    }

    public XmImgIcon(Image image){
        super();
        this.setSize(48);
        imageProperty().set(image);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new XmImgIconSkin(this);
    }

    private void loadImage(String path) {
        double size = getSize();
        imageProperty().set(new Image(path));
//        imageProperty().set(new Image(path, size, size, false, true));
    }

    private ObjectProperty<Image> image;
    public Image getImage() {
        return image.get();
    }
    public ObjectProperty<Image> imageProperty() {
        if(image == null){
            image = new SimpleObjectProperty<>();
        }
        return image;
    }
    public void setImage(Image image) {
        this.image.set(image);
    }
}

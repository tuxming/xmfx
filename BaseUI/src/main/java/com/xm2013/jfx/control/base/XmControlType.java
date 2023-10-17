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
package com.xm2013.jfx.control.base;

import java.util.Objects;

/**
 * 控件外观统一的属性
 */
public class XmControlType {

    //控件颜色
    private ColorType colorType = null;
    //控件大小
    private SizeType sizeType = null;
    //控件色调
    private HueType hueType =  null;

    private static XmControlType instance;
    private static XmControlType nullable;

    public XmControlType(){
        colorType = ColorType.primary();
        sizeType = SizeType.MEDIUM;
        hueType = HueType.DARK;
    }

    public static XmControlType getDefault(){
        if(instance  == null){
            instance = new XmControlType();
            instance.colorType = ColorType.primary();
            instance.sizeType = SizeType.MEDIUM;
            instance.hueType = HueType.DARK;
        }
        return instance;
    }

    public static XmControlType getNullable(){
        if(nullable  == null){
            nullable = new XmControlType();
        }
        return nullable;
    }

    /**
     * 获取控件颜色类型： primary | secondary | danger | warning | other("#ff00ff")
     * css: -fx-type-color: primary | secondary | danger | warning | other
     * 其中other是fx的颜色值： 可以是：rgba, rgb, hsb, linear-gradient, radial-gradient
     * @return ColorType
     */
    public ColorType getColorType() {
        return colorType;
    }

    /**
     * 设置控件颜色类型： primary | secondary | danger | warning | other("#ff00ff")
     * css: -fx-type-color: primary | secondary | danger | warning | other
     * 其中other是fx的颜色值： 可以是：rgba, rgb, hsb, linear-gradient, radial-gradient
     * @param colorType ColorType
     */
    public void setColorType(ColorType colorType) {
        this.colorType = colorType;
    }

    /**
     * 获取控件的尺寸大小： small, medium, large
     * css: -fx-type-size: small | medium | large
     * @return SizeType
     */
    public SizeType getSizeType() {
        return sizeType;
    }

    /**
     * 设置控件的尺寸大小： small, medium, large
     * css: -fx-type-size: small | medium | large
     * @param sizeType SizeType
     */
    public void setSizeType(SizeType sizeType) {
        this.sizeType = sizeType;
    }

    /**
     * 获取控件的色调，light | dark
     * light: 在light背景下，控件的表现形式
     * dark: 在暗色背景下，控件的表现形式
     * @return HueType
     */
    public HueType getHueType() {
        return hueType;
    }

    /**
     * 获取控件的色调，light | dark
     * light: 在light背景下，控件的表现形式
     * dark: 在暗色背景下，控件的表现形式
     * @param hueType HueType
     */
    public void setHueType(HueType hueType) {
        this.hueType = hueType;
    }

    public XmControlType clone(){

        XmControlType type = new XmControlType();
        type.setHueType(this.getHueType());
        type.setSizeType(this.getSizeType());
        type.setColorType(this.getColorType().clone());
        return type;
    }

    @Override
    public boolean equals(Object o) {
//        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XmControlType that = (XmControlType) o;
        return colorType.equals(that.colorType) && sizeType == that.sizeType && hueType == that.hueType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(colorType, sizeType, hueType);
    }
}

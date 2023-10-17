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
package com.xm2013.jfx.control.button;

import com.xm2013.jfx.control.base.*;

import java.util.Objects;

/**
 * 按钮的类型定义
 */
public class XmButtonType {

    /**
     * 在同时具有，图标，图文本的时候，才有效果
     */
    public enum BtnDisplayType {
        /**
         * 文字和和图标全部显示
         */
        FULL,

        /**
         * 先显示图标
         * 鼠标放上去，显示文本，
         * 鼠标移开，文本隐藏
         */
        OVER_SHOW_TEXT,

        /**
         * 先显示文本
         * 鼠标放上去，显示图标，
         * 鼠标移开，图标隐藏
         */
        OVER_SHOW_ICON,

        /**
         * 先显示图标
         * 鼠标放上去，切换成图标，
         * 鼠标移开，还原
         */
        OVER_SWITCH_TEXT,

        /**
         * 先显示文本
         * 鼠标放上去，切换成图标，
         * 鼠标移开，还原
         */
        OVER_SWITCH_ICON,

    }

    /**
     * 加载时的显示类型
     */
    public enum BtnLoadingType{
        /**
         * 没有任何效果
         */
        NONE,
        /**
         * 显示loading 旋转图标
         */
        ICON,
        /**
         * 显示水平进度条
         */
        H_PROGRESS,
        /**
         * 显示垂直进度条
         */
        V_PROGRESS;
    }

    /**
     * 图标动画类型， 有图标的时候，并且按BtnDisplayType=FULL
     */
    public enum BtnIconAnimationType{
        /**
         * 无
         */
        NONE,
        /**
         * 鼠标移入时
         * 图标旋转
         */
        ROTATE,
        /**
         * 鼠标移入时
         * 图标跳动
         */
        JITTER,
        /**
         * 鼠标移入时
         * 图标翻转
         */
        OVERTURN,
        /**
         * 鼠标移入时
         * 图标闪烁
         */
        FLASH,
        /**
         * 鼠标移入时
         * 图标放大
         */
        SCALE
    }


    private static XmButtonType DEFAULT;
    /**
     * Gets the default font which will be from the family "System",
     * and typically the style "Regular", and be of a size consistent
     * with the user's desktop environment, to the extent that can
     * be determined.
     * @return The default font.
     */
    public static synchronized XmButtonType getDefault() {
        if (DEFAULT == null) {
            DEFAULT =  new XmButtonType();
        }
        return DEFAULT;
    }

    public static synchronized XmButtonType filled() {
        XmButtonType type =  new XmButtonType();
        return type;
    }

    /**
     * 控件外观类型
     */
    private XmControlType controlType = new XmControlType();
    //按钮圆角大小
    private RoundType roundType = RoundType.NONE;
    //按钮边框类型，有变宽，无边框
    private BorderType borderType = BorderType.FULL;
    //按钮显示方式
    private BtnDisplayType displayType = BtnDisplayType.FULL;
    //按钮加载状态的动画
    private BtnLoadingType loadingType = BtnLoadingType.NONE;
    //按钮点击动画
    private ClickAnimateType clickAnimateType = ClickAnimateType.RIPPER;
    //按钮hover动画
    private BtnIconAnimationType hoverAnimateType = BtnIconAnimationType.NONE;

    public XmButtonType(){
    }

    /**
     * 按钮颜色
     * @return ColorType
     */
    public ColorType getColorType() {
        return controlType.getColorType();
    }

    /**
     * 按钮颜色
     * @param colorType ColorType
     * @return XmButtonType
     */
    public XmButtonType setColorType(ColorType colorType) {
//        System.out.println("setColorType:"+colorType.getColor());
        this.controlType.setColorType(colorType);
        return this;
    }

    /**
     * 按钮大小
     * @return SizeType
     */
    public SizeType getSizeType() {
        return controlType.getSizeType();
    }

    /**
     * 按钮大小
     * @param sizeType SizeType
     * @return XmButtonType
     */
    public XmButtonType setSizeType(SizeType sizeType) {
        this.controlType.setSizeType(sizeType);
        return this;
    }

    /**
     * 按钮圆角大小
     * @return RoundType
     */
    public RoundType getRoundType() {
        return roundType;
    }

    /**
     * 按钮圆角大小
     * @param roundType RoundType
     * @return XmButtonType
     */
    public XmButtonType setRoundType(RoundType roundType) {
        this.roundType = roundType;
        return this;
    }

    /**
     * 按钮边框类型，有变宽，无边框
     * @return BorderType
     */
    public BorderType getBorderType() {
        return borderType;
    }

    /**
     * 按钮边框类型，有变宽，无边框
     * @param borderType BorderType
     * @return XmButtonType
     */
    public XmButtonType setBorderType(BorderType borderType) {
        this.borderType = borderType;
        return this;
    }

    /**
     * 按钮色调类型，亮色，暗色, 无色
     * @return HueType
     */
    public HueType getHueType() {
        return controlType.getHueType();
    }

    /**
     * 按钮色调类型，亮色，暗色
     * @param hueType XmButtonType
     * @return XmButtonType
     */
    public XmButtonType setHueType(HueType hueType) {
        this.controlType.setHueType( hueType);
        return this;
    }

    /**
     * 按钮显示方式
     * @return BtnDisplayType
     */
    public BtnDisplayType getDisplayType() {
        return displayType;
    }

    /**
     * 按钮显示方式
     * @param displayType BtnDisplayType
     * @return XmButtonType
     */
    public XmButtonType setDisplayType(BtnDisplayType displayType) {
        this.displayType = displayType;
        return this;
    }

    /**
     * 按钮加载状态的动画
     * @return BtnLoadingType
     */
    public BtnLoadingType getLoadingType() {
        return loadingType;
    }

    /**
     * 按钮加载状态的动画
     * @param loadingType BtnLoadingType
     * @return XmButtonType
     */
    public XmButtonType setLoadingType(BtnLoadingType loadingType) {
        this.loadingType = loadingType;
        return this;
    }

    /**
     * 按钮点击动画
     * @return ClickAnimateType
     */
    public ClickAnimateType getClickAnimateType() {
        return clickAnimateType;
    }

    /**
     * 按钮点击动画
     * @param clickAnimateType ClickAnimateType
     */
    public void setClickAnimateType(ClickAnimateType clickAnimateType) {
        this.clickAnimateType = clickAnimateType;
    }

    /**
     * 按钮hover动画
     * @return BtnIconAnimationType
     */
    public BtnIconAnimationType getHoverAnimateType() {
        return hoverAnimateType;
    }

    /**
     * 按钮hover动画
     * @param hoverAnimateType BtnIconAnimationType
     * @return XmButtonType
     */
    public XmButtonType setHoverAnimateType(BtnIconAnimationType hoverAnimateType) {
        this.hoverAnimateType = hoverAnimateType;
        return this;
    }

    public XmButtonType clone(){

        XmButtonType type = new XmButtonType();
        type.controlType = this.controlType.clone();
        type.setColorType(this.getColorType());
        type.setLoadingType(this.getLoadingType());
        type.setDisplayType(this.getDisplayType());
        type.setRoundType(this.getRoundType());
        type.setBorderType(this.getBorderType());
        type.setClickAnimateType(this.getClickAnimateType());
        type.setHoverAnimateType(this.getHoverAnimateType());
        return type;
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;

        if (this == o) result = true;
        if (o == null || getClass() != o.getClass()) return false;
        XmButtonType that = (XmButtonType) o;
        result = controlType.equals (that.controlType)
                &&roundType == that.roundType
                && borderType == that.borderType
                && displayType == that.displayType
                && loadingType == that.loadingType
                && clickAnimateType == that.clickAnimateType
                && hoverAnimateType == that.hoverAnimateType;
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(controlType, roundType, borderType, displayType, loadingType, clickAnimateType, hoverAnimateType);
    }
}

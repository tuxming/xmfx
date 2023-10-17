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


import com.xm2013.jfx.common.FxKit;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class SkinInfo {

    private ColorType colorType;
    private SizeType sizeType;
    private RoundType roundType;
    private HueType hueType;
    private BorderType borderType;

    double radiusWidth = 0;
    private Insets outerBorderInsets;
    private Insets innerBorderInsets;
    private double outerBorderWidth = 3.5d;
    private double innerBorderWidth = 0.75;
    private Paint innerBorderColor;
    private Paint borderOutColor;
    private Paint backgroundColor;
    private Paint fontColor;
    private double fontSize;
    private Insets padding;

    public SkinInfo(){};

    public SkinInfo(ColorType colorType, SizeType sizeType, RoundType roundType, HueType hueType, BorderType borderType) {
        this.colorType = colorType;
        this.sizeType = sizeType;
        this.roundType = roundType;
        this.hueType = hueType;
        this.borderType = borderType;
    }

    public void set(ColorType colorType, SizeType sizeType, RoundType roundType, HueType hueType, BorderType borderType) {
        this.colorType = colorType;
        this.sizeType = sizeType;
        this.roundType = roundType;
        this.hueType = hueType;
        this.borderType = borderType;
    }

    /**
     * status: 1 ： 默认状态下的颜色
     * status: 2:  hover, out focus
     * status: 3 : hover,focus状态下的颜色
     * status: 4 : out hover, focus状态的颜色
     * @param status int
     */
    public void compute(int status){

        if(roundType.equals(RoundType.CIRCLE)){
            if(sizeType.equals(SizeType.SMALL)){
                fontSize = 14d;
                padding = new Insets(2.3d, 7.5d, 2.2d, 7.5d);
            }else if(sizeType.equals(SizeType.LARGE)){
                fontSize = 18d;
                padding = new Insets(13d, 20d, 13d, 20d);
            }else {
                fontSize = 16d;
                padding = new Insets(10d, 17.5d, 10.5d, 17d);
            }
        }else{
            if(sizeType.equals(SizeType.SMALL)){
                fontSize = 14d;
                padding = new Insets(2.3d, 8d, 2.2d, 8d);
            }else if(sizeType.equals(SizeType.LARGE)){
                fontSize = 18d;
                padding = new Insets(13.2d, 30d, 13d, 30d);
            }else {
                fontSize = 16d;
                padding = new Insets(10d, 15d, 10.5d, 15d);
            }
        }

        //计算圆角大小
        if(RoundType.SMALL.equals(roundType)){
            radiusWidth = 4d;
        }else if(RoundType.SEMICIRCLE.equals(roundType)){
            radiusWidth = 50d;
        }else if(RoundType.CIRCLE.equals(roundType)){
//            double raius = Math.max(control.prefHeight(-1), control.prefWidth(-1));
            radiusWidth = 1000d;
        }

        //计算边框的宽度
        outerBorderInsets = new Insets(-3.5);
        innerBorderInsets = new Insets(0);

        if(sizeType.equals(SizeType.LARGE)){
            innerBorderWidth = 1.35;
        }else if(sizeType.equals(SizeType.MEDIUM)){
            innerBorderWidth = 0.9;
        }else {
            innerBorderWidth = 0.5;
        }

        //根据状态设置变化颜色
        if(status == 1){ //默认状态下

            if(HueType.LIGHT.equals(hueType)){
                //在背景是亮色情况下, 有背景色，背景色跟随主题色，字体白色，边框根据设置是否显示，如果显颜色跟随主题色
                backgroundColor = colorType.getPaint();
                borderOutColor = Color.TRANSPARENT;
                fontColor = Color.WHITE;

                //边框颜色
                if(BorderType.FULL.equals(borderType)){
                    innerBorderColor = backgroundColor;
                }else{
                    innerBorderColor = Color.TRANSPARENT;
                }
            }else if(HueType.DARK.equals(hueType)){
                //在背景是DARK暗色情况下，淡淡的背景，字体主题色，边框根据设置是否显示，如果显颜色跟随主题色
                //hover的时候会显示淡淡的背景色
                //在NONE情况下，无背景色， 字体主题色吗， 边框根据设置是否显示，如果显颜色跟随主题色
                //hover的时候不显示背景色
                backgroundColor = FxKit.getOpacityPaint(colorType.getPaint(), 0.05);
                borderOutColor = Color.TRANSPARENT;
                fontColor = colorType.getPaint();

                if(BorderType.FULL.equals(borderType)){
                    innerBorderColor = fontColor;
                }else{
                    innerBorderColor = Color.TRANSPARENT;
                }
            }else{
                //在NONE情况下，无背景色， 字体主题色吗， 边框根据设置是否显示，如果显颜色跟随主题色
                //hover的时候不显示背景色
                backgroundColor = Color.TRANSPARENT;
                fontColor = FxKit.derivePaint(colorType.getPaint(), 0.1);
                borderOutColor = Color.TRANSPARENT;

                if(BorderType.FULL.equals(borderType)){
                    innerBorderColor = fontColor;
                }else{
                    innerBorderColor = Color.TRANSPARENT;
                }
            }
        }else if(status == 2){  //hover, out focus
            if(HueType.LIGHT.equals(hueType)){
                //在背景是亮色情况下, 有背景色，背景色跟随主题色，字体白色，边框根据设置是否显示，如果显颜色跟随主题色
                //hover的时候背景色变亮
                backgroundColor = FxKit.derivePaint(colorType.getPaint(), 0.4);
                fontColor = Color.WHITE;
                borderOutColor = Color.TRANSPARENT;

                //边框颜色
                if(BorderType.FULL.equals(borderType)){
                    innerBorderColor = backgroundColor;
                }else{
                    innerBorderColor = Color.TRANSPARENT;
                }

            }else if(HueType.DARK.equals(hueType)){
                //在背景是暗色情况下，无背景色，字体主题色，边框根据设置是否显示，如果显颜色跟随主题色
                //hover的时候会显示淡淡的背景色
                backgroundColor = FxKit.getOpacityPaint(colorType.getPaint(), 0.1);
                fontColor = FxKit.derivePaint(colorType.getPaint(), 0.1);
                borderOutColor = Color.TRANSPARENT;

                if(BorderType.FULL.equals(borderType)){
                    innerBorderColor = fontColor;
                }else{
                    innerBorderColor = Color.TRANSPARENT;
                }

            }else{
                //在NONE情况下，无背景色， 字体主题色吗， 边框根据设置是否显示，如果显颜色跟随主题色
                //hover的时候不显示背景色
                backgroundColor = Color.TRANSPARENT;
                fontColor = FxKit.derivePaint(colorType.getPaint(), 0.1);
                borderOutColor = Color.TRANSPARENT;

                if(BorderType.FULL.equals(borderType)){
                    innerBorderColor = fontColor;
                }else{
                    innerBorderColor = Color.TRANSPARENT;
                }
            }
        }else if(status == 3){  // hover,focus状态下的颜色
            if(HueType.LIGHT.equals(hueType)){
                //在背景是亮色情况下, 有背景色，背景色跟随主题色，字体白色，边框根据设置是否显示，如果显颜色跟随主题色
                //hover的时候背景色变亮
                backgroundColor = FxKit.derivePaint(colorType.getPaint(), 0.4);
                fontColor = Color.WHITE;
                borderOutColor = FxKit.getOpacityPaint(colorType.getPaint(), 0.2);

                //边框颜色
                if(BorderType.FULL.equals(borderType)){
                    innerBorderColor = backgroundColor;
                }else{
                    innerBorderColor = Color.TRANSPARENT;
                }

            }else if(HueType.DARK.equals(hueType)){
                //在背景是暗色情况下，无背景色，字体主题色，边框根据设置是否显示，如果显颜色跟随主题色
                //hover的时候会显示淡淡的背景色
                backgroundColor = FxKit.getOpacityPaint(colorType.getPaint(), 0.05);
                fontColor = FxKit.derivePaint(colorType.getPaint(), 0.1);
                borderOutColor = FxKit.getOpacityPaint(FxKit.derivePaint(colorType.getPaint(), 0.5), 0.4);

                if(BorderType.FULL.equals(borderType)){
                    innerBorderColor = fontColor;
                }else{
                    innerBorderColor = Color.TRANSPARENT;
                }

            }else{
                //在NONE情况下，无背景色， 字体主题色吗， 边框根据设置是否显示，如果显颜色跟随主题色
                //hover的时候不显示背景色
                backgroundColor = Color.TRANSPARENT;
                fontColor = FxKit.derivePaint(colorType.getPaint(), 0.1);
                borderOutColor = FxKit.getOpacityPaint(colorType.getPaint(), 0.2);

                if(BorderType.FULL.equals(borderType)){
                    innerBorderColor = fontColor;
                }else{
                    innerBorderColor = Color.TRANSPARENT;
                }
            }
        }else if(status == 4){  // out hover, focus状态的颜色
            if(HueType.LIGHT.equals(hueType)){
                //在背景是亮色情况下, 有背景色，背景色跟随主题色，字体白色，边框根据设置是否显示，如果显颜色跟随主题色
                backgroundColor = colorType.getPaint();
                borderOutColor = FxKit.getOpacityPaint(colorType.getPaint(), 0.15);
                fontColor = Color.WHITE;

                //边框颜色
                if(BorderType.FULL.equals(borderType)){
                    innerBorderColor = backgroundColor;
                }else{
                    innerBorderColor = Color.TRANSPARENT;
                }
            }else if(HueType.DARK.equals(hueType)){
                //在背景是DARK暗色情况下，无背景色，字体主题色，边框根据设置是否显示，如果显颜色跟随主题色
                //hover的时候会显示淡淡的背景色
                //在NONE情况下，无背景色， 字体主题色吗， 边框根据设置是否显示，如果显颜色跟随主题色
                //hover的时候不显示背景色
                backgroundColor = Color.TRANSPARENT;
                borderOutColor = FxKit.getOpacityPaint(FxKit.derivePaint(colorType.getPaint(), 0.5), 0.25);
                fontColor = colorType.getPaint();

                if(BorderType.FULL.equals(borderType)){
                    innerBorderColor = fontColor;
                }else{
                    innerBorderColor = Color.TRANSPARENT;
                }
            }else{
                //在背景是DARK暗色情况下，无背景色，字体主题色，边框根据设置是否显示，如果显颜色跟随主题色
                //hover的时候会显示淡淡的背景色
                //在NONE情况下，无背景色， 字体主题色吗， 边框根据设置是否显示，如果显颜色跟随主题色
                //hover的时候不显示背景色
                backgroundColor = Color.TRANSPARENT;
                borderOutColor = FxKit.getOpacityPaint(colorType.getPaint(), 0.15);
                fontColor = colorType.getPaint();

                if(BorderType.FULL.equals(borderType)){
                    innerBorderColor = fontColor;
                }else{
                    innerBorderColor = Color.TRANSPARENT;
                }
            }
        }
    }

    public void setColorType(ColorType colorType) {
        this.colorType = colorType;
    }

    public void setSizeType(SizeType sizeType) {
        this.sizeType = sizeType;
    }

    public void setRoundType(RoundType roundType) {
        this.roundType = roundType;
    }

    public void setHueType(HueType hueType) {
        this.hueType = hueType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public double getRadiusWidth() {
        return radiusWidth;
    }

    public Insets getOuterBorderInsets() {
        return outerBorderInsets;
    }

    public Insets getInnerBorderInsets() {
        return innerBorderInsets;
    }

    public double getOuterBorderWidth() {
        return outerBorderWidth;
    }

    public double getInnerBorderWidth() {
        return innerBorderWidth;
    }

    public Paint getInnerBorderColor() {
        return innerBorderColor;
    }

    public Paint getBorderOutColor() {
        return borderOutColor;
    }

    public Paint getBackgroundColor() {
        return backgroundColor;
    }

    public Paint getFontColor() {
        return fontColor;
    }

    public double getFontSize() {
        return fontSize;
    }

    public Insets getPadding() {
        return padding;
    }
}

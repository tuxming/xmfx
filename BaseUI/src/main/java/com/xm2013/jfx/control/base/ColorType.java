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
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;

import java.util.Objects;

/**
 * 组件颜色
 */
public class ColorType {
    /**
     * 主要颜色
     */
    public static String PRIMARY = "#4c14c1ff";
    public static String SECONDARY="#585858ff";
    public static String DANGER="#ff4d4fff";
    public static String WARNING="#fc9e1bff";
    public static String SUCCESS="#49c31bff";
    public static String OTHER="";

    public static ColorType other(String color){
        return new ColorType(color, "other");
    }
    public static ColorType other(Paint color){
        return new ColorType(color);
    }
    public static ColorType primary(){
        return new ColorType(PRIMARY, "primary");
    }
    public static ColorType secondary(){
        return new ColorType(SECONDARY, "secondary");
    }
    public static ColorType danger(){
        return new ColorType(DANGER, "danger");
    }
    public static ColorType warning(){
        return new ColorType(WARNING, "warning");
    }
    public static ColorType success(){
        return new ColorType(SUCCESS, "success");
    }

    public ColorType(String color) {
        this.color = color;
    }
    public ColorType(Paint color) {
        this.paint = color;
    }

    public ColorType(String color, String label) {
        this.color = color;
        this.label = label;
    }

    public static ColorType get(String value){
        if(value == null || value.isEmpty()){
            return null;
        }

        value = value.toLowerCase();
        if(value.equals("primary")){
            return primary();
        }else if(value.equals("secondary")){
            return secondary();
        }else if(value.equals("danger")){
            return danger();
        }else if(value.equals("warning")){
            return warning();
        }else if(value.equals("success")){
            return success();
        }else{
            return other(value);
        }
    }

    private String color;
    private Paint paint;
    private Color fxColor;
    //颜色名
    private String label;

    public String getColor() {
        if(color == null){
            if(paint instanceof Color){
                color = FxKit.formatHexString((Color) paint);
            }else{
                color = paint.toString();
            }
        }
        return color;
    }

    public ColorType clone(){
        return new ColorType(this.color, this.label);
    }

    public String getLabel() {
        return label;
    }

    public Paint getPaint(){
        if(paint == null){
            //TODO 这里支持渐变色的转换
            if(color.toLowerCase().startsWith("linear-gradient")){
                paint = LinearGradient.valueOf(color);
            }else if(color.toLowerCase().startsWith("radial-gradient")){
                paint = RadialGradient.valueOf(color);
            }else{
                paint = Color.web(color);
            }
        }
        return paint;
    }

    public Color getFxColor(){
        if(fxColor == null){
            //TODO 这里支持渐变色的转换
            if(color.toLowerCase().startsWith("linear-gradient")){
                LinearGradient lg = LinearGradient.valueOf(color);
                paint = lg;
                fxColor = lg.getStops().get(0).getColor();
            }else if(color.toLowerCase().startsWith("radial-gradient")){
                RadialGradient rg = RadialGradient.valueOf(color);
                paint = rg;
                fxColor = rg.getStops().get(0).getColor();
            }else{
                paint = Color.web(color);
                fxColor = (Color) paint;
            }
        }
        return fxColor;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
//        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorType that = (ColorType) o;
        return Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }

    @Override
    public String toString() {

        if("primary".equals(label) || "secondary".equals(label) || "danger".equals(label) || "warning".equals(label) || "success".equals(label)  )
            return label;

        return getColor();
    }
}

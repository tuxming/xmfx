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

import javafx.css.CssMetaData;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.Map;

/**
 * 颜色类型转换
 */
public class ColorTypeConverter extends StyleConverter<ParsedValue[], ColorType> {
    @Override
    public ColorType convert(ParsedValue<ParsedValue[], ColorType> value, Font font) {

        Object pv = value.getValue();

        if(pv instanceof Paint){
            try {
                return ColorType.other((Paint) pv);
            }catch (Exception e){
                return ColorType.primary();
            }
        }else{
            String color = (String) pv;
            if("primary".equals(color)){
                return ColorType.primary();
            }else if("secondary".equals(color)){
                return ColorType.secondary();
            }else if("danger".equals(color)){
                return ColorType.danger();
            }else if("warning".equals(color)){
                return ColorType.warning();
            }else if("success".equals(color)) {
                return ColorType.success();
            }
        }

        return ColorType.primary();
    }

    @Override
    public ColorType convert(Map<CssMetaData<? extends Styleable, ?>, Object> convertedValues) {
//        System.out.println("bb:"+convertedValues);
        return super.convert(convertedValues);
    }
}

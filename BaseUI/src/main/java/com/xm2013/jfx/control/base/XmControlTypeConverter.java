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
import javafx.scene.text.Font;

import java.util.Map;

public class XmControlTypeConverter extends StyleConverter<ParsedValue[], XmControlType> {

    private static class Holder {
        static final XmControlTypeConverter INSTANCE = new XmControlTypeConverter();
        static final XmControlTypeConverter.SequenceConverter SEQUENCE_INSTANCE = new XmControlTypeConverter.SequenceConverter();
    }

    private XmControlType type;
    public XmControlTypeConverter(){
    }
    public XmControlTypeConverter(XmControlType type){
        this.type = type;
    }

    public static StyleConverter<ParsedValue[], XmControlType> getInstance() {
        return XmControlTypeConverter.Holder.INSTANCE;
    }

    @Override
    public XmControlType convert(ParsedValue<ParsedValue[], XmControlType> value, Font font) {
//        System.out.println("value:"+value);
//        return super.convert(value, font);
        return type==null? XmControlType.getNullable():type;
    }


    @Override
    public XmControlType convert(Map<CssMetaData<? extends Styleable, ?>, Object> convertedValues) {
//        System.out.println("111111");
//        return super.convert(convertedValues);

        XmControlType t = type==null?XmControlType.getNullable():type;
        for(CssMetaData<?,?> key : convertedValues.keySet()){
            Object value = convertedValues.get(key);
            String property = key.getProperty();
            if(property.equals(CssKeys.PropTypeSize)){
                t.setSizeType((SizeType) value);
            }else if(property.equals(CssKeys.PropTypeColor)){
                t.setColorType((ColorType) value);
            }else if(property.equals(CssKeys.PropTypeHue)){
                HueType hue = (HueType) value;
                t.setHueType(hue);
            }
        }
        return t;
    }


    @Override
    public String toString() {
        return "XmControlTypeConverter";
    }

    public static final class SequenceConverter extends StyleConverter<ParsedValue<ParsedValue[], XmControlType>[], XmControlType[]> {
        public static XmControlTypeConverter.SequenceConverter getInstance() {
            return XmControlTypeConverter.Holder.SEQUENCE_INSTANCE;
        }

        private SequenceConverter() {
            super();
        }

        @Override
        public XmControlType[] convert(ParsedValue<ParsedValue<ParsedValue[], XmControlType>[], XmControlType[]> value, Font font) {
//            System.out.println(value);
            ParsedValue<ParsedValue[], XmControlType>[] layers = value.getValue();
            XmControlType[] strings = new XmControlType[layers.length];
            for (int layer = 0; layer < layers.length; layer++) {
                strings[layer] = XmControlTypeConverter.getInstance().convert(layers[layer], font);
            }
            return strings;
        }

        @Override
        public String toString() {
            return "XmButtonConverter.SequenceConverter";
        }
    }
}

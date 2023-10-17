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
import javafx.css.CssMetaData;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.scene.text.Font;

import java.util.Map;

public class XmButtonTypeConverter extends StyleConverter<ParsedValue[], XmButtonType> {

    private static class Holder {
        static final XmButtonTypeConverter INSTANCE = new XmButtonTypeConverter();
        static final XmButtonTypeConverter.SequenceConverter SEQUENCE_INSTANCE = new XmButtonTypeConverter.SequenceConverter();
    }

    private XmButtonType type;
    public XmButtonTypeConverter(){
    }
    public XmButtonTypeConverter(XmButtonType type){
        this.type = type;
    }

    public static StyleConverter<ParsedValue[], XmButtonType> getInstance() {
        return XmButtonTypeConverter.Holder.INSTANCE;
    }

    @Override
    public XmButtonType convert(ParsedValue<ParsedValue[], XmButtonType> value, Font font) {
//        System.out.println("value:"+value);
//        return super.convert(value, font);
        return type==null? XmButtonType.getDefault():type;
    }


    @Override
    public XmButtonType convert(Map<CssMetaData<? extends Styleable, ?>, Object> convertedValues) {
//        return super.convert(convertedValues);
        XmButtonType t = type==null?new XmButtonType():type;
        for(CssMetaData<?,?> key : convertedValues.keySet()){
            Object value = convertedValues.get(key);
            String property = key.getProperty();
            if(property.equals(CssKeys.PropTypeSize)){
                t.setSizeType((SizeType) value);
            }else if(property.equals(CssKeys.PropTypeColor)){
                t.setColorType((ColorType) value);
            }else if(property.equals(CssKeys.PropTypeRound)){
                RoundType round = value == null?
                        RoundType.NONE: (RoundType) value;
                t.setRoundType(round);
            }else if(property.equals(CssKeys.PropTypeHue)){
                HueType hue = (HueType) value;
                t.setHueType(hue);
            }else if(property.equals(CssKeys.PropTypeDisplay)){
                t.setDisplayType((XmButtonType.BtnDisplayType) value);
            }else if(property.equals(CssKeys.PropTypeLoading)){
                t.setLoadingType((XmButtonType.BtnLoadingType) value);
            }else if(property.equals(CssKeys.PropTypeClickAnimate)){
                t.setClickAnimateType((ClickAnimateType) value);
            }else if(property.equals(CssKeys.PropTypeIconAnimate)){
                t.setHoverAnimateType((XmButtonType.BtnIconAnimationType) value);
            }
        }
        return t;
    }


    @Override
    public String toString() {
        return "XmButtonTypeConverter";
    }

    public static final class SequenceConverter extends StyleConverter<ParsedValue<ParsedValue[], XmButtonType>[], XmButtonType[]> {
        public static XmButtonTypeConverter.SequenceConverter getInstance() {
            return XmButtonTypeConverter.Holder.SEQUENCE_INSTANCE;
        }

        private SequenceConverter() {
            super();
        }

        @Override
        public XmButtonType[] convert(ParsedValue<ParsedValue<ParsedValue[], XmButtonType>[], XmButtonType[]> value, Font font) {
//            System.out.println(value);
            ParsedValue<ParsedValue[], XmButtonType>[] layers = value.getValue();
            XmButtonType[] strings = new XmButtonType[layers.length];
            for (int layer = 0; layer < layers.length; layer++) {
                strings[layer] = XmButtonTypeConverter.getInstance().convert(layers[layer], font);
            }
            return strings;
        }

        @Override
        public String toString() {
            return "XmButtonConverter.SequenceConverter";
        }
    }
}

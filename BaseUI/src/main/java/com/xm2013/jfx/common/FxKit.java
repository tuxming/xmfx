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
package com.xm2013.jfx.common;


import com.xm2013.jfx.control.svg.PathInfo;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.css.*;
import javafx.scene.paint.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// TODO: Auto-generated Javadoc
/**
 * The Class FxKit.
 */
public class FxKit {

    /** The Constant DOUBLE_ARROW_RIGHT. */
    public static final String DOUBLE_ARROW_RIGHT = "<svg version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"1050\">" +
            "<path d=\"M759.466667 533.333333L469.333333 243.2l29.866667-29.866667 320 320-320 320-29.866667-29.866666 " +
            "290.133334-290.133334z m-298.666667 0L170.666667 243.2l29.866666-29.866667 320 320L200.533333 853.333333l" +
            "-29.866666-29.866666 290.133333-290.133334z\" fill=\"#444444\" p-id=\"1051\"></path></svg>";

    /** The Constant DOUBLE_ARROW_LEFT. */
    public static final String DOUBLE_ARROW_LEFT = "<svg version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"1197\">" +
            "<path d=\"M170.666667 533.333333L490.666667 213.333333l29.866666 29.866667-290.133333 290.133333 290.133333 " +
            "290.133334-29.866666 29.866666L170.666667 533.333333z m298.666666 0L789.333333 213.333333l29.866667 29.866667" +
            "-290.133333 290.133333 290.133333 290.133334-29.866667 29.866666-320-320z\" fill=\"#444444\" p-id=\"1198\"></path></svg>";

    /** The Constant ARROW_RIGHT. */
    public static final String ARROW_RIGHT = "M674.133333 533.333333L341.333333 200.533333l29.866667-29.866666 " +
            "362.666667 362.666666L371.2 896l-29.866667-29.866667 332.8-332.8z";

    /** The Constant ARROW_LEFT. */
    public static final String ARROW_LEFT = "M396.8 494.933333l332.8-332.8-29.866667-29.866666-362.666666 362.666666 " +
            "362.666666 362.666667 29.866667-29.866667-332.8-332.8z";

    /** The Constant CLEAN_PATH. */
    public static final String CLEAN_PATH = "M512 64c-247.00852 0-448 200.960516-448 448S264.960516 960 512 " +
            "960c247.00852 0 448-200.960516 448-448S759.039484 64 512 64zM694.752211 " +
            "649.984034c12.480043 12.54369 12.447359 32.768069-0.063647 45.248112-6.239161 " +
            "6.208198-14.399785 9.34412-22.591372 9.34412-8.224271 0-16.415858-3.135923-22.65674" +
            "-9.407768l-137.60043-138.016718-138.047682 136.576912c-6.239161 6.14455-14.368821" +
            " 9.247789-22.496761 9.247789-8.255235 0-16.479505-3.168606-22.751351-9.504099-12.416396" +
            "-12.576374-12.320065-32.800753 0.25631-45.248112l137.887703-136.384249-137.376804-137.824056c" +
            "-12.480043-12.512727-12.447359-32.768069 0.063647-45.248112 12.512727-12.512727 " +
            "32.735385-12.447359 45.248112 0.063647l137.567746 137.984034 138.047682-136.575192c12.54369" +
            "-12.447359 32.831716-12.320065 45.248112 0.25631 12.447359 12.576374 12.320065 " +
            "32.831716-0.25631 45.248112L557.344443 512.127295 694.752211 649.984034z";

    /** The Constant DATE_PATH. */
    public static final String DATE_PATH = "M929.493333 73.130667l-187.413333 0L742.08 24.874667c0-11.776-9.536-" +
            "21.333333-21.333333-21.333333s-21.333333 9.557333-21.333333 21.333333l0 48.256L324.565333 73.130667 " +
            "324.565333 24.874667c0-11.776-9.557333-21.333333-21.333333-21.333333s-21.333333 9.557333-21.333333 " +
            "21.333333l0 48.256L129.28 73.130667c-55.210667 0-125.717333 70.485333-125.717333 125.717333l0 695.914667c0 " +
            "55.210667 70.506667 125.717333 125.717333 125.717333L929.493333 1020.48c59.349333 0 90.965333-73.002667 " +
            "90.965333-125.717333L1020.458667 198.826667C1020.458667 146.133333 988.864 73.130667 929.493333 " +
            "73.130667zM129.28 115.797333l152.64 0 0 152.64c0 11.776 9.557333 21.333333 21.333333 21.333333s21.333333-" +
            "9.557333 21.333333-21.333333L324.586667 115.797333l374.848 0 0 152.64c0 11.776 9.536 21.333333 21.333333 " +
            "21.333333s21.333333-9.557333 21.333333-21.333333L742.101333 115.797333l187.413333 0c26.752 0 48.298667 " +
            "45.418667 48.298667 83.050667l0 187.434667L46.229333 386.282667 46.229333 198.826667C46.229333 166.229333 " +
            "96.661333 115.797333 129.28 115.797333zM929.493333 977.792 129.28 977.792c-31.829333 0-83.050667-51.221333" +
            "-83.050667-83.050667L46.229333 428.928l931.562667 0 0 465.813333C977.792 932.352 956.245333 " +
            "977.792 929.493333 977.792z";

    /** The user agent stylesheet. */
    public static String USER_AGENT_STYLESHEET = getResourceURL("/css/control.css");

    /** The Constant INPUT_STYLE_CLASS. */
    public final static String INPUT_STYLE_CLASS = "xm-input-field";

    /**
     * Gets the resource URL.
     *
     * @param path the path
     * @return the resource URL
     */
    public static String getResourceURL(String path) {
        return FxKit.class.getResource(path).toExternalForm();
    }

    /** The time pattern. */
    public static String TIME_PATTERN = "HH:mm:ss";
    
    /** The year pattern. */
    public static String YEAR_PATTERN = "yyyy";
    
    /** The month pattern. */
    public static String MONTH_PATTERN = "yyyy-MM";
    
    /** The date pattern. */
    public static String DATE_PATTERN = "yyyy-MM-dd";
    
    /** The datetime pattern. */
    public static String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * New property.
     *
     * @param <T> the generic type
     * @param initValue the init value
     * @param cssMetaData the css meta data
     * @param bean the bean
     * @param name the name
     * @return the object property
     */
    public static <T> ObjectProperty<T> newProperty(T initValue, CssMetaData cssMetaData, Object bean, String name) {

        ObjectProperty<T> value = new StyleableObjectProperty<T>(initValue) {
            @Override
            public Object getBean() {
                return bean;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public CssMetaData<? extends Styleable, T> getCssMetaData() {
                return cssMetaData;
            }
        };
        return value;
    }

    /**
     * New double property.
     *
     * @param initValue the init value
     * @param cssMetaData the css meta data
     * @param bean the bean
     * @param name the name
     * @return the double property
     */
    public static DoubleProperty newDoubleProperty(Double initValue, CssMetaData cssMetaData, Object bean, String name) {
        return new StyleableDoubleProperty(initValue) {

            @Override
            public void invalidated() {
            }

            @Override
            public CssMetaData getCssMetaData() {
                return cssMetaData;
            }

            @Override
            public Object getBean() {
                return bean;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    /**
     * New integer property.
     *
     * @param initValue the init value
     * @param cssMetaData the css meta data
     * @param bean the bean
     * @param name the name
     * @return the integer property
     */
    public static IntegerProperty newIntegerProperty(Integer initValue, CssMetaData cssMetaData, Object bean, String name) {
        return new StyleableIntegerProperty(initValue) {

            @Override
            public void invalidated() {
            }

            @Override
            public CssMetaData getCssMetaData() {
                return cssMetaData;
            }

            @Override
            public Object getBean() {
                return bean;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    /**
     * New boolean property.
     *
     * @param initValue the init value
     * @param cssMetaData the css meta data
     * @param bean the bean
     * @param name the name
     * @return the boolean property
     */
    public static BooleanProperty newBooleanProperty(Boolean initValue, CssMetaData cssMetaData, Object bean, String name) {
        return new StyleableBooleanProperty(initValue) {

            @Override
            public void invalidated() {
            }

            @Override
            public CssMetaData getCssMetaData() {
                return cssMetaData;
            }

            @Override
            public Object getBean() {
                return bean;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    /**
     * New string property.
     *
     * @param initValue the init value
     * @param cssMetaData the css meta data
     * @param bean the bean
     * @param name the name
     * @return the styleable string property
     */
    public static StyleableStringProperty newStringProperty(String initValue, CssMetaData cssMetaData, Object bean, String name) {
        return new StyleableStringProperty(initValue) {

            @Override
            public void invalidated() {
            }

            @Override
            public CssMetaData getCssMetaData() {
                return cssMetaData;
            }

            @Override
            public Object getBean() {
                return bean;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    /**
     * 颜色转16进制格式的颜色.
     *
     * @param c the c
     * @return the string
     */
    public static String formatHexString(Color c) {
        if (c != null) {

            return String.format((Locale) null, "#%02x%02x%02x%02x", Math.round(c.getRed() * 255), Math.round(c.getGreen() * 255), Math.round(c.getBlue() * 255), Math.round(c.getOpacity() * 255));
        } else {
            return null;
        }
    }

    /**
     * Derive paint.
     *
     * @param paint the paint
     * @param brightneee the brightneee
     * @return the paint
     */
    public static Paint derivePaint(Paint paint, double brightneee){
        Paint color = null;
        if(paint instanceof  Color){
            color = deriveColor((Color) paint, brightneee);
        }else if(paint instanceof LinearGradient){

            LinearGradient lgColor = (LinearGradient) paint;
            List<Stop> stops = lgColor.getStops();
            List<Stop> newStops = new ArrayList<Stop>();
            for(Stop stop : stops){
                Color c1 = deriveColor( stop.getColor(), brightneee);
                newStops.add(new Stop(stop.getOffset(), c1));
            }

            color = new LinearGradient(
                    lgColor.getStartX(),
                    lgColor.getStartY(),
                    lgColor.getEndX(),
                    lgColor.getEndX(),
                    lgColor.isProportional(),
                    lgColor.getCycleMethod(),
                    newStops
            );
        }else if(paint instanceof  RadialGradient){

            RadialGradient lgColor = (RadialGradient) paint;
            List<Stop> stops = lgColor.getStops();
            List<Stop> nstops = new ArrayList<Stop>();
            for(Stop stop : stops){
                Color c1 = deriveColor( stop.getColor(), brightneee);
                nstops.add(new Stop(stop.getOffset(), c1));
            }

            color = new RadialGradient(
                lgColor.getFocusAngle(),
                lgColor.getFocusDistance(),
                lgColor.getCenterX(),
                lgColor.getCenterY(),
                lgColor.getRadius(),
                lgColor.isProportional(),
                lgColor.getCycleMethod(),
                nstops
            );
        }

        return color;
    }

    /**
     * Derives a lighter or darker of a given color.
     *
     * @param c          The color to derive from
     * @param brightness The brightness difference for the new color -1.0 being 100% dark which is always black, 0.0 being
     *                   no change and 1.0 being 100% lighter which is always white
     * @return the color
     */
    public static Color deriveColor(Color c, double brightness) {
        double baseBrightness = calculateBrightness(c);
        double calcBrightness = brightness;
        // Fine adjustments to colors in ranges of brightness to adjust the contrast for them
        if (brightness > 0) {
            if (baseBrightness > 0.85) {
                calcBrightness = calcBrightness * 1.6;
            } else if (baseBrightness > 0.6) {
                // no change
            } else if (baseBrightness > 0.5) {
                calcBrightness = calcBrightness * 0.9;
            } else if (baseBrightness > 0.4) {
                calcBrightness = calcBrightness * 0.8;
            } else if (baseBrightness > 0.3) {
                calcBrightness = calcBrightness * 0.7;
            } else {
                calcBrightness = calcBrightness * 0.6;
            }
        } else {
            if (baseBrightness < 0.2) {
                calcBrightness = calcBrightness * 0.6;
            }
        }
        // clamp brightness
        if (calcBrightness < -1) {
            calcBrightness = -1;
        } else if (calcBrightness > 1) {
            calcBrightness = 1;
        }
        // window two take the calculated brightness multiplyer and derive color based on source color
        double[] hsb = RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue());
        // change brightness
        if (calcBrightness > 0) { // brighter
            hsb[1] *= 1 - calcBrightness;
            hsb[2] += (1 - hsb[2]) * calcBrightness;
        } else { // darker
            hsb[2] *= calcBrightness + 1;
        }
        // clip saturation and brightness
        if (hsb[1] < 0) {
            hsb[1] = 0;
        } else if (hsb[1] > 1) {
            hsb[1] = 1;
        }
        if (hsb[2] < 0) {
            hsb[2] = 0;
        } else if (hsb[2] > 1) {
            hsb[2] = 1;
        }
        // convert back to color
        //        Color c2 = Color.hsb((int)hsb[0], hsb[1], hsb[2],c.getOpacity());
        return Color.hsb((int) hsb[0], hsb[1], hsb[2], c.getOpacity());

		/*   var hsb:Number[] = RGBtoHSB(c.red,c.green,c.blue);
        // change brightness
        if (brightness > 0) {
            //var bright:Number = brightness * (1-calculateBrightness(c));
            var bright:Number = if (calculateBrightness(c)<0.65 and brightness > 0.5) {
                    if (calculateBrightness(c)<0.2) then brightness * 0.55 else brightness * 0.7
            } else brightness;
            // brighter
            hsb[1] *= 1 - bright;
            hsb[2] += (1 - hsb[2]) * bright;
        } else {
            // darker
            hsb[2] *= brightness+1;
        }
        // clip saturation and brightness
        if (hsb[1] < 0) { hsb[1] = 0;} else if (hsb[1] > 1) {hsb[1] = 1}
        if (hsb[2] < 0) { hsb[2] = 0;} else if (hsb[2] > 1) {hsb[2] = 1}
        // convert back to color
        return Color.hsb(hsb[0],hsb[1],hsb[2]) */
    }

    /**
     * Calculates a perceptual brightness for a color between 0.0 black and 1.0 while
     *
     * @param color the color
     * @return the double
     */
    public static double calculateBrightness(Color color) {
        return (0.3 * color.getRed()) + (0.59 * color.getGreen()) + (0.11 * color.getBlue());
    }

    /**
     * RG bto HSB.
     *
     * @param r the r
     * @param g the g
     * @param b the b
     * @return the double[]
     */
    public static double[] RGBtoHSB(double r, double g, double b) {
        double hue, saturation, brightness;
        double[] hsbvals = new double[3];
        double cmax = (r > g) ? r : g;
        if (b > cmax) cmax = b;
        double cmin = (r < g) ? r : g;
        if (b < cmin) cmin = b;

        brightness = cmax;
        if (cmax != 0) saturation = (double) (cmax - cmin) / cmax;
        else saturation = 0;

        if (saturation == 0) {
            hue = 0;
        } else {
            double redc = (cmax - r) / (cmax - cmin);
            double greenc = (cmax - g) / (cmax - cmin);
            double bluec = (cmax - b) / (cmax - cmin);
            if (r == cmax) hue = bluec - greenc;
            else if (g == cmax) hue = 2.0 + redc - bluec;
            else hue = 4.0 + greenc - redc;
            hue = hue / 6.0;
            if (hue < 0) hue = hue + 1.0;
        }
        hsbvals[0] = hue * 360;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }

    /**
     * Gets the gray.
     *
     * @param color the color
     * @return the gray
     */
    public static int getGray(Color color) {
        String grayColor = color.grayscale().toString();
        int gray = Integer.parseInt(grayColor.substring(2,4), 16);
        return gray;
    }

    /**
     * 获取指定灰度的颜色，亮色
     * 1白色，0黑色.
     *
     * @param color the color
     * @param light the light
     * @return the light color
     */
    public static Color getLightColor(Color color, double light){

        if(light < color.grayscale().getRed()){
            return color;
        }

        while(light>color.grayscale().getRed()){
            color = deriveColor(color, 0.1);
        }
        return color;
    }

    /**
     * 获取指定灰度的颜色，亮色
     * 1白色，0黑色.
     *
     * @param paint the paint
     * @param light the light
     * @return the light paint
     */
    public static Paint getLightPaint(Paint paint, double light){

        if(paint instanceof  Color){
            return getLightColor((Color) paint, light);
        }else if(paint instanceof  LinearGradient){

            LinearGradient lgColor = (LinearGradient) paint;
            List<Stop> stops = lgColor.getStops();
            List<Stop> newStops = new ArrayList<Stop>();
            for(Stop stop : stops){
                Color c1 = stop.getColor().deriveColor(0, 1, 0.1, 1);
                c1 = getLightColor(c1, light);
                newStops.add(new Stop(stop.getOffset(), c1));
            }

            return new LinearGradient(
                    lgColor.getStartX(),
                    lgColor.getStartY(),
                    lgColor.getEndX(),
                    lgColor.getEndX(),
                    lgColor.isProportional(),
                    lgColor.getCycleMethod(),
                    newStops
            );

        }else if(paint instanceof  RadialGradient){

            RadialGradient lgColor = (RadialGradient) paint;
            List<Stop> stops = lgColor.getStops();
            List<Stop> newStops = new ArrayList<Stop>();
            for(Stop stop : stops){
                Color c1 = stop.getColor().deriveColor(0, 1, 0.1, 1);
                c1 = getLightColor(c1, light);
                newStops.add(new Stop(stop.getOffset(), c1));
            }

            return new RadialGradient(
                    lgColor.getFocusAngle(),
                    lgColor.getFocusDistance(),
                    lgColor.getCenterX(),
                    lgColor.getCenterY(),
                    lgColor.getRadius(),
                    lgColor.isProportional(),
                    lgColor.getCycleMethod(),
                    newStops
            );

        }
        return paint;
    }

    /**
     * 获取指定灰度的颜色，亮色
     * 1白色，0黑色.
     *
     * @param paint the paint
     * @param dark the dark
     * @return the dark paint
     */
    public static Paint getDarkPaint(Paint paint, double dark){
        if(paint instanceof  Color){
            return getDarkColor((Color) paint, dark);
        }else if(paint instanceof  LinearGradient){

            LinearGradient lgColor = (LinearGradient) paint;
            List<Stop> stops = lgColor.getStops();
            List<Stop> newStops = new ArrayList<Stop>();
            for(Stop stop : stops){
                Color c1 = getDarkColor( stop.getColor(), dark);
                newStops.add(new Stop(stop.getOffset(), c1));
            }

            return new LinearGradient(
                    lgColor.getStartX(),
                    lgColor.getStartY(),
                    lgColor.getEndX(),
                    lgColor.getEndX(),
                    lgColor.isProportional(),
                    lgColor.getCycleMethod(),
                    newStops
            );

        }else if(paint instanceof  RadialGradient){

            RadialGradient lgColor = (RadialGradient) paint;
            List<Stop> stops = lgColor.getStops();
            List<Stop> newStops = new ArrayList<Stop>();
            for(Stop stop : stops){
                Color c1 = getDarkColor( stop.getColor(), dark);
                newStops.add(new Stop(stop.getOffset(), c1));
            }

            return new RadialGradient(
                    lgColor.getFocusAngle(),
                    lgColor.getFocusDistance(),
                    lgColor.getCenterX(),
                    lgColor.getCenterY(),
                    lgColor.getRadius(),
                    lgColor.isProportional(),
                    lgColor.getCycleMethod(),
                    newStops
            );

        }
        return paint;
    }

    /**
     * 获取指定灰度的颜色，暗色
     * 1白色，0黑色.
     *
     * @param color the color
     * @param light the light
     * @return the dark color
     */
    public static Color getDarkColor(Color color, double light){

        double curr = color.grayscale().getBlue();
        if(curr<light){
            return color;
        }

        while(light<curr){
            color = deriveColor(color, -0.1);
            curr = color.grayscale().getBlue();
        }
        return color;
    }

    /**
     * 解析svg的xml文档.
     *
     * @param content the content
     * @return the array list
     */
    public static ArrayList<PathInfo> parseSvg(String content){
        ArrayList<PathInfo> list=new ArrayList<>();

        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();

        DocumentBuilder db=null;
        Document dom=null;

        try {
            StringReader sr=new StringReader(content);
            InputSource is=new InputSource(sr);
            //设置不验证
            dbf.setValidating(false);
            db=dbf.newDocumentBuilder();
            //忽略DTD文档类型定义验证
            db.setEntityResolver(new EntityResolver() {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
                }
            });

            dom=db.parse(is);
            Element item = dom.getDocumentElement();
            NodeList paths = dom.getElementsByTagName("path");
            for (int i = 0; i < paths.getLength(); i++) {
                Node node= paths.item(i);
                Element element = (Element) node;
                PathInfo info=new PathInfo();
                String pathD=element.getAttribute("d");
                //info.setPathD(ratePath(pathD,rate));
                info.setPathD(pathD);
                String pathFill = element.getAttribute("fill");
                info.setPathFill(pathFill==""?"#000000":pathFill);
                String pathID = element.getAttribute("p-id");
                //如果不存在p-id,就设置p-id
                pathID=pathID.trim().isEmpty()?String.valueOf(i):pathID;
                info.setPathId("p-id"+pathID);
                list.add(info);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 获取指定颜色的透明度
     * 1白色，0黑色.
     *
     * @param paint the paint
     * @param opacity the opacity
     * @return the opacity paint
     */
    public static Paint getOpacityPaint(Paint paint, double opacity){

        if(paint instanceof  Color){
            return getOpacityColor((Color) paint, opacity);
        }else if(paint instanceof  LinearGradient){

            LinearGradient lgColor = (LinearGradient) paint;
            List<Stop> stops = lgColor.getStops();
            List<Stop> newStops = new ArrayList<Stop>();
            for(Stop stop : stops){
                Color c1 = stop.getColor();
                newStops.add(new Stop(stop.getOffset(), getOpacityColor(c1, opacity)));
            }

            return new LinearGradient(
                    lgColor.getStartX(),
                    lgColor.getStartY(),
                    lgColor.getEndX(),
                    lgColor.getEndX(),
                    lgColor.isProportional(),
                    lgColor.getCycleMethod(),
                    newStops
            );

        }else if(paint instanceof  RadialGradient){

            RadialGradient lgColor = (RadialGradient) paint;
            List<Stop> stops = lgColor.getStops();
            List<Stop> newStops = new ArrayList<Stop>();
            for(Stop stop : stops){
                newStops.add(new Stop(stop.getOffset(), getOpacityColor(stop.getColor(), opacity)));
            }

            return new RadialGradient(
                    lgColor.getFocusAngle(),
                    lgColor.getFocusDistance(),
                    lgColor.getCenterX(),
                    lgColor.getCenterY(),
                    lgColor.getRadius(),
                    lgColor.isProportional(),
                    lgColor.getCycleMethod(),
                    newStops
            );

        }
        return paint;
    }


    /**
     * Gets the opacity color.
     *
     * @param color the color
     * @param opacity the opacity
     * @return the opacity color
     */
    public static Color getOpacityColor(Color color, double opacity){
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

}

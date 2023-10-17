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
package com.xm2013.jfx.control.date;

import com.xm2013.jfx.common.CallBack;
import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.animate.ClickAnimate;
import com.xm2013.jfx.control.animate.ClickRipperAnimate;
import com.xm2013.jfx.control.animate.ClickShadowAnimate;
import com.xm2013.jfx.control.base.BorderType;
import com.xm2013.jfx.control.base.ClickAnimateType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SkinInfo;
import com.xm2013.jfx.control.textfield.XmSimpleTextField;
import com.xm2013.jfx.control.icon.XmSVGIcon;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class XmDateSelectorSkin extends SkinBase {
    private XmSimpleTextField field;
    private XmSVGIcon icon;
    private Pane iconPane;
    private XmDateSelector control;
    private ClickAnimate iconClickAnimate;

    //组件外观信息缓存，用于动态切换组件时缓存改变之前的外观信息
    private final Map<Integer, SkinInfo> skins = new HashMap<Integer, SkinInfo>();

    private DateSelectorPopup popup;

    public XmDateSelectorSkin(XmDateSelector control) {
        super(control);
        this.control = control;

        field = new XmSimpleTextField();
        field.setEnableSkin(true);
        field.setPromptText(control.getPromptText());
        icon = new XmSVGIcon(FxKit.DATE_PATH);
        iconPane = new Pane(icon);

        iconPane.prefWidthProperty().bind(field.heightProperty());
        iconPane.prefHeightProperty().bind(field.heightProperty());
        icon.layoutXProperty().bind(Bindings.createDoubleBinding(()->(field.getHeight() - icon.prefWidth(-1))/2
                , field.heightProperty(), icon.widthProperty()));
        icon.layoutYProperty().bind(Bindings.createDoubleBinding(()->(field.getHeight() - icon.prefHeight(-1))/2
                , field.heightProperty(), icon.heightProperty()));

        getChildren().addAll(field, iconPane);

        iconPane.setFocusTraversable(true);

        updateSkin(1,0);

        field.focusedProperty().addListener(fieldStatusListener);
        field.hoverProperty().addListener(fieldStatusListener);
        field.textProperty().addListener(fieldTextListener);
        iconPane.focusedProperty().addListener(iconStatusListener);
        iconPane.hoverProperty().addListener(iconStatusListener);
        iconPane.addEventFilter(MouseEvent.MOUSE_CLICKED, iconPaneClickHandler);
        iconPane.addEventFilter(KeyEvent.KEY_RELEASED, iconPaneKeyReleaseHandler);

        //监听关闭弹窗
        this.control.getScene().getWindow().focusedProperty().addListener(windowFocusListener);
        this.control.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, windowClickFilter);
        this.control.selectedDateProperty().addListener(selectedDateListener);
        this.field.addEventFilter(KeyEvent.KEY_RELEASED, inputKeyReleaseHandler);

        registerChangeListener(control.hueTypeProperty(), e->{skins.clear(); updateSkin(1, 0);});
        registerChangeListener(control.colorTypeProperty(), e->{skins.clear(); updateSkin(1, 0);});
        registerChangeListener(control.sizeTypeProperty(), e->{skins.clear(); updateSkin(1, 0);});
        registerChangeListener(control.roundTypeProperty(), e->{skins.clear(); updateSkin(1, 0);});
        registerChangeListener(control.fillIconProperty(), e->{skins.clear(); updateSkin(1, 0);});

    }

    /* ------------------------ listener ---------------------- */

    private ChangeListener<String> fieldTextListener = (ob, ov, nv) -> {
        if(nv == null || nv.trim().length() == 0){
            this.control.setSelectedDate(null);
        }
    };

    /**
     * 日期变化监听
     */
    private ChangeListener<LocalDateTime> selectedDateListener = (ob, ov, nv)->{

        if(nv!=null){
            String pattern = control.getFormatPattern();
//            System.out.println(pattern);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            String formattedDate = nv.format(formatter);
            this.field.setText(formattedDate);
        }
    };

    /**
     * 输入日期，处理
     */
    public EventHandler<KeyEvent> inputKeyReleaseHandler = (e) ->{
        if(e.getCode() == KeyCode.ENTER){
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(control.getFormatPattern());
                LocalDateTime date = LocalDateTime.parse(this.field.getText(), formatter);
                this.control.setSelectedDate(date);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    };

    /**
     * 点击主窗体的时候，隐藏下拉框
     */
    private final EventHandler<MouseEvent> windowClickFilter = e -> {
        //判断是不是点击iconPane
        if(isControl((Node) e.getTarget(), 1) == 0){
            getPopup().hide();
        }
    };

    /**
     * 窗体失去焦点的时候，隐藏下拉弹框
     */
    private final ChangeListener<Boolean> windowFocusListener = (ob, ov, nv) -> {
        if (!nv) {
            getPopup().hide();
        }
    };

    /**
     * 播放按钮动画，显示下拉框
     */
    private EventHandler<MouseEvent> iconPaneClickHandler = (e)->{
        iconPane.requestFocus();
        getIconClickAnimate().setPoint(e.getX(), e.getY()).play();
        getPopup().setDate(control.getSelectedDate()).show(this.control);
    };

    /**
     * 按下enter, space键，出发icon的点击事件
     */
    private EventHandler<KeyEvent> iconPaneKeyReleaseHandler = e -> {
        if(iconPane.isFocused() && (e.getCode().equals(KeyCode.ENTER)|| e.getCode().equals(KeyCode.SPACE))){
            getIconClickAnimate().setPoint(iconPane.prefWidth(-1)/2, iconPane.prefHeight(-1)/2).play();
            getPopup().setDate(control.getSelectedDate()).show(this.control);
        }
    };

    /**
     * 设置外观，显示下拉框
     */
    private ChangeListener<Boolean> fieldStatusListener =  (ob, ov, nv) ->{
        if(field.isFocused()){
            updateSkin(field.isHover()?3:4, 1);
//            getPopup().setDate(control.getSelectedDate()).show(this.control);
        }else{
            updateSkin(field.isHover()?2: 1, 1);
        }
    };

    /**
     * 图标的状态变化监听
     */
    private ChangeListener<Boolean> iconStatusListener = (ob, ov, nv) -> {
        if(iconPane.isFocused()){
            updateSkin(iconPane.isHover()?3:4, 2);
        }else{
            updateSkin(iconPane.isHover()?2: 1, 2);
        }
    };

    /* --------------------------- override ---------------------------- */
    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double width = field.prefWidth(-1) + field.prefHeight(-1);
        Insets padding = control.getPadding();
        width += padding.getLeft() + padding.getRight();
        return width;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {

        double fieldWidth = field.prefWidth(-1),
                fieldHeight = field.prefHeight(-1);
        double iconPaneX = contentX + fieldWidth,
                iconPaneY = (contentHeight - fieldHeight)/2;

        field.setPrefWidth(contentWidth-fieldHeight);
        layoutInArea(field, contentX, contentY, contentWidth-fieldHeight, fieldHeight, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(iconPane, iconPaneX, iconPaneY, fieldHeight, fieldHeight, 0, HPos.CENTER, VPos.CENTER);

    }

    @Override
    public void dispose() {
        super.dispose();

        iconPane.prefWidthProperty().unbind();
        iconPane.prefHeightProperty().unbind();
        icon.layoutXProperty().unbind();
        icon.layoutYProperty().unbind();

        field.focusedProperty().removeListener(fieldStatusListener);
        field.hoverProperty().removeListener(fieldStatusListener);
        iconPane.focusedProperty().removeListener(iconStatusListener);
        iconPane.hoverProperty().removeListener(iconStatusListener);
        iconPane.removeEventFilter(MouseEvent.MOUSE_CLICKED, iconPaneClickHandler);

        this.control.getScene().getWindow().focusedProperty().removeListener(windowFocusListener);
        this.control.getScene().removeEventFilter(MouseEvent.MOUSE_PRESSED, windowClickFilter);
        this.control.selectedDateProperty().removeListener(selectedDateListener);
        this.field.removeEventFilter(KeyEvent.KEY_RELEASED, inputKeyReleaseHandler);

        unregisterChangeListeners(control.hueTypeProperty());
        unregisterChangeListeners(control.colorTypeProperty());
        unregisterChangeListeners(control.sizeTypeProperty());
        unregisterChangeListeners(control.roundTypeProperty());
        unregisterChangeListeners(control.fillIconProperty());
    }

    /* ----------------------- method ----------------------------- */

    /**
     * 获取点击动画
     * @return
     */
    private ClickAnimate getIconClickAnimate(){
        if(iconClickAnimate == null){

            ClickAnimateType type = control.getClickAnimateType();
            if(ClickAnimateType.SHADOW.equals(type)){
                iconClickAnimate = new ClickShadowAnimate(iconPane, control.getColorType());
            }else if(ClickAnimateType.RIPPER.equals(type)){
                iconClickAnimate = new ClickRipperAnimate(iconPane, control.getColorType(), control.getHueType());
            }

            //设置阴影的动画节点所在的位置
            iconClickAnimate.setNodePosition((node, width, height, x, y) -> {

                Insets margin = control.getMargin();
                double left = 0, top = 0;
                if(margin!=null){
                    left = margin.getLeft();
                    top = margin.getTop();
                }

                double w = field.prefWidth(-1);

                left += w;

//                System.out.println(String.format("left:%f, top:%f, height:%f, width:%f", left, top, iconPane.minWidth(-1), valuePane.prefHeight(-1) ));
                layoutInArea(node, left, top, iconPane.prefWidth(-1), iconPane.prefHeight(-1),
                        0, HPos.CENTER, VPos.CENTER);

            });

            //将动画节点添加控件中
            iconClickAnimate.setAddNode(node -> {
                if(ClickAnimateType.SHADOW.equals(type)){
                    getChildren().add(0, node);
                }else if(ClickAnimateType.RIPPER.equals(type)){
                    //node.setStyle("-fx-background-color: red;");
                    getChildren().add(node);
                }

            });

            //动画播放完成后，将动画节点从控件中移除
            iconClickAnimate.setRemoveNode(node -> getChildren().remove(node));
        }
        return iconClickAnimate;
    }

    /**
     * 获取弹出框
     * @return DateSelectorPopup
     */
    public DateSelectorPopup getPopup(){
        if(popup == null){
            popup = new DateSelectorPopup(control.colorTypeProperty(), control.dateTypeProperty());
            popup.setSelectedCallback(new CallBack<LocalDateTime>() {
                @Override
                public void call(LocalDateTime localDateTime) {
                    control.setSelectedDate(localDateTime);
                }
            });
        }
        return popup;
    }

    /**
     * 判断是不是组件里面的内容
     * @param node Node
     * @param index int
     * @return int
     */
    public int isControl(Node node, int index){

        if(node == null || index == 4) return 0;

        if(node.equals(iconPane)){
            return 1;
        }else if(node.equals(field)){
            return 1;
        }else if(node.equals(control)){
            return 1;
        }else{
            return isControl(node.getParent(), ++index);
        }
    }

    /**
     * 设置外观颜色
     * status: 1 ： 默认状态下的颜色
     * status: 2:  hover|out focus
     * status: 3 : hover|focus状态下的颜色
     * status: 4 : out hover | focus状态的颜色
     * controlIndex: 0: all, 1: text, 2: icon
     */
    private void updateSkin(int status, int controlIndex){
        BorderType borderType = control.getBorderType();
        SkinInfo info = getSkinInfo(status);

        Paint borderOutColor = info.getBorderOutColor(),
                backgroundColor = info.getBackgroundColor(),
                borderColor = info.getInnerBorderColor(),
                fontColor;

        double radii = info.getRadiusWidth(),
                widths1 = info.getOuterBorderWidth(),
                widths2 = info.getInnerBorderWidth();

        Insets insets1 = info.getOuterBorderInsets(),
                insets2 = info.getInnerBorderInsets();

        boolean isArrowFill = control.getFillIcon();

        //构建边框
        Border valueBorder = null, iconBorder = null;
        if(BorderType.FULL.equals(borderType)){
            //外边框,
            BorderStroke stroke1 = new BorderStroke(
                    borderOutColor, BorderStrokeStyle.SOLID,
                    new CornerRadii(radii, 0, 0, radii, false),
                    new BorderWidths(widths1), insets1);
            BorderStroke stroke2 = new BorderStroke(borderOutColor, BorderStrokeStyle.SOLID,
                    new CornerRadii(0, radii, radii, 0, false),
                    new BorderWidths(widths1), insets1);

            //内边框
            Paint rightColor = borderColor;
            if(HueType.LIGHT.equals(control.getHueType())){
                rightColor = FxKit.getLightPaint(control.getColorType().getPaint(), 0.5);
            }

            if(status == 1){
                borderColor = Color.web("#999999");
            }

            BorderStroke stroke11 = new BorderStroke(borderColor, BorderStrokeStyle.SOLID,
                    new CornerRadii(radii, 0, 0, radii, false),
                    new BorderWidths(widths2), insets2);
            BorderStroke stroke21 = new BorderStroke(
                    borderColor, borderColor, borderColor, rightColor,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,BorderStrokeStyle.SOLID,BorderStrokeStyle.SOLID,
                    new CornerRadii(0, radii, radii, 0, false),
                    new BorderWidths(widths2, widths2, widths2, 0), insets2);

            valueBorder = new Border(stroke1, stroke11);
            iconBorder = new Border(stroke2, stroke21);
        }

        if(!isArrowFill){
            backgroundColor = Color.TRANSPARENT;
            fontColor = borderColor;
        }else{
            fontColor = Color.WHITE;
        }

        //构建背景色背景色
        BackgroundFill fill2 = new BackgroundFill(backgroundColor, new CornerRadii(0, radii, radii, 0, false),
                new Insets(0,0,0,0));
        Background background2 = new Background(fill2);

        //设置
        if(controlIndex == 1){
            //设置
            field.setBorder(valueBorder);
            field.setPadding(info.getPadding());
            field.setFont(Font.font(field.getFont().getFamily(), info.getFontSize()));
        }else if(controlIndex == 2){
            setArrowBtnColor(background2, iconBorder, fontColor);
        }else{
            field.setBorder(valueBorder);
            field.setPadding(info.getPadding());
            field.setFont(Font.font(field.getFont().getFamily(), info.getFontSize()));
            icon.setSize(info.getFontSize()-2);
            setArrowBtnColor(background2, iconBorder, fontColor);
        }
    }

    /**
     * 获取外观样式信息
     * @param status
     * @return
     */
    private SkinInfo getSkinInfo(int status){
        SkinInfo info = skins.get(status);
        if(info == null){
            info = new SkinInfo(control.getColorType(), control.getSizeType(), control.getRoundType(), control.getHueType(), control.getBorderType());
            info.compute(status);
            skins.put(status, info);
        }
        return info;
    }

    /**
     * 设置下拉按钮图标样式
     * @param bg
     * @param border
     * @param fontColor
     */
    private void setArrowBtnColor(Background bg, Border border, Paint fontColor){
        iconPane.setBackground(bg);
        iconPane.setBorder(border);
        icon.setColor(fontColor);
    }

}

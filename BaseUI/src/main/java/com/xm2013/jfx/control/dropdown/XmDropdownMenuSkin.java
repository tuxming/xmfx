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
package com.xm2013.jfx.control.dropdown;

import com.xm2013.jfx.common.CallBack;
import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.animate.ClickAnimate;
import com.xm2013.jfx.control.animate.ClickRipperAnimate;
import com.xm2013.jfx.control.animate.ClickShadowAnimate;
import com.xm2013.jfx.control.base.BorderType;
import com.xm2013.jfx.control.base.ClickAnimateType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SkinInfo;
import com.xm2013.jfx.control.icon.XmIcon;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class XmDropdownMenuSkin extends SkinBase<XmDropdownMenu> {
    public static final EventType<Event> ON_SHOWING = new EventType<Event>(Event.ANY, "DROPDOWN_MENU_SHOWING");
    public static final EventType<Event> ON_HIDING = new EventType<Event>(Event.ANY, "DROPDOWN_MENU_HIDING");

    private final Text label = new Text(); //显示文本
    private HBox textPane = null;  //文本容器
    private HBox iconPane = null;  //图标容器

    //组件类
    private XmDropdownMenu control;

    //组件外观信息缓存，用于动态切换组件时缓存改变之前的外观信息
    private Map<Integer, SkinInfo> skins = new HashMap<Integer, SkinInfo>();


    //下拉框显示的Y坐标值
//    private DoubleProperty popupY = new SimpleDoubleProperty(0);
    private DropdownMenuPopup popup;
    private ClickAnimate textClickAnimate;
    private ClickAnimate iconClickAnimate;

    protected XmDropdownMenuSkin(XmDropdownMenu control) {
        super(control);
        this.control = control;

        //初始化组件
        label.getStyleClass().add(".xm-dropdown-menu-label");
        label.textProperty().bind(control.textProperty());
        label.setTextOrigin(VPos.TOP);
        textPane = new HBox(label);
        textPane.setAlignment(Pos.CENTER);
        iconPane = new HBox(control.getIcon());
        iconPane.setAlignment(Pos.CENTER);
        textPane.getStyleClass().add(".xm-dropdown-menu-icon");

        textPane.setFocusTraversable(true);
        iconPane.setFocusTraversable(true);
//        textPane.paddingProperty().bind(control.paddingProperty());
//        iconPane.paddingProperty().bind(control.paddingProperty());

        this.getChildren().addAll(textPane, iconPane);

        //设置外观
        updateSkin();
        updateColor(1, 0);
        if (control.isShowing()) {
            showPop();
        }

        //添加监听 / 事件
        control.colorTypeProperty().addListener(skinChangeListener);
        control.sizeTypeProperty().addListener(skinChangeListener);
        control.roundTypeProperty().addListener(skinChangeListener);
        control.clickAnimateTypeProperty().addListener(skinChangeListener);
        control.hueTypeProperty().addListener(skinChangeListener);
        control.borderTypeProperty().addListener(skinChangeListener);
        control.useGroupProperty().addListener(skinChangeListener);

        textPane.focusedProperty().addListener(focusTextListenter);
        textPane.hoverProperty().addListener(hoverTextChangeListener);

        iconPane.focusedProperty().addListener(focusIconListenter);
        iconPane.hoverProperty().addListener(hoverIconChangeListener);

        textPane.addEventFilter(MouseEvent.MOUSE_CLICKED, textPaneClick);
        textPane.addEventFilter(MouseEvent.MOUSE_PRESSED, textPanePress);
        textPane.addEventFilter(MouseEvent.MOUSE_RELEASED, textPaneRelease);

        control.getItems().addListener(itemListChangeListener);

        this.control.getScene().getWindow().focusedProperty().addListener(windowFocusListener);
        this.control.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, windowClickFilter);
//        this.control.showingProperty().addListener(showListener);

        this.control.addEventFilter(ON_SHOWING, showHandler);
        this.control.addEventFilter(ON_HIDING, hideHandler);
        this.control.addEventFilter(KeyEvent.KEY_RELEASED, keyEventHandler);

    }

    /* --------------------- event / listener --------------------------- */
    private ChangeListener<Boolean> popupShowListener =(ob, ov, nv) -> {
        control.showingProperty().set(nv);
    };

    private ChangeListener<Object> skinChangeListener = (ob, ov, nv) -> {
        if(nv!=null){
            if(popup!=null){
                popup.dispose();
                popup.showProperty().removeListener(popupShowListener);
            }
            popup = null;

            if(textClickAnimate!=null){
                textClickAnimate.dispose();
                textClickAnimate = null;
            }

            if(iconClickAnimate!=null){
                iconClickAnimate.dispose();
                iconClickAnimate = null;
            }
            updateIcon = false;
            iconPane.setPadding(Insets.EMPTY);
            skins.clear();
            updateSkin();
            updateColor(1, 0);
        }
    };

    private final EventHandler<Event> showHandler = e->{
//        System.out.println("show");
        showPop();
    };

    private final EventHandler<Event> hideHandler = e-> {
//        System.out.println("hide");
        hidePop();
    };

    /**
     * 点击主窗体的时候，隐藏下拉框
     */
    private final EventHandler<MouseEvent> windowClickFilter = e -> {

        //判断是不是点击iconPane
        boolean isIconPane = findIconPane((Node) e.getTarget());

        if(isIconPane){

            //这里是监听的windowListener，所有x，y坐标是window的位置，需要获取节点的位置并设置
            Bounds nodeBounds = iconPane.localToScreen(iconPane.getBoundsInLocal());

            double x = e.getScreenX() - nodeBounds.getMinX();
            double y = e.getScreenY() - nodeBounds.getMinY();

            getIconClickAnimate().setPoint(x, y).play();

            iconPane.requestFocus();
            if(TriggerType.CLICK.equals(control.getTriggerType())){
                if(control.isShowing()){
                    hidePop();
                }else{
                    showPop();
                }
            }
        }else{
            hidePop();
        }
    };

    /**
     * 当按下空格键或者enter键，出发类似于点击的时间
     */
    private final EventHandler<? super KeyEvent> keyEventHandler = e->{

        if(e.getCode().equals(KeyCode.ENTER)|| e.getCode().equals(KeyCode.SPACE)){
            if(textPane.isFocused()){

                double x = textPane.prefWidth(-1)/2;
                double y = textPane.prefHeight(-1)/2;

                Point2D point = textPane.localToScreen(x, y);

                Event.fireEvent(textPane, new MouseEvent(
                        MouseEvent.MOUSE_CLICKED,
                        x, y, point.getX(), point.getY(), null,
                        1,
                        false, false,false,false,false,false,false,false,false,false,
                        new PickResult(textPane, new Point3D(x, y, 0d), 0)
                ));

            }else if(iconPane.isFocused()){
                getIconClickAnimate()
                        .setPoint(iconPane.prefWidth(-1)/2, iconPane.prefHeight(-1)/2)
                        .play();
                if(TriggerType.CLICK.equals(control.getTriggerType())){
                    if(control.isShowing()){
                        hidePop();
                    }else{
                        showPop();
                    }
                }

            }
        }
    };

    private boolean findIconPane(Node node){
        if(node == null) return false;
        return node.equals(iconPane) || findIconPane(node.getParent());
    }

    /**
     * 窗体失去焦点的时候，隐藏下拉弹框
     */
    private final ChangeListener<Boolean> windowFocusListener = (ob, ov, nv) -> {
        if (!nv) {
            hidePop();
        }
    };

    /**
     * 下拉列表数据发生变化的时候，重构下拉列表
     */
    private final ListChangeListener<DropdownMenuItem> itemListChangeListener = new ListChangeListener<>() {
        @Override
        public void onChanged(Change<? extends DropdownMenuItem> c) {
            control.selectedItemProperty().unbind();
            popup.dispose();
            popup = null;
        }
    };

    /**
     * 点击文本的时候，回调事件，并且获取焦点
     */
    private EventHandler<MouseEvent> textPaneClick = e -> {
        CallBack<MouseEvent> callback = control.getClickCallback();
        if(callback != null){
            callback.call(e);
        }
        textPane.requestFocus();
        getTextClickAnimate().setPoint(e.getX(), e.getY()).play();
    };

    private EventHandler<MouseEvent> textPanePress = e-> {
        label.setScaleX(0.95);
        label.setScaleY(0.95);
    };
    private EventHandler<MouseEvent> textPaneRelease = e-> {
        label.setScaleX(1);
        label.setScaleY(1);
    };

    /**
     * focus监听
     */
    private ChangeListener<Boolean> focusTextListenter = (ob, ov, nv) -> {
        /*
         * status: 1 ： 默认状态下的颜色
         * status: 2:  hover-out focus
         * status: 3 : hover-focus状态下的颜色
         * status: 4 : out-hover focus状态的颜色
         */
        updateColor(nv?(textPane.isHover()?3:4):(textPane.isHover()?2:1), 1);
        updateColor(1, 2);
    };
    /**
     * focus监听
     */
    private ChangeListener<Boolean> focusIconListenter = (ob, ov, nv) -> {
        /*
         * status: 1 ： 默认状态下的颜色
         * status: 2:  hover-out focus
         * status: 3 : hover-focus状态下的颜色
         * status: 4 : out-hover focus状态的颜色
         */
        updateColor(nv?(iconPane.isHover()?3:4):(iconPane.isHover()?2:1), 2);
        updateColor(1, 1);
    };

    /**
     * hover属性变化监听
     */
    private ChangeListener<Boolean> hoverTextChangeListener = (ob, ov, nv) -> {

        /*
         * status: 1 ： 默认状态下的颜色
         * status: 2:  hover-out focus
         * status: 3 : hover-focus状态下的颜色
         * status: 4 : out-hover focus状态的颜色
         */

        if (nv) {
            if (textPane.isFocused()) {
                updateColor(3,1);
            } else {
                updateColor(2,1);
            }
        } else {
            if (textPane.isFocused()) {
                updateColor(4,1);
            } else {
                updateColor(1,1);
            }
        }

        if(iconPane.isFocused() && !textPane.isFocused()){
            updateColor(4, 2);
        }else{
            updateColor(1, 2);
        }


    };
    /**
     * hover属性变化监听
     */
    private ChangeListener<Boolean> hoverIconChangeListener = (ob, ov, nv) -> {

        /*
         * status: 1 ： 默认状态下的颜色
         * status: 2:  hover-out focus
         * status: 3 : hover-focus状态下的颜色
         * status: 4 : out-hover focus状态的颜色
         */

        if (nv) {
            if (iconPane.isFocused()) {
                updateColor(3, 2);
            } else {
                updateColor(2, 2);
            }

//            System.out.println("trigger:"+control.getTriggerType());
            if(TriggerType.HOVER.equals(control.getTriggerType())){
                showPop();
            }
        } else {
            if (iconPane.isFocused()) {
                updateColor(4, 2);
            } else {
                updateColor(1, 2);
            }
        }

        if(textPane.isFocused() && !iconPane.isFocused()){
            updateColor(4, 1);
        }else{
            updateColor(1, 1);
        }
    };

    /* ------------------------------- methods ---------------------------- */

    private ClickAnimate getTextClickAnimate(){
        if(textClickAnimate == null){

            ClickAnimateType type = control.getClickAnimateType();
            if(ClickAnimateType.SHADOW.equals(type)){
                textClickAnimate = new ClickShadowAnimate(textPane, control.getColorType());
            }else if(ClickAnimateType.RIPPER.equals(type)){
                textClickAnimate = new ClickRipperAnimate(textPane, control.getColorType(), control.getHueType());
            }

            textClickAnimate.setNodePosition((node, width, height, x, y) -> {

                Insets margin = control.getMargin();
                double left = 0, top = 0;
                if(margin!=null){
                    left = margin.getLeft();
                    top = margin.getTop();
                }
                layoutInArea(node, left, top, textPane.prefWidth(-1), textPane.prefHeight(-1),
                        0, HPos.CENTER, VPos.CENTER);

            });

            textClickAnimate.setAddNode(node -> {
                if(ClickAnimateType.SHADOW.equals(type)){
                    getChildren().add(0, node);
                }else if(ClickAnimateType.RIPPER.equals(type)){
                    getChildren().add(node);
                }

            });

            textClickAnimate.setRemoveNode(node -> getChildren().remove(node));
        }
        return textClickAnimate;
    }

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

                left += textPane.prefWidth(-1);

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
     * 显示下拉框
     */
    private void showPop(){
        if(popup == null){
            popup = new DropdownMenuPopup(control.getItems(), control.getColorType(), control.getSizeType(), control.isUseGroup());
            control.selectedItemProperty().bind(popup.selectedItemProperty());

            popup.showProperty().addListener(popupShowListener);

            CallBack<DropdownMenuItem> selectedCallback = control.getSelectedCallback();
            if(selectedCallback != null) popup.setSelectedCallback(selectedCallback);
        }

//        Bounds layoutBounds = control.getLayoutBounds();
//        Window window = control.getScene().getWindow();

//        //由于设置了dropShadow, 会导致弹窗的尺寸发生变化，因此需要添加偏移量，
//        double x = window.getX()+layoutBounds.getMinX()+control.getMargin().getLeft()-21+control.getOffset().getX();
//        double y = window.getY()+layoutBounds.getMaxY()+control.getMargin().getBottom()-15+control.getOffset().getY();
        popup.doShow(control, control.prefWidth(-1), -1, true, 0, 15);
        control.showingProperty().set(true);
    }

    /**
     * 隐藏下拉框
     */
    private void hidePop(){
        if(popup != null && popup.isShow()){
            popup.doHide();
            control.showingProperty().set(false);
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
     * 设置外观
     */
    private void updateSkin(){
        updateIcon = false;
        //更新尺寸
        SkinInfo info = getSkinInfo(1);
        Font font = control.getFont();
        Insets padding = info.getPadding();
        double fontSize = info.getFontSize();

        //isAutoTextSize 默认是开启的，意思就跟随设置，如果是关闭，则文字的大小需要自己设置
        if(control.isAutoTextSize()){
            label.setFont(Font.font(font.getFamily(), fontSize));
        }else{
            label.setFont(font);
        }

        //isAutoGraphicSize默认是开启的，默认是开启的，意思就跟随设置，如果是关闭，则图标的大小需要自己设置
        //这里图标是使用的XmIcon，如果是想使用自定义图标，见实现XmIcon接口，不然图标的大小不会根据设置而发生变化
        if(control.isAutoGraphicSize()){
            Node icon = control.getIcon();
            if(icon instanceof XmIcon){
                ((XmIcon) icon).setSize(fontSize+2);
            }
        }

        //为了保证高度一致，这里重置icon的padding
        if(control.isAutoPadding()){
            textPane.setPadding(new Insets(padding.getTop(), padding.getRight(), padding.getBottom(), padding.getLeft()));
        }else{
            textPane.setPadding(control.getPadding());
        }

    }

    /**
     * status: 1 ： 默认状态下的颜色
     * status: 2:  hover-out focus
     * status: 3 : hover-focus状态下的颜色
     * status: 4 : out-hover focus状态的颜色
     * controlIndex: 0: all, 1: text, 2: icon
     */
    private void updateColor(int status, int controlIndex){

        BorderType borderType = control.getBorderType();
        SkinInfo info = getSkinInfo(status);

        Paint borderOutColor = info.getBorderOutColor(),
                backgroundColor = info.getBackgroundColor(),
                borderColor = info.getInnerBorderColor(),
                fontColor = info.getFontColor();

        double radii = info.getRadiusWidth(),
                widths1 = info.getOuterBorderWidth(),
                widths2 = info.getInnerBorderWidth();

        Insets insets1 = info.getOuterBorderInsets(),
                insets2 = info.getInnerBorderInsets();


        //构建边框
        Border textBorder = null, iconBorder = null;
        if(BorderType.FULL.equals(borderType)){
            //外边框
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
                rightColor = FxKit.derivePaint(control.getColorType().getPaint(), 0.5);
            }

            BorderStroke stroke11 = new BorderStroke(borderColor, BorderStrokeStyle.SOLID,
                    new CornerRadii(radii, 0, 0, radii, false),
                    new BorderWidths(widths2, 0, widths2, widths2), insets2);
            BorderStroke stroke21 = new BorderStroke(
                    borderColor, borderColor, borderColor, rightColor,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,BorderStrokeStyle.SOLID,BorderStrokeStyle.SOLID,
                    new CornerRadii(0, radii, radii, 0, false),
                    new BorderWidths(widths2), insets2);
            textBorder = new Border(stroke1, stroke11);
            iconBorder = new Border(stroke2, stroke21);
        }

        //构建背景色背景色
        BackgroundFill fill1 = new BackgroundFill(backgroundColor, new CornerRadii(radii, 0, 0, radii, false),
                new Insets(0,0,0,0));
        Background background1 = new Background(fill1);

        BackgroundFill fill2 = new BackgroundFill(backgroundColor, new CornerRadii(0, radii, radii, 0, false),
                new Insets(0,0,0,0));
        Background background2 = new Background(fill2);

        //设置
        if(controlIndex == 1){
            //设置
            textPane.setBackground(background1);
            textPane.setBorder(textBorder);
            label.setFill(fontColor);
        }else if(controlIndex == 2){
            iconPane.setBackground(background2);
            iconPane.setBorder(iconBorder);
            Node icon = control.getIcon();
            if(icon instanceof XmIcon){
                ((XmIcon) icon).setColor(fontColor);
            }
        }else{
            textPane.setBackground(background1);
            textPane.setBorder(textBorder);

            iconPane.setBackground(background2);
            iconPane.setBorder(iconBorder);

            label.setFill(fontColor);
            Node icon = control.getIcon();
            if(icon instanceof XmIcon){
                ((XmIcon) icon).setColor(fontColor);
            }
        }
    }

    /* ------------------------------------------ override ----------------------------- */

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double width = super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);

        Insets margin = control.getMargin();

//        double width1 = textPane.prefWidth(-1) + iconPane.prefWidth(-1);

        if(margin!=null){
//            width1 += margin.getLeft()+margin.getRight();
            width += margin.getLeft()+margin.getRight();
        }

//        width1 += leftInset+rightInset;
        width += leftInset+rightInset;

//        return Math.max(width, width1);
        return width;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double height = super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);

//        double height1 = Math.max( textPane.prefHeight(-1) , iconPane.prefHeight(-1));

        Insets margin = control.getMargin();
        if(margin!=null){
            height += margin.getTop() + margin.getBottom();
        }

        height += topInset + bottomInset;

//        System.out.println("height, height1 = "+height +", "+height1);

//        return Math.max(height1, height);
        return height;

    }

    private boolean updateIcon = false;
    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
//        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);

        double tX=0, tY=0, tW = textPane.prefWidth(-1), tH = textPane.prefHeight(-1);
        double iX=0, iY=0, iW = iconPane.prefWidth(-1), iH = iconPane.prefHeight(-1);

        double height = Math.max(control.prefHeight(-1), contentHeight)
//                ,width = Math.max(control.prefWidth(-1), contentWidth)
                ;

        double maxHeight = Math.max(tH, iH);

        Insets margin = control.getMargin();
        if(margin!=null){
            tX += margin.getLeft();
        }
        tY = (height-tH) / 2;

        iX = tX + tW;
        iY = (height - tH) / 2;

        layoutInArea(textPane, tX, tY, tW, maxHeight, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(iconPane, iX, iY, iW, maxHeight, 0, HPos.CENTER, VPos.CENTER);

        if(tH > iH && !updateIcon){
            double top = (tH - iH) /2;
            double left = (tH - control.getIcon().prefWidth(-1))/2;
            updateIcon = true;
            iconPane.setPadding(new Insets(top, left, top, left));
        }
    }

    @Override
    public void dispose() {

        control.colorTypeProperty().removeListener(skinChangeListener);
        control.sizeTypeProperty().removeListener(skinChangeListener);
        control.roundTypeProperty().removeListener(skinChangeListener);
        control.clickAnimateTypeProperty().removeListener(skinChangeListener);
        control.hueTypeProperty().removeListener(skinChangeListener);
        control.borderTypeProperty().removeListener(skinChangeListener);
        control.useGroupProperty().removeListener(skinChangeListener);

        textPane.focusedProperty().removeListener(focusTextListenter);
        textPane.hoverProperty().removeListener(hoverTextChangeListener);
        iconPane.focusedProperty().removeListener(focusIconListenter);
        iconPane.hoverProperty().removeListener(hoverIconChangeListener);
        textPane.removeEventFilter(MouseEvent.MOUSE_CLICKED, textPaneClick);
        textPane.removeEventFilter(MouseEvent.MOUSE_PRESSED, textPanePress);
        textPane.removeEventFilter(MouseEvent.MOUSE_RELEASED, textPaneRelease);
        control.getItems().removeListener(itemListChangeListener);
        this.control.getScene().getWindow().focusedProperty().removeListener(windowFocusListener);
        this.control.getScene().removeEventFilter(MouseEvent.MOUSE_CLICKED, windowClickFilter);
        this.control.removeEventFilter(ON_SHOWING, showHandler);
        this.control.removeEventFilter(ON_HIDING, hideHandler);
        this.control.removeEventFilter(KeyEvent.KEY_RELEASED, keyEventHandler);

        super.dispose();
    }
}

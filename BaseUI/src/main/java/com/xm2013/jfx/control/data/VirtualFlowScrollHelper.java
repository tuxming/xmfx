package com.xm2013.jfx.control.data;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.ColorType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.layout.*;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;

/**
 * 修改VirtualFlow的scrollbar皮肤
 */
public class VirtualFlowScrollHelper {

    private static String H_INCREMENT_PATH = "M10,0L10,6L15,3Z",
            H_DECREMENT_PATH = "M0,3L5,6L5,0Z",
            V_INCREMENT_PATH = "M0,0L10,0L5,3Z",
            V_DECREMENT_PATH = "M0,3L10,3L5,0Z";

    private ScrollBar vbar, hbar;
    private Region vbarBackground, hbarBackground, vbarTrack, hbarTrack;
    private Region vbarIncrementBtn, vbarIncrementArrow, hbarIncrementBtn, hbarIncrementArrow,
            vbarDecrementBtn, vbarDecrementArrow, hbarDecrementBtn, hbarDecrementArrow;
    private StackPane vbarThumb, hbarThumb;

    private ObjectProperty<ColorType> colorType = new SimpleObjectProperty<ColorType>(ColorType.primary());
    public ColorType getColorType() {
        return colorType.get();
    }
    public ObjectProperty<ColorType> colorTypeProperty() {
        return colorType;
    }
    public void setColorType(ColorType colorType) {
        this.colorType.set(colorType);
    }

    public VirtualFlowScrollHelper(VirtualFlow flow){
        getBar(flow);
        updateSkin();

        colorType.addListener(propertyChangeListener);

    }

    private ChangeListener<Object> propertyChangeListener = (ob, ov, nv) ->{
        updateSkin();
    };

    /**
     * 获取虚拟滚动条,
     */
    private void getBar(VirtualFlow flow){
        ObservableList<Node> flowChildren = flow.getChildrenUnmodifiable();
        for (Node flowChild : flowChildren) {
            if(flowChild instanceof ScrollBar){
                ScrollBar bar = (ScrollBar) flowChild;
                Orientation orientation = bar.getOrientation();
                if(Orientation.VERTICAL.equals(orientation)){
                    vbar = bar;
                }else if(Orientation.HORIZONTAL.equals(orientation)){
                    hbar = bar;
                }
            }
        }
        getBarChildren();
    }

    /**
     * 获取滚动条的字子节点
     */
    private void getBarChildren(){
        hbar.setStyle("-fx-background-color: transparent;");
        vbar.setStyle("-fx-background-color: transparent;");
        ObservableList<Node> vbarChildren = vbar.getChildrenUnmodifiable();
        for (Node vbarChild : vbarChildren) {
            ObservableList<String> styleClass = vbarChild.getStyleClass();
            if(styleClass.contains("track-background")){
                vbarBackground = (Region) vbarChild;
            }else if(styleClass.contains("increment-button")){
                vbarIncrementBtn = (Region) vbarChild;
                vbarIncrementArrow = (Region) vbarIncrementBtn.getChildrenUnmodifiable().get(0);
                SVGPath incPath = new SVGPath();
                incPath.setContent(V_INCREMENT_PATH);
                vbarIncrementArrow.setShape(incPath);
                vbarIncrementBtn.setStyle("-fx-background-color: transparent;");

            }else if(styleClass.contains("decrement-button")){
                vbarDecrementBtn = (Region) vbarChild;
                vbarDecrementArrow = (Region) vbarDecrementBtn.getChildrenUnmodifiable().get(0);
                SVGPath decPath = new SVGPath();
                decPath.setContent(V_DECREMENT_PATH);
                vbarDecrementArrow.setShape(decPath);
                vbarDecrementBtn.setStyle("-fx-background-color: transparent;");

            }else if(styleClass.contains("track")){
                vbarTrack = (Region) vbarChild;
                vbarChild.setStyle("-fx-background-color: transparent;");
            }else if(styleClass.contains("thumb")){
                vbarThumb = (StackPane) vbarChild;
            }
        }

        ObservableList<Node> hbarChildren = hbar.getChildrenUnmodifiable();
        for (Node hbarChild : hbarChildren) {
            ObservableList<String> styleClass = hbarChild.getStyleClass();
            if(styleClass.contains("track-background")){
                hbarBackground = (Region) hbarChild;
            }else if(styleClass.contains("increment-button")){
                hbarIncrementBtn = (Region) hbarChild;
                hbarIncrementArrow = (Region) hbarIncrementBtn.getChildrenUnmodifiable().get(0);
                SVGPath incPath = new SVGPath();
                incPath.setContent(H_INCREMENT_PATH);
                hbarIncrementArrow.setShape(incPath);
                hbarIncrementBtn.setStyle("-fx-background-color: transparent;");

            }else if(styleClass.contains("decrement-button")){
                hbarDecrementBtn = (Region) hbarChild;
                hbarDecrementArrow = (Region) hbarDecrementBtn.getChildrenUnmodifiable().get(0);
                SVGPath decPath = new SVGPath();
                decPath.setContent(H_DECREMENT_PATH);
                hbarDecrementArrow.setShape(decPath);
                hbarDecrementBtn.setStyle("-fx-background-color: transparent;");

            }else if(styleClass.contains("track")){
                hbarTrack = (Region) hbarChild;
                hbarChild.setStyle("-fx-background-color: transparent;");
            }else if(styleClass.contains("thumb")){
                hbarThumb = (StackPane) hbarChild;
            }
        }
    }

    private ArrowBtnHoverListener hbarIncrementBtnHoverListener;
    private ArrowBtnHoverListener hbarDecrementBtnHoverListener;
    private ArrowBtnHoverListener vbarIncrementBtnHoverListener;
    private ArrowBtnHoverListener vbarDecrementBtnHoverListener;
    private ThumbHoverListener hbarThumbHoverListener;
    private ThumbHoverListener vbarThumbHoverListener;

    private void updateSkin() {

        if(hbarIncrementBtnHoverListener!=null && hbarIncrementBtn!=null){
            hbarIncrementBtn.hoverProperty().removeListener(hbarIncrementBtnHoverListener);
        }

        if(hbarDecrementBtnHoverListener!=null && hbarDecrementBtn!=null){
            hbarDecrementBtn.hoverProperty().removeListener(hbarDecrementBtnHoverListener);
        }

        if(vbarIncrementBtnHoverListener!=null && vbarIncrementBtn!=null){
            vbarIncrementBtn.hoverProperty().removeListener(vbarIncrementBtnHoverListener);
        }

        if(vbarDecrementBtnHoverListener!=null && vbarDecrementBtn!=null){
            vbarDecrementBtn.hoverProperty().removeListener(vbarDecrementBtnHoverListener);
        }

        if(hbarThumbHoverListener!=null && hbarThumb!=null){
            hbarThumb.hoverProperty().removeListener(hbarThumbHoverListener);
        }

        if(vbarThumbHoverListener!=null && vbarThumb!=null){
            vbarThumb.hoverProperty().removeListener(vbarThumbHoverListener);
        }

        Paint paint = colorType.get().getPaint(),
                scrollBarArrowColor = FxKit.getOpacityPaint(paint, 0.5),
                scrollBarArrowHoverColor = FxKit.getOpacityPaint(paint, 0.85),
                scrollBarBackgroundColor = FxKit.getOpacityPaint(paint, 0.1),
                scrollBarThumbHoverColor =  FxKit.getOpacityPaint(paint, 0.85),
                scrollBarThumbColor = FxKit.getOpacityPaint(paint, 0.5)
        ;

        Background btnBackground = new Background(new BackgroundFill(scrollBarArrowColor, new CornerRadii(0), Insets.EMPTY));
        Background btnHoverBackground = new Background(new BackgroundFill(scrollBarArrowHoverColor, new CornerRadii(0), Insets.EMPTY));
        Background trackBackground = new Background(new BackgroundFill(scrollBarBackgroundColor, new CornerRadii(0), Insets.EMPTY));

//        Insets ins = new Insets(0,2,0,2);
//        Background vbarThumbBackground = new Background(new BackgroundFill(scrollBarThumbColor, new CornerRadii(10), ins));
//        Background vbarThumbHoverBackground = new Background(new BackgroundFill(scrollBarThumbHoverColor, new CornerRadii(10), ins));
//
//        Insets ins1 = new Insets(2,0,2,0);
//        Background hbarThumbBackground = new Background(new BackgroundFill(scrollBarThumbColor, new CornerRadii(10), ins1));
//        Background hbarThumbHoverBackground = new Background(new BackgroundFill(scrollBarThumbHoverColor, new CornerRadii(10), ins1));


        if(hbarIncrementArrow!=null) hbarIncrementArrow.setBackground(btnBackground);
        if(hbarDecrementArrow!=null) hbarDecrementArrow.setBackground(btnBackground);
        if(vbarIncrementArrow!=null) vbarIncrementArrow.setBackground(btnBackground);
        if(vbarDecrementArrow!=null) vbarDecrementArrow.setBackground(btnBackground);

        if(hbarBackground!=null) hbarBackground.setBackground(trackBackground);
        if(vbarBackground!=null) vbarBackground.setBackground(trackBackground);

//        hbarThumb.setBackground(hbarThumbBackground);
//        vbarThumb.setBackground(vbarThumbBackground);
        String thumbBg = scrollBarThumbColor.toString().replace("0x", "#");
        String thumbBgHover = scrollBarThumbHoverColor.toString().replace("0x", "#");
        if(hbarThumb!=null) hbarThumb.setStyle("-fx-background-color:"+thumbBg+";");
        if(vbarThumb!=null) vbarThumb.setStyle("-fx-background-color:"+thumbBg+";");

        if(hbarIncrementBtn != null){
            hbarIncrementBtnHoverListener = new ArrowBtnHoverListener(hbarIncrementBtn, hbarIncrementArrow, btnBackground, btnHoverBackground);
            hbarIncrementBtn.hoverProperty().addListener(hbarIncrementBtnHoverListener);
        }
        if(hbarDecrementBtn != null){
            hbarDecrementBtnHoverListener = new ArrowBtnHoverListener(hbarDecrementBtn, hbarDecrementArrow, btnBackground, btnHoverBackground);
            hbarDecrementBtn.hoverProperty().addListener(hbarDecrementBtnHoverListener);
        }

        if(vbarIncrementBtn!=null){
            vbarIncrementBtnHoverListener = new ArrowBtnHoverListener(vbarIncrementBtn, vbarIncrementArrow, btnBackground, btnHoverBackground);
            vbarIncrementBtn.hoverProperty().addListener(vbarIncrementBtnHoverListener);
        }

        if(vbarDecrementBtn!=null){
            vbarDecrementBtnHoverListener = new ArrowBtnHoverListener(vbarDecrementBtn, vbarDecrementArrow, btnBackground, btnHoverBackground);
            vbarDecrementBtn.hoverProperty().addListener(vbarDecrementBtnHoverListener);
        }

        if(hbarThumb!=null){
            hbarThumbHoverListener = new ThumbHoverListener(hbarThumb, thumbBg, thumbBgHover);
            hbarThumb.hoverProperty().addListener(hbarThumbHoverListener);
        }

        if(vbarThumb!=null){
            vbarThumbHoverListener = new ThumbHoverListener(vbarThumb, thumbBg, thumbBgHover);
            vbarThumb.hoverProperty().addListener(vbarThumbHoverListener);
        }

    }

    class ArrowBtnHoverListener implements ChangeListener<Boolean>{

        private Background background, hoverBackground;
        private Region incrementBtn, incrementArrow;

        public ArrowBtnHoverListener(Region incrementBtn, Region incrementArrow, Background background, Background hoverBackground){
            this.incrementBtn = incrementBtn;
            this.incrementArrow = incrementArrow;
            this.background = background;
            this.hoverBackground = hoverBackground;

        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue){
                incrementBtn.setScaleY(1.2);
                incrementBtn.setScaleX(1.2);
                incrementArrow.setBackground(hoverBackground);
            }else{
                incrementBtn.setScaleY(1);
                incrementBtn.setScaleX(1);
                incrementArrow.setBackground(background);
            }
        }
    }

    class ThumbHoverListener implements ChangeListener<Boolean>{
        private String background, hoverBackground;
        private Region thumb;
        public ThumbHoverListener(Region thumb, String background, String hoverBackground){
            this.thumb = thumb;
            this.background = background;
            this.hoverBackground = hoverBackground;
        }
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue){
                thumb.setStyle("-fx-background-color:"+hoverBackground+";");
            }else{
                thumb.setStyle("-fx-background-color:"+background+";");
            }
        }
    }

}

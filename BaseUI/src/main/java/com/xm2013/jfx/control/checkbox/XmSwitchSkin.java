package com.xm2013.jfx.control.checkbox;

import com.xm2013.jfx.control.base.ColorType;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import com.xm2013.jfx.control.icon.XmIcon;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class XmSwitchSkin extends SkinBase {
    private final XmSwitch control;
    private final Pane box;

    class SwitchSkinInfo{
        public Paint borderColor;
        public Paint backgroundColor;
        public Paint textColor;
        public Paint boxColor;
        public double size;
        public double fontSize;

        @Override
        public String toString() {
            return "SwitchSkinInfo{" +
                    "borderColor=" + borderColor +
                    ", backgroundColor=" + backgroundColor +
                    ", textColor=" + textColor +
                    ", boxColor=" + boxColor +
                    ", size=" + size +
                    ", fontSize=" + fontSize +
                    '}';
        }
    }

    private Map<Integer,SwitchSkinInfo> infos = new HashMap<>();

    public XmSwitchSkin(XmSwitch control) {
        super(control);
        this.control = control;

        box = new Pane();
        box.getStyleClass().add("xm-switch-box");
//        InnerShadow is = new InnerShadow();
//        is.setBlurType(BlurType.THREE_PASS_BOX);
//        is.setRadius(5);
//        is.setColor(Color.web("#bbbbbbee"));
//        box.setEffect(is);

        getChildren().add(box);
        Node activeNode = control.getActiveNode();
        if(activeNode!=null){
            activeNode.visibleProperty().bind(control.checkedProperty());
            getChildren().add(activeNode);
            activeNode.setManaged(false);
        }

        Node inactiveNode = control.getInactiveNode();
        if(inactiveNode!=null){
            inactiveNode.visibleProperty().bind(control.checkedProperty().not());
            getChildren().add(inactiveNode);
            inactiveNode.setManaged(false);
        }

        control.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            boolean checked = !control.isChecked();
            control.setChecked(checked);
            updateSkin(checked?2:1);
        });

        updateSkin(1);
    }

    private void updateSkin(int status ){

        SwitchSkinInfo info = getSkinInfo(status);
//        System.out.println(info);

        box.setPrefWidth(info.size);
        box.setPrefHeight(info.size);
        this.control.setPrefWidth(info.size*2.5);
        this.control.setPrefHeight(info.size*1.2);

        this.control.setBorder(new Border(new BorderStroke(info.borderColor, BorderStrokeStyle.SOLID, new CornerRadii(info.size*1.2), new BorderWidths(1))));
//        this.control.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(info.size*1.2), new BorderWidths(1))));
        this.control.setBackground(new Background(new BackgroundFill(info.backgroundColor, new CornerRadii(info.size*1.2), Insets.EMPTY)));
        this.box.setBackground(new Background(new BackgroundFill(info.boxColor, new CornerRadii(info.size), Insets.EMPTY)));

        Node activeNode = control.getActiveNode();
        Node inactiveNode = control.getInactiveNode();

        if(activeNode!=null ){
            if(activeNode instanceof Text) {
                setTextNodeSkin(activeNode, info.textColor, info.fontSize);
            }
        }

        if(inactiveNode!=null){
            if(inactiveNode instanceof Text) {
                setTextNodeSkin(inactiveNode, info.textColor, info.fontSize);
            }

        }

    }

    private void setTextNodeSkin(Node node, Paint textColor, double fontSize){
        Text text = (Text)node;
        text.setTextOrigin(VPos.BOTTOM);
        text.setFill(textColor);
        text.setFont(Font.font(text.getFont().getFamily(), fontSize));
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {

        return infos.get(1).size * 2.5;

    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return infos.get(1).size * 1.2;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
//        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);

        boolean checked = control.isChecked();
        double y = contentY, width = box.prefWidth(-1), x = checked?contentWidth-width - 1 : contentX + 1;
        layoutInArea(box, x, y, width, width, -1, HPos.CENTER, VPos.CENTER);

        Node activeNode = control.getActiveNode();
        Node inactiveNode = control.getInactiveNode();

        if(inactiveNode!=null){
            if(inactiveNode instanceof Text){
                inactiveNode.setLayoutX(contentWidth - inactiveNode.prefWidth(-1) - 2);
                inactiveNode.setLayoutY(inactiveNode.prefHeight(-1));
            }
        }

        if(activeNode!=null){
            if(activeNode instanceof Text){
                activeNode.setLayoutX(2);
                activeNode.setLayoutY(activeNode.prefHeight(-1));
            }
        }

    }


    public SwitchSkinInfo getSkinInfo(int status){
        SwitchSkinInfo info = infos.get(status);
        if(info == null){
            info = buildInfo(status);
        }
        return info;
    }

    private SwitchSkinInfo buildInfo(int status) {
        SwitchSkinInfo info = new SwitchSkinInfo();

        ColorType colorType = control.getColorType();
        SizeType sizeType = control.getSizeType();
        HueType hueType = control.getHueType();
//        BorderType borderType = control.getBorderType();

        Paint color = colorType.getPaint(),
                defaultColor = Color.web("#bbbbbb");

        if(status == 1){
            if(HueType.DARK.equals(hueType)){
                info.borderColor = defaultColor;
                info.backgroundColor = Color.TRANSPARENT;
                info.textColor = defaultColor;
                info.boxColor = defaultColor;

            }else{
                info.borderColor = Color.TRANSPARENT;
                info.backgroundColor = defaultColor;
                info.textColor = Color.WHITE;
                info.boxColor = Color.WHITE;
            }
        }else if(status == 2){
            if(HueType.DARK.equals(hueType)){
                info.borderColor = color;
                info.backgroundColor = Color.TRANSPARENT;
                info.textColor = color;
                info.boxColor = color;
            }else{
                info.borderColor = Color.TRANSPARENT;
                info.backgroundColor = color;
                info.textColor = Color.WHITE;
                info.boxColor = Color.WHITE;
            }
        }

        if(SizeType.SMALL.equals(sizeType)){
            info.size = 14;
            info.fontSize = 12;
        }else if(SizeType.LARGE.equals(sizeType)){
            info.size = 18;
            info.fontSize = 16;
        }else{
            info.size = 16;
            info.fontSize = 14;
        }

        infos.put(status,info);

        return info;
    }


}

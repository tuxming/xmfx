package com.xm2013.jfx.control.checkbox;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.CssKeys;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.XmControl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.EnumConverter;
import javafx.geometry.VPos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmSwitch extends XmControl {

    private static String DEFAULT_STYLE_CLASS = "xm-switch";

    /* -------------------------------- constructor ---------------------------------- */

    public XmSwitch(){
        init();
    }

    private void init() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.RADIO_BUTTON);

    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new XmSwitchSkin(this);
    }

    /* -------------------------------- properties ---------------------------------- */
    /**
     * 是否选中
     */
    private BooleanProperty checked;
    public boolean isChecked() {
        return checkedProperty().get();
    }
    public BooleanProperty checkedProperty() {
        if(checked == null){
            checked = new SimpleBooleanProperty(false);
        }
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checkedProperty().set(checked);
    }

    /**
     * 激活时显示的节点，可以是文本，或者是图标，这里建议使用XmIcon/label
     */
    private ObjectProperty<Node> activeNode;
    public Node getActiveNode() {
        return activeNodeProperty().get();
    }
    public ObjectProperty<Node> activeNodeProperty() {
        if(activeNode == null){
            activeNode = new SimpleObjectProperty<>();
        }
        return activeNode;
    }
    public void setActiveNode(Node activeNode) {
        this.activeNodeProperty().set(activeNode);
    }
    public void setActiveText(String text){
        Text text1 = new Text(text);
        text1.setTextOrigin(VPos.BOTTOM);
        this.setActiveNode(text1);
    }

    /**
     * 未激活时显示的节点，可以是文本，或者是图标，这里建议使用XmIcon/label
     */
    private ObjectProperty<Node> inactiveNode;

    public Node getInactiveNode() {
        return inactiveNodeProperty().get();
    }

    public ObjectProperty<Node> inactiveNodeProperty() {
        if(inactiveNode == null){
            this.inactiveNode = new SimpleObjectProperty<>();
        }
        return inactiveNode;
    }
    public void setInactiveNode(Node inactiveNode) {
        this.inactiveNodeProperty().set(inactiveNode);
    }
    public void setInactiveText(String text){
        Text text1 = new Text(text);
        text1.setTextOrigin(VPos.BOTTOM);
        this.inactiveNodeProperty().set(text1);
    }
    
    /*
     * 色调类型，在亮色和暗色背景下面，控件的样式
     */
    private ObjectProperty<HueType> hueType;
    public HueType getHueType() {
        return hueTypeProperty().get();
    }

    public ObjectProperty<HueType> hueTypeProperty() {
        if(hueType == null)
            hueType = FxKit.newProperty(HueType.LIGHT, XmSwitch.StyleableProperties.HUE_TYPE, this, "hueType");
        return hueType;
    }
    public void setHueType(HueType hueType){
        this.hueTypeProperty().set(hueType);
    }


    private static class StyleableProperties {

        /**
         * 按钮色调，dark/light/none
         * -fx-type-round: dark | light | none
         */
        final static CssMetaData<XmSwitch, HueType> HUE_TYPE =
                new CssMetaData<XmSwitch, HueType>(CssKeys.PropTypeHue,
                        new EnumConverter<HueType>(HueType.class),
                        HueType.LIGHT, true) {
                    @Override
                    public boolean isSettable(XmSwitch styleable) {
                        return styleable.hueType == null || !styleable.hueType.isBound();
                    }

                    @Override
                    public StyleableProperty<HueType> getStyleableProperty(XmSwitch styleable) {
                        return (StyleableProperty<HueType>)styleable.hueTypeProperty();
                    }
                };

        private static List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(XmControl.getClassCssMetaData());
            Collections.addAll(styleables
                    , HUE_TYPE
            );
            STYLEABLES = Collections.unmodifiableList(styleables);

        }
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return XmSwitch.StyleableProperties.STYLEABLES;
    }
}

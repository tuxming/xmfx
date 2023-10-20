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
package com.xm2013.jfx.control.checkbox;

import com.xm2013.jfx.control.base.*;
import com.xm2013.jfx.control.label.SelectableText;
import javafx.beans.property.*;
import javafx.collections.ObservableMap;
import javafx.css.PseudoClass;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Skin;
import javafx.scene.paint.Paint;

/**
 *  XmCheckBox checkbox = new XmCheckBox(); 
 *  checkbox.setText("复选框测试"); 
 *  支持泛型传值，当使用泛型传值的时候，显示的值，会调用converter，所以需要自己实现convert 
   <pre>
   XmCheckBox&lt;Menu&gt; checkbox = new XmCheckBox&lt;Menu&gt;();
   checkbox.setConverter(new XmStringConverter&lt;Menu&gt;() {
       public String toString(Menu menu) { 
           return menu.getLabel(); 
       } 
   }); 
   checkbox.setValue(new Menu("a", "这是label", 1)); 

   //XmCheckBox 结合 XmToggleGroup使用
   </pre>
 */
public class XmCheckBox<T> extends SelectableText implements XmToggle<T>{

    /* *************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a check box with an empty string for its label.
     */
    public XmCheckBox() {
        initialize();
    }

    /**
     * Creates a check box with the specified text as its label.
     *
     * @param text A text string for its label.
     */
    public XmCheckBox(String text) {
        setText(text);
        initialize();
    }

    public XmCheckBox(T t){
        setValue(t);
        initialize();
    }

    private void initialize() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.CHECK_BOX);

        // initialize pseudo-class state
        pseudoClassStateChanged(PSEUDO_CLASS_DETERMINATE, true);
    }

    /* *************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * 是否radiobutton，如果是radioButton，这不能使用第三状态
     */
    private BooleanProperty radioButton;
    public boolean isRadioButton() {
        return radioButtonProperty().get();
    }
    public BooleanProperty radioButtonProperty() {
        if(radioButton == null){
            radioButton = new BooleanPropertyBase(false) {
                @Override protected void invalidated() {
                    final boolean isRadio = get();
                    if(isRadio){
                        indeterminateProperty().set(false);
                        allowIndeterminateProperty().set(false);
                        roundTypeProperty().set(RoundType.CIRCLE);
                    }
                }

                @Override
                public Object getBean() {
                    return XmCheckBox.this;
                }

                @Override
                public String getName() {
                    return "radioButton";
                }
            };
        }
        return radioButton;
    }
    public void setRadioButton(boolean radioButton) {
        this.radioButtonProperty().set(radioButton);
    }

    /**
     * 高亮显示，默认是灰色在失去焦点以后
     */
    private BooleanProperty selectedHighLight;

    public boolean isSelectedHighLight() {
        return selectedHighLightProperty().get();
    }

    public BooleanProperty selectedHighLightProperty() {
        if(selectedHighLight == null){
            selectedHighLight = new SimpleBooleanProperty(false);
        }
        return selectedHighLight;
    }

    public void setSelectedHightLight(boolean selectedHightLight) {
        this.selectedHighLightProperty().set(selectedHightLight);
    }

    @Override
    public void setRoundType(RoundType roundType) {
        if(this.isRadioButton()) return;
        super.setRoundType(roundType);
    }

    /**
     * Determines whether the XmCheckBox is in the indeterminate state.
     * 如果是RadioButton，则不能使用第三状态
     */
    private BooleanProperty indeterminate;
    public final void setIndeterminate(boolean value) {
        if(isRadioButton() && value) return;
        indeterminateProperty().set(value);
    }

    public final boolean isIndeterminate() {
        return indeterminate != null && indeterminate.get();
    }

    public final BooleanProperty indeterminateProperty() {
        if (indeterminate == null) {
            indeterminate = new BooleanPropertyBase(false) {
                @Override protected void invalidated() {
                    final boolean active = get();
                    pseudoClassStateChanged(PSEUDO_CLASS_DETERMINATE,  !active);
                    pseudoClassStateChanged(PSEUDO_CLASS_INDETERMINATE, active);
                    notifyAccessibleAttributeChanged(AccessibleAttribute.INDETERMINATE);
//                    System.out.println(getText()+":"+active);
                }
                @Override
                public Object getBean() {
                    return XmCheckBox.this;
                }

                @Override
                public String getName() {
                    return "indeterminate";
                }
            };
        }
        return indeterminate;
    }
    /**
     * Indicates whether this XmCheckBox is checked.
     */
    private BooleanProperty selected;
    public final void setSelected(boolean value) {
        selectedProperty().set(value);
    }
    public final boolean isSelected() {
        return selected == null ? false : selected.get();
    }

    public final BooleanProperty selectedProperty() {
        if (selected == null) {
            selected = new BooleanPropertyBase() {
                @Override protected void invalidated() {
                    final Boolean selected = get();
                    pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, selected);
                    notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTED);

                    final XmToggleGroup<T> tg = getToggleGroup();
                    if (tg != null) {
                        if (selected) {
                            tg.selectToggle(XmCheckBox.this);
                        } else if (tg.getSelectedToggle() == XmCheckBox.this) {

                            if (!tg.getSelectedToggle().isSelected()) {
                                for (XmToggle<T> toggle: tg.getToggles()) {
                                    if (toggle.isSelected()) {
                                        return;
                                    }
                                }
                            }
                            tg.selectToggle(null);
                        }
                    }
                }

                @Override
                public Object getBean() {
                    return XmCheckBox.this;
                }

                @Override
                public String getName() {
                    return "selected";
                }
            };
        }
        return selected;
    }

    private ObjectProperty<XmToggleGroup<T>> toggleGroup;
    public final void setToggleGroup(XmToggleGroup<T> value) {
        toggleGroupProperty().set(value);
    }

    public final XmToggleGroup<T> getToggleGroup() {
        return toggleGroup == null ? null : toggleGroup.get();
    }

    public final ObjectProperty<XmToggleGroup<T>> toggleGroupProperty() {
        if (toggleGroup == null) {
            final XmCheckBox<T> that = this;
            toggleGroup = new ObjectPropertyBase<>() {
                private XmToggleGroup<T> old;
//                private ChangeListener<XmToggle<T>> listener = (o, oV, nV) ->{
//                    XmCheckBox.this.setFocusTraversable(true);
//                };

                @Override protected void invalidated() {
                    final XmToggleGroup<T> tg = get();
                    if (tg != null && !tg.getToggles().contains(that)) {
                        if (old != null) {
                            old.getToggles().remove(that);
                        }
//                        XmCheckBox.this.setFocusTraversable(true);
                        tg.getToggles().add(that);
//                        tg.selectedToggleProperty().addListener(listener);
                    } else if (tg == null) {
//                        old.selectedToggleProperty().removeListener(listener);
                        old.getToggles().remove(that);
//                        XmCheckBox.this.setFocusTraversable(false);
                    }

                    old = tg;
                }

                @Override
                public Object getBean() {
                    return that;
                }

                @Override
                public String getName() {
                    return "toggleGroup";
                }
            };
        }
        return toggleGroup;
    }

    // --- string converter
    public ObjectProperty<XmStringConverter<T>> converterProperty() { return converter; }
    private ObjectProperty<XmStringConverter<T>> converter =
            new SimpleObjectProperty<XmStringConverter<T>>(this, "converter", defaultStringConverter());
    public final void setConverter(XmStringConverter<T> value) { converterProperty().set(value); }
    public final XmStringConverter<T> getConverter() {return converterProperty().get(); }
    private static <T> XmStringConverter<T> defaultStringConverter() {
        return new XmStringConverter<T>() {
            @Override public String toString(T t) {
                return t == null ? null : t.toString();
            }
        };
    }

    /**
     * 支持设置一个默认值，当选中的时候可以获取到这个默认值，
     * 如果设置了这个默认值，checkbox显示的文本，将会使用value的toString()方法，
     * 所以如果设置了value, 请根据需要自己设置converter,
     */
    private ObjectProperty<T> value = new SimpleObjectProperty<>();
    public T getValue() {
        return value.get();
    }

    public ObjectProperty<T> valueProperty() {
        return value;
    }

    public void setValue(T value) {
        this.value.set(value);
    }

    @Override
    public ObservableMap<Object, T> getXmProperties() {
        return toggleGroupProperty().get().getProperties();
    }

    /**
     * Determines whether the user toggling the XmCheckBox should cycle through
     * all three states: <em>checked</em>, <em>unchecked</em>, and
     * <em>undefined</em>. If {@code true} then all three states will be
     * cycled through; if {@code false} then only <em>checked</em> and
     * <em>unchecked</em> will be cycled.
     */
    private BooleanProperty allowIndeterminate;

    public final void setAllowIndeterminate(boolean value) {
        if(isRadioButton() && value)
            return;
        allowIndeterminateProperty().set(value);
    }

    public final boolean isAllowIndeterminate() {
        return allowIndeterminateProperty().get();
    }

    public final BooleanProperty allowIndeterminateProperty() {
        if (allowIndeterminate == null) {
            allowIndeterminate =
                    new SimpleBooleanProperty(this, "allowIndeterminate");
        }
        return allowIndeterminate;
    }

    private ObjectProperty<Paint> selectedColor = new SimpleObjectProperty<>();
    public Paint getSelectedColor() {
        return selectedColor.get();
    }
    public ObjectProperty<Paint> selectedColorProperty() {
        return selectedColor;
    }
    public void setSelectedColor(Paint selectedColor) {
        this.selectedColor.set(selectedColor);
    }
    /* *************************************************************************
     *                                                                         *
     * Methods                                                                 *
     *                                                                         *
     **************************************************************************/

    /**
     * Toggles the state of the {@code XmCheckBox}. If allowIndeterminate is
     * true, then each invocation of this function will advance the XmCheckBox
     * through the states checked, unchecked, and undefined. If
     * allowIndeterminate is false, then the XmCheckBox will only cycle through
     * the checked and unchecked states, and forcing indeterminate to equal to
     * false.
     */
    public void fire() {
        if (!isDisabled()) {
            if (isAllowIndeterminate()) {
                if (!isSelected() && !isIndeterminate()) {
                    setIndeterminate(true);
                } else if (isSelected() && !isIndeterminate()) {
                    setSelected(false);
                } else if (isIndeterminate()) {
                    setSelected(true);
                    setIndeterminate(false);
                }
            } else {
                setSelected(!isSelected());
                setIndeterminate(false);
            }
//            fireEvent(new ActionEvent());
        }
    }

    /** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new XmCheckBoxSkin(this);
    }

    /* *************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "xm-check-box";
    private static final PseudoClass PSEUDO_CLASS_DETERMINATE =
            PseudoClass.getPseudoClass("determinate");
    private static final PseudoClass PSEUDO_CLASS_INDETERMINATE =
            PseudoClass.getPseudoClass("indeterminate");
    private static final PseudoClass PSEUDO_CLASS_SELECTED =
            PseudoClass.getPseudoClass("selected");


    /* *************************************************************************
     *                                                                         *
     * Accessibility handling                                                  *
     *                                                                         *
     **************************************************************************/

    /** {@inheritDoc} */
    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case SELECTED: return isSelected();
            case INDETERMINATE: return isIndeterminate();
            default: return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}

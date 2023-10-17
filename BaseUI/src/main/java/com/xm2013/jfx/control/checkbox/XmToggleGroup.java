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

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.ToggleGroup;

import java.util.HashMap;
import java.util.List;

public class XmToggleGroup<T> {
    /**
     * Creates a default ToggleGroup instance.
     */
    public XmToggleGroup() {
        toggles.addListener(new ListChangeListener<XmToggle<T>>() {
            @Override
            public void onChanged(Change<? extends XmToggle<T>> c) {
                while (c.next()) {
                    List<? extends XmToggle<T>> addedToggles = c.getAddedSubList();

                    // Look through the removed toggles.
                    for (XmToggle<T> t : c.getRemoved()) {
                        // If any of them was the one and only selected toggle,
                        // then we will clear the selected toggle property.
                        if (t.isSelected()) {
                            selectToggle(null);
                        }

                        // If the toggle is not added again (below) remove
                        // the group association.
                        if (!addedToggles.contains(t)) {
                            t.setToggleGroup(null);
                        }
                    }

                    // A Toggle can only be in one group at any one time. If the
                    // group is changed, then the toggle is removed from the old group prior to
                    // being added to the new group.
                    for (XmToggle t: addedToggles) {
                        if (!XmToggleGroup.this.equals(t.getToggleGroup())) {
                            if (t.getToggleGroup() != null) {
                                t.getToggleGroup().getToggles().remove(t);
                            }
                            t.setToggleGroup(XmToggleGroup.this);
                        }
                    }

                    // Look through all the added toggles and the very first selected
                    // toggle we encounter will become the one we make the selected
                    // toggle for this group.
                    for (XmToggle<T> t : addedToggles) {
                        if (t.isSelected()) {
                            selectToggle(t);
                            break;
                        }
                    }
                }
            }
        });
    }

    /**
     * The list of toggles within the ToggleGroup.
     * @return the list of toggles within the ToggleGroup
     */
    public final ObservableList<XmToggle<T>> getToggles() {
        return toggles;
    }

    private final ObservableList<XmToggle<T>> toggles = FXCollections.observableArrayList();

    private final ReadOnlyObjectWrapper<XmToggle<T>> selectedToggle = new ReadOnlyObjectWrapper<XmToggle<T>>() {
        // Note: "set" is really what I want here. If the selectedToggle property
        // is bound, then this whole chunk of code is bypassed, which is exactly
        // what I want to do.
        @Override public void set(final XmToggle<T> newSelectedToggle) {
            if (isBound()) {
                throw new java.lang.RuntimeException("A bound value cannot be set.");
            }
            final XmToggle<T> old = get();
            if (old == newSelectedToggle) {
                return;
            }
            if (setSelected(newSelectedToggle, true) ||
                    (newSelectedToggle != null && newSelectedToggle.getToggleGroup() == XmToggleGroup.this) ||
                    (newSelectedToggle == null)) {
                if (old == null || old.getToggleGroup() == XmToggleGroup.this || !old.isSelected()) {
                    setSelected(old, false);
                }
                if(newSelectedToggle !=null){
                    super.set(newSelectedToggle);
                }
            }
        }
    };

    /**
     * Selects the toggle.
     *
     * @param value The {@code Toggle} that is to be selected.
     */
    // Note that since selectedToggle is a read-only property, the selectToggle method is some
    // other method than setSelectedToggle, even though it is in essence doing the same thing
    public final void selectToggle(XmToggle<T> value) { selectedToggle.set(value); }

    /**
     * Gets the selected {@code Toggle}.
     * @return Toggle The selected toggle.
     */
    public final XmToggle<T> getSelectedToggle() { return selectedToggle.get(); }

    /**
     * The selected toggle.
     * @return the selected toggle
     */
    public final ReadOnlyObjectProperty<XmToggle<T>> selectedToggleProperty() { return selectedToggle.getReadOnlyProperty(); }

    private boolean setSelected(XmToggle<T> toggle, boolean selected) {
        if (toggle != null &&
                toggle.getToggleGroup() == this &&
                !toggle.selectedProperty().isBound()) {
            toggle.setSelected(selected);

            if(selected){
                setValue(toggle.getValue());
                text = toggle.getText();
            }
//            else{
//                setValue(null);
//                text = null;
//            }
            return true;
        }
        return false;
    }

    // Clear the selected toggle only if there are no other toggles selected.
    final void clearSelectedToggle() {
        if (!selectedToggle.getValue().isSelected()) {
            for (XmToggle<T> toggle: getToggles()) {
                if (toggle.isSelected()) {
                    return;
                }
            }
        }
        selectedToggle.set(null);
    }

    /* ***********************************************************************
     *                                                                        *
     *                                                                        *
     *                                                                        *
     *************************************************************************/

    private static final Object USER_DATA_KEY = new Object();
    private static final Object TEXT_KEY = new Object();
    // A map containing a set of properties for this scene
    private ObservableMap<Object, T> properties;
    private String text ;

    /**
     * Returns an observable map of properties on this node for use primarily
     * by application developers.
     *
     * @return an observable map of properties on this node for use primarily
     * by application developers
     *
     * @since JavaFX 8u40
     */
    public final ObservableMap<Object, T> getProperties() {
        if (properties == null) {
            properties = FXCollections.observableMap(new HashMap<Object, T>());
        }
        return properties;
    }

    /**
     * Tests if ToggleGroup has properties.
     * @return true if node has properties.
     *
     * @since JavaFX 8u40
     */
    public boolean hasProperties() {
        return properties != null && !properties.isEmpty();
    }

    /**
     * Convenience method for setting a single Object property that can be
     * retrieved at a later date. This is functionally equivalent to calling
     * the getProperties().put(Object key, Object value) method. This can later
     * be retrieved by calling {@link ToggleGroup#getUserData()}.
     *
     * @param value The value to be stored - this can later be retrieved by calling
     *          {@link ToggleGroup#getUserData()}.
     *
     * @since JavaFX 8u40
     */
    public void setValue(T value) {
//        System.out.println("setValue:"+value);
        getProperties().put(USER_DATA_KEY, value);
    }

    /**
     * Returns a previously set Object property, or null if no such property
     * has been set using the {@link ToggleGroup#setUserData(java.lang.Object)} method.
     *
     * @return The Object that was previously set, or null if no property
     *          has been set or if null was set.
     *
     * @since JavaFX 8u40
     */
    public T getValue() {
        return getProperties().get(USER_DATA_KEY);
    }

    public String getText(){
        return text;
    }

}

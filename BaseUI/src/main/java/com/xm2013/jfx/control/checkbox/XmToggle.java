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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableMap;

public interface XmToggle<T> {
    XmToggleGroup<T> getToggleGroup();

    void setToggleGroup(XmToggleGroup<T> toggleGroup);

    ObjectProperty<XmToggleGroup<T>> toggleGroupProperty();

    boolean isSelected();

    void setSelected(boolean selected);

    /**
     * The selected state for this {@code Toggle}.
     * @return the selected property
     */
    BooleanProperty selectedProperty();

    /**
     * Returns a previously set Object property, or null if no such property
     * has been set using the {@code Node.setUserData(java.lang.Object)} method.
     *
     * @return The Object that was previously set, or null if no property
     *          has been set or if null was set.
     */
    T getValue();

    /**
     * Convenience method for setting a single Object property that can be
     * retrieved at a later date. This is functionally equivalent to calling
     * the getProperties().put(Object key, Object value) method. This can later
     * be retrieved by calling {@code Node.getUserData()}.
     *
     * @param value The value to be stored - this can later be retrieved by calling
     *          {@code Node.getUserData()}.
     */
    void setValue(T value);

    String getText();

    /**
     * Returns an observable map of properties on this toggle for use primarily
     * by application developers.
     *
     * @return An observable map of properties on this toggle for use primarily
     * by application developers
     */
    ObservableMap<Object, T> getXmProperties();

}

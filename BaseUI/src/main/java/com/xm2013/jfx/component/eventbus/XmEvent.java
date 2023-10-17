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
package com.xm2013.jfx.component.eventbus;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;
public class XmEvent<T> extends Event{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4011774587169578132L;

	public static final EventType<XmEvent<Object>> ANY = new EventType<>(Event.ANY, "XM-EVENT");
	public static final EventType<XmEvent<Object>> CLOSE_WINDOW = new EventType<>(Event.ANY, "XM-CLOSE-WINDOW");
	public static final EventType<XmEvent<Object>> MAX_WINDOW = new EventType<>(Event.ANY, "XM-MAX-WINDOW");
	public static final EventType<XmEvent<Object>> MIN_WINDOW = new EventType<>(Event.ANY, "XM-MIN-WINDOW");

    private T data;

    public XmEvent(EventType<? extends XmEvent<T>> eventType) {
		super(eventType);
	}

    
    public XmEvent(@NamedArg("eventType") EventType<? extends XmEvent<T>> eventType, T data) {
        super(eventType);
        this.data = data;
    }

    public T getData() {
        return data;
    }
}

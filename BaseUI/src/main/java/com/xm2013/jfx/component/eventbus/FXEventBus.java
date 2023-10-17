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

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public final class FXEventBus implements EventBus {
	
	private static FXEventBus instance = new FXEventBus();
	
	public static FXEventBus getDefault() {
		return instance;
	}
	
    private Group eventHandlers = new Group();

    @Override
    public <T extends Event> Subscriber addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        eventHandlers.addEventHandler(eventType, eventHandler);
        return new Subscriber(this, eventType, (EventHandler<? super Event>) eventHandler);
    }

    @Override
    public <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        eventHandlers.removeEventHandler(eventType, eventHandler);
    }

    @Override
    public void fireEvent(Event event) {
        eventHandlers.fireEvent(event);
    }
}

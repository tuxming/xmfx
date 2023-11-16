package com.xm2013.template.template;

import com.xm2013.jfx.component.eventbus.XmEvent;
import javafx.event.Event;
import javafx.event.EventType;

public class XmEventType {

    public static final EventType<XmEvent<Object>> SIDE_BAR_EXPAND = new EventType<>(Event.ANY, "SIDE_BAR_EXPAND");

}

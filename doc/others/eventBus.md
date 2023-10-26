# EventBus

eventBus在很多组件中都有这个概念，javaFx并没有自带，这里使用了开源组件[AlmasB/FXEventBus](https://github.com/AlmasB/FXEventBus) 继承。

eventBus的功能就是能够跨组件通信， 



## 添加事件

1, 定义事件类型

```java
public static final EventType<XmEvent<Object>> ACTIVE_TAB = new EventType<>(Event.ANY, "ACTIVE_TAB");
//Object 是一个泛型，可以用于触发事件的传值
```

2, 发起事件

```java
//普通发起事件
FXEventBus.getDefault().fireEvent(new XmEvent<>(XmEvent.ACTIVE_TAB));
//带参数的发起事件
FXEventBus.getDefault().fireEvent(new XmEvent<>(XmEvent.ACTIVE_TAB), "111");
```

3, 监听事件

```JAVA
FXEventBus.getDefault().addEventHandler(XmEvent.ACTIVE_TAB, e->{
    Object data = e.getData();  //获取传过来的值
    
});
```






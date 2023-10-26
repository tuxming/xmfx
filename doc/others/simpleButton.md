# 简单按钮(XmNodeButton)

如果你不想要那么复杂的按钮，就可以使用这个按钮，这个按钮只有一个ColorType属性， 

## 使用

```java
//文本节点
XmNodeButton btn = new XmNodeButton("按钮");
//图标节点, 如果子节点继承制XmIcon，节点颜色会根据ColorType属性的变化而变化，如果是其他节点，请自行监听hover属性，进行更新
XmNodeButton btn = new XmNodeButton(new XmFontIcon("\ue0654"));
```



## 属性

```java
btn.setColorType(); //设置颜色类型
btn.setOnAction();  //点击后的回调函数
btn.setActive(true);  //设置为激活状态， 描边为ColorType颜色
btn.setFilled(true);  //设置filled状态， 背景会被填充为colorType颜色
btn.setOutside(true); //设置outside状态, 外观类似于disable,但是相应事件

/**
 * 节点初始初始颜色，如果是文字，则是文字的初始颜色
 * 0: 默认为灰色
 * 1：主题色
 */
btn.setTextStyle();

/**
 * hover时，按钮的状态
 * 0: 默认，改变背景色
 * 1: 提高文字亮度
 * 2：显示为主题色
 */
btn.setHoverStyle();
```

通过这几个属性，可以实现不同外观展示。
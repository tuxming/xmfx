# ColorType 颜色定义



自定义了5中颜色（primary, secondary, danger, success, warning）， 可以通过other自定义颜色， 要想自己改变主题色，并且想要所有组件颜色保持一致。可直接修改ColorType.PRIMARY变量。



- ColorType.primary(), 返回主色调。
- ColorType.secondary(), 返回次要色调。
- ColorType.danger(), 返回红色
- ColorType.success(), 返回绿色
- ColorType.warning(), 黄色黄色。
- ColorType.other(String), 自定义颜色，String是标准的css格式：
  - 纯色： ColorType.other("#ff00ff")
  - 渐变色：线性渐变： ColorType.other("linear-gradient(from 0.0% 0.0% to 100.0% 0.0%, #23d0f3ff 0.0%, #d791f9ff 50.0%, #fe7b84ff 100.0%)");  
  - 径向渐变： ColorType.other("radial-gradient(focus-angle 45deg, focus-distance 20%, center 25% 25%, radius 50%, reflect, gray, darkgray 75%, dimgray")


# EasySwipeLayout
[![Release](https://img.shields.io/badge/JCenter-0.0.1-brightgreen.svg)](https://bintray.com/dkzwm/maven/esl)
[![MinSdk](https://img.shields.io/badge/MinSdk-14-blue.svg)](https://developer.android.com/about/versions/android-4.0.html)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://github.com/dkzwm/SmoothRefreshLayout/blob/master/LICENSE)
## [English](https://github.com/dkzwm/EasySwipeLayout/blob/master/README_EN.md) | 中文
A simple sliding layout view, 一款简单高效的滑动库，模仿MIUI滑动返回功能。

## 特性:
- 支持上下左右边缘拉出.    
- 支持全局添加、Xml添加、单独添加.    
- 支持自定义效果.    
- 支持多点触摸.    

## 引入
添加如下依赖到你的 build.gradle 文件:
```
dependencies {
    implementation 'me.dkzwm.widget.esl:core:0.0.1'
	
	AndroidX版本
	implementation 'me.dkzwm.widget.esl:core:0.0.1.androidx'
}
```

## 快照
![](https://github.com/dkzwm/EasySwipeLayout/blob/master/snapshot/demo.gif)

## 演示程序
下载 [Demo.apk](https://raw.githubusercontent.com/dkzwm/EasySwipeLayout/master/apk/demo.apk)    

## 使用   
#### 在Xml中配置
```
<?xml version="1.0" encoding="utf-8"?>
<me.dkzwm.widget.esl.EasySwipeLayout
    android:id="@+id/easySwipeLayout"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:esl_direction="right"
	//指定自定义Drawer的类名
    app:esl_specified="@string/specified_the_class_name"
	//指定样式为自定义，此时需要配置esl_specified属性
    app:esl_style="custom">
</me.dkzwm.widget.esl.EasySwipeLayout>
```
####  Java代码全局配置
```
EasySwipeConfig config =
      new EasySwipeConfig.Builder(application)
              .direction(Constants.DIRECTION_ALL)
              .style(Constants.STYLE_MIUI)
              .build();
EasySwipeManager.init(config);
}
```
####  Java代码单独配置
```
EasySwipeLayout layout = EasySwipeManager.attach(activity);
if (layout != null) {
     layout.setDirection(Constants.DIRECTION_LEFT);
     layout.setDrawer(new CustomDrawer(this));
     layout.setSwipeListener(
             new OnSwipeListener() {
                 @Override
                 public void onSwipe(int side) {
                     onBackPressed();
                 }
             });
}
```
#### Xml属性 
|名称|类型|描述|
|:---:|:---:|:---:|
|esl_edgeDiff|reference|配置边缘点击容差，默认为2倍系统触摸容差（系统ScaledTouchSlop*2）|
|esl_style|enum|配置拉出的效果，默认MIUI效果|
|esl_specified|string|配置自定义效果的实现类路径，仅当`esl_style`为`custom`时生效|
|esl_resistance|float|配置移动阻尼（默认:`3f`）|
|esl_durationOfClose|int|配置收起效果的时长（默认:`500`）|
|esl_direction|enum|配置支持划出方向（默认:`左边缘往右划`）|

#### java属性设置方法
|名称|参数|描述|
|:---:|:---:|:---:|
|setSwipeListener|OnSwipeListener|配置监听|
|setDirection|int|配置支持划出方向|
|setStyle|int,String|配置拉出的效果|
|setDrawer|Drawer|配置指定自定义效果实现|
|setEdgeDiff|int|配置边缘点击容差|
|setResistance|float|配置移动阻尼|
|setDurationOfClose|int|配置收起效果的时长|

## License

	MIT License

	Copyright (c) 2018 dkzwm

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.

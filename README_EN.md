# EasySwipeLayout
[![Release](https://img.shields.io/badge/JCenter-0.0.1-brightgreen.svg)](https://bintray.com/dkzwm/maven/esl)
[![MinSdk](https://img.shields.io/badge/MinSdk-14-blue.svg)](https://developer.android.com/about/versions/android-4.0.html)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://github.com/dkzwm/SmoothRefreshLayout/blob/master/LICENSE)
## English | [中文](https://github.com/dkzwm/EasySwipeLayout/blob/master/README.md)
A simple sliding layout view

## Features:
- Supports all direction.    
- Supports code addition, xml addition.    
- Supports custom drawer.    
- Supports Multi-Touch.    

## Installation
Add the following dependency to your build.gradle file:
```
dependencies {
    implementation 'me.dkzwm.widget.esl:core:0.0.1'
    AndroidX version
    implementation 'me.dkzwm.widget.esl:core:0.0.1.androidx'
}
```

## Snapshot
![](https://github.com/dkzwm/EasySwipeLayout/blob/master/snapshot/demo.gif)

## Demo
Download [Demo.apk](https://raw.githubusercontent.com/dkzwm/EasySwipeLayout/master/apk/demo.apk)    

## How to use  
#### Xml
```
<?xml version="1.0" encoding="utf-8"?>
<me.dkzwm.widget.esl.EasySwipeLayout
    android:id="@+id/easySwipeLayout"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:esl_direction="right"
    app:esl_specified="@string/specified_the_class_name"
    app:esl_style="custom">
</me.dkzwm.widget.esl.EasySwipeLayout>
```
####  Global
```
EasySwipeConfig config =
      new EasySwipeConfig.Builder(application)
              .direction(Constants.DIRECTION_ALL)
              .style(Constants.STYLE_MIUI)
              .build();
EasySwipeManager.init(config);
}
```
####  Separate
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
#### Xml attr
|Name|Format|Desc|
|:---:|:---:|:---:|
|esl_edgeDiff|reference|Configure the distance in pixels a touch can wander before we think the user is scrolling（default:two times of system ScaledTouchSlop）|
|esl_style|enum|Configure the style of Drawer, (Default: MIUI)|
|esl_specified|string|Configure the path to the implementation class for the custom style，Only valid when `esl_style` is `custom`|
|esl_resistance|float|Configure resistance to touch movement（Default:`3f`）|
|esl_durationOfClose|int|Configure the duration of close Drawer（Default:`500`）|
|esl_direction|enum|Configure support direction（Default:`left`）|

#### Java method
|Name|Params|Desc|
|:---:|:---:|:---:|
|setSwipeListener|OnSwipeListener|Configure listener|
|setDirection|int|Configure support direction|
|setStyle|int,String|Configure the style of Drawer|
|setDrawer|Drawer|Configure the custom Drawer|
|setEdgeDiff|int|Configure the distance in pixels a touch can wander before we think the user is scrolling|
|setResistance|float|Configure resistance to touch movement|
|setDurationOfClose|int|Configure the duration of close Drawer|

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

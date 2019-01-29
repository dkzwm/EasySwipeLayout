/*
 * MIT License
 *
 * Copyright (c) 2018 dkzwm
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
 */
package me.dkzwm.widget.esl;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.dkzwm.widget.esl.annotation.Direction;
import me.dkzwm.widget.esl.annotation.Edge;
import me.dkzwm.widget.esl.annotation.Style;
import me.dkzwm.widget.esl.config.Constants;
import me.dkzwm.widget.esl.graphics.Drawer;

public class EasySwipeConfig {
    private EasySwipeConfig() {}

    private static final OnEasySwipeListener sSwipeListener =
            new OnEasySwipeListener() {
                @Override
                public void onSwipe(@NonNull Activity activity, @Edge int side) {
                    activity.onBackPressed();
                }
            };
    Application mApplication;
    OnEasySwipeListener mSwipeListener = sSwipeListener;
    boolean mOnlyRunMainProcess;
    @Style int mStyle = Constants.STYLE_MIUI;
    String mClassName;
    Drawer mDrawer;
    @Direction int mDirection = Constants.DIRECTION_HORIZONTAL;
    float mResistance = Constants.DEFAULT_RESISTANCE;
    int mDurationOfClose = Constants.DEFAULT_DURATION_OF_CLOSE;
    int mEdgeDiff = -1;

    public static class Builder {
        private EasySwipeConfig mConfig;

        public Builder(Application application) {
            mConfig = new EasySwipeConfig();
            mConfig.mApplication = application;
        }

        public Builder swipeListener(@NonNull OnEasySwipeListener listener) {
            mConfig.mSwipeListener = listener;
            return this;
        }

        public Builder runOnMainProcess(boolean run) {
            mConfig.mOnlyRunMainProcess = run;
            return this;
        }

        public Builder durationOfClose(@IntRange(from = 0) int duration) {
            mConfig.mDurationOfClose = duration;
            return this;
        }

        public Builder style(@Style int style) {
            return style(style, null);
        }

        public Builder style(@Style int style, @Nullable String className) {
            mConfig.mStyle = style;
            mConfig.mClassName = className;
            mConfig.mDrawer = null;
            return this;
        }

        public Builder edgeDiff(@IntRange(from = 0) int edgeDiff) {
            mConfig.mEdgeDiff = edgeDiff;
            return this;
        }

        public Builder resistance(@FloatRange(from = 0) float resistance) {
            mConfig.mResistance = resistance;
            return this;
        }

        public Builder direction(@Direction int direction) {
            mConfig.mDirection = direction;
            return this;
        }

        public Builder drawer(Drawer drawer) {
            mConfig.mStyle = Constants.STYLE_CUSTOM;
            mConfig.mClassName = null;
            mConfig.mDrawer = drawer;
            return this;
        }

        public EasySwipeConfig build() {
            return mConfig;
        }
    }
}

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
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import java.lang.ref.WeakReference;
import java.util.List;

public class EasySwipeManager {
    private static boolean sInitialized = false;

    public static void init(EasySwipeConfig config) {
        if (sInitialized) throw new IllegalArgumentException("Can only be initialized once");
        if (!config.mOnlyRunMainProcess
                || TextUtils.equals(
                        config.mApplication.getPackageName(),
                        getProcessName(config.mApplication))) {
            config.mApplication.registerActivityLifecycleCallbacks(
                    new EasyActivityLifecycleCallback(config));
            sInitialized = true;
        }
    }

    private static String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) return null;
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) return null;
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }

    private static class EasyActivityLifecycleCallback
            implements Application.ActivityLifecycleCallbacks {
        private EasySwipeConfig mConfig;

        private EasyActivityLifecycleCallback(EasySwipeConfig config) {
            mConfig = config;
        }

        @Override
        public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
            if (activity instanceof IgnoreMakeEasy) return;
            View view = activity.getWindow().getDecorView();
            if (view instanceof ViewGroup) {
                ViewGroup decor = (ViewGroup) view;
                View easySwipeLayout = decor.findViewById(R.id.sl_swipe_gesture_layout);
                if (easySwipeLayout != null) return;
                ViewGroup.LayoutParams layoutParams =
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                EasySwipeLayout layout = new EasySwipeLayout(activity);
                layout.setId(R.id.sl_swipe_gesture_layout);
                layout.setSwipeListener(new SafetyListener(activity, mConfig.mSwipeListener));
                layout.setResistance(mConfig.mResistance);
                layout.setDirection(mConfig.mDirection);
                layout.setStyle(mConfig.mStyle, mConfig.mClassName);
                layout.setDurationOfClose(mConfig.mDurationOfClose);
                if (mConfig.mDrawer != null) layout.setDrawer(mConfig.mDrawer);
                layout.setEdgeDiff(mConfig.mEdgeDiff);
                decor.addView(layout, layoutParams);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {}

        @Override
        public void onActivityResumed(Activity activity) {}

        @Override
        public void onActivityPaused(Activity activity) {}

        @Override
        public void onActivityStopped(Activity activity) {}

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (activity instanceof IgnoreMakeEasy) return;
            View view = activity.getWindow().getDecorView();
            if (view instanceof ViewGroup) {
                ViewGroup decor = (ViewGroup) view;
                EasySwipeLayout layout = decor.findViewById(R.id.sl_swipe_gesture_layout);
                if (layout != null) {
                    layout.setSwipeListener(null);
                    decor.removeView(layout);
                }
            }
        }
    }

    private static class SafetyListener implements OnSwipeListener {
        private WeakReference<Activity> mWeakActivity;
        private OnEasySwipeListener mSwipeListener;

        private SafetyListener(Activity activity, OnEasySwipeListener swipeListener) {
            mWeakActivity = new WeakReference<>(activity);
            mSwipeListener = swipeListener;
        }

        @Override
        public void onSwipe(int side) {
            final Activity activity = mWeakActivity.get();
            if (activity != null) {
                mSwipeListener.onSwipe(activity, side);
            }
        }
    }
}

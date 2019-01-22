package me.dkzwm.widget.esl.demo;

import android.app.Application;
import me.dkzwm.widget.esl.EasySwipeConfig;
import me.dkzwm.widget.esl.EasySwipeManager;
import me.dkzwm.widget.esl.config.Constants;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EasySwipeConfig config =
                new EasySwipeConfig.Builder(this)
                        .direction(Constants.DIRECTION_ALL)
                        .style(Constants.STYLE_MIUI)
                        .build();
        EasySwipeManager.init(config);
    }
}

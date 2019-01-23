package me.dkzwm.widget.esl.demo.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import me.dkzwm.widget.esl.EasySwipeLayout;
import me.dkzwm.widget.esl.EasySwipeManager;
import me.dkzwm.widget.esl.IgnoreMakeEasy;
import me.dkzwm.widget.esl.OnSwipeListener;
import me.dkzwm.widget.esl.config.Constants;
import me.dkzwm.widget.esl.demo.R;
import me.dkzwm.widget.esl.demo.graphics.CustomDrawer;

public class CodeCustomActivity extends AppCompatActivity implements IgnoreMakeEasy {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_custom);
        EasySwipeLayout layout = EasySwipeManager.attach(this);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EasySwipeManager.detach(this);
    }
}

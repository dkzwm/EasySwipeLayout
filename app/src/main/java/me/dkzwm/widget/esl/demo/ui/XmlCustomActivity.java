package me.dkzwm.widget.esl.demo.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import me.dkzwm.widget.esl.EasySwipeLayout;
import me.dkzwm.widget.esl.IgnoreMakeEasy;
import me.dkzwm.widget.esl.OnSwipeListener;
import me.dkzwm.widget.esl.demo.R;

public class XmlCustomActivity extends AppCompatActivity implements IgnoreMakeEasy {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_custom);
        EasySwipeLayout layout = findViewById(R.id.easySwipeLayout);
        layout.setSwipeListener(
                new OnSwipeListener() {
                    @Override
                    public void onSwipe(int side) {
                        onBackPressed();
                    }
                });
    }
}

package me.dkzwm.widget.esl.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import me.dkzwm.widget.esl.demo.R;

public class MainActivity extends AppCompatActivity {
    private int mCount = 0;
    private Handler mHandler = new Handler();
    private Runnable mDelayedCountdown =
            new Runnable() {
                @Override
                public void run() {
                    mCount = 0;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_go_xml_custom)
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(
                                        new Intent(MainActivity.this, XmlCustomActivity.class));
                            }
                        });
        findViewById(R.id.button_go_code_custom)
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(
                                        new Intent(MainActivity.this, CodeCustomActivity.class));
                            }
                        });
    }

    @Override
    public void onBackPressed() {
        mCount++;
        mHandler.removeCallbacks(mDelayedCountdown);
        if (mCount < 2) {
            Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_SHORT).show();
            mHandler.postDelayed(mDelayedCountdown, 800);
            return;
        }
        super.onBackPressed();
    }
}

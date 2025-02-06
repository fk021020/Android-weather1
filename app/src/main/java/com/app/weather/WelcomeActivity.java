package com.app.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private TextView tvCountdown;
    private CardView card_logo;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 4000; // 设置倒计时时长，单位为毫秒



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //初始化控件
        tvCountdown = findViewById(R.id.tv_countdown);
        card_logo = findViewById(R.id.card_logo);
        // 启动倒计时
        startCountdown();

        //实现logo缩放动画
        startAnim();

    }

    private void startAnim() {
        ViewCompat.animate(card_logo)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(1000)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {

                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                })
                .start();
    }


    private void startCountdown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                tvCountdown.setText(secondsRemaining + "s | 跳转");
            }

            @Override
            public void onFinish() {
                //跳转到登录页面（看自己逻辑想跳转哪个页面）
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                // 倒计时结束后的操作，例如跳转到主页面
                finish();

            }
        }.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
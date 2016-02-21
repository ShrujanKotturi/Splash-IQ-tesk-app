package com.example.shruj.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SplashScreenActivity extends AppCompatActivity {

    Intent intent;
    CountDownTimer startTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        startTimer = new CountDownTimer(8000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                moveToWelcomeActivity();
            }
        }.start();

        findViewById(R.id.buttonStartQuiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer.cancel();
                moveToWelcomeActivity();

            }
        });

    }

    public void moveToWelcomeActivity() {
        finish();
        intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }
}

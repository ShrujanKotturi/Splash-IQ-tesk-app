package com.example.shruj.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    Intent intent;
    int quizScore = 0;
    TextView textViewResult, textViewGeekDescription;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textViewResult = (TextView) findViewById(R.id.textViewResult);
        textViewGeekDescription = (TextView) findViewById(R.id.textViewGeeknessDescription);
        imageView = (ImageView) findViewById(R.id.imageViewResult);

        if (getIntent().getExtras() != null) {
            quizScore = getIntent().getExtras().getInt(Constants.CURRENT_SCORE);
        }

        setResultView(quizScore);

        findViewById(R.id.buttonQuitResult).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                intent = new Intent(ResultActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.buttonTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                intent = new Intent(ResultActivity.this, QuizActivity.class);
                intent.putExtra(Constants.FROM_RESULT, Constants.BOOLEAN_TRUE);
                startActivity(intent);
            }
        });
    }

    private void setResultView(int quizScore) {
        if (quizScore > -1 && quizScore < 11) {
            textViewResult.setText(Constants.GEEK_LEVEL_NON_GEEK);
            textViewGeekDescription.setText(Constants.GEEK_LEVEL_NON_GEEK_DESCRIPTION);
            imageView.setImageResource(R.drawable.non_geek);
        } else if (quizScore > 10 && quizScore < 51) {
            textViewResult.setText(Constants.GEEK_LEVEL_SEMI_GEEK);
            textViewGeekDescription.setText(Constants.GEEK_LEVEL_SEMI_GEEK_DESCRIPTION);
            imageView.setImageResource(R.drawable.semi_geek);
        } else if (quizScore > 50 && quizScore < 73) {
            textViewResult.setText(Constants.GEEK_LEVEL_UBER_GEEK);
            textViewGeekDescription.setText(Constants.GEEK_LEVEL_UBER_GEEK_DESCRIPTION);
            imageView.setImageResource(R.drawable.uber_geek);
        }
    }
}

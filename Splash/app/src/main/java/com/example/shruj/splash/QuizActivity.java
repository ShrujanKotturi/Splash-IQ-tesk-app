package com.example.shruj.splash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class QuizActivity extends AppCompatActivity {

    Intent intent;
    static List<Questions> questionsList = new ArrayList<>();
    TextView textViewQuestionNumber, textViewQuestionText;
    ImageView imageViewQuestionImage;
    RadioGroup radioGroupQuestionOptions;
    ProgressDialog progressDialog;
    Bitmap bitmapImage;
    InputStream inputStream;
    LinearLayout linearLayoutQuestionOptions, linearLayoutImageView;
    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    TableRow.LayoutParams layoutParamsImage = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    RadioButton radioButton, radioButtonChecked;
    TableRow tableRow;

    Questions currentQuestion;

    int currentQuestionId = 0;
    int currentScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (getIntent().getExtras().getBoolean(Constants.FROM_WELCOME)) {
            questionsList = getIntent().getExtras().getParcelableArrayList(Constants.QUESTIONS_LIST);
        } else if (getIntent().getExtras().getBoolean(Constants.FROM_RESULT)) {
            currentScore = 0;
        }

        tableRow = (TableRow) findViewById(R.id.tableRowQuestionOptions);
        textViewQuestionNumber = (TextView) findViewById(R.id.textViewQuestionNumber);
        textViewQuestionText = (TextView) findViewById(R.id.textViewQuestionText);
        //imageViewQuestionImage = (ImageView) findViewById(R.id.imageViewQuestionImage);
        radioGroupQuestionOptions = (RadioGroup) findViewById(R.id.radioGroupQuestionOptions);
        linearLayoutQuestionOptions = (LinearLayout) findViewById(R.id.linearLayoutQuestionOptions);
        linearLayoutImageView = (LinearLayout) findViewById(R.id.linearLayoutImageView);

        setQuestion();

        findViewById(R.id.buttonQuit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(QuizActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.buttonNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroupQuestionOptions.getCheckedRadioButtonId() != -1) {
                    //currentScore get it from the radiobutton values
                    int id = radioGroupQuestionOptions.getCheckedRadioButtonId();
                    radioButtonChecked = (RadioButton) findViewById(id);
                    currentScore += Integer.parseInt(radioButtonChecked.getTag().toString());
                    currentQuestionId++;
                    setQuestion();
                } else {
                    Constants.ToastMessages(QuizActivity.this, Constants.OPTION_MESSAGE);
                }
            }
        });
    }

    private void setQuestion() {
        if (currentQuestionId < questionsList.size()) {
            currentQuestion = questionsList.get(currentQuestionId);
            textViewQuestionNumber.setText(Constants.QUESTION_TAG + Integer.toString(currentQuestion.questionId + 1));
            textViewQuestionText.setText(currentQuestion.questionText);

            //Question options
            //linearLayoutQuestionOptions;
            radioGroupQuestionOptions.clearCheck();
            radioGroupQuestionOptions.removeAllViews();
            Set<Integer> keys = currentQuestion.options.keySet();
            //call random function
            keys = GetRandomOptions(keys);

            for (Integer key : keys) {
                radioButton = new RadioButton(QuizActivity.this);
                radioButton.setTag(key);
                radioButton.setText(currentQuestion.options.get(key));
                radioButton.setLayoutParams(layoutParams);
                radioGroupQuestionOptions.addView(radioButton);
            }

            if (!currentQuestion.imageUrl.isEmpty()) {
                imageViewQuestionImage = new ImageView(QuizActivity.this);
                linearLayoutImageView.removeAllViews();
                new GetQuestionImage().execute(currentQuestion.imageUrl);
                imageViewQuestionImage.setLayoutParams(layoutParamsImage);
                linearLayoutImageView.addView(imageViewQuestionImage);
            } else {
                imageViewQuestionImage.setImageResource(android.R.color.transparent);
            }
        } else if (currentQuestionId == questionsList.size()) {
            intent = new Intent(QuizActivity.this, ResultActivity.class);
            intent.putExtra(Constants.CURRENT_SCORE, currentScore);
            startActivity(intent);
        }
    }

    private Set<Integer> GetRandomOptions(Set<Integer> keys) {
        Integer[] keyArray = new Integer[keys.size()];
        Integer[] shuffledArray = new Integer[keys.size()];
        int j = 0;
        for (Integer integer : keys) {
            keyArray[j] = integer;
            j++;
        }

        Random rnd = ThreadLocalRandom.current();
        for (int i = keyArray.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = keyArray[index];
            keyArray[index] = keyArray[i];
            keyArray[i] = a;
        }

        for (int k = 0; k < keyArray.length; k++) {
            shuffledArray[k] = keyArray[k];
        }


//        List<Integer> randomIntegerList = new LinkedList<>(keys);
//        Collections.shuffle(randomIntegerList);
//        Set<Integer> randomKeyOptions = new HashSet<>(randomIntegerList.subList(0, keys.size()));
        Set<Integer> randomKeyOptions = new HashSet<>(Arrays.asList(shuffledArray));
        return randomKeyOptions;
    }

    private class GetQuestionImage extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");// "POST"
                inputStream = httpURLConnection.getInputStream();
                bitmapImage = BitmapFactory.decodeStream(inputStream);
                return bitmapImage;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            progressDialog.dismiss();
            imageViewQuestionImage.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(QuizActivity.this);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(Boolean.FALSE);
            progressDialog.show();
        }
    }
}

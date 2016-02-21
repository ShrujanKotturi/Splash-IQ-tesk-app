package com.example.shruj.splash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    Intent intent;
    ProgressDialog progressDialog;
    BufferedReader bufferedReader;
    Button buttonStartQuiz;
    URL url = null;
    List<String> urlInfo = new ArrayList<>();
    Questions individualQuestion;
    List<Questions> questionsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        buttonStartQuiz = (Button) findViewById(R.id.buttonStartQuizWelcome);
        buttonStartQuiz.setEnabled(Boolean.FALSE);

        if (isConnectedOnline()) {
            new GetQuestions().execute(Constants.URL);
        } else {
            Constants.ToastMessages(WelcomeActivity.this, Constants.NO_INTERNET_CONNECTION);
        }

        findViewById(R.id.buttonExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close the application
                finish();
            }
        });

        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                intent = new Intent(WelcomeActivity.this, QuizActivity.class);
                intent.putExtra(Constants.FROM_WELCOME, Constants.BOOLEAN_TRUE);
                intent.putParcelableArrayListExtra(Constants.QUESTIONS_LIST, (ArrayList<? extends Parcelable>) questionsList);
                startActivity(intent);
            }
        });
    }

    public class GetQuestions extends AsyncTask<String, Integer, List<Questions>> {
        @Override
        protected List<Questions> doInBackground(String... params) {
            try {
                for (int i = 0; i < 7; i++) {
                    url = new URL(params[0] + i);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");// "POST"
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    urlInfo.add(stringBuilder.toString());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bufferedReader.close();
                    parseUrlInfoToQuestionsList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(WelcomeActivity.this);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(Boolean.FALSE);
            progressDialog.setMessage(Constants.LOADING_QUESTIONS);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(List<Questions> questionsList) {
            progressDialog.dismiss();
            buttonStartQuiz.setEnabled(Boolean.TRUE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }
    }

    private void parseUrlInfoToQuestionsList() {
        for (String string : urlInfo) {
            String[] str = string.split(Constants.SPLIT_CHARACTER);
            individualQuestion = new Questions(Parcel.obtain());
            individualQuestion.questionId = Integer.parseInt(str[0]);
            if (str[str.length - 1].startsWith(Constants.START_WITH_HTTP)) {
                individualQuestion.imageUrl = str[str.length - 1];
                individualQuestion.questionText = str[1];
                individualQuestion.options = new HashMap<>();
                for (int i = 2; i < str.length - 2; i++) {
                    individualQuestion.options.put(Integer.parseInt(str[i + 1]), str[i]);
                    i++;
                }
                questionsList.add(individualQuestion);
            } else {
                individualQuestion.imageUrl = "";
                individualQuestion.questionText = str[1];
                individualQuestion.options = new HashMap<>();
                for (int i = 2; i < str.length - 1; i++) {
                    individualQuestion.options.put(Integer.parseInt(str[i + 1]), str[i]);
                    i++;
                }
                questionsList.add(individualQuestion);
            }

        }
    }

    private boolean isConnectedOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }
}

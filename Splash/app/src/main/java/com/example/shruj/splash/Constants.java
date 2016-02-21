package com.example.shruj.splash;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by shruj on 02/16/2016.
 */
public class Constants {
    public static String LOADING_QUESTIONS = "Loading Questions..";
    public static String NO_INTERNET_CONNECTION = "Sorry, your Internet is OFF , Turn ON and try again...";
    public static String SPLIT_CHARACTER = ";";
    public static String START_WITH_HTTP = "http";
    public static String QUESTIONS_LIST = "QuestionsList";
    public static String OPTION_MESSAGE = "Please select an option..";
    public static String QUESTION_TAG = "Q";
    public static String CURRENT_SCORE = "CurrentScore";
    public static String FROM_WELCOME = "FromWelcome";
    public static String FROM_RESULT = "FromResult";
    public static String URL = "http://dev.theappsdr.com/apis/spring_2016/hw3/index.php?qid=";
    public static Boolean BOOLEAN_TRUE = Boolean.TRUE;
    public static String GEEK_LEVEL_NON_GEEK = " NON-GEEK";
    public static String GEEK_LEVEL_SEMI_GEEK = " SEMI-GEEK";
    public static String GEEK_LEVEL_UBER_GEEK = " UBER-GEEK";
    public static String GEEK_LEVEL_NON_GEEK_DESCRIPTION = "There isn't a single geeky bone in your " +
            "body. You prefer to party rather than study, " +
            "and have someone else fix your computer, if " +
            "need be. You're just too cool for this. You " +
            "probably don't even wear glasses!";
    public static String GEEK_LEVEL_SEMI_GEEK_DESCRIPTION = "Maybe you're just influenced by the trend, or " +
            "maybe you just got it all perfectly balanced. " +
            "You have some geeky traits, but they aren't " +
            "as \"hardcore\" and they don't take over your " +
            "life. You like some geeky things, but aren't " +
            "nearly as obsessive about them as the " +
            "uber-geeks. You actually get to enjoy both " +
            "worlds";
    public static String GEEK_LEVEL_UBER_GEEK_DESCRIPTION = "You are the geek supreme! You are likely to " +
            "be interested in technology, science, gaming " +
            "and geeky media such as Sci-Fi and " +
            "fantasy. All the mean kids that used to laugh " +
            "at you in high school are now begging you " +
            "for a job. Be proud of your geeky nature, for " +
            "geeks shall inherit the Earth!";

    public static void ToastMessages(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}

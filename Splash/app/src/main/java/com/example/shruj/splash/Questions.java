package com.example.shruj.splash;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shruj on 02/16/2016.
 */
public class Questions implements Parcelable {
    Integer questionId;
    String questionText;
    Map<Integer, String> options;
    String imageUrl;

    protected Questions(Parcel in) {
        options = new HashMap<>();
        questionId = in.readInt();
        questionText = in.readString();
        imageUrl = in.readString();
        options = in.readHashMap(ClassLoader.getSystemClassLoader());
    }

    public static final Creator<Questions> CREATOR = new Creator<Questions>() {
        @Override
        public Questions createFromParcel(Parcel in) {
            return new Questions(in);
        }

        @Override
        public Questions[] newArray(int size) {
            return new Questions[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(questionId);
        dest.writeString(questionText);
        dest.writeString(imageUrl);
        dest.writeMap(options);
    }
}

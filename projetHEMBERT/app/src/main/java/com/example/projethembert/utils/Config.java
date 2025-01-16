package com.example.projethembert.utils;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.projethembert.entities.enums.Difficulty;

public class Config implements Parcelable {
    private Difficulty difficulty;
    private String playerName;

    public Config(Difficulty difficulty){
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setDifficulty(Difficulty difficulty){
        this.difficulty = difficulty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(difficulty.ordinal());
    }

    protected Config(Parcel in){
        difficulty = Difficulty.values()[in.readInt()];
    }

    public static final Creator<Config> CREATOR = new Creator<Config>() {
        @Override
        public Config createFromParcel(Parcel source) {
            return new Config(source);
        }

        @Override
        public Config[] newArray(int size) {
            return new Config[size];
        }
    };
}

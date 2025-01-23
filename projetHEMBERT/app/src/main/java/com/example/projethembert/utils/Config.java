package com.example.projethembert.utils;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.projethembert.entities.enums.Difficulty;

/**
 * Représente la configuration de la partie, définie par le joueur
 */
public class Config implements Parcelable {

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

    /// Difficulté de la partie
    private Difficulty difficulty;
    /// Indique le thème à utiliser
    private boolean isDarkMode;
    /// Nom du joueur
    private String playerName;

    public Config() {
    }

    public Config(Config config) {
        this.difficulty = config.difficulty;
        this.playerName = config.playerName;
        this.isDarkMode = config.isDarkMode;
    }

    protected Config(Parcel in) {
        difficulty = Difficulty.values()[in.readInt()];
        playerName = in.readString();
        isDarkMode = in.readByte() != 0;
    }

    public Config copy() {
        return new Config(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(difficulty.ordinal());
        dest.writeString(playerName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(isDarkMode);
        } else {
            dest.writeByte((byte) (isDarkMode ? 1 : 0));
        }
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isDarkMode() {
        return isDarkMode;
    }

    public void setDarkMode(boolean darkMode) {
        isDarkMode = darkMode;
    }
}

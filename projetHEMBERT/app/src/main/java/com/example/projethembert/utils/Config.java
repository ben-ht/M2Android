package com.example.projethembert.utils;

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

    /// Nom du joueur
    private String playerName;

    public Config(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.playerName = "Player"; // TODO changer nom joueur
    }

    protected Config(Parcel in) {
        difficulty = Difficulty.values()[in.readInt()];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(difficulty.ordinal());
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
}

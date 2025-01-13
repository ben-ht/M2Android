package com.example.projethembert;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Player implements Parcelable {
    private final static int DEFAUlT_POWER = 100;
    private final static int DEFAULT_HEALTH = 10;
    private int power;
    private int health;

    public Player(int power, int health){
        this.power = power;
        this.health = health;
    }

    public Player(){
        power = DEFAUlT_POWER;
        health = DEFAULT_HEALTH;
    }

    public boolean fight(Opponent opponent){
        boolean playerWins = (power * Math.random() - opponent.getPower() * Math.random()) >= 0;
        if (playerWins){
            power += 10;
        } else {
            health -= 3;
        }

        return playerWins;
    }

    public void flee(){
        health--;
    }

    public int getHealth() {
        return health;
    }

    public int getPower() {
        return power;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(power);
        dest.writeInt(health);
    }

    protected Player(Parcel source){
        power = source.readInt();
        health = source.readInt();
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel source) {
            return new Player(source);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}

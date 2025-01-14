package com.example.projethembert.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Représente le joueur
 */
public class Player implements Parcelable {
    /**
     * Creator du Parcelable
     */
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

    /// Points de vie par défaut
    private final static int DEFAULT_HEALTH = 10;

    /// Puissance par défaut
    private final static int DEFAUlT_POWER = 100;

    /// Points de vie
    private int health;

    /// Puissance
    private int power;

    public Player(){
        power = DEFAUlT_POWER;
        health = DEFAULT_HEALTH;
    }

    /// Constructeur du parcelable
    protected Player(Parcel source){
        power = source.readInt();
        health = source.readInt();
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

    /**
     * Effectue le combat contre un monstre et met à jour les points de vie du joueur
     * @param monster Monstre à combattre
     * @return True si le joueur gagne, false sinon
     */
    public boolean fight(Monster monster){
        boolean playerWins = (power * Math.random() - monster.getPower() * Math.random()) >= 0;
        if (playerWins){
            power += 10;
        } else {
            health -= 3;
        }

        return playerWins;
    }

    /**
     * Retire 1 point de vie quand le joueur fuit
     */
    public void flee(){
        health--;
    }

    public int getHealth() {
        return health;
    }

    public int getPower() {
        return power;
    }
}

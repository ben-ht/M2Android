package com.example.projethembert.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.projethembert.utils.Config;


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

    /// Points de vie
    private int health;

    /// Puissance
    private int power;

    private int level;
    private final Config config;

    public Player(Config config){
        this.config = config;
        level = 1;
        power = config.getDifficulty().getPlayerPower();
        health = config.getDifficulty().getPlayerHealth();
    }

    /// Constructeur du parcelable
    protected Player(Parcel source){
        level = source.readInt();
        power = source.readInt();
        health = source.readInt();
        config = source.readParcelable(Config.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(level);
        dest.writeInt(power);
        dest.writeInt(health);
        dest.writeParcelable(config, flags);
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

    public void levelUp(){
        level++;
    }

    public void setDefaultStats(){
        this.health = config.getDifficulty().getPlayerHealth() * level;
        this.power = config.getDifficulty().getPlayerPower() * level;
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

    public int getLevel() {
        return level;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setPower(int power) {
        this.power = power;
    }
}

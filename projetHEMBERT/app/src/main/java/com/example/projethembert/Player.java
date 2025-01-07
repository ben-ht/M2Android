package com.example.projethembert;

import java.io.Serializable;

public class Player implements Serializable {
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
}

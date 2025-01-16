package com.example.projethembert.entities.enums;

import com.example.projethembert.R;

public enum Difficulty {
    EASY(R.string.easy, 20, 100, 150),
    MEDIUM(R.string.medium, 10, 100, 150),
    HARD(R.string.hard, 10, 75, 150),
    CUSTOM(R.string.custom);

    private final int name;
    private int playerHealth;
    private int playerPower;
    private int maxMonsterPower;

    Difficulty(int name){
        this.name = name;
    }
    Difficulty(int name, int playerHealth, int playerPower, int maxMonsterPower){
        this.name = name;
        this.playerHealth = playerHealth;
        this.playerPower = playerPower;
        this.maxMonsterPower = maxMonsterPower;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public int getPlayerPower() {
        return playerPower;
    }

    public int getMaxMonsterPower() {
        return maxMonsterPower;
    }

    public int getName() {
        return name;
    }

    public void setMaxMonsterPower(int maxMonsterPower) {
        this.maxMonsterPower = maxMonsterPower;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public void setPlayerPower(int playerPower) {
        this.playerPower = playerPower;
    }
}

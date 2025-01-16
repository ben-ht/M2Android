package com.example.projethembert.entities.enums;

public enum Difficulty {
    EASY(20, 100, 150),
    MEDIUM(10, 100, 150),
    HARD(10, 75, 150),
    CUSTOM();

    private int playerHealth;
    private int playerPower;
    private int maxMonsterPower;

    Difficulty(){}
    Difficulty(int playerHealth, int playerPower, int maxMonsterPower){
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

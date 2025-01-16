package com.example.projethembert.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "leaderboard")
public class LeaderboardEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String playerName;
    private int levelReached;
    private int power;
    private Date date;
    private String difficulty;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LeaderboardEntry(String playerName,
                            int levelReached,
                            int power,
                            Date date,
                            String difficulty){
        this.playerName = playerName;
        this.levelReached = levelReached;
        this.power = power;
        this.date = date;
        this.difficulty = difficulty;
    }

    public LeaderboardEntry(){}

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getLevelReached() { return levelReached; }

    public void setLevelReached(int levelReached) {
        this.levelReached = levelReached;
    }

    public int getPower() { return power; }

    public void setPower(int power) {
        this.power = power;
    }

    public Date getDate() { return date; }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDifficulty() { return difficulty; }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}

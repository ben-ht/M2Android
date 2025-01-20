package com.example.projethembert.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * Représente une sauvegarde du score d'une partie
 */
@Entity(tableName = "leaderboard")
public class LeaderboardEntry {

    /// Date de la partie
    private Date date;

    /// Difficulté de la partie
    private String difficulty;

    /// Id de la sauvegarde
    @PrimaryKey(autoGenerate = true)
    private int id;

    /// Niveau atteint par le joueur
    private int levelReached;

    /// Nom du joueur
    private String playerName;

    /// Niveau de puissance atteint par le joueur
    private int power;

    public LeaderboardEntry(String playerName,
                            int levelReached,
                            int power,
                            Date date,
                            String difficulty) {
        this.playerName = playerName;
        this.levelReached = levelReached;
        this.power = power;
        this.date = date;
        this.difficulty = difficulty;
    }

    public LeaderboardEntry() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevelReached() {
        return levelReached;
    }

    public void setLevelReached(int levelReached) {
        this.levelReached = levelReached;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}

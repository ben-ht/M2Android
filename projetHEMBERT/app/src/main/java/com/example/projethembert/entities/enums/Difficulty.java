package com.example.projethembert.entities.enums;

import android.content.Context;

import com.example.projethembert.R;

import java.util.Objects;

/**
 * Représente les différents niveaux de difficulté possible
 */
public enum Difficulty {
    EASY(R.string.easy, 20, 100, 150),
    MEDIUM(R.string.medium, 10, 100, 150),
    HARD(R.string.hard, 10, 75, 150),
    CUSTOM(R.string.custom);

    /// Nom de la difficulté "Facile" en bdd
    public final static String DB_EASY = "Easy";
    /// Nom de la difficulté "Moyen" en bdd
    public final static String DB_MEDIUM = "Medium";
    /// Nom de la difficulté "Difficile" en bdd
    public final static String DB_HARD = "Hard";
    /// Nom de la difficulté "Personnalisé" en bdd
    public final static String DB_CUSTOM = "Custom";

    /// Nom de la difficulé affiché sur l'UI
    private final int name;

    /// Santé initiale du joueur
    private int playerHealth;

    /// Puissance initiale du joueur
    private int playerPower;

    /// Puissance maximale du monstre
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

    /**
     *  Retourne le nom de la difficulté en base de donnée. Sert pour l'internationalisation
     * @param difficulty Difficulté affichée sur l'UI
     * @return La chaine à insérer en bdd
     */
    public static String getInDatabaseName(Context context, String difficulty){
        if (Objects.equals(difficulty, context.getString(R.string.easy))){
            return DB_EASY;
        }
        if (Objects.equals(difficulty, context.getString(R.string.medium))){
            return DB_MEDIUM;
        }
        if (Objects.equals(difficulty, context.getString(R.string.hard))){
            return DB_HARD;
        }

        return DB_CUSTOM;
    }
}

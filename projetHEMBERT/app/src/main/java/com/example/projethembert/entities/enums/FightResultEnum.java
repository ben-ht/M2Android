package com.example.projethembert.entities.enums;

import com.example.projethembert.R;

/**
 * Représente les différentes issues à un combat
 */
public enum FightResultEnum {
    WON(R.string.fight_won),
    LOST(R.string.fight_lost),
    FLED(R.string.fight_fled);

    /// Id de la ressource (message) à afficher sur l'UI
    private final int message;

    FightResultEnum(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }
}
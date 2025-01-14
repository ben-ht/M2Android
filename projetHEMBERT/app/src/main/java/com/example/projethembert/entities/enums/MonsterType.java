package com.example.projethembert.entities.enums;

import com.example.projethembert.R;

/**
 * Représente les différents types de monstre
 */
public enum MonsterType {
    ASSASSIN(R.drawable.monster_assassin, R.string.monster_assassin),
    ORC(R.drawable.monster_orc, R.string.monster_orc),
    DRAGON(R.drawable.monster_dragon, R.string.monster_dragon),
    VAMPIRE(R.drawable.monster_vampire, R.string.monster_vampire);

    /// Image du monstre
    private final int asset;

    /// Id de la ressource du type de monstre
    private final int name;

    MonsterType(int asset, int name) {
        this.asset = asset;
        this.name = name;
    }

    public int getAsset() {
        return asset;
    }

    public int getName() {
        return name;
    }
}

package com.example.projethembert;

public enum MonsterType {
    ASSASSIN(R.drawable.kenku_head, "Assassin"),
    ORC(R.drawable.orc_head, "Orc"),
    DRAGON(R.drawable.spiked_dragon_head, "Dragon"),
    VAMPIRE(R.drawable.vampire_dracula, "Vampire");

    private final int asset;
    private final String name;

    MonsterType(int asset, String name) {
        this.asset = asset;
        this.name = name;
    }

    public int getAsset() {
        return asset;
    }

    public String getName() {
        return name;
    }
}

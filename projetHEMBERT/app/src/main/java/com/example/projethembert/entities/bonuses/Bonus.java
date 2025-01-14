package com.example.projethembert.entities.bonuses;

import android.os.Parcelable;

import com.example.projethembert.entities.Player;

public interface Bonus extends Parcelable {
    void use(Player player);
    String getName();
    String getDescription();
}

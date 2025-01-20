package com.example.projethembert.entities.bonuses;

import android.os.Parcelable;

import com.example.projethembert.entities.Player;

/**
 * Contrat d'un bonus trouvable dans une salle
 */
public interface Bonus extends Parcelable {

    /// Applique l'effet du bonus
    void use(Player player);
    int getName();
    int getDescription();
    int getImage();
}

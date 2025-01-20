package com.example.projethembert.entities.bonuses;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.example.projethembert.R;
import com.example.projethembert.entities.Player;

import java.util.Random;

/**
 * Représente un charme de puissance
 */
public class PowerCharm implements Bonus {

    public static final Creator<PowerCharm> CREATOR = new Creator<PowerCharm>() {
        @Override
        public PowerCharm createFromParcel(Parcel source) {
            return new PowerCharm(source);
        }

        @Override
        public PowerCharm[] newArray(int size) {
            return new PowerCharm[size];
        }
    };

    /// Puissance maximale du charme de puissance
    private static final int MAX_POWER = 10;

    /// Puissance minimale du charme de puissance
    private static final int MIN_POWER = 5;
    private static final Random RANDOM = new Random();

    /// Puissance comprise entre MIN_POWER et MAX_POWER
    private final int power;

    /// Description affiché sur l'UI
    private int description = R.string.power_charm_desc;

    /// Nom affiché sur l'UI
    private int name = R.string.power_charm;

    public PowerCharm() {
        power = RANDOM.nextInt(MAX_POWER - MIN_POWER + 1) + MIN_POWER;
    }

    protected PowerCharm(Parcel in) {
        name = in.readInt();
        description = in.readInt();
        power = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(name);
        dest.writeInt(description);
        dest.writeInt(power);
    }

    @Override
    public void use(Player player) {
        player.setPower(player.getPower() + power);
    }

    @Override
    public int getName() {
        return name;
    }

    @Override
    public int getDescription() {
        return description;
    }

    @Override
    public int getImage() {
        return R.drawable.bonus_power_charm;
    }
}

package com.example.projethembert.entities.bonuses;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.example.projethembert.R;
import com.example.projethembert.entities.Player;

import java.util.Random;

public class MagicPotion implements Bonus{
    private static final Random RANDOM = new Random();
    private static final int MIN_HEALTH = 1;
    private static final int MAX_HEALTH = 3;
    private int name = R.string.magic_potion;
    private int description = R.string.magic_potion_desc;
    private final int health;

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(name);
        dest.writeInt(description);
        dest.writeInt(health);
    }

    public MagicPotion(){
        health = RANDOM.nextInt(MAX_HEALTH - MIN_HEALTH + 1) + MIN_HEALTH;
    }

    protected MagicPotion(Parcel in) {
        name = in.readInt();
        description = in.readInt();
        health = in.readInt();
    }

    public static final Creator<MagicPotion> CREATOR = new Creator<MagicPotion>() {
        @Override
        public MagicPotion createFromParcel(Parcel source) {
            return new MagicPotion(source);
        }

        @Override
        public MagicPotion[] newArray(int size) {
            return new MagicPotion[size];
        }
    };

    @Override
    public void use(Player player) {
        player.setHealth(player.getHealth() + health);
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
        return (R.drawable.bonus_health_potion);
    }
}

package com.example.projethembert.entities.bonuses;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.example.projethembert.R;
import com.example.projethembert.entities.Player;

import java.util.Random;

public class PowerCharm implements Bonus{

    private static final Random RANDOM = new Random();
    private static final int MIN_POWER = 5;
    private static final int MAX_POWER = 10;
    private int name = R.string.power_charm;
    private int description = R.string.power_charm_desc;
    private final int power;


    public PowerCharm(){
        power = RANDOM.nextInt(MAX_POWER - MIN_POWER + 1) + MIN_POWER;
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

    protected PowerCharm(Parcel in) {
        name = in.readInt();
        description = in.readInt();
        power = in.readInt();
    }

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

    @Override
    public int getImage() {
        return R.drawable.bonus_power_charm;
    }
}

package com.example.projethembert.entities.bonuses;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.example.projethembert.entities.Player;

public class PowerCharm implements Bonus{

    private String name;
    private String description;
    private int power;

    public PowerCharm(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(power);
    }

    @Override
    public void use(Player player) {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    protected PowerCharm(Parcel in) {
        name = in.readString();
        description = in.readString();
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
}

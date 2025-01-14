package com.example.projethembert.entities.bonuses;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.example.projethembert.entities.Player;

public class MagicPotion implements Bonus{
    private String name;

    private String description;
    private int power;

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

    public MagicPotion(){

    }

    protected MagicPotion(Parcel in) {
        name = in.readString();
        description = in.readString();
        power = in.readInt();
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

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}

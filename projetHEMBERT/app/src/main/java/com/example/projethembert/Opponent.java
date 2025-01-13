package com.example.projethembert;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Opponent implements Parcelable {
    private int power;

    public Opponent(){
        power = (int) Math.round(Math.random() * 150);
    }

    public int getPower(){
        return power;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(power);
    }

    protected Opponent(Parcel source){
        power = source.readInt();
    }

    public static final Creator<Opponent> CREATOR = new Creator<Opponent>() {
        @Override
        public Opponent createFromParcel(Parcel source) {
            return new Opponent(source);
        }

        @Override
        public Opponent[] newArray(int size) {
            return new Opponent[size];
        }
    };
}

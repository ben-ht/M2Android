package com.example.projethembert;

import java.io.Serializable;

public class Opponent implements Serializable {
    private int power;

    public Opponent(){
        power = (int) Math.round(Math.random() * 150);
    }

    public int getPower(){
        return power;
    }
}

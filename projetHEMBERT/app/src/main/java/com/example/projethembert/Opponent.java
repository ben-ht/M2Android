package com.example.projethembert;

public class Opponent {
    private int power;

    public Opponent(){
        power = (int) Math.round(Math.random() * 150);
    }

    public int getPower(){
        return power;
    }
}

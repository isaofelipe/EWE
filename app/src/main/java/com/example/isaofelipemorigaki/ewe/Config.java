package com.example.isaofelipemorigaki.ewe;

import android.app.Application;

public class Config extends Application {

    public static final double minRange = 3;

    public static double getMinRange() {
        return minRange;
    }
}

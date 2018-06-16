package com.example.isaofelipemorigaki.ewe.GD;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Beacon.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract BeaconDAO beaconDAO();
}
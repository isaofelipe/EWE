package com.example.isaofelipemorigaki.ewe.GD;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface BeaconDAO {
    @Query("SELECT * FROM beacons")
    List<Beacons> getAll();

    @Query("SELECT * FROM beacons WHERE id IN (:beaconIds)")
    List<Beacons> loadAllByIds(int[] beaconIds);

    @Query("SELECT * FROM beacons WHERE instance LIKE :instance LIMIT 1")
    Beacons findByInstance(String instance);

    @Insert
    void insertAll(Beacons... users);

    @Delete
    void delete(Beacons user);
}

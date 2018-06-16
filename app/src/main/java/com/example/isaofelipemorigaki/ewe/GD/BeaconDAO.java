package com.example.isaofelipemorigaki.ewe.GD;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface BeaconDAO {
    @Query("SELECT * FROM beacon")
    List<Beacon> getAll();

    @Query("SELECT * FROM beacon WHERE id IN (:beaconIds)")
    List<Beacon> loadAllByIds(int[] beaconIds);

    @Query("SELECT * FROM beacon WHERE instance LIKE :instance LIMIT 1")
    Beacon findByInstance(String instance);

    @Insert
    void insertAll(Beacon... users);

    @Delete
    void delete(Beacon user);
}

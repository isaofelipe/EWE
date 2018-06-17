package com.example.isaofelipemorigaki.ewe.GD;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ColaboradorDAO {
    @Query("SELECT * FROM colaboradores")
    List<Colaboradores> getAll();

    @Query("SELECT * FROM colaboradores WHERE id IN (:colaboradorIds)")
    List<Colaboradores> loadAllByIds(int[] colaboradorIds);

    @Query("SELECT * FROM colaboradores WHERE login LIKE :login LIMIT 1")
    Colaboradores findByInstance(String login);

    @Insert
    void insertAll(Colaboradores... users);

    @Delete
    void delete(Colaboradores user);
}

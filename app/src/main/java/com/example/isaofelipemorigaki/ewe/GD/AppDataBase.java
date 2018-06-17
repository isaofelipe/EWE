package com.example.isaofelipemorigaki.ewe.GD;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Beacons.class, Colaboradores.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract BeaconDAO beaconDAO();
    public abstract ColaboradorDAO colaboradorDAO();

    private static AppDataBase INSTANCE;

    public static AppDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class, "app_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
        new RoomDatabase.Callback(){

            @Override
            public void onOpen (@NonNull SupportSQLiteDatabase db){
                super.onOpen(db);
                new PopulateDbAsync(INSTANCE).execute();
            }
        };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final BeaconDAO beaconDao;
        private final ColaboradorDAO colaboradorDAO;

        PopulateDbAsync(AppDataBase db) {
            beaconDao = db.beaconDAO();
            colaboradorDAO = db.colaboradorDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            Beacons beacon = new Beacons("0x000005000bc1", "entrada do prédio azul", "Você está entrando no prédio azul", "Cuidado, o chão está molhado");
            Beacons beacon2 = new Beacons("0x000000000001", "Corredor prédio azul", "Vire à esquerda para acesso a escada e a direita para laboratórios e rampas para outros pavimentos",
                    "Escada em manutenção");
            Beacons beacon3 = new Beacons("0x0000000000002", "Corredor prédio azul", "Vire à esquerda para acesso a escada e a direita para laboratórios e rampas para outros pavimentos",
                    "Escada em manutenção");
            Colaboradores colaborador1 = new Colaboradores("Edislaine", "edi@linda.com", "123123");
            beaconDao.insertAll(beacon, beacon2);
            colaboradorDAO.insertAll(colaborador1);
            return null;
        }
    }
}
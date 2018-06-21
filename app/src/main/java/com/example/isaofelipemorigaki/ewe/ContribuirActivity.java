package com.example.isaofelipemorigaki.ewe;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.isaofelipemorigaki.ewe.GD.AppDataBase;
import com.example.isaofelipemorigaki.ewe.GD.Beacons;
import com.example.isaofelipemorigaki.ewe.GD.Colaboradores;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContribuirActivity extends AppCompatActivity {
    private AutoCompleteTextView identificacaoDispositivoView;
    private AutoCompleteTextView localDispositivoView;
    private EditText mensagemFixaView;
    private EditText mensagemTemporariaView;
    private Button botao_contribuir;
    AppDataBase appDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribuir);
        identificacaoDispositivoView = findViewById(R.id.identificacaoDispositivo);
        identificacaoDispositivoView.setEnabled(false);
        localDispositivoView = findViewById(R.id.localDispositivo);
        mensagemFixaView = findViewById(R.id.mensagemFixaView);
        mensagemTemporariaView = findViewById(R.id.mensagemTemporariaView);

        identificacaoDispositivoView.setText(getIntent().getStringExtra("idDispositivo"));
        localDispositivoView.setText(getIntent().getStringExtra("localDispositivo"));
        mensagemFixaView.setText(getIntent().getStringExtra("mensagemFixa"));
        mensagemTemporariaView.setText(getIntent().getStringExtra("mensagemTemporaria"));

        botao_contribuir = findViewById(R.id.btnContribuir);
        botao_contribuir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appDataBase = Room.databaseBuilder(getApplicationContext(),
                        AppDataBase.class, "app_database").build();
                new ContribuirActivity.DatabaseAsync().execute();
            }
        });
    }

    private class DatabaseAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Beacons beacon = appDataBase.beaconDAO().findByInstance(identificacaoDispositivoView.getText().toString());
            beacon.setLocal(localDispositivoView.getText().toString());
            beacon.setMensagemFixa(mensagemFixaView.getText().toString());
            beacon.setMensagemTemporaria(mensagemTemporariaView.getText().toString());
            appDataBase.beaconDAO().update(beacon);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Operação realizada com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

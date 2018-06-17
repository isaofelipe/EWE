package com.example.isaofelipemorigaki.ewe;

import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.isaofelipemorigaki.ewe.GD.AppDataBase;
import com.example.isaofelipemorigaki.ewe.GD.Beacons;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

public class ContribuirActivity extends AppCompatActivity {
    private AutoCompleteTextView identificacaoDispositivoView;
    private AutoCompleteTextView localDispositivoView;
    private EditText mensagemFixaView;
    private EditText mensagemTemporariaView;
    private Button botao_contribuir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribuir);
        identificacaoDispositivoView = findViewById(R.id.identificacaoDispositivo);
        localDispositivoView = findViewById(R.id.localDispositivo);
        mensagemFixaView = findViewById(R.id.mensagemFixaView);
        mensagemTemporariaView = findViewById(R.id.mensagemTemporariaView);

        identificacaoDispositivoView.setText(getIntent().getStringExtra("idDispositivo"));
        localDispositivoView.setText(getIntent().getStringExtra("localDispositivo"));
        mensagemFixaView.setText(getIntent().getStringExtra("mensagemFixa"));
        mensagemTemporariaView.setText(getIntent().getStringExtra("mensagemTemporaria"));

        botao_contribuir = findViewById(R.id.btnContribuir);


    }


}

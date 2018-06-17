package com.example.isaofelipemorigaki.ewe;

import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

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

public class ContribuirActivity extends AppCompatActivity implements BeaconConsumer, RangeNotifier {
    private BeaconManager mBeaconManager;
    private String ultimoBeacon;
    private AutoCompleteTextView campoBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribuir);

        Button botao_contribuir = findViewById(R.id.btnContribuir);

    }

    @Override
    public void onBeaconServiceConnect() {
        // Encapsulates a beacon identifier of arbitrary byte length
        ArrayList<Identifier> identifiers = new ArrayList<>();

        // Set null to indicate that we want to match beacons with any value
        identifiers.add(null);
        // Represents a criteria of fields used to match beacon
        Region region = new Region("AllBeaconsRegion", identifiers);
        try {
            // Tells the BeaconService to start looking for beacons that match the passed Region object
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // Specifies a class that should be called each time the BeaconService gets ranging data, once per second by default
        mBeaconManager.addRangeNotifier(this);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (beacons.size() > 0) {
            Beacon beacon = beacons.iterator().next();
            Identifier instance = beacon.getId2();
            double distancia = beacon.getDistance();
            campoBeacon = findViewById(R.id.identificacaoDispositivo);
            if (!instance.toString().equals(ultimoBeacon) && distancia <= 6){
                Beacons beaconDetectado = AppDataBase.getDatabase(this).beaconDAO().findByInstance(instance.toString());
                campoBeacon.setText(beaconDetectado.getId());
            }
        }
    }
}

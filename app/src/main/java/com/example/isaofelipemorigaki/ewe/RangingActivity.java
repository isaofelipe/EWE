package com.example.isaofelipemorigaki.ewe;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

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
import java.util.Locale;

public class RangingActivity extends AppCompatActivity implements BeaconConsumer, RangeNotifier, TextToSpeech.OnInitListener {
    protected static final String TAG = "RangingActivity";
    private BeaconManager mBeaconManager;
    private TextToSpeech tts;
    static public final Locale BRAZIL = new Locale("pt_BR_", "pt", "BR");
    private String ultimoBeacon;
    public static Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        if (sp.getBoolean("logado", false)){
            startActivity(new Intent(RangingActivity.this, ColaboradorActivity.class));
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);


        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        mBeaconManager.bind(this);
        checkLocationPermission();

        tts = new TextToSpeech(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login_nav:
                startActivity(new Intent(RangingActivity.this, LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
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
            if (!instance.toString().equals(ultimoBeacon) && distancia <= ((Config)this.getApplication()).getMinRange()){
                Beacons beaconDetectado = AppDataBase.getDatabase(this).beaconDAO().findByInstance(instance.toString());
                if (beaconDetectado != null){
                    ultimoBeacon = beaconDetectado.getInstance();
                    String texto = beaconDetectado.getMensagemFixa() + " " + beaconDetectado.getMensagemTemporaria();
                    speakOut(texto);
                }
            }
        }
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(BRAZIL);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                //speakOut("Modo navegação");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }
    private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        speakOut("Modo navegação.");
        getDelegate().onStart();
    }
    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBeaconManager.unbind(this);
    }
}
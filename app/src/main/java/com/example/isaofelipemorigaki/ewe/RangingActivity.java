package com.example.isaofelipemorigaki.ewe;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.isaofelipemorigaki.ewe.firebase.Beacons;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
import java.util.Locale;

public class RangingActivity extends AppCompatActivity implements BeaconConsumer, RangeNotifier, TextToSpeech.OnInitListener {
    protected static final String TAG = "RangingActivity";
    private BeaconManager mBeaconManager;
    private TextToSpeech tts;
    static public final Locale BRAZIL = new Locale("pt_BR_", "pt", "BR");
    private String ultimoBeacon;
    public static Handler h;
    private List<Beacons> listaBeacons = new ArrayList<>();
    private DatabaseReference dbBeacons;
    private ValueEventListener bListener;

    ArrayList<Identifier> identifiers = new ArrayList<>();

    Region region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        if (sp.getBoolean("logado", false)){
            Intent intent = new Intent(RangingActivity.this, ColaboradorActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            finish();
            startActivity(intent);
        }
        else{
            setContentView(R.layout.activity_ranging);

            identifiers.add(null);
            region = new Region("AllBeaconsRegion", identifiers);
            mBeaconManager = BeaconManager.getInstanceForApplication(this);
            mBeaconManager.getBeaconParsers().add(new BeaconParser().
                    setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
            mBeaconManager.bind(this);
            checkLocationPermission();

            tts = new TextToSpeech(this, this);

            dbBeacons = FirebaseDatabase.getInstance().getReference("beacons");
        }
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
                int indexBeacon = listaBeacons.lastIndexOf(new Beacons(instance.toString()));
                Beacons beaconDetectado = null;
                if (indexBeacon >= 0)
                    beaconDetectado = listaBeacons.get(indexBeacon);
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

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaBeacons = new ArrayList<>();
                for(DataSnapshot entry : (Iterable<DataSnapshot>) dataSnapshot.getChildren()){
                    listaBeacons.add(entry.getValue(Beacons.class));
                }
                ultimoBeacon = "";
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Erro Firebase", Toast.LENGTH_SHORT).show();
            }
        };
        dbBeacons.addValueEventListener(listener);
        bListener = listener;
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
        try{
            mBeaconManager.stopRangingBeaconsInRegion(region);
        }
        catch(RemoteException e){

        }
        mBeaconManager.removeAllRangeNotifiers();
        mBeaconManager.unbind(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(bListener != null){
            dbBeacons.removeEventListener(bListener);
        }
    }
}
package com.example.isaofelipemorigaki.ewe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;

public class ColaboradorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BeaconConsumer, RangeNotifier {
    private TextView viewNome;
    private TextView viewLogin;
    private TextView idTextView;
    private TextView localTextView;
    private TextView mensagemTemporariaTextView;
    private TextView mensagemFixaTextView;
    private BeaconManager mBeaconManager;
    private String ultimoBeacon;
    private String identificacaoDetectada;
    private String localDetectado;
    private String mensagemFixaDetectada;
    private String mensagemTemporariaDetectada;
    private FloatingActionButton botao_colaborar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colaborador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        botao_colaborar = findViewById(R.id.fab);
        botao_colaborar.setEnabled(false);
        botao_colaborar.setAlpha(0.5f);
        botao_colaborar.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ColaboradorActivity.this, ContribuirActivity.class);
                i.putExtra("idDispositivo", identificacaoDetectada);
                i.putExtra("localDispositivo", localDetectado);
                i.putExtra("mensagemFixa", mensagemFixaDetectada);
                i.putExtra("mensagemTemporaria", mensagemTemporariaDetectada);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        viewNome = headerView.findViewById(R.id.viewNome);
        viewNome.setText(sp.getString("nome", "Android"));
        viewLogin = headerView.findViewById(R.id.viewLogin);
        viewLogin.setText(sp.getString("login", "email@email.com"));

        idTextView = findViewById(R.id.textViewId);
        localTextView = findViewById(R.id.textViewLocal);
        mensagemTemporariaTextView = findViewById(R.id.textViewMensagemFixa);
        mensagemFixaTextView = findViewById(R.id.textViewmensagemTemporaria);

        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        mBeaconManager.bind(this);

    }
    @Override
    protected void onResume() {
        ultimoBeacon = "";
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.colaborado, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
                startActivity(new Intent(ColaboradorActivity.this, AboutAcitivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_historico) {
            // Abrir histórico
        } else if (id == R.id.nav_sair) {
            SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
            sp.edit().putBoolean("logado", false).apply();
            sp.edit().putString("login", "").apply();
            sp.edit().putString("nome", "").apply();

            Intent intent = new Intent(ColaboradorActivity.this, RangingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_perfil) {
            startActivity(new Intent(ColaboradorActivity.this, PerfilActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                    identificacaoDetectada = beaconDetectado.getInstance();
                    localDetectado = beaconDetectado.getLocal();
                    mensagemFixaDetectada = beaconDetectado.getMensagemFixa();
                    mensagemTemporariaDetectada = beaconDetectado.getMensagemTemporaria();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            idTextView.setText(identificacaoDetectada);
                            localTextView.setText(localDetectado);
                            mensagemFixaTextView.setText(mensagemFixaDetectada);
                            mensagemTemporariaTextView.setText(mensagemTemporariaDetectada);
                            botao_colaborar.setEnabled(true);
                            botao_colaborar.setAlpha(1f);
                        }
                    });
                }
            }
        }
    }
}

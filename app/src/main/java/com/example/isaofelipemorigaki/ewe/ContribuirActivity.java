package com.example.isaofelipemorigaki.ewe;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.isaofelipemorigaki.ewe.firebase.Beacons;
import com.example.isaofelipemorigaki.ewe.firebase.Contribuicao;
import com.google.common.collect.Iterables;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ContribuirActivity extends AppCompatActivity {
    private AutoCompleteTextView identificacaoDispositivoView;
    private AutoCompleteTextView localDispositivoView;
    private EditText mensagemFixaView;
    private EditText mensagemTemporariaView;
    private Button botao_contribuir;
    String bInstance;
    String bLocal;
    String bMensagemFixa;
    String bMensagemTemporaria;

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
                new ContribuirActivity.DatabaseAsync().execute();
            }
        });
    }

    private class DatabaseAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            bInstance = identificacaoDispositivoView.getText().toString();
            bLocal = localDispositivoView.getText().toString();
            bMensagemFixa = mensagemFixaView.getText().toString();
            bMensagemTemporaria = mensagemTemporariaView.getText().toString();

            DatabaseReference dbCBeacons = FirebaseDatabase.getInstance().getReference("beacons");
            Query query = dbCBeacons.orderByChild("instance").equalTo(bInstance);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Beacons beacon = Iterables.get(dataSnapshot.getChildren(), 0).getValue(Beacons.class);
                        beacon.setLocal(bLocal);
                        beacon.setMensagemFixa(bMensagemFixa);
                        beacon.setMensagemTemporaria(bMensagemTemporaria);
                        DatabaseReference dbBeacons = FirebaseDatabase.getInstance().getReference("beacons");
                        dbBeacons.child(String.valueOf(beacon.getId())).setValue(beacon);

                        adicionarHistorico(beacon);
                    }
                }

                private void adicionarHistorico(Beacons beacon) {
                    DatabaseReference dbContribuicao = FirebaseDatabase.getInstance().getReference("contribuicao");
                    SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                    Contribuicao contribuicao = new Contribuicao(dbContribuicao.push().getKey(), beacon.getNamespace(),
                            beacon.getInstance(), beacon.getLocal(), beacon.getMensagemFixa(), beacon.getMensagemTemporaria(), beacon.getId(), sp.getString("id", ""));
                    dbContribuicao.child(contribuicao.getId()).setValue(contribuicao);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Erro Firebase", Toast.LENGTH_SHORT).show();
                }
            });
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

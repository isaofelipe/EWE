package com.example.isaofelipemorigaki.ewe;

import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaofelipemorigaki.ewe.firebase.Contribuicao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoricoActivity extends ListActivity{
    List<Contribuicao> listaContribuicoes;

    String[] items={"EDI 1 1", "EDI 2", "EDI 3","EDI 4", " EDI 5", " ITEM 6"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        buscarContribuicoes();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
    }
    private void buscarContribuicoes() {
        DatabaseReference dbContribuicao = FirebaseDatabase.getInstance().getReference("contribuicao");
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        String idColaborador = sp.getString("id", "");
        Query query = dbContribuicao.orderByChild("idColaborador").equalTo(idColaborador);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listaContribuicoes = new ArrayList<>();
                    for (DataSnapshot entry : dataSnapshot.getChildren()){
                        listaContribuicoes.add(entry.getValue(Contribuicao.class));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setListAdapter(new IconicAdapter(HistoricoActivity.this));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Erro Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class IconicAdapter extends ArrayAdapter {
        Activity context;
        IconicAdapter(Activity context) {
            super(context, R.layout.acitivity_historico_row, listaContribuicoes);
            this.context = context;
        }
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = context.getLayoutInflater();
            View row = inflater.inflate(R.layout.acitivity_historico_row, null);
            TextView beacon = (TextView) row.findViewById(R.id.id_beacon_historico);
            TextView mensagemFixa = (TextView) row.findViewById(R.id.mensagem_fixa_historico);
            TextView mensagemTemp = (TextView) row.findViewById(R.id.mensagem_temp_historico);
            TextView beaconLocal = row.findViewById(R.id.beacon_local);

            beacon.setText(listaContribuicoes.get(position).getInstance());
            mensagemFixa.setText(listaContribuicoes.get(position).getMensagemFixa());
            mensagemTemp.setText(listaContribuicoes.get(position).getMensagemTemporaria());
            beaconLocal.setText(listaContribuicoes.get(position).getLocal());
            return (row);
        }
    }
}

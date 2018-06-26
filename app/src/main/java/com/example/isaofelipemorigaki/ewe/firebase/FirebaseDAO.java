package com.example.isaofelipemorigaki.ewe.firebase;

import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDAO {

    public static FirebaseDatabase db = FirebaseDatabase.getInstance();
    public static DatabaseReference dbColaboradores = FirebaseDatabase.getInstance().getReference("colaboradores");

    public static void inserirColaborador(Colaboradores colaborador){
        colaborador.setId(dbColaboradores.push().getKey());
        dbColaboradores.child(colaborador.getId()).setValue(colaborador);
    }

    /*public static Colaboradores buscarColaborador(String login){
        Query query = dbColaboradores.orderByChild("login").equalTo(login);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Colaboradores colaborador = dataSnapshot.getValue(Colaboradores.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/
}

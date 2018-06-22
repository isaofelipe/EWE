package com.example.isaofelipemorigaki.ewe;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PerfilActivity extends AppCompatActivity {
    private TextView viewNome;
    private TextView viewLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        viewNome = findViewById(R.id.nome_perfil);
        viewNome.setText(sp.getString("nome", "Android"));
        viewLogin = findViewById(R.id.login_perfil);
        viewLogin.setText(sp.getString("login", "email@email.com"));

    }
}

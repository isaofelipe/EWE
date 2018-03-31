package com.example.isaofelipemorigaki.ewe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ColaboradorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colaborador);

        ImageButton botao_historico = findViewById(R.id.botao_historico);
        botao_historico.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ColaboradorActivity.this, HistoricoActivity.class);
                startActivity(i);
            }
        });

        ImageButton botao_validar = findViewById(R.id.botao_validar);
        botao_validar.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ColaboradorActivity.this, ValidarActivity.class);
                startActivity(i);
            }
        });

        ImageButton botao_colaborar = findViewById(R.id.botao_colaborar);
        botao_colaborar.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ColaboradorActivity.this, ContribuirActivity.class);
                startActivity(i);
            }
        });
    }
}

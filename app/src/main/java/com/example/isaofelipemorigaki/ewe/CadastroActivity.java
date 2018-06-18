package com.example.isaofelipemorigaki.ewe;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.isaofelipemorigaki.ewe.GD.AppDataBase;
import com.example.isaofelipemorigaki.ewe.GD.Colaboradores;

import java.util.List;

public class CadastroActivity extends AppCompatActivity{
    private Button cadastrar;
    private EditText nome;
    private EditText login;
    private EditText senha;
    private EditText senha2;
    AppDataBase appDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        cadastrar = findViewById(R.id.cadastro_button);
        nome = findViewById(R.id.nome_colaborador);
        login = findViewById(R.id.login);
        senha = findViewById(R.id.senha);
        senha2 = findViewById(R.id.senha2);

        cadastrar.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (senha.getText() == senha2.getText()) {
                appDataBase = Room.databaseBuilder(getApplicationContext(),
                AppDataBase.class, "app_database").build();
                new DatabaseAsync().execute();
                }else{
                    Toast.makeText(getApplicationContext(), "Senha não confere!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private class DatabaseAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Colaboradores colaborador = new Colaboradores(nome.getText().toString(), login.getText().toString(), senha.getText().toString());
            appDataBase.colaboradorDAO().insertAll(colaborador);
            List<Colaboradores> lista = appDataBase.colaboradorDAO().getAll();
         /*   for (Colaboradores c : lista) {
                System.out.println("Dado: " + lista);
                appDataBase.colaboradorDAO().delete(c);
            } */

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(CadastroActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
            finish();
        }
    }

}

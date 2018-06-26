package com.example.isaofelipemorigaki.ewe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.isaofelipemorigaki.ewe.firebase.Colaboradores;
import com.example.isaofelipemorigaki.ewe.firebase.FirebaseDAO;

public class CadastroActivity extends AppCompatActivity{
    private Button cadastrar;
    private EditText nome;
    private EditText login;
    private EditText senha;
    private EditText senha2;

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
                if (senha.getText().toString().equals(senha2.getText().toString()) && !nome.getText().toString().isEmpty() && !login.getText().toString().isEmpty() && !senha.getText().toString().isEmpty()){
                    FirebaseDAO.inserirColaborador(new Colaboradores(nome.getText().toString(), login.getText().toString(), senha.getText().toString()));
                    Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(CadastroActivity.this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Senha não confere!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

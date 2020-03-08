package br.usp.ardhouse.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import br.usp.R;
import br.usp.ardhouse.controller.MainActivityController;

public class MainActivity extends AppCompatActivity {

    private String nomeArduino;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivityController controller = new MainActivityController(MainActivity.this.getBaseContext());
        nomeArduino = controller.lerArduinoAtual();
        TextView exibicao = findViewById(R.id.arduinoAtual);
        if(nomeArduino != null) {
            exibicao.setText(exibicao.getText().toString() + "" + nomeArduino);
        }else {
            exibicao.setText(exibicao.getText().toString() + "Nenhum arduino selecionado");
        }
    }

    // Método para levar usuário à tela de selecionar um novo Arduino
    public void selecionarArduino(View view){
        Intent intent = new Intent(this, SelecionarArduinoActivity.class);
        startActivity(intent);
    }
}
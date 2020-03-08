package br.usp.ardhouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import br.usp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Método para levar usuário à tela de selecionar um novo Arduino
    public void selecionarArduino(View view){
        Intent intent = new Intent(this, SelecionarArduinoActivity.class);
        startActivity(intent);
    }
}
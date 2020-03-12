package br.usp.ardhouse.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.usp.R;
import br.usp.ardhouse.controller.MainActivityController;

public class MainActivity extends AppCompatActivity {

    private String nomeArduino;

    private LocalDateTime ultimaAtualizacao;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivityController controller = new MainActivityController(MainActivity.this);
        nomeArduino = controller.lerArduinoAtual();
        TextView exibicao = findViewById(R.id.lblArduinoAtual);
        if(nomeArduino != null) {
            exibicao.setText(exibicao.getText().toString() + "" + nomeArduino);
        }else {
            exibicao.setText(exibicao.getText().toString() + "Nenhum arduino selecionado");
        }

        atualizarData(null);


    }

    // Método para levar usuário à tela de selecionar um novo Arduino
    public void selecionarArduino(View view){
        Intent intent = new Intent(this, SelecionarArduinoActivity.class);
        startActivity(intent);
    }

    // Ultima atualização de horário
    public void atualizarData(View view) {
        ultimaAtualizacao = LocalDateTime.now();
        TextView exibirData = findViewById(R.id.lblData);
        String formatoDeData = "dd/MM/yyyy HH:mm:ss";
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern(formatoDeData);
        String dateFormated = ultimaAtualizacao.format(formatador);
        exibirData.setText("");
        exibirData.setText(exibirData.getText() + " " + dateFormated);

    }


}
package br.usp.ardhouse.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.usp.R;
import br.usp.ardhouse.controller.MainActivityController;
import br.usp.ardhouse.infrastructure.ServerCallback;

public class MainActivity extends AppCompatActivity {

    private String nomeArduino;

    private LocalDateTime ultimaAtualizacao;
    private MainActivityController controller;
    TextView exibicao;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        

        controller = new MainActivityController(MainActivity.this);
        exibicao = findViewById(R.id.lblNomeArduino);

    }

    // Método para levar usuário à tela de selecionar um novo Arduino
    public void selecionarArduino(View view){
        Intent intent = new Intent(this, SelecionarArduinoActivity.class);
        startActivity(intent);
    }

    // Método responsável por atualizar o nome do Arduino selecionado atualmente, lendo a partir de um arquivo .txt
    private void atualizarNomeArduino(){
        nomeArduino = controller.lerArduinoAtual();

        if(nomeArduino != null) {
            exibicao.setText(nomeArduino);
        }else {
            exibicao.setText("Nenhum arduino selecionado");
        }
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

    public void mudarEstadoLampada(View view){
        controller.mudarEstadoLampada(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Sempre que a tela principal é chamada, atualiza a data e o nome do Arduino atual
    @Override
    protected void onResume(){
        super.onResume();
        atualizarData(null);
        atualizarNomeArduino();

    }


}
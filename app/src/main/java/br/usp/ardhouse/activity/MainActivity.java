package br.usp.ardhouse.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.usp.R;
import br.usp.ardhouse.controller.MainController;
import br.usp.ardhouse.infrastructure.ServerCallback;

public class MainActivity extends AppCompatActivity {

    private String nomeArduino;

    private LocalDateTime ultimaAtualizacao;
    private MainController controller;
    TextView exibicao;
    TextView temperatura;
    TextView umidade;
    SwipeRefreshLayout mySwipeRefreshLayout;
    final Handler handler = new Handler();
    boolean statusLampada = false;
    boolean statusAlarme = false;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);

        controller = new MainController(MainActivity.this);
        exibicao = findViewById(R.id.lblNomeArduino);
        temperatura = findViewById(R.id.text_temperatura);
        umidade = findViewById(R.id.text_umidade);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // Chama o método de atualizar o painel toda vez que o usuário desliza a tela
                        atualizarPainel(null);
                    }
                }
        );


    }

    // Método para levar usuário à tela de selecionar um novo Arduino
    public void selecionarArduino(View view){
        Intent intent = new Intent(this, SelecionarArduinoActivity.class);
        startActivity(intent);
    }

    public void controlarVentilador(View view){
        Intent intent = new Intent(this, ControlarVentiladorActivity.class);
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
    public void atualizarPainel(View view) {
        controller.obterTemperaturaEUmidade(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                if(result != null && result.split("\\r?\\n").length >= 2) {
                    String tempTemperatura = result.split("\\r?\\n")[0];
                    String tempUmidade = result.split("\\r?\\n")[1];
                    temperatura.setText((tempTemperatura + "°C"));
                    umidade.setText((tempUmidade + "%"));
                }
            }
        });
        ultimaAtualizacao = LocalDateTime.now();
        TextView exibirData = findViewById(R.id.text_data);
        String formatoDeData = "dd/MM/yyyy HH:mm:ss";
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern(formatoDeData);
        String dateFormated = ultimaAtualizacao.format(formatador);
        exibirData.setText("");
        exibirData.setText(exibirData.getText() + " " + dateFormated);
        mySwipeRefreshLayout.setRefreshing(false);

    }

    public void mudarEstadoLampada(View view){
        controller.obterEstadoLampada(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                if(result!=null) {
                    if (result.equals("0") || result.equals("1")) {
                        statusLampada = result.equals("1");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                controller.mudarEstadoLampada(new ServerCallback() {
                                    @Override
                                    public void onSuccess(String result) {
                                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                                    }
                                }, statusLampada);
                            }
                        }, 500);
                    } else {
                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void mudarEstadoAlarme(View view){
        controller.obterEstadoAlarme(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                if(result != null){
                    if(result.equals("0") || result.equals("1")){
                        statusAlarme = result.equals("1");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                controller.mudarEstadoAlarme(new ServerCallback() {
                                    @Override
                                    public void onSuccess(String result) {
                                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                                    }
                                }, statusAlarme);
                            }
                        }, 500);
                    } else {
                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    

    // Sempre que a tela principal é chamada, atualiza a data e o nome do Arduino atual
    @Override
    protected void onResume(){
        super.onResume();
        atualizarPainel(null);
        atualizarNomeArduino();

    }
}

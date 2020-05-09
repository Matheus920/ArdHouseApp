package br.usp.ardhouse.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import br.usp.ardhouse.infrastructure.MyFirebaseMessagingService;
import br.usp.ardhouse.infrastructure.ServerCallback;

/*
    A classe MainActivity representa a tela inicial e principal da aplicação.
    É ela quem leva para todas as outras e conta com as principais ações de
    comunicação do sistema.
 */

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


    /*
        O método OnCreate instancia os objetos necessários e também
        faz certas configurações, como o canal de notificações do aplicativo
        e o SwipeToRefresh.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = getString(R.string.default_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }

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

    // Método para levar usuário à tela de controlar velocidade do ventilador
    public void controlarVentilador(View view){
        Intent intent = new Intent(this, ControlarVentiladorActivity.class);
        startActivity(intent);
    }

    // Método responsável por atualizar o nome do Arduino selecionado atualmente, lendo a partir de um arquivo .txt
    private void atualizarNomeArduino(){
        nomeArduino = controller.lerArduinoAtual();
        exibicao.setText(nomeArduino);
    }

    // Ultima atualização de horário e da umidade e temperatura
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

    // Método responsável por ligar/desligar a lâmpada a depender do estado atual da mesma
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

    // Método responsável por ligar/desligar alarme dependendo do estado atual do mesmo
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

    // Método responsável por destrancar a porta
    public void abrirPorta(View view){
        controller.destrancarPorta(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        });
    }
    

    // Sempre que a tela principal é chamada, atualiza a temperatura e a umidade e o nome do Arduino atual
    @Override
    protected void onResume(){
        super.onResume();
        atualizarPainel(null);
        atualizarNomeArduino();

    }
}

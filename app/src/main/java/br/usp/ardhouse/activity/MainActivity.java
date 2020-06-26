package br.usp.ardhouse.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import androidx.appcompat.widget.Toolbar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import com.google.android.material.navigation.NavigationView;
import com.jakewharton.rxbinding4.view.RxView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import br.usp.R;
import br.usp.ardhouse.controller.MainController;
import br.usp.ardhouse.infrastructure.ServerCallback;
import br.usp.ardhouse.progress.CustomProgressBar;

/*
    A classe MainActivity representa a tela inicial e principal da aplicação.
    É ela quem leva para todas as outras e conta com as principais ações de
    comunicação do sistema.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String nomeArduino;
    private LocalDateTime ultimaAtualizacao;
    private MainController controller;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView exibicao;
    TextView temperatura;
    TextView umidade;
    ImageButton lampadaBtn;
    ImageButton alarmeBtn;
    ImageButton ventiladorBtn;
    ImageButton portaBtn;
    SwipeRefreshLayout mySwipeRefreshLayout;
    ViewFlipper viewFlipper;
    final Handler handler = new Handler();
    static final float END_SCALE = 0.7f;
    ConstraintLayout contentView;
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = getString(R.string.default_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        contentView  = findViewById(R.id.content);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        animateNavigationDrawer();

        temperatura = findViewById(R.id.text_temperatura);
        umidade = findViewById(R.id.text_umidade);
        portaBtn = findViewById(R.id.button_porta);
        ventiladorBtn = findViewById(R.id.button_ventilador);
        alarmeBtn = findViewById(R.id.button_alarme);
        lampadaBtn = findViewById(R.id.btnLampada);
        controller = new MainController(MainActivity.this);

        viewFlipper = findViewById(R.id.view_flipper);

        atualizarNomeArduino();

        RxView.clicks(portaBtn).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(empty -> {
                    abrirPorta(null);
                }
        );

        RxView.clicks(alarmeBtn).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(empty -> {
                    mudarEstadoAlarme(null);
                }
        );

        RxView.clicks(lampadaBtn).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(empty -> {
                    mudarEstadoLampada(null);
                }
        );

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

    private void animateNavigationDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

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
        // Acessa o nome do Arduino que está dentro do toolbar
        View headerView = navigationView.getHeaderView(0);
        exibicao = headerView.findViewById(R.id.lblNomeArduino);
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

                    int tempUmidadeConvertido = Integer.parseInt(tempUmidade);
                    int tempTemperaturaConvertido = Integer.parseInt(tempTemperatura);

                    temperatura.setText(tempTemperaturaConvertido);
                    umidade.setText(tempUmidadeConvertido);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_selecionararduino:
                Intent intent = new Intent(this, SelecionarArduinoActivity.class);
                startActivity(intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void verTemperatura(View v) {
        viewFlipper.setInAnimation(this, R.anim.slide_in_right);
        viewFlipper.setOutAnimation(this, R.anim.slide_out_left);
        viewFlipper.showPrevious();
    }

    public void verUmidade(View v) {
        viewFlipper.setInAnimation(this, R.anim.slide_in_right);
        viewFlipper.setOutAnimation(this, R.anim.slide_out_left);
        viewFlipper.showNext();
    }
}

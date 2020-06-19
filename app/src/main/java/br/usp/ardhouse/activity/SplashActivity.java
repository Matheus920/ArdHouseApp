package br.usp.ardhouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import br.usp.R;
import br.usp.ardhouse.controller.MainController;

/*
    Classe responsável por representar a tela de espera antes de iniciar o sistema
 */
public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIMER = 4000;

    // Variáveis
    ImageView backgroundImage;
    TextView title, subtitle;
    Intent intent = new Intent();
    MainController mainController = new MainController(this);

    // Animações
    Animation topAnim, bottomAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        // Animações
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        backgroundImage = findViewById(R.id.background_image);
        title = findViewById(R.id.titlesplash);
        subtitle = findViewById(R.id.subtitlesplash);

        backgroundImage.setAnimation(topAnim);
        title.setAnimation(bottomAnim);
        subtitle.setAnimation(bottomAnim);

        // Temporizador
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Verifica se já existe ou não um Arduino para decidir a tela inicial do usuário
                if(!mainController.lerArduinoAtual().equals("Nenhum arduino selecionado")) {
                    intent.setClass(getApplicationContext(), MainActivity.class);
                } else {
                    intent.setClass(getApplicationContext(), SelecionarArduinoActivity.class);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up_anim, R.anim.slide_out_up_anim);
                finish();
            }
        }, SPLASH_TIMER);
    }
}

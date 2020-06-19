package br.usp.ardhouse.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.usp.R;
import br.usp.ardhouse.controller.ControlarVentiladorController;
import br.usp.ardhouse.infrastructure.ServerCallback;

/*
    Essa classe é responsável por representar a tela de controlar
    a velocidade do ventilador. Ela é gerada quando é clicado no
    botão de ventilador a partir da tela inicial.
 */


public class ControlarVentiladorActivity extends AppCompatActivity {

    SeekBar seekBar;
    TextView valorNovaVelocidade;
    TextView valorVelocidadeAtual;
    ControlarVentiladorController controller;
    int inverso;

    Animation rotateAnimation;
    ImageView imageView;

    int valorAtual;

    /*
        O método onCreate realiza as instancias necessárias dos objetos
        e também coloca um listener na seekBar de modo a escutar por alterações
        feitas pelo usuário e atualizar o texto da tela.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_controlar_ventilador);

        seekBar = (SeekBar) findViewById(R.id.velocidadeVentiladorSeekBar);
        valorVelocidadeAtual = (TextView) findViewById(R.id.velocidadeAtualValor);
        valorNovaVelocidade = (TextView) findViewById(R.id.novaVelocidadeValue);
        imageView = (ImageView) findViewById(R.id.fan_rotate);

        rotateAnimation();

        controller = new ControlarVentiladorController(ControlarVentiladorActivity.this);
        atualizarValorAtual();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valorNovaVelocidade.setText(String.valueOf(progress));

                rotateAnimation();

                if (progress == 0) rotateAnimation.setDuration(0);
                else if (progress < 64) rotateAnimation.setDuration(2000-progress);
                else if (progress < 128) rotateAnimation.setDuration(1500-progress);
                else if (progress < 192) rotateAnimation.setDuration(1000-progress);
                else rotateAnimation.setDuration(500-progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void rotateAnimation() {
        rotateAnimation= AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotateAnimation.setFillAfter(true);
        imageView.startAnimation(rotateAnimation);
    }


    @Override
    protected void onResume(){
        super.onResume();
        atualizarValorAtual();
    }

    /*
        O método de enviar escuta pelo clique no botão de enviar
        mudando a velocidade do ventilador no Arduino.
     */
    public void enviar(View view){
        controller.mudarVelocidadeVentilador(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(ControlarVentiladorActivity.this, result, Toast.LENGTH_SHORT).show();

            }
        }, Integer.parseInt(valorNovaVelocidade.getText().toString()));
        this.finish();
    }

    /*
        O método de atualizar o valor atual faz uma requisição para o Arduino
        pedindo o valor da velocidade atual em que o ventilador se encontra e
        preenchendo os respectivos campos na tela do aplicativo.
     */
    private void atualizarValorAtual(){
        controller.obterVelocidadeVentilador(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                try{
                   valorAtual = Integer.parseInt(result);
                   valorVelocidadeAtual.setText(String.valueOf(valorAtual));
                   seekBar.setProgress(valorAtual);
                }catch(NumberFormatException ex){
                    valorAtual = 0;
                    valorVelocidadeAtual.setText(String.valueOf(valorAtual));
                    seekBar.setProgress(valorAtual);
                }
            }
        });
    }
}

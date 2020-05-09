package br.usp.ardhouse.activity;

import android.os.Bundle;
import android.view.View;
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
    int valorAtual;

    /*
        O método onCreate realiza as instancias necessárias dos objetos
        e também coloca um listener na seekBar de modo a escutar por alterações
        feitas pelo usuário e atualizar o texto da tela.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlar_ventilador);
        seekBar = (SeekBar) findViewById(R.id.velocidadeVentiladorSeekBar);
        valorVelocidadeAtual = (TextView) findViewById(R.id.velocidadeAtualValor);
        valorNovaVelocidade = (TextView) findViewById(R.id.novaVelocidadeValue);
        controller = new ControlarVentiladorController(ControlarVentiladorActivity.this);
        atualizarValorAtual();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valorNovaVelocidade.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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

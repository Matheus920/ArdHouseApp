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

public class ControlarVentiladorActivity extends AppCompatActivity {

    SeekBar seekBar;
    TextView valorNovaVelocidade;
    TextView valorVelocidadeAtual;
    ControlarVentiladorController controller;
    int valorAtual;

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

    public void enviar(View view){
        controller.mudarVelocidadeVentilador(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(ControlarVentiladorActivity.this, result, Toast.LENGTH_SHORT).show();

            }
        }, Integer.parseInt(valorNovaVelocidade.getText().toString()));
        this.finish();
    }

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

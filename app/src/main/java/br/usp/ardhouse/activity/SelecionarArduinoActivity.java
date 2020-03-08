package br.usp.ardhouse.activity;

import androidx.appcompat.app.AppCompatActivity;
import br.usp.R;
import br.usp.ardhouse.controller.SelecionarArduinoController;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SelecionarArduinoActivity extends AppCompatActivity {

    private SelecionarArduinoController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_arduino);
        controller = new SelecionarArduinoController();
    }

    public void salvar(View view){
        EditText nomeArduino = findViewById(R.id.arduinoTexto);
        String resposta = controller.salvarArduino(nomeArduino.toString());
        if(!resposta.equals("")){
            // TODO tratar erro
        }
    }
}

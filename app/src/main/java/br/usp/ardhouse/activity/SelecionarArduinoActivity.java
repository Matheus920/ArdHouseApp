package br.usp.ardhouse.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import br.usp.R;
import br.usp.ardhouse.controller.SelecionarArduinoController;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SelecionarArduinoActivity extends AppCompatActivity {

    private SelecionarArduinoController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_arduino);
        controller = new SelecionarArduinoController(SelecionarArduinoActivity.this.getBaseContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void salvar(View view){
        EditText nomeArduino = findViewById(R.id.arduinoTexto);
        String nomeArduinoString = nomeArduino.getText().toString();

        if(nomeArduinoString.length() <= 0){
            nomeArduino.requestFocus();
            nomeArduino.setError("Campo de nome não pode ser vazio");
            return;
        }

        String resposta = controller.salvarArduino(nomeArduino.getText().toString());
        if(!resposta.equals("")){
            Toast.makeText(SelecionarArduinoActivity.this, resposta, Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(SelecionarArduinoActivity.this, "Salvo com sucesso", Toast.LENGTH_LONG).show();
            SelecionarArduinoActivity.this.finish();
        }
    }
}

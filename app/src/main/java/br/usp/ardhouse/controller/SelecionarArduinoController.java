package br.usp.ardhouse.controller;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;

import br.usp.ardhouse.data.Arquivo;

public class SelecionarArduinoController {

    private Context context;

    public SelecionarArduinoController(Context context){
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String salvarArduino(String ip){
        String resposta = "";
        try {
            Arquivo.salvarArquivo("configuration.txt", "NAME = " + ip, context);
        } catch (IOException e){
            resposta = "Ocorreu um erro durante a leitura. Por favor tente novamente mais tarde";
        }
        return resposta;
    }
}

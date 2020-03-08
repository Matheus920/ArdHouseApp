package br.usp.ardhouse.controller;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.Map;

import br.usp.ardhouse.data.Arquivo;

public class MainActivityController {

    private Context context;

    public MainActivityController(Context context){
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String lerArduinoAtual(){
        String resposta = "";
        try {
            Map<String, String> conteudo = Arquivo.lerArquivo("configuration.txt", context);
            resposta = conteudo.get("NAME");
        } catch (IOException e) {
            resposta = "Ocorreu um erro ao ler o arduino";
        }
        return resposta;
    }
}

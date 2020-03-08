package br.usp.ardhouse.controller;

import java.io.IOException;

import br.usp.ardhouse.data.Arquivo;

public class SelecionarArduinoController {

    public String salvarArduino(String ip){
        String resposta = "";
        try {
            Arquivo.salvarArquivo("configuration.txt", "NAME = " + ip);
        } catch (IOException e){
            resposta = "Ocorreu um erro durante a leitura. Por favor tente novamente mais tarde";
        }
        return resposta;
    }
}

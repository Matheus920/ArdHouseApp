package br.usp.ardhouse.data;

import android.content.Context;
import android.os.Build;
import android.util.ArrayMap;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;


public abstract class Arquivo {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void salvarArquivo(String nomeArquivo, String conteudo, Context context) throws IOException {
        File arquivo = new File(context.getDataDir(), nomeArquivo);
        FileOutputStream stream = new FileOutputStream(arquivo);
        stream.write(conteudo.getBytes());
        stream.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Map<String, String> lerArquivo(String nomeArquivo, Context context) throws IOException, FileNotFoundException {
        Map<String, String> conteudo = new ArrayMap<String, String>();
        String linha;
        BufferedReader bf = new BufferedReader(new FileReader(new File(context.getDataDir(), nomeArquivo)));
        while((linha = bf.readLine()) != null){
            String chave = "";
            String valor = "";
            if(linha.split("=").length == 2) {
                chave = linha.split("=")[0];
                valor = linha.split("=")[1];
            } else {
                chave = linha.split("=")[0];
                valor = null;
            }
            conteudo.put(chave, valor);
        }

        return conteudo;


    }
}

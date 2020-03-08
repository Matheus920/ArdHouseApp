package br.usp.ardhouse.data;

import android.os.Build;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Arquivo {

    public static void salvarArquivo(String nomeArquivo, String conteudo) throws IOException {
        File arquivo = new File(nomeArquivo);
        BufferedWriter escritor = new BufferedWriter(new FileWriter(arquivo));
        escritor.write(conteudo);
        escritor.close();
    }
}

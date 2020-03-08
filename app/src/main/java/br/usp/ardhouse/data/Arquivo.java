package br.usp.ardhouse.data;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;


public abstract class Arquivo {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void salvarArquivo(String nomeArquivo, String conteudo, Context context) throws IOException {
        File arquivo = new File(context.getDataDir(), nomeArquivo);
        FileOutputStream stream = new FileOutputStream(arquivo);
        stream.write(conteudo.getBytes());
        stream.close();
    }
}

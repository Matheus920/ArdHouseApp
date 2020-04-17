package br.usp.ardhouse.controller;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;


public class SelecionarArduinoController {

    private Context context;

    public SelecionarArduinoController(Context context){
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void salvarArduino(String ip){
        context.getSharedPreferences("_", Context.MODE_PRIVATE).edit().putString("NAME", ip).apply();
    }
}

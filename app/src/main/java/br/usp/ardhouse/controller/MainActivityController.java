package br.usp.ardhouse.controller;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
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

    public String mudarEstadoLampada(){
        String nomeArduino = lerArduinoAtual();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://" + nomeArduino + "/?ledParam";
        final ArrayList<String> resposta = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                resposta.add(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resposta.add("Erro na requisição");
            }
        });

        queue.add(stringRequest);

        if(resposta.size() > 0) {
            return resposta.get(0);
        } else {
            return "Sem resposta do servidor especificado";
        }
    }
}

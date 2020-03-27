package br.usp.ardhouse.controller;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.usp.ardhouse.data.Arquivo;
import br.usp.ardhouse.infrastructure.ServerCallback;

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

    public void obterEstadoLampada(final ServerCallback callback){
        String nomeArduino = lerArduinoAtual();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://" + nomeArduino + "/?ledStatus";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response.substring(0, 1));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSuccess(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Connection", "close");
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void mudarEstadoLampada(final ServerCallback callback, boolean status){
        String nomeArduino = lerArduinoAtual();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = status ? "http://" + nomeArduino + "/?ledParam=0" : "http://" + nomeArduino + "/?ledParam=1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSuccess(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Connection", "close");
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void obterTemperaturaEUmidade(final ServerCallback callback){
        String nomeArduino = lerArduinoAtual();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://" + nomeArduino + "/?tempUmi";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSuccess(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Connection", "close");
                return params;
            }
        };

        queue.add(stringRequest);
    }
}

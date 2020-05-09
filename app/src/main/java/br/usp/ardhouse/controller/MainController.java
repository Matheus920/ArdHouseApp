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
import br.usp.ardhouse.infrastructure.RequestSingleton;
import br.usp.ardhouse.infrastructure.ServerCallback;

/*
    A classe MainController é responsável por realizar comunicação com o Arduino
    no que diz respeito às ações da tela principal.
 */
public class MainController {

    private Context context;

    public MainController(Context context){
        this.context = context;
    }

    // Obtém o endereço do Arduino Atual salvo nas configurações do aplicativo
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String lerArduinoAtual(){
        return context.getSharedPreferences("_", Context.MODE_PRIVATE).getString("NAME", "Nenhum arduino selecionado");
    }

    // Obtém estado atual da lâmpada, 0 para desligada e 1 para ligada
    public void obterEstadoLampada(final ServerCallback callback){
        String nomeArduino = lerArduinoAtual();
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

        RequestSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    // Muda o estado da lâmpada
    public void mudarEstadoLampada(final ServerCallback callback, boolean status){
        String nomeArduino = lerArduinoAtual();
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

        RequestSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    // Obtém dados sobre a temperatura e umidade lidas pelo Arduino
    public void obterTemperaturaEUmidade(final ServerCallback callback){
        String nomeArduino = lerArduinoAtual();
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

        RequestSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    // Obtém o estado atual do alarme, 0 para desligado e 1 para ligado
    public void obterEstadoAlarme(final ServerCallback callback){
        String nomeArduino = lerArduinoAtual();
        String url = "http://" + nomeArduino + "/?alarmStatus";

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

        RequestSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    // Muda o estado do alarme
    public void mudarEstadoAlarme(final ServerCallback callback, boolean status){
        String nomeArduino = lerArduinoAtual();
        String url = status ? "http://" + nomeArduino + "/?alarmParam=0" : "http://" + nomeArduino + "/?alarmParam=1";
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

        RequestSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    // Realiza requisição para destrancar a porta
    public void destrancarPorta(final ServerCallback callback){
        String nomeArduino = lerArduinoAtual();
        String url = "http://" + nomeArduino + "/?portOpen";
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

        RequestSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}

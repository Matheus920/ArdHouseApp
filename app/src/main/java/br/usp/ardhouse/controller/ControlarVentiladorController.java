package br.usp.ardhouse.controller;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import br.usp.ardhouse.infrastructure.RequestSingleton;
import br.usp.ardhouse.infrastructure.ServerCallback;

public class ControlarVentiladorController {
    private Context context;

    public ControlarVentiladorController(Context context){
        this.context = context;
    }

    public void mudarVelocidadeVentilador(final ServerCallback callback, int valor){
        String nomeArduino = new MainController(context).lerArduinoAtual();
        String url = "http://" + nomeArduino + "/?vent=" + valor;

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

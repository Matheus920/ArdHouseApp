package br.usp.ardhouse.controller;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.usp.ardhouse.infrastructure.RequestSingleton;
import br.usp.ardhouse.infrastructure.ServerCallback;


public class SelecionarArduinoController {

    private Context context;

    public SelecionarArduinoController(Context context){
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void salvarArduino(String ip){
        context.getSharedPreferences("_", Context.MODE_PRIVATE).edit().putString("NAME", ip).apply();
    }

    public void salvarNomeDispositivo(final ServerCallback callback){
        String id = context.getSharedPreferences("_", Context.MODE_PRIVATE).getString("fb", "empty");
        String url = "https://ardhousenotifier.herokuapp.com/saveId";

        Map<String, String> tempData = new HashMap<>();
        tempData.put("id", id);

        JSONObject data = new JSONObject(tempData);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onSuccess(response.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    public void salvarIdArduino(final ServerCallback callback){
        String nomeArduino = new MainController(context).lerArduinoAtual();
        String id = context.getSharedPreferences("_", Context.MODE_PRIVATE).getString("deviceId", "0");
        String url = "http://" + nomeArduino + "/?setId=" + id;

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

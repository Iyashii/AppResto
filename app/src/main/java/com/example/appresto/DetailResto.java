package com.example.appresto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailResto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_resto);

        long idR = -1;

        Intent intent = getIntent();
        if (intent != null) {
            idR = intent.getLongExtra("IDResto", -1);
        }
        final int id = (int) (idR);

        //creation de la requete http sur le serveur local, cela necessite
        OkHttpClient httpclient = new OkHttpClient();

        //prépare la requête
        //Request requestClients = new Request.Builder().url(getString(R.string.server) + getString(R.string.location_api)).build();
        Request requestClients = new Request.Builder().url("http://10.15.12.18/TC5/ProjetResto/getAllRestoByIdJSON.php?idR=" + id + "").build();

        httpclient.newCall(requestClients).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.i("erreur1", "erreur echec requete");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String myResponse = response.body().string();
                DetailResto.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            // on crée un objet JSON à partir de la reponse de la requete
                            JSONObject jsonObjectUnresto = new JSONObject(myResponse);
                            //on transforme cette objet en un array d'objet resto
                            JSONArray jsonArray = jsonObjectUnresto.optJSONArray("restos");
                            //comme le jsonarray il va contenir qu'un seul element, on considere c'est l'objet d'indice 0
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            TextView Adresse = findViewById(R.id.textViewAdresse);
                            TextView Description = findViewById(R.id.textViewDescR);
                            Adresse.setText(jsonObject.getString("voieAdrR"));
                            Description.setText(jsonObject.getString("descR"));



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }
}
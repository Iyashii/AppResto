package com.example.appresto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    //on définit une collection de caractéristiques d'un resto
    ArrayList<Resto> lesRestos = new ArrayList<Resto>();

    //on définit un objet ListView
    ListView listViewRestos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //on associe l'objet au widget
        listViewRestos = findViewById(R.id.listViewRestos);

        //creation de la requete http sur le serveur local, cela necessite
        OkHttpClient httpclient = new OkHttpClient();

        //prépare la requête
        Request requestClients = new Request.Builder().url("http://172.20.10.4/TC5/ProjetResto/getAllRestoJSON.php").build();

        httpclient.newCall(requestClients).enqueue(new Callback() {
            @Override
            //si la requête échoue affichage d'un message d'erreur dans les log
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.i("erreur1", "erreur requete getAllRestosJSON.php");
            }

            @Override
            //si la requête réussie
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // on crée un objet JSON à partir de notre réponse.
                            JSONObject jsonObjectlesrestos = new JSONObject(myResponse);
                            //on transforme cet objet JSON en array d'objet resto sous forme JSON
                            JSONArray jsonArray = jsonObjectlesrestos.optJSONArray("restos");
                            //on parcours cette collection d'objet restos pour ajouter chaque restos dans notre liste d'objet resto


                            //on efface le contenu de la liste
                            lesRestos.clear();

                            for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Integer num = jsonObject.getInt("idR");
                                String nomR = jsonObject.getString("nomR");
                                String villeR = jsonObject.getString("villeR");



                                // TO DO
                                Log.i("restaurant", num + nomR + " " +villeR+" "); //message qui apparait dans la console pour vérifier
                                // TO DO
                                Resto c = new Resto(num, nomR, villeR);
                                //on ajoute le resto à la collection lesClients
                                lesRestos.add(c);
                            }

                            ArrayAdapter<Resto> dataAdapter = new ArrayAdapter<Resto>(MainActivity.this, android.R.layout.simple_list_item_1, lesRestos);
                            listViewRestos.setAdapter(dataAdapter);



                        } catch (final JSONException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("erreur", "Exceptions " + String.valueOf(e));
                                }
                            });
                        }
                    }

                });
            }


        });
        listViewRestos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Resto unresto= (Resto) listViewRestos.getItemAtPosition(position);
                Long idresto = Long.valueOf(unresto.getNumresto());
                Toast.makeText(getApplicationContext(), "id selectionné : " + String.valueOf(id), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, DetailResto.class);
                intent.putExtra("IDResto", idresto);
                startActivity(intent);
            }
        });
    }
}
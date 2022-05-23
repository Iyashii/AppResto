package com.example.appresto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
        Request requestClients = new Request.Builder().url("http://172.20.10.4/TC5/ProjetResto/getAllRestoByIdJSON.php?idR=" + id + "").build();

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
//programmation du bouton quitter pour retourner à la liste des clients
        Button btnQuitter = findViewById(R.id.btnListe);
        Button btnReserver = findViewById(R.id.btnReserver);

        View.OnClickListener ecouteur = new View.OnClickListener() {
            //on implémente la méthode onclick
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnReserver:
                        reserverUnResto(id);
                        //    Intent intent5 = new Intent(DetailResto.this,Reservation.class);
                        //    intent5.putExtra("id",id);
                        //    startActivity(intent5);
                        break;
                    case R.id.btnListe:
                        Intent intent = new Intent(DetailResto.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + v.getId());
                }
            }
        };
        btnQuitter.setOnClickListener(ecouteur);
        btnReserver.setOnClickListener(ecouteur);

    }

    public void reserverUnResto(long id) {
        final long idR = id;
        final EditText nomprenom = findViewById(R.id.editTextNomprenommodif);
        final EditText nbpersonne = findViewById(R.id.editTexteNbPersonnemodif);
        final EditText date = findViewById(R.id.editTextDate);
        final EditText horaire = findViewById(R.id.editTextHoraire);
        final EditText tel = findViewById(R.id.editTextTelmodif);
        Button btnEnregistrerReservation = findViewById(R.id.btnEnregistrerReservation);
        //on affiche les champs cachés permettant de saisir les nouvelles valeurs du client
        nomprenom.setVisibility(View.VISIBLE);
        nbpersonne.setVisibility(View.VISIBLE);
        date.setVisibility(View.VISIBLE);
        horaire.setVisibility(View.VISIBLE);
        tel.setVisibility(View.VISIBLE);
        btnEnregistrerReservation.setVisibility(View.VISIBLE);

        //on prépare la requete http et on modifie le client
        View.OnClickListener ecouteur5 = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //on enregistre dans des variables les données saisies dans les zones text
                Editable nomR= nomprenom.getText();
                Editable nbPersonne = nbpersonne.getText();
                Editable dateR = date.getText();
                Editable heure = horaire.getText();
                Editable telR = tel.getText();
                OkHttpClient reservation = new OkHttpClient();

                RequestBody detailBody = new FormBody.Builder().add("nomR", String.valueOf(nomR)).add("nbPersonne", String.valueOf(nbPersonne))
                        .add("dateR", String.valueOf(dateR)).add("heure", String.valueOf(heure)).add("telR", String.valueOf(telR))
                        .add("idR", String.valueOf(idR)).build();

                Request requestReservation = new Request.Builder().url("http://172.20.10.4/TC5/ProjetResto/ajoutReservationJSON.php").post(detailBody).build();
                reservation.newCall(requestReservation).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        Log.i("erreur1", e.getMessage());
                    }

                    public void onResponse(Call call, Response response) throws IOException {

                        // final String myResponse = response.body().string();
                        DetailResto.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //       Toast.makeText(getApplicationContext(), "resto réservé !", Toast.LENGTH_LONG).show();
                                Toast.makeText(DetailResto.this,String.valueOf(nomR)+" "+ String.valueOf(idR)+" "+ String.valueOf(nbPersonne)+" "+ String.valueOf(dateR)+ " "+ String.valueOf(heure)+" "+String.valueOf(telR),Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }

                });
            }
        };
        btnEnregistrerReservation.setOnClickListener(ecouteur5);

    }
}
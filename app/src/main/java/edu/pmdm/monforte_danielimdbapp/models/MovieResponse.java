package edu.pmdm.monforte_danielimdbapp.models;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.pmdm.monforte_danielimdbapp.api.IMDBApiService;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieResponse {

    public static void buscarTop10(IMDBApiService service){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://imdb-com.p.rapidapi.com/title/get-top-meter?topMeterTitlesType=ALL&limit=10")
                .get()
                .addHeader("x-rapidapi-key", "f94b3a9b75mshf98573499366620p15aecejsndd002043f0ce")
                .addHeader("x-rapidapi-host", "imdb-com.p.rapidapi.com")
                .build();
        //No usamos execute porque no se puede ejecutar en el hilo principal
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Error en la solicitud: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String datos = response.body().string();
                    List<Movie>movies=MovieExtractor.extractMovies(datos);
                    if(service!=null){
                        service.onMoviesReceived(movies);
                    }
                } else {
                    System.out.println("Error en la respuesta: " + response.code()+response.message());
                }
            }
        });
    }

}

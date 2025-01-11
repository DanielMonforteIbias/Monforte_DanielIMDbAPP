package edu.pmdm.monforte_danielimdbapp.models;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import edu.pmdm.monforte_danielimdbapp.R;
import edu.pmdm.monforte_danielimdbapp.api.IMDBApiService;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieResponse {
    /**
     * Método que usa el endpoint get-top-meter de IMDBApi para obtener una lista de peliculas y series mas populares
     * @param service la interfaz que tiene el metodo a ejecutar tras obtener la lista
     */
    public static void buscarTop10(IMDBApiService service, Context context){
        OkHttpClient client = new OkHttpClient();
        //La peticion tiene la condicion limit=10 para obtener el top 10, se podria cambiar este numero para obtener mas o menos. Tambien podemos cambiar ALL para obtener solo series o solo peliculas
        Request request = new Request.Builder()
                .url("https://imdb-com.p.rapidapi.com/title/get-top-meter?topMeterTitlesType=ALL&limit=10")
                .get()
                .addHeader("x-rapidapi-key", "8bd8c55e73msh0e1794c9dba568cp141c78jsnd5d9db2ae48a")
                .addHeader("x-rapidapi-host", "imdb-com.p.rapidapi.com")
                .build();
        /*Other keys in case the current one runs out of calls:
        f94b3a9b75mshf98573499366620p15aecejsndd002043f0ce (not working anymore)
        db11b4b2d1mshf70e3a1da81bda4p1d070djsna023899c32b8
        8bd8c55e73msh0e1794c9dba568cp141c78jsnd5d9db2ae48a (currently being used)
        Make sure to change keys here AND in MovieOverviewResponse
        */
        //No usamos execute porque no se puede ejecutar en el hilo principal
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(context!=null) new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, R.string.error_solicitud_api, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String datos = response.body().string(); //Obtenemos el JSON en un String
                    List<Movie>movies= JSONExtractor.extractMovies(datos); //Extraemos los datos del JSON recibido con el metodo de MovieExtractor
                    if(service!=null){
                        service.onMoviesReceived(movies); //Ejecutamos el metodo que procesa la lista de peliculas creada a partir de los datos del JSON
                    }
                } else {
                    if(context!=null) new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, R.string.error_respuesta_api, Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}

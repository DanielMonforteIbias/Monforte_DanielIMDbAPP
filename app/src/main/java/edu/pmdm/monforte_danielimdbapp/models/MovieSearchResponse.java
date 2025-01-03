package edu.pmdm.monforte_danielimdbapp.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.pmdm.monforte_danielimdbapp.api.IMDBApiService;
import edu.pmdm.monforte_danielimdbapp.api.TMDBApiService;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieSearchResponse {
    /**
     * Método que usa el endpoint Movie List de genres para obtener una lista de generos de peliculas
     * @param service la interfaz que tiene el metodo a ejecutar tras obtener la lista de generos
     */
    public static void buscarGeneros(TMDBApiService service){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/genre/movie/list?language=en")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YmUwNWUzZDU4MWE1NzFjNzYzNzIwMTEyMDc3YjY5MyIsIm5iZiI6MTczNTkzMjgyOS42NTIsInN1YiI6IjY3NzgzYjlkMzIxYTNhMTY2YTc0YTY4NSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.kYScL-FHJi2LtErS_x8xtXX-ccD8gmhnRmF4ialnF9w")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Error en la solicitud: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String datos = response.body().string(); //Obtenemos el JSON en un String
                    List<Genre>genres=JSONExtractor.extractGenres(datos); //Usamos el metodo que extrae los nombres de los generos de la respuesta recibida
                    if(service!=null){
                        service.onGenresReceived(genres); //Ejecutamos el metodo que procesa la lista de generos creada a partir de los datos del JSON
                    }
                } else {
                    System.out.println("Error en la respuesta: " + response.code()+response.message());
                }
            }
        });
    }

    public static void buscarPeliculasPorAñoYGenero(String year, String genre, TMDBApiService service){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&primary_release_year="+year+"&sort_by=popularity.desc&with_genres="+genre)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YmUwNWUzZDU4MWE1NzFjNzYzNzIwMTEyMDc3YjY5MyIsIm5iZiI6MTczNTkzMjgyOS42NTIsInN1YiI6IjY3NzgzYjlkMzIxYTNhMTY2YTc0YTY4NSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.kYScL-FHJi2LtErS_x8xtXX-ccD8gmhnRmF4ialnF9w")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Error en la solicitud: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String datos = response.body().string(); //Obtenemos el JSON en un String
                    List<Movie>movies=JSONExtractor.extractMoviesByYearAndGenre(datos);
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

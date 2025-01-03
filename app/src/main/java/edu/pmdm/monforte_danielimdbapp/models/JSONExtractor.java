package edu.pmdm.monforte_danielimdbapp.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONExtractor {
    /**
     * Método que recibe un String en formato JSON con peliculas/series y lee los datos para pasarlos a objetos
     * @param jsonResponse el String en formato JSON del que sacar los datos
     * @return lista de peliculas extraidas del JSON recibido
     */
    public static List<Movie> extractMovies(String jsonResponse) {
        List<Movie> movieList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            //El endpoint usado devuelve un JSON que tiene varias peliculas en un array llamado edges
            JSONArray edges = jsonObject.getJSONObject("data").getJSONObject("topMeterTitles").getJSONArray("edges");
            for (int i = 0; i < edges.length(); i++) {
                JSONObject node = edges.getJSONObject(i).getJSONObject("node"); //Cada pelicula se identifica con la clave node
                //Sacamos los datos que nos interesan por sus claves
                String id = node.getString("id");
                String title = node.getJSONObject("titleText").getString("text");
                String fecha = node.getJSONObject("releaseDate").getInt("year")+"-"+node.getJSONObject("releaseDate").getInt("month")+"-"+node.getJSONObject("releaseDate").getInt("day");
                String imageUrl = node.getJSONObject("primaryImage").getString("url");
                int rating = node.getJSONObject("meterRanking").getInt("currentRank");
                Movie movie = new Movie(id,title, imageUrl, fecha,rating); //Construimos una pelicula con los datos obtenidos
                movieList.add(movie); //Añadimos la pelicula a la lista
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieList;
    }

    /**
     * Método que recibe un String en formato JSON con géneros y lee los datos para obtener una lista con sus nombres
     * @param jsonResponse el String en formato JSON donde están los datos
     * @return lista de String con los nombres de los generos
     */
    public static List<Genre> extractGenres(String jsonResponse){
        List<Genre> genresList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            //El endpoint usado devuelve un JSON que tiene varios generos en un array llamado genres
            JSONArray genres = jsonObject.getJSONArray("genres");
            for (int i = 0; i < genres.length(); i++) {
                JSONObject genre = genres.getJSONObject(i);
                String genreId=genre.getString("id");
                String genreName=genre.getString("name");//Sacamos el nombre del genero por su clave
                genresList.add(new Genre(genreId,genreName)); //Añadimos el genero a la lista
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return genresList;
    }

    public static List<Movie> extractMoviesByYearAndGenre(String jsonResponse){
        List<Movie> moviesList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            //El endpoint usado devuelve un JSON que tiene varios generos en un array llamado genres
            JSONArray movies = jsonObject.getJSONArray("results");
            for (int i = 0; i < movies.length(); i++) {
                JSONObject movieJson = movies.getJSONObject(i);
                String movieId=movieJson.getString("id");
                String movieTitle=movieJson.getString("original_title");
                String movieImage="https://image.tmdb.org/t/p/w500"+movieJson.getString("poster_path"); //La API solo da la ruta relativa, asi que lo concatenamos con el resto de la URL para que sea correcta
                String movieReleaseDate=movieJson.getString("release_date");
                String movieDescription=movieJson.getString("overview");
                Movie m=new Movie(movieId,movieTitle,movieImage,movieReleaseDate);
                m.setDescripcion(movieDescription);
                moviesList.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moviesList;
    }
}

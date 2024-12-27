package edu.pmdm.monforte_danielimdbapp.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieExtractor {
    /**
     * Método que recibe un String en formato JSON y lee los datos para pasarlos a objetos
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
}

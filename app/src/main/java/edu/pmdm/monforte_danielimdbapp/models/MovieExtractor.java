package edu.pmdm.monforte_danielimdbapp.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieExtractor {
    public static List<Movie> extractMovies(String jsonResponse) {
        List<Movie> movieList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray edges = jsonObject.getJSONObject("data").getJSONObject("topMeterTitles").getJSONArray("edges");
            for (int i = 0; i < edges.length(); i++) {
                JSONObject node = edges.getJSONObject(i).getJSONObject("node");
                String id = node.getString("id");
                String title = node.getJSONObject("titleText").getString("text");
                String fecha = node.getJSONObject("releaseDate").getInt("year")+"-"+node.getJSONObject("releaseDate").getInt("month")+"-"+node.getJSONObject("releaseDate").getInt("day");
                String imageUrl = node.getJSONObject("primaryImage").getString("url");
                int rating = node.getJSONObject("meterRanking").getInt("currentRank");
                Movie movie = new Movie(id,title, imageUrl, fecha,rating);
                movieList.add(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieList;
    }
}

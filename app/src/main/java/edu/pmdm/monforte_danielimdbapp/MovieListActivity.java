package edu.pmdm.monforte_danielimdbapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import edu.pmdm.monforte_danielimdbapp.adapters.MovieAdapter;
import edu.pmdm.monforte_danielimdbapp.api.TMDBApiService;
import edu.pmdm.monforte_danielimdbapp.databinding.ActivityMovieDetailsBinding;
import edu.pmdm.monforte_danielimdbapp.databinding.ActivityMovieListBinding;
import edu.pmdm.monforte_danielimdbapp.models.Genre;
import edu.pmdm.monforte_danielimdbapp.models.Movie;
import edu.pmdm.monforte_danielimdbapp.models.MovieSearchResponse;

public class MovieListActivity extends AppCompatActivity {
    private ActivityMovieListBinding binding;
    private MovieAdapter moviesAdapter;
    private List<Movie> moviesSearch =new ArrayList<Movie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMovieListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent=getIntent();
        String year=intent.getStringExtra("Year");
        String genreId=intent.getStringExtra("GenreId");
        MovieSearchResponse.buscarPeliculasPorAñoYGenero(year, genreId, new TMDBApiService() {
            @Override
            public void onGenresReceived(List<Genre> genres) {

            }

            @Override
            public void onMoviesReceived(List<Movie> movies) {
                moviesSearch.clear();
                moviesSearch.addAll(movies);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        moviesAdapter.notifyDataSetChanged(); //Notificamos al adaptador de que han cambiado los datos
                    }
                });
            }
        },this);
        moviesAdapter=new MovieAdapter(moviesSearch,null);
        binding.recyclerViewMovies.setLayoutManager(new GridLayoutManager(this,2));
        binding.recyclerViewMovies.setAdapter(moviesAdapter);

        binding.btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
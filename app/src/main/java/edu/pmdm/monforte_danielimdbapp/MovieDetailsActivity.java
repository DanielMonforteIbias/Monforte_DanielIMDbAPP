package edu.pmdm.monforte_danielimdbapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import edu.pmdm.monforte_danielimdbapp.databinding.ActivityMainBinding;
import edu.pmdm.monforte_danielimdbapp.databinding.ActivityMovieDetailsBinding;
import edu.pmdm.monforte_danielimdbapp.models.Movie;

public class MovieDetailsActivity extends AppCompatActivity {
    private ActivityMovieDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent=getIntent();
        Movie pelicula=intent.getParcelableExtra("Movie");
        Glide.with(this).load(pelicula.getPortada()).into(binding.imgPortadaDetalles);
        binding.txtTituloDetalles.setText(pelicula.getTitulo());
        binding.txtFechaDetalles.setText("Release date: "+pelicula.getFecha());
        binding.txtRatingDetalles.setText("Rating: "+pelicula.getRating());
        binding.txtDescripcionDetalles.setText(pelicula.getDescripcion());
    }
}
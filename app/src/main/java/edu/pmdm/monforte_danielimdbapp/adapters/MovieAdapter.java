package edu.pmdm.monforte_danielimdbapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.pmdm.monforte_danielimdbapp.MovieDetailsActivity;
import edu.pmdm.monforte_danielimdbapp.R;
import edu.pmdm.monforte_danielimdbapp.api.IMDBApiService;
import edu.pmdm.monforte_danielimdbapp.models.Movie;
import edu.pmdm.monforte_danielimdbapp.models.MovieOverviewResponse;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    private final List<Movie> movies;

    public MovieAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView portada;
        public ViewHolder(View itemView) {
            super(itemView);
            portada = itemView.findViewById(R.id.imgPortada);
        }

        public ImageView getPortada() {
            return portada;
        }
    }
    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        Movie pelicula=movies.get(position);
        Context contexto=holder.itemView.getContext();
        Glide.with(contexto).load(pelicula.getPortada()).into(holder.portada);
        //En lugar de cargar todas las descripciones, cargamos la descripcion aqui solo para la pelicula en la que hagamos click, no para todas para no sobrecargar la API con peticiones (el limite es 5 por segundo y se ralentizaria mucho de todas formas)
        holder.portada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieOverviewResponse.obtenerDescripcion(pelicula.getId(), new IMDBApiService() {
                    //No se usa, se implementa porque IMDBApiService debe implementar todos los metodos de la interfaz
                    @Override
                    public void onMoviesReceived(List<Movie> movies) {

                    }

                    @Override
                    public void onDescriptionReceived(String descripcion) {
                        pelicula.setDescripcion(descripcion);
                        Intent intent=new Intent(holder.itemView.getContext(), MovieDetailsActivity.class);
                        intent.putExtra("Movie",pelicula);
                        contexto.startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}

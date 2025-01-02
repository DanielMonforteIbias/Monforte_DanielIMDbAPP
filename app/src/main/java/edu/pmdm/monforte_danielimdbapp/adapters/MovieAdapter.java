package edu.pmdm.monforte_danielimdbapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import java.util.List;

import edu.pmdm.monforte_danielimdbapp.MovieDetailsActivity;
import edu.pmdm.monforte_danielimdbapp.R;
import edu.pmdm.monforte_danielimdbapp.api.IMDBApiService;
import edu.pmdm.monforte_danielimdbapp.database.FavoritesDatabaseHelper;
import edu.pmdm.monforte_danielimdbapp.models.Movie;
import edu.pmdm.monforte_danielimdbapp.models.MovieOverviewResponse;
import edu.pmdm.monforte_danielimdbapp.ui.favorites.FavoritesFragment;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    private List<Movie> movies;
    private Fragment fragment;
    FavoritesDatabaseHelper dbHelper;

    public MovieAdapter(List<Movie> movies, Fragment fragment) {
        this.movies = movies;
        this.fragment=fragment;
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
        Context context=holder.itemView.getContext();
        dbHelper=new FavoritesDatabaseHelper(context);
        Glide.with(context).load(pelicula.getPortada()).into(holder.portada); //Usamos Glide para convertir el String de la portada a imagen
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
                        pelicula.setDescripcion(descripcion); //Le damos a la pelicula pulsada la descripcion que hemos obtenido de la API
                        Intent intent=new Intent(holder.itemView.getContext(), MovieDetailsActivity.class); //Creamos un Intent de la actividad de edetalles
                        intent.putExtra("Movie",pelicula); //Pasamos la pelicula en el intent
                        context.startActivity(intent); //Iniciamos la actividad de detalles
                    }
                });
            }
        });
        holder.portada.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String userId=GoogleSignIn.getLastSignedInAccount(context).getId();
                Activity activity = (Activity) context;
                if(!dbHelper.movieExists(pelicula.getId()))dbHelper.addMovie(pelicula);
                if(dbHelper.movieIsFavorite(userId, pelicula.getId())){
                    dbHelper.removeFavorite(userId, pelicula.getId());
                    Toast.makeText(context,pelicula.getTitulo()+" eliminada de favoritos",Toast.LENGTH_SHORT).show();
                    if(fragment instanceof FavoritesFragment){
                        movies.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                }
                else{
                    dbHelper.addFavorite(userId,pelicula.getId());
                    Toast.makeText(context,"Agregada a favoritos: "+pelicula.getTitulo(),Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return movies.size();
    }
}

package edu.pmdm.monforte_danielimdbapp.ui.searchMovie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import edu.pmdm.monforte_danielimdbapp.R;
import edu.pmdm.monforte_danielimdbapp.adapters.MovieAdapter;
import edu.pmdm.monforte_danielimdbapp.api.TMDBApiService;
import edu.pmdm.monforte_danielimdbapp.databinding.FragmentSearchMovieBinding;
import edu.pmdm.monforte_danielimdbapp.models.Genre;
import edu.pmdm.monforte_danielimdbapp.models.Movie;
import edu.pmdm.monforte_danielimdbapp.models.MovieSearchResponse;

public class SearchMovieFragment extends Fragment {

    private FragmentSearchMovieBinding binding;
    private MovieAdapter moviesAdapter;
    private static List<Genre> genres =new ArrayList<Genre>(); //Se hace estática porque no va a cambiar, siempre será la misma. Así solo llamamos a la API la primera vez, y si cambiamos de fragmento al volver se mantendrán los géneros y no habrá que obtenerlos otra vez
    private List<Movie> moviesSearch =new ArrayList<Movie>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SearchMovieViewModel searchMovieViewModel = new ViewModelProvider(this).get(SearchMovieViewModel.class);

        binding = FragmentSearchMovieBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<Genre> adaptador = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, genres);
        if(genres.isEmpty()){ //Si la lista de géneros está vacía es que es la primera vez que cargamos el fragmento, asi que buscamos los géneros con la API
            binding.btnBuscar.setEnabled(false); //Desactivamos el botón para no poder buscar sin género
            Toast.makeText(getContext(),R.string.buscando_generos,Toast.LENGTH_SHORT).show();
            MovieSearchResponse.buscarGeneros(new TMDBApiService() {
                @Override
                public void onGenresReceived(List<Genre> genres) {
                    SearchMovieFragment.this.genres.addAll(genres);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.btnBuscar.setEnabled(true); //Volvemos a activar el botón ahora que hay géneros
                            adaptador.notifyDataSetChanged(); //Notificamos al adaptador de que han cambiado los datos, ya que esto no se ejecuta antes que lo de abajo, sino cuando termina la respuesta de la API. Lo de abajo se ejecuta y la lista de generos queda vacia
                        }
                    });
                }

                @Override
                public void onMoviesReceived(List<Movie> movies) {

                }
            },getContext());
        }
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGenero.setAdapter(adaptador);
        binding.spinnerGenero.setSelection(0);//Hacemos que esté seleccionado el primer género por defecto
        binding.btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid=true;
                String year=binding.editTextYear.getText().toString();
                if(year.equals("")){
                    valid=false;
                    Toast.makeText(getContext(), R.string.año_vacio,Toast.LENGTH_SHORT).show();
                }
                if(valid){
                    Genre selectedGenre=(Genre)binding.spinnerGenero.getSelectedItem();
                    MovieSearchResponse.buscarPeliculasPorAñoYGenero(year, selectedGenre.getId(), new TMDBApiService() {
                        @Override
                        public void onGenresReceived(List<Genre> genres) {

                        }

                        @Override
                        public void onMoviesReceived(List<Movie> movies) {
                            moviesSearch.clear();
                            moviesSearch.addAll(movies);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    moviesAdapter.notifyDataSetChanged(); //Notificamos al adaptador de que han cambiado los datos
                                }
                            });
                        }
                    },getContext());
                }
            }
        });
        moviesAdapter=new MovieAdapter(moviesSearch,this);
        binding.recyclerViewMovies.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.recyclerViewMovies.setAdapter(moviesAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
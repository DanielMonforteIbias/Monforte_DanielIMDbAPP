package edu.pmdm.monforte_danielimdbapp.ui.top10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.pmdm.monforte_danielimdbapp.adapters.MovieAdapter;
import edu.pmdm.monforte_danielimdbapp.api.IMDBApiService;
import edu.pmdm.monforte_danielimdbapp.databinding.FragmentTop10Binding;
import edu.pmdm.monforte_danielimdbapp.models.Movie;
import edu.pmdm.monforte_danielimdbapp.models.MovieOverviewResponse;
import edu.pmdm.monforte_danielimdbapp.models.MovieResponse;

public class Top10Fragment extends Fragment {

    private FragmentTop10Binding binding;
    private List<Movie>topMovies=new ArrayList<Movie>();
    private MovieAdapter adaptador;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Top10ViewModel top10ViewModel =
                new ViewModelProvider(this).get(Top10ViewModel.class);

        binding = FragmentTop10Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerViewTop10;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        adaptador=new MovieAdapter(topMovies);
        MovieResponse.buscarTop10(new IMDBApiService() {
            @Override
            public void onMoviesReceived(List<Movie> movies) {
                topMovies.addAll(movies);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adaptador.notifyDataSetChanged();
                    }
                });
            }
            //No se usa, se implementa porque IMDBApiService debe implementar todos los metodos de la interfaz
            @Override
            public void onDescriptionReceived(String descripcion) {

            }
        });
        recyclerView.setAdapter(adaptador);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
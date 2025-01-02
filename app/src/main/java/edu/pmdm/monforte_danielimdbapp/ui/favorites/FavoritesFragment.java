package edu.pmdm.monforte_danielimdbapp.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;

import java.util.ArrayList;
import java.util.List;

import edu.pmdm.monforte_danielimdbapp.adapters.MovieAdapter;
import edu.pmdm.monforte_danielimdbapp.database.FavoritesDatabaseHelper;
import edu.pmdm.monforte_danielimdbapp.databinding.FragmentFavoritasBinding;
import edu.pmdm.monforte_danielimdbapp.models.Movie;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritasBinding binding;
    private static List<Movie> favoriteMovies=new ArrayList<Movie>();
    private static MovieAdapter adaptador;
    private static FavoritesDatabaseHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FavoritesViewModel favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        binding = FragmentFavoritasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper=new FavoritesDatabaseHelper(getContext());
        RecyclerView recyclerView = binding.recyclerViewFavoritas;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favoriteMovies=dbHelper.getUserFavorites(GoogleSignIn.getLastSignedInAccount(getContext()).getId());
        adaptador=new MovieAdapter(favoriteMovies,this);
        recyclerView.setAdapter(adaptador); //Ponemos el adaptador al RecyclerView
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
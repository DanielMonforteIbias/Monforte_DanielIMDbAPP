package edu.pmdm.monforte_danielimdbapp.ui.searchMovie;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.pmdm.monforte_danielimdbapp.R;
import edu.pmdm.monforte_danielimdbapp.databinding.FragmentSearchMovieBinding;

public class SearchMovieFragment extends Fragment {

    private FragmentSearchMovieBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SearchMovieViewModel searchMovieViewModel = new ViewModelProvider(this).get(SearchMovieViewModel.class);

        binding = FragmentSearchMovieBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] opciones = {"Item 1", "Item 2"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, opciones);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGenero.setAdapter(adapter);

        binding.btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid=true;
                String yearString=binding.editTextYear.getText().toString();
                if(yearString.equals("")){
                    valid=false;
                    Toast.makeText(getContext(), R.string.año_vacio,Toast.LENGTH_SHORT).show();
                }
                if(valid){
                    int year=Integer.parseInt(yearString);
                    String genre=binding.spinnerGenero.getSelectedItem().toString();
                    
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
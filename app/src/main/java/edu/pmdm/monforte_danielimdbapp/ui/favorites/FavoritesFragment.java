package edu.pmdm.monforte_danielimdbapp.ui.favorites;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
    private static final int BLUETOOTH=3;
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
        binding.btnCompartirLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!comprobarPermisosBluetooth()){
                    pedirPermisosBluetooth();
                    Toast.makeText(getContext(),"Permisos de Bluetooth denegados!",Toast.LENGTH_SHORT).show();
                }
                else{
                    iniciarBluetooth();
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext()); //Inicializamos el dialogo
                    dialogo.setCancelable(false); //Establecemos que no es cancelable para que no se pueda cerrar al pulsar en otro lado
                    dialogo.setTitle("Películas favoritas en JSON"); //Ponemos el título
                    dialogo.setMessage(favoriteMovies.toString()); //Establecemos el mensaje del diálogo
                    dialogo.setPositiveButton("OK", new DialogInterface.OnClickListener() { //Ponemos un botón para cerrarlo
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    dialogo.show(); //Mostramos el diálogo
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void iniciarBluetooth(){
        BluetoothAdapter btAdapter= BluetoothAdapter.getDefaultAdapter();
        if(btAdapter==null){
            Toast.makeText(getContext(),"El dispositivo no soporta Bluetooth",Toast.LENGTH_SHORT).show();
        }
        else if(!btAdapter.isEnabled()){
            Intent enableBluetooth=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBluetooth);
        }
    }

    private void pedirPermisosBluetooth(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT}, BLUETOOTH);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH}, BLUETOOTH);
        }
    }

    private boolean comprobarPermisosBluetooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED;
        }
    }
}
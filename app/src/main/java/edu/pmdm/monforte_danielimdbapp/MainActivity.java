package edu.pmdm.monforte_danielimdbapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import edu.pmdm.monforte_danielimdbapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private ImageView imgFoto;
    private TextView txtNombre;
    private TextView txtEmail;
    private Button btnLogout;
    private GoogleSignInClient gClient;
    private GoogleSignInOptions gOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View headerView=navigationView.getHeaderView(0);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_top10, R.id.nav_favorites, R.id.nav_slideshow).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        txtNombre=headerView.findViewById(R.id.txtNombre);
        txtEmail=headerView.findViewById(R.id.txtEmail);
        btnLogout=headerView.findViewById(R.id.btnLogout);
        imgFoto=headerView.findViewById(R.id.imgViewFoto);
        gOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient= GoogleSignIn.getClient(this,gOptions);
        GoogleSignInAccount gAccount=GoogleSignIn.getLastSignedInAccount(this);
        String gName=gAccount.getDisplayName();
        String gEmail=gAccount.getEmail();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Uri gPhoto= gAccount.getPhotoUrl();
        txtNombre.setText(gName);
        txtEmail.setText(gEmail);
        Glide.with(this).load(gPhoto).placeholder(R.drawable.usuario).into(imgFoto);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId(); //Obtenemos el id del item pulsado
        if(id==R.id.action_credits){ //Si el item pulsado es el de creditos
            mostrarCreditos(); //Mostramos el dialogo de creditos
        }
        else if (id==R.id.action_settings){ //Si el item pulsado es el de opciones

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void mostrarCreditos(){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this); //Inicializamos el dialogo
        dialogo.setCancelable(false); //Establecemos que no es cancelable para que no se pueda cerrar al pulsar en otro lado
        dialogo.setTitle("Créditos"); //Ponemos el título como "Créditos"
        String instruccionesMensaje="Aplicación hecha por Daniel Monforte Ibias\n\nDAM2 2024/25"; //Creamos el mensaje
        dialogo.setMessage(instruccionesMensaje); //Establecemos el mensaje del diálogo
        dialogo.setPositiveButton("OK", new DialogInterface.OnClickListener() { //Ponemos un botón para cerrarlo
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        dialogo.show(); //Mostramos el diálogo
    }
}
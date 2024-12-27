package edu.pmdm.monforte_danielimdbapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Esta actividad es la que se invoca al iniciar la app, y decide qué verá primero el usuario en base a si hay sesión iniciada o no
 */
public class SplashActivity extends AppCompatActivity {
    boolean sesionIniciada=false;
    private GoogleSignIn GoogleSignIn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sesionIniciada= checkLoginStatus(); //COmprobamos si hay sesion iniciada
        if (sesionIniciada) { //Si hay sesion iniciada, iremos a la pantalla principal
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        } else { //Si no, iremos a la pantalla de Login
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        finish();
    }

    private boolean checkLoginStatus() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        return account != null; //Devuelve true si hay cuenta, es decir, si no es null, y false si es null
    }
}
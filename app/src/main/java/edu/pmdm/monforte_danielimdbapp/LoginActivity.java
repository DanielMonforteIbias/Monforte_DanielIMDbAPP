package edu.pmdm.monforte_danielimdbapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import edu.pmdm.monforte_danielimdbapp.databinding.ActivityLoginBinding;
import edu.pmdm.monforte_danielimdbapp.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private GoogleSignInOptions gOptions;
    private GoogleSignInClient gClient;
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth=FirebaseAuth.getInstance();
        gOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient= GoogleSignIn.getClient(this,gOptions);

        GoogleSignInAccount gAccount=GoogleSignIn.getLastSignedInAccount(this);
        if(gAccount!=null){
            System.out.println("a");
            finish();
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
        ActivityResultLauncher<Intent>activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()== LoginActivity.RESULT_OK){
                    Intent data=result.getData();
                    Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
                    try{
                        task.getResult(ApiException.class);
                        finish();
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }catch(ApiException e){
                        showToast("Ocurrio un error");
                    }
                }
            }
        });
        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent=gClient.getSignInIntent();
                activityResultLauncher.launch(signInIntent);
            }
        });

    }
    private void showToast(String mensaje){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }
}
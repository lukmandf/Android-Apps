package com.wnetsi.w_net;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wnetsi.R;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView daftarl;
    private EditText emaill, passwordl;
    private Button loginl;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        daftarl = (TextView) findViewById(R.id.daftaral);
        daftarl.setOnClickListener(this);

        loginl = (Button) findViewById(R.id.login);
        loginl.setOnClickListener(this);

        emaill = (EditText) findViewById(R.id.email);
        passwordl = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressbarl);

        mAuth = FirebaseAuth.getInstance();

//        if(mAuth.getCurrentUser()!=null){
//            startActivity(new Intent(getApplicationContext(),SendData.class));
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.daftaral:
                startActivity(new Intent(this, SignUp.class));
                break;

            case R.id.login:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = emaill.getText().toString().trim();
        String password = passwordl.getText().toString().trim();

        if(email.isEmpty()){
            emaill.setError("Isi Email");
            emaill.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emaill.setError("Tolong Masukkan Email yang Benar");
            return;
        }
        if(password.isEmpty()){
            passwordl.setError("Isi Password");
            passwordl.requestFocus();
            return;
        }
        if(password.length()<6){
            passwordl.setError("Password Harus Lebih dari 6");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Redirect ke profil user
                    startActivity(new Intent(Login.this, SendData.class));
                }
                else{
                    Toast.makeText(Login.this,"Login Gagal! Mohon Dicek Kembali",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
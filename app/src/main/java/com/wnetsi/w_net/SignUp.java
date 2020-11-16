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
import com.google.firebase.database.FirebaseDatabase;
import com.wnetsi.R;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private TextView wnetsu, sulogin, sudaftar;
    private EditText enama, enotelp, eemail, epassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        wnetsu = (TextView) findViewById(R.id.wnetsu);
        wnetsu.setOnClickListener(this);

        sulogin = (TextView) findViewById(R.id.sulogin);
        sulogin.setOnClickListener(this);

        sudaftar = (Button) findViewById(R.id.sudaftar);
        sudaftar.setOnClickListener(this);

        enama = (EditText) findViewById(R.id.nama);
        enotelp = (EditText) findViewById(R.id.notelp);
        eemail = (EditText) findViewById(R.id.email);
        epassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sulogin:
                startActivity(new Intent(this, Login.class));
                break;

            case R.id.sudaftar:
                susignup();
                break;
        }

    }

    private void susignup() {
        final String nama = enama.getText().toString().trim();
        final String notelp = enotelp.getText().toString().trim();
        final String email = eemail.getText().toString().trim();
        String password = epassword.getText().toString().trim();

        if(nama.isEmpty()){
            enama.setError("Nama wajib diisi!");
            enama.requestFocus();
            return;
        }
        if(notelp.isEmpty()){
            enotelp.setError("No Telepon wajib diisi!");
            enama.requestFocus();
            return;
        }
        if(email.isEmpty()){
            eemail.setError("Email wajib diisi!");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            eemail.setError("Tolong diisi Email yang Benar!");
            eemail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            epassword.setError("Password wajib diisi!");
            return;
        }
        if(password.length()<6){
            epassword.setError("Password harus lebih dari 6digit");
            epassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user=new User(nama, notelp, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUp.this, "Berhasil Terdaftar!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                        //redirect ke login
                                    }else{
                                        Toast.makeText(SignUp.this, "Gagal Terdaftar..., Mohon Diulangi!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(SignUp.this, "Gagal Terdaftar..., Mohon Diulangi!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
    }
}
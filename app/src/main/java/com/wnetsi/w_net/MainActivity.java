package com.wnetsi.w_net;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.wnetsi.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button signup, loginma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(this);

        loginma = (Button) findViewById(R.id.login);
        loginma.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup:
                startActivity (new Intent(this, SignUp.class));
                break;

            case R.id.login:
                startActivity (new Intent(this, Login.class));
                break;
        }
    }
}
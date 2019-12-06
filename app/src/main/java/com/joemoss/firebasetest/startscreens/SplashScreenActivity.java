package com.joemoss.firebasetest.startscreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.joemoss.firebasetest.main.MainViewActivity;
import com.joemoss.firebasetest.R;

public class SplashScreenActivity extends AppCompatActivity {

    private Button toRegisterActivity;
    private Button toLoginActivity;
    public final static int REQUEST_EXIT = 135;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        toRegisterActivity = findViewById(R.id.to_register_button);
        toLoginActivity = findViewById(R.id.to_login_button);

        toRegisterActivity.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                loadRegisterActivity();
            }
        });

        toLoginActivity.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                loadLoginActivity();
            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            Intent mainViewIntent = new Intent(this, MainViewActivity.class);
            startActivityForResult(mainViewIntent, REQUEST_EXIT);
            finish();

        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXIT) {
            if (resultCode == LoginActivity.LOGGED_IN) {
                this.finish();

            }
        }
    }

    private void loadRegisterActivity(){
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivityForResult(registerIntent, REQUEST_EXIT);
    }

    private void loadLoginActivity(){
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginIntent, REQUEST_EXIT);
    }




}

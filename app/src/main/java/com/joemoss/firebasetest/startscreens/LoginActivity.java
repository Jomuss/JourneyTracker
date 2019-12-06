package com.joemoss.firebasetest.startscreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.joemoss.firebasetest.main.MainViewActivity;
import com.joemoss.firebasetest.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private EditText email;
    private EditText password;
    private  Button loginButton;
    public final static int LOGGED_IN = 134;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email_login_field);
        password = findViewById(R.id.password_login_field);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                loginUser();
            }
        });
    }

    private void loginUser(){
        String userEmail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        fAuth.signInWithEmailAndPassword(userEmail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try{
                            //If user logs in, Start Main Activity and set result to kill Splash Screen
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                startMainViewActivity();
                                setResult(LOGGED_IN);
                                finish();

                            }else{
                                Toast.makeText(LoginActivity.this, "There was an error logging you in", Toast.LENGTH_SHORT).show();
                            }
                        }catch(Exception e){
                            e.printStackTrace();

                        }
                    }
                });
    }

    private void startMainViewActivity(){
        Intent mainViewIntent = new Intent(this, MainViewActivity.class);
        startActivity(mainViewIntent);
    }
}



package com.joemoss.firebasetest.startscreens;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.joemoss.firebasetest.main.MainViewActivity;
import com.joemoss.firebasetest.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import static com.joemoss.firebasetest.startscreens.LoginActivity.LOGGED_IN;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private EditText userPassword;
    private EditText userPasswordConfirm;
    private EditText userEmail;
    private EditText username;
    private Button registerButton;

    public RegisterActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fAuth = FirebaseAuth.getInstance();
        userEmail = findViewById(R.id.email_register_field);
        userPassword = findViewById(R.id.password_register_field);
        userPasswordConfirm = findViewById(R.id.password_confirm_field);
        username = findViewById(R.id.username_register_field);
        registerButton = findViewById(R.id.register_button);
        getSupportActionBar().setTitle("Journey Tracker");

        db = FirebaseFirestore.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                registerUser();
            }
        });


    }

    //Handles registering a user
    public void registerUser(){
        String password = userPassword.getText().toString().trim();

        //if either field is empty notify user
       //if(email.isEmpty() || password.isEmpty()){ }\
        boolean passCheck = checkPassword();

        if(!passCheck){
            Toast.makeText(RegisterActivity.this, "Your passwords do not match", Toast.LENGTH_SHORT).show();
        }
        else{
            isUsernameUnique(username.getText().toString());
        }

    }

    //checks if username is unique. If so, proceed to create firebase account
    private void isUsernameUnique(String username){

        CollectionReference users;
        users = db.collection("users");
        users.whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        System.out.println(task.getResult());
                        if(task.getResult().getDocuments().isEmpty()){
                            createFirebaseAuthUser();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "That Username already exists!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //Create user entry in Firebase Auth, if successful create user entry in FireStore
    private void createFirebaseAuthUser(){
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            if (task.isSuccessful()) {
                                FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username.getText().toString())
                                        .build();
                                curUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                        createFirestoreUserEntry();

                                    }
                                });

                            } else {
                                Toast.makeText(RegisterActivity.this, "There was an error registering you", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }



    private void createFirestoreUserEntry(){
        String uID = fAuth.getCurrentUser().getUid();
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", userEmail.getText().toString());
        userData.put("username", username.getText().toString());
        db.collection("users").document(uID).set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("addUserData", "User successfully registered and data added");
                        setResult(LOGGED_IN);
                        startMainViewActivity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("addUserData", "User successfully registered but data could not be added");
                    }
                });
    }

    //Updates FirebaseAuth Entry with selected username, starts Main View Activity on completion
//    private void updateFirebaseAuthEntry(){
//        FirebaseUser curUser = fAuth.getCurrentUser();
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(username.getText().toString())
//                .build();
//        curUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                startMainViewActivity();
//            }
//        });
//    }

    private void startMainViewActivity(){
        Intent mainViewIntent = new Intent(this, MainViewActivity.class);
        startActivity(mainViewIntent);
        finish();
    }






    private boolean checkPassword(){
        String pass = userPassword.getText().toString();
        String passConfirm = userPasswordConfirm.getText().toString();

        if(pass.equals(passConfirm)){return true;}

        return false;
    }




}

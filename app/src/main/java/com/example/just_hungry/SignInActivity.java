package com.example.just_hungry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.just_hungry.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignInActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextInputLayout emailField;
    private TextInputLayout passwordField;
    private TextInputLayout nameField;
    private Button signInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_page);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();


        // getting the email and password ELement
        emailField = (TextInputLayout) findViewById(R.id.emailField);
        passwordField = (TextInputLayout) findViewById(R.id.passwordField);
        nameField = (TextInputLayout) findViewById(R.id.nameField);
        signInButton = (Button) findViewById(R.id.signInButton);
        TextView haveAccountText = findViewById(R.id.haveaccount);

        // Add logic to check if the username and password are correct
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        if (preferences.getBoolean("logged_in", false)) {
            startHomeActivity();
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getEditText().getText().toString();
                String password = passwordField.getEditText().getText().toString();
                String name = nameField.getEditText().getText().toString();

//                Log.d("INPUTTED:", email+ password);
                // Do logic to check if the email and password are correct
                if (TextUtils.isEmpty(email)) {
                    emailField.setError("Please enter email");
                    emailField.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailField.setError("Please enter a valid email");
                    emailField.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordField.setError("Please enter password");
                    passwordField.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordField.setError("Please enter password");
                    passwordField.requestFocus();
                    return;
                }

                if (password.length() < 8) {
                    passwordField.setError("Password must be at least 8 characters");
                    passwordField.requestFocus();
                    return;
                }

                //Firebase Stuff
                // Create a new user object to store in Firestore

                String userId= UUID.randomUUID().toString();
                UserModel UserObject = new UserModel(email, password, name, userId);
                Map<String, Object> user = UserObject.getHashMapForFirestore();

                db.collection("users").add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(SignInActivity.this, "User added to Firestore", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignInActivity.this, "Error adding user to Firestore", Toast.LENGTH_SHORT).show();
                            }
                        });


                Log.d("signIn",email+ password);
                System.out.println("Email: " + email);
                System.out.println("Password: " + password);

                // If the username and password are correct, start the home activity and
                // set the "logged_in" preference to true
                editor.putBoolean("logged_in", true);
                editor.putString("userId", userId);
                editor.putString("email", email);
                editor.putString("password", password);
                editor.putString("name", name);
                editor.apply();
                startHomeActivity();
            }
        });
        haveAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to sign-in page
                Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void startHomeActivity() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

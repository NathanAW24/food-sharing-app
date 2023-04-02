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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextInputLayout emailField;
    private TextInputLayout passwordField;
    private Button loginButton;
    public boolean isPasswordCorrect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();


        // getting the email and password ELement
        emailField = (TextInputLayout) findViewById(R.id.emailField);
        passwordField = (TextInputLayout) findViewById(R.id.passwordField);
        loginButton = (Button) findViewById(R.id.loginButton);
        TextView donthaveAccountText = findViewById(R.id.donthaveaccount);
        // getting the email and password from the text fields
        String email = emailField.getEditText().getText().toString();
        String password = passwordField.getEditText().getText().toString();
        Log.d("login",email+ password);

        // Add logic to check if the username and password are correct
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        if (preferences.getBoolean("logged_in", false)) {
            startHomeActivity();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getEditText().getText().toString();
                String password = passwordField.getEditText().getText().toString();

                Log.d("INPUTTED:", email + password);
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

                if (password.length() < 8) {
                    passwordField.setError("Password must be at least 8 characters");
                    passwordField.requestFocus();
                    return;
                }

                //Firebase Stuff
                // Create a new user object to store in Firestore
                Map<String, Object> user = new HashMap<>();
                user.put("email", email);
                user.put("password", password);


                db.collection("users")
                        .whereEqualTo("email", email)
                        .whereEqualTo("password", password)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                System.out.println("QuerySnapshot: " + queryDocumentSnapshots);
                                if (queryDocumentSnapshots.size() > 0) {
                                    // Password matches

                                    Toast.makeText(LoginActivity.this, "Password matches", Toast.LENGTH_SHORT).show();
                                    handleSuccessfulLogin();

                                } else {
                                    // Password does not match
                                    isPasswordCorrect = false;
                                    Toast.makeText(LoginActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();

                                    return;
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error querying Firestore", Toast.LENGTH_SHORT).show();
                            }
                        }); }

                private void handleSuccessfulLogin(){
                    Log.d("login",email+ password);
                    System.out.println("Email: " + email);
                    System.out.println("Password: " + password);


                    // If the username and password are correct, start the home activity and
                    // set the "logged_in" preference to true
                    editor.putBoolean("logged_in", true);
                    editor.apply();
                    startHomeActivity();
                }


        });
        donthaveAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to sign-in page
                Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

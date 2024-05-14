package com.example.parkme.LoginRegisterActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parkme.MainActivity;
import com.example.parkme.R;
import com.example.parkme.Others.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterFullName, editTextRegisterEmail, editTextRegisterDOB, editTextRegisterMobile,
            editTextRegisterPassword, editTextRegisterConfirmPassword;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeractivity);

        editTextRegisterFullName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDOB = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterPassword = findViewById(R.id.editText_register_password);
        editTextRegisterConfirmPassword = findViewById(R.id.editText_register_confirm_password);

        auth = FirebaseAuth.getInstance();

        Button Button_register = findViewById(R.id.Button_register);
        Button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textFullName = editTextRegisterFullName.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textDOB = editTextRegisterDOB.getText().toString();
                String textMobile = editTextRegisterMobile.getText().toString();
                String textPassword = editTextRegisterPassword.getText().toString();
                String textConfirmPassword = editTextRegisterConfirmPassword.getText().toString();

                // Validate form fields
                if (validateForm(textFullName, textEmail, textDOB, textMobile, textPassword, textConfirmPassword)) {
                    // If all validations pass, register the user
                    registerUser(textFullName, textEmail, textDOB, textMobile, textPassword);
                }
            }
        });
    }

    private boolean validateForm(String fullName, String email, String dob, String mobile, String password, String confirmPassword) {
        // Perform validation on form fields
        if (fullName.isEmpty() || email.isEmpty() || dob.isEmpty() || mobile.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isPasswordValid(password)) {
            Toast.makeText(RegisterActivity.this, "Password must be at least 8 characters long and contain at least one symbol, one capital letter, and one number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registerUser(String textFullName, String textEmail, String textDOB, String textMobile, String textPassword) {
        // Register the user with Firebase Authentication
        auth.createUserWithEmailAndPassword(textEmail, textPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                // Save additional user details to Firebase Realtime Database
                                DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered users");
                                String userId = firebaseUser.getUid();
                                referenceProfile.child(userId).setValue(new User(textFullName, textDOB, textMobile))
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // After successful registration, redirect to MainActivity
                                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                                    finish(); // Close the current activity
                                                    Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // Registration failed
                                                    Toast.makeText(RegisterActivity.this, "Failed to save user details: " + task.getException(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Registration failed
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isPasswordValid(String password) {
        // Password must contain at least one symbol, one capital letter, one number, and be at least 8 characters long
        return password != null && password.length() >= 8 &&
                password.matches(".*\\d.*") &&  // Contains at least one digit
                password.matches(".*[A-Z].*") &&  // Contains at least one uppercase letter
                password.matches(".*[@#$%^&+=!].*"); // Contains at least one symbol
    }
}

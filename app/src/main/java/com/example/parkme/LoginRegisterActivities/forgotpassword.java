package com.example.parkme.LoginRegisterActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parkme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {

    // Declare EditText and Button objects
    EditText emailEditText;
    Button sendCodeButton;

    // Firebase authentication instance
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        // Initialize Firebase authentication
        fAuth = FirebaseAuth.getInstance();

        // Initialize EditText and Button objects
        emailEditText = findViewById(R.id.email_forgot);
        sendCodeButton = findViewById(R.id.send_code_btn);

        // Set click listener for the "Send Code" button
        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the email entered by the user
                String email = emailEditText.getText().toString().trim();

                // Check if email is empty
                if (email.isEmpty()) {
                    // Show error message if email is empty
                    emailEditText.setError("Email is required");
                    emailEditText.requestFocus();
                    return;
                }

                // Check if email is in valid format (ending with "@gmail.com")
                if (!email.endsWith("@gmail.com")) {
                    // Show error message if email format is invalid
                    emailEditText.setError("Enter a valid Gmail address");
                    emailEditText.requestFocus();
                    return;
                }

                // Send a password reset email to the provided email address
                fAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Email sent successfully
                                    Toast.makeText(getApplicationContext(), "Code sent to " + email, Toast.LENGTH_SHORT).show();
                                    // Navigate to login activity
                                    Intent intent = new Intent(forgotpassword.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Failed to send email
                                    Toast.makeText(getApplicationContext(), "Failed to send code", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}

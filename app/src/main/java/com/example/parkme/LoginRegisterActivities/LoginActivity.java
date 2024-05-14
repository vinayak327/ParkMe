package com.example.parkme.LoginRegisterActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkme.MainActivity2;
import com.example.parkme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    EditText memail, mpassword;
    Button mloginbtn;
    TextView mcreatebtn, mForgotPasswordBtn;

    ProgressBar progressBar;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        memail = findViewById(R.id.email);
        mpassword = findViewById(R.id.password);
        mloginbtn = findViewById(R.id.loginbtn);
        mcreatebtn = findViewById(R.id.createbtn);
        mForgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);
        progressBar = findViewById(R.id.progressbar);
        fAuth = FirebaseAuth.getInstance();

        ImageView imageView = findViewById(R.id.showhidepass);
        imageView.setImageResource(R.drawable.ic_hide_pwd);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mpassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    mpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageView.setImageResource(R.drawable.ic_hide_pwd);
                } else {
                    mpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageView.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });

        mcreatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        mForgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), forgotpassword.class));
            }
        });

        Button ButtonLogin = findViewById(R.id.loginbtn);
        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = memail.getText().toString().trim();
                String password = mpassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                    memail.setError("Email is Required");
                    memail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(LoginActivity.this, "Please re-Enter Your Email", Toast.LENGTH_SHORT).show();
                    memail.setError(" ValidEmail is Required");
                    memail.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    mpassword.setError(" Password is Required");
                    mpassword.requestFocus();
                } else if (password.length() < 6) {
                    Toast.makeText(LoginActivity.this, "Password Must be more than 6 characters", Toast.LENGTH_SHORT).show();
                    mpassword.setError("Password is too Weak");
                    mpassword.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(email, password);
                }
            }
        });
    }

    private void loginUser(String textEmail, String textpassword) {
        fAuth.signInWithEmailAndPassword(textEmail, textpassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = fAuth.getCurrentUser();
                    if (firebaseUser.isEmailVerified()) {
                        Toast.makeText(LoginActivity.this, "You are Logged in Now", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity2.class));
                        finish();
                    } else {
                        firebaseUser.sendEmailVerification();
                        fAuth.signOut();
                        showAlertDialog();
                    }
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        memail.setError("User does not exist or is no longer valid. please register again");
                        memail.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        memail.setError("Invalid credentials. kindly, check and re-enter");
                        memail.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email is not verified");
        builder.setMessage("Please verify your email now. You cannot log in without email verification");
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(LoginActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, LoginActivity.class));
        finish();
    }





    @Override
    protected void onStart() {
        super.onStart();
        if (fAuth.getCurrentUser() != null) {
            // Remove the code that automatically redirects to MainActivity2
            Toast.makeText(LoginActivity.this, "Log in now", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(LoginActivity.this, "You can log in", Toast.LENGTH_SHORT).show();
        }
    }}
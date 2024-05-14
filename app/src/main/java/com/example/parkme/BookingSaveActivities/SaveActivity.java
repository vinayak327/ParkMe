package com.example.parkme.BookingSaveActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parkme.Others.Payment;
import com.example.parkme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SaveActivity extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText plate;
    private EditText durationfrom;
    private EditText durationto;

    private EditText slotno;

    private Button save;
    private Button payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        phone = findViewById(R.id.editTextPhone);
        plate = findViewById(R.id.editTextPlateNo);
        durationfrom = findViewById(R.id.editTextDurationFrom);
        durationto = findViewById(R.id.editTextDurationTo);
        slotno = findViewById(R.id.editTextSlotNo);
        save = findViewById(R.id.buttonSave);
        payment = findViewById(R.id.buttonMakePayment);

        payment.setEnabled(false); // Initially disable the Make Payment button

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get values from EditText fields
                String nameValue = name.getText().toString();
                String emailValue = email.getText().toString();
                String phoneValue = phone.getText().toString();
                String plateValue = plate.getText().toString();
                String durationFromValue = durationfrom.getText().toString();
                String durationToValue = durationto.getText().toString();
                String slotNoValue = slotno.getText().toString();

                // Email format validation
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches() || !emailValue.endsWith("@gmail.com")) {
                    email.setError("Enter a valid Gmail address");
                    email.requestFocus();
                    return;
                }

                // Phone number length validation
                if (phoneValue.length() != 10) {
                    phone.setError("Phone number should be 10 digits long");
                    phone.requestFocus();
                    return;
                }

                // Car plate number format validation
                if (!plateValue.matches("[a-zA-Z]{2}\\d{2}\\d{4}") || plateValue.length() != 8) {
                    plate.setError("Enter a valid car plate number");
                    plate.requestFocus();
                    return;
                }

                // Slot number format validation
                if (!slotNoValue.matches("\\d{1,2}")) {
                    slotno.setError("Enter a valid slot number");
                    slotno.requestFocus();
                    return;
                }

                // All validations passed, proceed with saving data
                Map<String, String> v = new HashMap<>();
                v.put("name", nameValue);
                v.put("email", emailValue);
                v.put("phone", phoneValue);
                v.put("plate", plateValue);
                v.put("durationfrom", durationFromValue);
                v.put("durationto", durationToValue);
                v.put("slotno", slotNoValue);
                FirebaseFirestore.getInstance().collection("vendor").add(v).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Details saved. Now make payment", Toast.LENGTH_SHORT).show();
                            payment.setEnabled(true); // Enable the Make Payment button
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to save details. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if all details are filled and saved
                if (!name.getText().toString().isEmpty() &&
                        !email.getText().toString().isEmpty() &&
                        !phone.getText().toString().isEmpty() &&
                        !plate.getText().toString().isEmpty() &&
                        !durationfrom.getText().toString().isEmpty() &&
                        !durationto.getText().toString().isEmpty() &&
                        !slotno.getText().toString().isEmpty()) {
                    Intent intent = new Intent(SaveActivity.this, Payment.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Fill all details first and click on Save", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

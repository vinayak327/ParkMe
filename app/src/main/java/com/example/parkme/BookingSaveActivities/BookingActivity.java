package com.example.parkme.BookingSaveActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parkme.Others.Booking;
import com.example.parkme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextPhone, editTextPlateNo, editTextDurationFrom, editTextDurationTo, editTextSlotNo;
    private Button buttonSave;


    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Initialize FirebaseApp
        FirebaseApp.initializeApp(this);

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPlateNo = findViewById(R.id.editTextPlateNo);
        editTextDurationFrom = findViewById(R.id.editTextDurationFrom);
        editTextDurationTo = findViewById(R.id.editTextDurationTo);
        editTextSlotNo = findViewById(R.id.editTextSlotNo);
        buttonSave = findViewById(R.id.buttonSave);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("bookings");

        // Set click listener for the Save button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    saveBookingData();
                }
            }
        });

        // Set click listener for Duration From EditText to open date and time picker
        editTextDurationFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement date and time picker functionality
                openDateTimePicker(editTextDurationFrom);
            }
        });

        // Set click listener for Duration To EditText to open date and time picker
        editTextDurationTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement date and time picker functionality
                openDateTimePicker(editTextDurationTo);
            }
        });
    }

    private void openDateTimePicker(final EditText editText) {
        // TODO: Implement date and time picker functionality
        // For now, let's just set the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String currentDateAndTime = dateFormat.format(new Date());
        editText.setText(currentDateAndTime);
    }

    private boolean validateForm() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String plateNo = editTextPlateNo.getText().toString().trim();
        String durationFrom = editTextDurationFrom.getText().toString().trim();
        String durationTo = editTextDurationTo.getText().toString().trim();
        String slotNo = editTextSlotNo.getText().toString().trim();

        // Name validation
        if (name.isEmpty()) {
            editTextName.setError("Name is required");
            return false;
        }

        // Check for numeric characters in name
        if (name.matches(".*\\d.*")) {
            editTextName.setError("Invalid name: Numeric characters not allowed");
            return false;
        }

        // Email validation
        if (email.isEmpty() || !email.endsWith("@gmail.com")) {
            editTextEmail.setError("Enter a valid Gmail address");
            return false;
        }

        // Phone number validation
        if (phone.isEmpty() || phone.length() != 10 || !phone.matches("[0-9]+")) {
            editTextPhone.setError("Enter a valid 10-digit phone number");
            return false;
        }

        // Plate number validation
        if (plateNo.isEmpty() || plateNo.length() > 15) {
            editTextPlateNo.setError("Enter a valid plate number (max 15 characters)");
            return false;
        }

        // Slot number validation
        if (slotNo.isEmpty() || !slotNo.matches("\\d{1,2}")) {
            editTextSlotNo.setError("Enter a valid slot number (1-99)");
            return false;
        }

        // If all validations pass
        return true;
    }

    private void saveBookingData() {
        // Get user input from EditText fields
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String plateNo = editTextPlateNo.getText().toString().trim();
        String durationFrom = editTextDurationFrom.getText().toString().trim();
        String durationTo = editTextDurationTo.getText().toString().trim();
        String slotNo = editTextSlotNo.getText().toString().trim();

        // Create a new Booking object with the retrieved data
        Booking booking = new Booking(name, email, phone, plateNo, durationFrom, durationTo, slotNo);

        // Push the booking data to Firebase Realtime Database
        databaseReference.push().setValue(booking)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Data saved successfully
                            Toast.makeText(BookingActivity.this, "Details saved", Toast.LENGTH_SHORT).show();
                        } else {
                            // Data failed to save
                            Toast.makeText(BookingActivity.this, "Failed to save details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Clear EditText fields after saving
        editTextName.setText("");
        editTextEmail.setText("");
        editTextPhone.setText("");
        editTextPlateNo.setText("");
        editTextDurationFrom.setText("");
        editTextDurationTo.setText("");
        editTextSlotNo.setText("");
    }

}

package com.example.parkme.BookingSaveActivities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parkme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
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

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private FirebaseFirestore db;
    private String documentId;

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

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        db = FirebaseFirestore.getInstance();

        durationfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(durationfrom);
            }
        });

        durationto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(durationto);
            }
        });

        final int spaceIndex = getIntent().getIntExtra("spaceIndex", -1);

        // Mark space as "booking in progress"
        markSpaceAsBookingInProgress(spaceIndex);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameValue = name.getText().toString();
                String emailValue = email.getText().toString();
                String phoneValue = phone.getText().toString();
                String plateValue = plate.getText().toString().toUpperCase(); // Convert to upper case for consistency
                String durationFromValue = durationfrom.getText().toString();
                String durationToValue = durationto.getText().toString();
                String slotNoValue = slotno.getText().toString();

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches() || !emailValue.endsWith("@gmail.com")) {
                    email.setError("Enter a valid Gmail address");
                    email.requestFocus();
                    return;
                }

                if (phoneValue.length() != 10) {
                    phone.setError("Phone number should be 10 digits long");
                    phone.requestFocus();
                    return;
                }

                if (!plateValue.matches("[a-zA-Z]{2}\\d{2}[a-zA-Z]{2}\\d{4}") || plateValue.length() != 10) {
                    plate.setError("Enter a valid car plate number");
                    plate.requestFocus();
                    return;
                }

                if (!slotNoValue.matches("[1-6]")) {
                    slotno.setError("Enter a valid slot number (1-6)");
                    slotno.requestFocus();
                    return;
                }

                // Send email functionality
                sendEmail(nameValue, emailValue, phoneValue, plateValue, durationFromValue, durationToValue, slotNoValue);

                // Save details to Firestore
                saveDetailsToFirestore(nameValue, emailValue, phoneValue, plateValue, durationFromValue, durationToValue, slotNoValue, spaceIndex);
            }
        });
    }

    private void markSpaceAsBookingInProgress(int spaceIndex) {
        Map<String, Object> spaceStatus = new HashMap<>();
        spaceStatus.put("bookingInProgress", true);
        spaceStatus.put("booked", false);

        db.collection("parkingSpaces").document(String.valueOf(spaceIndex))
                .set(spaceStatus)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(SaveActivity.this, "Failed to mark space as booking in progress", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void saveDetailsToFirestore(String nameValue, String emailValue, String phoneValue, String plateValue,
                                        String durationFromValue, String durationToValue, String slotNoValue, int spaceIndex) {
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
                    // Mark space as booked
                    markSpaceAsBooked(spaceIndex);
                    Toast.makeText(getApplicationContext(), "Details saved", Toast.LENGTH_SHORT).show();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("spaceIndex", spaceIndex);
                    resultIntent.putExtra("name", nameValue);
                    resultIntent.putExtra("email", emailValue);
                    resultIntent.putExtra("phone", phoneValue);
                    resultIntent.putExtra("plate", plateValue);
                    resultIntent.putExtra("durationFrom", durationFromValue);
                    resultIntent.putExtra("durationTo", durationToValue);
                    resultIntent.putExtra("slotNo", slotNoValue);

                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to save details. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void markSpaceAsBooked(int spaceIndex) {
        Map<String, Object> spaceStatus = new HashMap<>();
        spaceStatus.put("bookingInProgress", false);
        spaceStatus.put("booked", true);

        db.collection("parkingSpaces").document(String.valueOf(spaceIndex))
                .update(spaceStatus)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(SaveActivity.this, "Failed to mark space as booked", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showDateTimePicker(final EditText editText) {
        final Calendar currentDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(SaveActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                TimePickerDialog timePickerDialog = new TimePickerDialog(SaveActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        if (editText != null)
                            editText.setText(dateFormat.format(calendar.getTime()) + " " + timeFormat.format(calendar.getTime()));
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void sendEmail(String nameValue, String emailValue, String phoneValue, String plateValue,
                           String durationFromValue, String durationToValue, String slotNoValue) {
        String subject = "Parking Booking Details";
        String message = "Hi " + nameValue + ",\n\n" +
                "Your parking booking details are as follows:\n\n" +
                "Email: " + emailValue + "\n" +
                "Phone: " + phoneValue + "\n" +
                "Plate Number: " + plateValue + "\n" +
                "Duration From: " + durationFromValue + "\n" +
                "Duration To: " + durationToValue + "\n" +
                "Slot Number: " + slotNoValue + "\n\n" +
                "Thank you for booking with us.";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailValue});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(intent, "Send Email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}

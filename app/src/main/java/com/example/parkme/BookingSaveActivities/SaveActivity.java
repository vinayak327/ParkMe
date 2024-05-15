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
import com.example.parkme.razorpay;
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
    private Button payment;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;

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

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

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

        payment.setEnabled(false);

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
                            payment.setEnabled(true);
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
                if (!name.getText().toString().isEmpty() &&
                        !email.getText().toString().isEmpty() &&
                        !phone.getText().toString().isEmpty() &&
                        !plate.getText().toString().isEmpty() &&
                        !durationfrom.getText().toString().isEmpty() &&
                        !durationto.getText().toString().isEmpty() &&
                        !slotno.getText().toString().isEmpty()) {
                    Intent intent = new Intent(SaveActivity.this, razorpay.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Fill all details first and click on Save", Toast.LENGTH_SHORT).show();
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
}

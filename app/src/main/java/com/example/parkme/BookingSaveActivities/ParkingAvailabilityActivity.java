package com.example.parkme.BookingSaveActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parkme.R;

public class ParkingAvailabilityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_availability);

        Button bookSlotButton1 = findViewById(R.id.button_book_slot_1);
        bookSlotButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the BookingActivity when the button is clicked
                Intent intent = new Intent(ParkingAvailabilityActivity.this, SaveActivity.class);
                startActivity(intent);
            }
        });

        Button bookSlotButton2 = findViewById(R.id.button_book_slot_2);
        bookSlotButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the BookingActivity when the button is clicked
                Intent intent = new Intent(ParkingAvailabilityActivity.this, SaveActivity.class);
                startActivity(intent);
            }
        });

        Button bookSlotButton3 = findViewById(R.id.button_book_slot_3);
        bookSlotButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the BookingActivity when the button is clicked
                Intent intent = new Intent(ParkingAvailabilityActivity.this, SaveActivity.class);
                startActivity(intent);
            }
        });

        Button bookSlotButton4 = findViewById(R.id.button_book_slot_4);
        bookSlotButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the BookingActivity when the button is clicked
                Intent intent = new Intent(ParkingAvailabilityActivity.this, SaveActivity.class);
                startActivity(intent);
            }
        });

        Button bookSlotButton5 = findViewById(R.id.button_book_slot_5);
        bookSlotButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the BookingActivity when the button is clicked
                Intent intent = new Intent(ParkingAvailabilityActivity.this, SaveActivity.class);
                startActivity(intent);
            }
        });

        Button bookSlotButton6 = findViewById(R.id.button_book_slot_6);
        bookSlotButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the BookingActivity when the button is clicked
                Intent intent = new Intent(ParkingAvailabilityActivity.this, SaveActivity.class);
                startActivity(intent);
            }
        });
    }
}

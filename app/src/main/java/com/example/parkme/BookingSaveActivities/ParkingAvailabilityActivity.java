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

        Button bookSlotButton = findViewById(R.id.button_book_slot);
        bookSlotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the BookingActivity when the button is clicked
                Intent intent = new Intent(ParkingAvailabilityActivity.this, SaveActivity.class);
                startActivity(intent);
            }
        });
    }
}

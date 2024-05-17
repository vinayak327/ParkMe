// demo.java

package com.example.parkme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parkme.BookingSaveActivities.SaveActivity;

public class demo extends AppCompatActivity {

    private TextView[] parkingSpaceTexts;
    private Button[] paymentButtons;
    private static final int REQUEST_CODE_SAVE_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        // Initialize array to hold parking space TextViews
        parkingSpaceTexts = new TextView[6];
        parkingSpaceTexts[0] = findViewById(R.id.parking_space_1_text);
        parkingSpaceTexts[1] = findViewById(R.id.parking_space_2_text);
        parkingSpaceTexts[2] = findViewById(R.id.parking_space_3_text);
        parkingSpaceTexts[3] = findViewById(R.id.parking_space_4_text);
        parkingSpaceTexts[4] = findViewById(R.id.parking_space_5_text);
        parkingSpaceTexts[5] = findViewById(R.id.parking_space_6_text);

        // Initialize array to hold payment buttons
        paymentButtons = new Button[6];
        paymentButtons[0] = findViewById(R.id.make_payment_1_button);
        paymentButtons[1] = findViewById(R.id.make_payment_2_button);
        paymentButtons[2] = findViewById(R.id.make_payment_3_button);
        paymentButtons[3] = findViewById(R.id.make_payment_4_button);
        paymentButtons[4] = findViewById(R.id.make_payment_5_button);
        paymentButtons[5] = findViewById(R.id.make_payment_6_button);

        // Disable all payment buttons initially
        for (Button button : paymentButtons) {
            button.setEnabled(false);
            button.setVisibility(View.GONE); // Hide the button initially
        }

        // Set click listeners for book space buttons
        setButtonClickListener(R.id.book_space_1_button, 0);
        setButtonClickListener(R.id.book_space_2_button, 1);
        setButtonClickListener(R.id.book_space_3_button, 2);
        setButtonClickListener(R.id.book_space_4_button, 3);
        setButtonClickListener(R.id.book_space_5_button, 4);
        setButtonClickListener(R.id.book_space_6_button, 5);
    }

    // Function to set click listener for book space buttons
    private void setButtonClickListener(int bookButtonId, final int spaceIndex) {
        Button bookButton = findViewById(bookButtonId);

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SaveActivity
                Intent intent = new Intent(demo.this, SaveActivity.class);
                intent.putExtra("spaceIndex", spaceIndex);
                startActivityForResult(intent, REQUEST_CODE_SAVE_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SAVE_ACTIVITY) {
            if (resultCode == RESULT_OK && data != null) {
                int spaceIndex = data.getIntExtra("spaceIndex", -1);
                if (spaceIndex != -1) {
                    parkingSpaceTexts[spaceIndex].setText("Booked");
                    // Enable payment button for the booked space
                    if (spaceIndex >= 0 && spaceIndex < paymentButtons.length) {
                        paymentButtons[spaceIndex].setEnabled(true);
                        paymentButtons[spaceIndex].setVisibility(View.VISIBLE); // Make the button visible
                        paymentButtons[spaceIndex].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Redirect to RazorpayActivity
                                Intent razorpayIntent = new Intent(demo.this, razorpay.class);
                                startActivity(razorpayIntent);
                            }
                        });
                    }
                }
            }
        }
    }
}

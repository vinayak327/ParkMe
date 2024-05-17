package com.example.parkme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parkme.BookingSaveActivities.SaveActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class demo extends AppCompatActivity {

    private TextView[] parkingSpaceTexts;
    private Button[] paymentButtons;
    private Button[] cancelButton;
    private static final int REQUEST_CODE_SAVE_ACTIVITY = 1;
    private FirebaseFirestore db;

    // Map to store user details for each space index
    private Map<Integer, Map<String, String>> userDetailsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        db = FirebaseFirestore.getInstance();
        userDetailsMap = new HashMap<>();

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

        // Initialize array to hold cancel buttons
        cancelButton = new Button[6];
        cancelButton[0] = findViewById(R.id.cancel_space_1_button);
        cancelButton[1] = findViewById(R.id.cancel_space_2_button);
        cancelButton[2] = findViewById(R.id.cancel_space_3_button);
        cancelButton[3] = findViewById(R.id.cancel_space_4_button);
        cancelButton[4] = findViewById(R.id.cancel_space_5_button);
        cancelButton[5] = findViewById(R.id.cancel_space_6_button);

        // Disable all payment buttons initially
        for (Button button : paymentButtons) {
            button.setEnabled(false);
            button.setVisibility(View.GONE); // Hide the button initially
        }

        // Disable all cancel buttons initially
        for (Button button : cancelButton) {
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

        // Set click listeners for payment buttons
        setPaymentButtonClickListener(paymentButtons[0], 0);
        setPaymentButtonClickListener(paymentButtons[1], 1);
        setPaymentButtonClickListener(paymentButtons[2], 2);
        setPaymentButtonClickListener(paymentButtons[3], 3);
        setPaymentButtonClickListener(paymentButtons[4], 4);
        setPaymentButtonClickListener(paymentButtons[5], 5);

        // Set click listeners for cancel buttons
        setCancelButtonClickListener(cancelButton[0], 0);
        setCancelButtonClickListener(cancelButton[1], 1);
        setCancelButtonClickListener(cancelButton[2], 2);
        setCancelButtonClickListener(cancelButton[3], 3);
        setCancelButtonClickListener(cancelButton[4], 4);
        setCancelButtonClickListener(cancelButton[5], 5);

        // Listen for real-time updates
        listenForRealTimeUpdates();
    }

    // Function to set click listener for book space buttons


    // Function to set click listener for payment buttons
    private void setPaymentButtonClickListener(Button paymentButton, final int spaceIndex) {
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(demo.this, razorpay.class);
                Map<String, String> userDetails = userDetailsMap.get(spaceIndex);
                if (userDetails != null) {
                    intent.putExtra("name", userDetails.get("name"));
                    intent.putExtra("email", userDetails.get("email"));
                    intent.putExtra("phone", userDetails.get("phone"));
                    intent.putExtra("plate", userDetails.get("plate"));
                    intent.putExtra("durationFrom", userDetails.get("durationFrom"));
                    intent.putExtra("durationTo", userDetails.get("durationTo"));
                    intent.putExtra("slotNo", userDetails.get("slotNo"));
                }
                startActivity(intent);
            }
        });
    }


    private void listenForRealTimeUpdates() {
        for (int i = 0; i < 6; i++) {
            final int index = i;
            db.collection("parkingSpaces").document(String.valueOf(index))
                    .addSnapshotListener((snapshot, e) -> {
                        if (e != null) {
                            Toast.makeText(demo.this, "Listen failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Boolean bookingInProgress = snapshot.getBoolean("bookingInProgress");
                            Boolean booked = snapshot.getBoolean("booked");

                            if (bookingInProgress != null && bookingInProgress) {
                                parkingSpaceTexts[index].setText("Booking in progress");
                                // Show cancel and payment buttons
                                paymentButtons[index].setVisibility(View.VISIBLE);
                                cancelButton[index].setVisibility(View.VISIBLE);
                                paymentButtons[index].setEnabled(true);
                                cancelButton[index].setEnabled(true);
                                // Hide book space button
                                findViewById(getResources().getIdentifier("book_space_" + (index + 1) + "_button", "id", getPackageName())).setVisibility(View.GONE);
                            } else if (booked != null && booked) {
                                parkingSpaceTexts[index].setText("Booked");
                                // Show cancel and payment buttons
                                paymentButtons[index].setVisibility(View.VISIBLE);
                                cancelButton[index].setVisibility(View.VISIBLE);
                                paymentButtons[index].setEnabled(true);
                                cancelButton[index].setEnabled(true);
                                // Hide book space button
                                findViewById(getResources().getIdentifier("book_space_" + (index + 1) + "_button", "id", getPackageName())).setVisibility(View.GONE);
                            } else {
                                parkingSpaceTexts[index].setText("Available");
                                // Show book space button and hide payment and cancel buttons
                                paymentButtons[index].setVisibility(View.GONE);
                                cancelButton[index].setVisibility(View.GONE);
                                paymentButtons[index].setEnabled(false);
                                cancelButton[index].setEnabled(false);
                                // Show book space button
                                findViewById(getResources().getIdentifier("book_space_" + (index + 1) + "_button", "id", getPackageName())).setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }


    // Function to set click listener for cancel buttons
    // Function to set click listener for cancel buttons
    private void setCancelButtonClickListener(Button cancelButton, final int spaceIndex) {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset the status to not booked and booking in progress to false
                db.collection("parkingSpaces").document(String.valueOf(spaceIndex))
                        .update("booked", false, "bookingInProgress", false)
                        .addOnSuccessListener(aVoid -> {
                            // Update UI
                            listenForRealTimeUpdates();
                            Toast.makeText(demo.this, "Booking cancelled", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(demo.this, "Error cancelling booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }



    private void setButtonClickListener(int bookButtonId, final int spaceIndex) {
        Button bookButton = findViewById(bookButtonId);

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("parkingSpaces").document(String.valueOf(spaceIndex))
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists() && (document.getBoolean("bookingInProgress") || document.getBoolean("booked"))) {
                                    Toast.makeText(demo.this, "Parking space is not available for booking.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Navigate to SaveActivity
                                    Intent intent = new Intent(demo.this, SaveActivity.class);
                                    intent.putExtra("spaceIndex", spaceIndex);
                                    startActivityForResult(intent, REQUEST_CODE_SAVE_ACTIVITY);
                                }
                            }
                        });
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
                    // Store user details
                    Map<String, String> userDetails = new HashMap<>();
                    userDetails.put("name", data.getStringExtra("name"));
                    userDetails.put("email", data.getStringExtra("email"));
                    userDetails.put("phone", data.getStringExtra("phone"));
                    userDetails.put("plate", data.getStringExtra("plate"));
                    userDetails.put("durationFrom", data.getStringExtra("durationFrom"));
                    userDetails.put("durationTo", data.getStringExtra("durationTo"));
                    userDetails.put("slotNo", data.getStringExtra("slotNo"));

                    userDetailsMap.put(spaceIndex, userDetails);

                    // Update UI is already handled by real-time listener
                }
            }
        }
    }
}

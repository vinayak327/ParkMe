package com.example.parkme.OtherActivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parkme.MainActivity2;
import com.example.parkme.R;

public class RateActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button submitBtn, submitFeedbackBtn;
    private EditText feedbackEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        ratingBar = findViewById(R.id.ratingBar);
        submitBtn = findViewById(R.id.submitBtn);
        submitFeedbackBtn = findViewById(R.id.submitFeedbackBtn);
        feedbackEditText = findViewById(R.id.feedbackEditText);

        submitBtn.setOnClickListener(view -> {
            float rating = ratingBar.getRating();
            if (rating == 0) {
                Toast.makeText(RateActivity.this, "Please rate before submitting.", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(RateActivity.this, "Thanks For Rating: " + rating, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RateActivity.this, MainActivity2.class));
                finish();
            }
        });

        submitFeedbackBtn.setOnClickListener(view -> {
            String feedback = feedbackEditText.getText().toString();
            if (feedback.isEmpty()) {
                Toast.makeText(RateActivity.this, "Please provide feedback before submitting.", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(RateActivity.this, "Thanks For Your Feedback!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RateActivity.this, MainActivity2.class));
                finish();
            }
        });
    }
}

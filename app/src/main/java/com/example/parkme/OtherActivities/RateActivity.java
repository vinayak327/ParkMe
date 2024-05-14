package com.example.parkme.OtherActivities;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parkme.R;

public class RateActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        ratingBar = findViewById(R.id.ratingBar);
        submitBtn = findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(view -> {
            float rating = ratingBar.getRating();
            // You can handle the rating here, for example, send it to a database
            // For simplicity, let's just display a toast message with the rating
            Toast.makeText(RateActivity.this, "Rating: " + rating, Toast.LENGTH_SHORT).show();
        });
    }
}

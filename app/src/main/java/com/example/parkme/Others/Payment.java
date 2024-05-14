package com.example.parkme.Others;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.parkme.R;

public class Payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Launch Google Pay
    // Launch Google Pay
    public void launchGooglePay(View view) {
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.nbu.paisa.user");
            if (intent != null) {
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("upi://pay"));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Google Pay app not installed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error launching Google Pay", Toast.LENGTH_SHORT).show();
        }
    }

    public void launchPhonePe(View view) {
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.phonepe.app");
            if (intent != null) {
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("upi://pay"));
                startActivity(intent);
            } else {
                Toast.makeText(this, "PhonePe app not installed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error launching PhonePe", Toast.LENGTH_SHORT).show();
        }
    }

}

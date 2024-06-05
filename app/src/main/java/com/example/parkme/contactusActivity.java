package com.example.parkme;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class contactusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus); // Replace "your_xml_layout_name" with the actual name of your XML layout file

        // Initialize TextViews
        TextView developerName1TextView = findViewById(R.id.developer_name1);
        TextView email1TextView = findViewById(R.id.email1);
        TextView mobile1TextView = findViewById(R.id.mobile1);
        TextView developerName2TextView = findViewById(R.id.developer_name2);
        TextView email2TextView = findViewById(R.id.email2);
        TextView mobile2TextView = findViewById(R.id.mobile2);

        // Set text for TextViews
        developerName1TextView.setText("vinayak choudhary");
        email1TextView.setText("Email: choudharyvinayak325@gmail.com");
        mobile1TextView.setText("Mobile: +91 9309749862");
        developerName2TextView.setText("Eshwari Kulkarni");
        email2TextView.setText("Email: s20_kulkarni_eshwari@mgmcen.ac.in");
        mobile2TextView.setText("Mobile: +91 9112255896");
    }
}

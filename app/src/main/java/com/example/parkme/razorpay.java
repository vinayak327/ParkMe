package com.example.parkme;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class razorpay extends AppCompatActivity implements PaymentResultListener {

    Button pay_btn ;
    TextView pay_status;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_razorpay);

        Checkout.preload(getApplicationContext());

        pay_btn=findViewById(R.id.pay_btn);
        pay_status=findViewById(R.id.pay_status);


        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentNow("100");
            }
        });

    }
    private void PaymentNow(String amount){
        final Activity activity =this;

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_YhIthmJRLJtQqm");
        checkout.setImage(R.drawable.ic_launcher_background);

        double finalAmount = Float.parseFloat(amount)*100;

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Parkme");
            options.put("description", "Reference No. #123456");
            options.put("image", "https:");
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", finalAmount+"");
            options.put("prefill.email", "choudharyvinayak325@gmail.com");
            options.put("prefill.contact", "9309749862");

            checkout.open(activity, options);


        }catch (Exception e){
            Log.e("TAG", "Error in starting razorpay checkout", e);
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(),"Payment success!", Toast.LENGTH_SHORT).show();
        pay_status.setText(s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),"Payment failed!", Toast.LENGTH_SHORT).show();
        pay_status.setText(s);

    }
}
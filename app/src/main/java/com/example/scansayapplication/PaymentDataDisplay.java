package com.example.scansayapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PaymentDataDisplay extends AppCompatActivity {
    TextView appNameText , senderNameText , paymentAmountText , paymentTimeText;
    String appNameStr , senderNameStr , paymentAmountStr , paymentTimeStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_data_display);

        Intent intent = getIntent();
        appNameStr = intent.getStringExtra("appname");
        senderNameStr = intent.getStringExtra("sendername");
        paymentAmountStr = intent.getStringExtra("paymentamount");
        paymentTimeStr = intent.getStringExtra("paymenttime");

        appNameText = (TextView) findViewById(R.id.appNameData);
        senderNameText = (TextView) findViewById(R.id.senderNameData);
        paymentAmountText = (TextView) findViewById(R.id.paymentAmountData);
        paymentTimeText = (TextView) findViewById(R.id.paymentTimeData);

        appNameText.setText("App Name = " + appNameStr);
        senderNameText.setText("Sender Name = " + senderNameStr);
        paymentAmountText.setText("Payment Amount = " + paymentAmountStr);
        paymentTimeText.setText("Payment Time = " + paymentTimeStr);
    }
}
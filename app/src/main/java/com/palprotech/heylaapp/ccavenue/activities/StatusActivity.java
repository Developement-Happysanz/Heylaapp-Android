package com.palprotech.heylaapp.ccavenue.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.ccavenue.utilities.AvenuesParams;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class StatusActivity extends Activity {

    private TextView tv4, OrderNum, PaymentId, TransactionDate, PaymentAmount, PaymentStatus;
    private Button PaymentDone;
    private ImageView Success, Failure, Cancel;
    private RelativeLayout payment, statusBG;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_status_ns);

//		Toast.makeText(getApplicationContext(), "status", Toast.LENGTH_SHORT).show();
        Intent mainIntent = getIntent();
        tv4 = (TextView) findViewById(R.id.textView1);
        OrderNum = (TextView) findViewById(R.id.txt_ordernum);
        PaymentId = (TextView) findViewById(R.id.txt_payid);
        TransactionDate = (TextView) findViewById(R.id.txt_transdate);
        PaymentAmount = (TextView) findViewById(R.id.txt_payamt);
        PaymentStatus = (TextView) findViewById(R.id.txt_paystatus);
        Success = (ImageView) findViewById(R.id.img_success);
        Failure = (ImageView) findViewById(R.id.img_fail);
        Cancel = findViewById(R.id.img_cancel);
//        OrderNum.setText(AvenuesParams.MERCHANT_ID);
        PaymentId.setText(AvenuesParams.ORDER_ID);
        TransactionDate.setText(AvenuesParams.ORDER_ID);
        PaymentAmount.setText(AvenuesParams.AMOUNT);
        PaymentDone = (Button) findViewById(R.id.pay_done);

        statusBG = findViewById(R.id.status_img_layout);
        findViewById(R.id.back_res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String getOrderId = PreferenceStorage.getOrderId(getApplicationContext());
        String getPaymentAmount = PreferenceStorage.getPaymentAmount(getApplicationContext());
        String getTransactionDate = PreferenceStorage.getTransactionDate(getApplicationContext());
        String showTransactionDate = "";

        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            Date date = (Date) formatter.parse(getTransactionDate);
            SimpleDateFormat year_date = new SimpleDateFormat("yyyy");
            String year = year_date.format(date.getTime());
            SimpleDateFormat month_date = new SimpleDateFormat("MMM");
            String month_name = month_date.format(date.getTime());
            SimpleDateFormat event_date = new SimpleDateFormat("dd");
            String date_name = event_date.format(date.getTime());
            if ((getTransactionDate != null)) {
                showTransactionDate = date_name + "-" + month_name + "-" + year;
            } else {
                showTransactionDate = "N/A";
            }
        } catch (final ParseException e) {
            e.printStackTrace();
        }

        tv4.setText(mainIntent.getStringExtra("transStatus"));
        String abc = mainIntent.getStringExtra("transStatus");

        switch (abc) {
            case "Transaction Declined!":
                tv4.setTextColor(getResources().getColor(R.color.failure_txt));
                Failure.setVisibility(View.VISIBLE);
                Success.setVisibility(View.INVISIBLE);
                Cancel.setVisibility(View.INVISIBLE);
                PaymentStatus.setText("Failed");
                OrderNum.setText(getOrderId);
                PaymentAmount.setText(getPaymentAmount);
                TransactionDate.setText(getTransactionDate);
                PaymentDone.setText("Try Again");
                PaymentDone.setBackgroundColor(getResources().getColor(R.color.failure_txt));
//                statusBG.setBackgroundColor(getResources().getColor(R.color.failure_new_bg));
//                payment.setBackground(getResources().getDrawable(R.drawable.payment_status_failure));
                break;
            case "Transaction Successful!":
                tv4.setTextColor(getResources().getColor(R.color.success_txt));
                Success.setVisibility(View.VISIBLE);
                Failure.setVisibility(View.INVISIBLE);
                Cancel.setVisibility(View.INVISIBLE);
                PaymentStatus.setText("Success");
                OrderNum.setText(getOrderId);
                PaymentAmount.setText(getPaymentAmount);
                TransactionDate.setText(getTransactionDate);
                PaymentDone.setText("Done");
                PaymentDone.setBackgroundColor(getResources().getColor(R.color.success_txt));
//                statusBG.setBackgroundColor(getResources().getColor(R.color.success_new_bg));
//                payment.setBackground(getResources().getDrawable(R.drawable.payment_status_success));
                break;
            case "Transaction Cancelled!":
                tv4.setTextColor(getResources().getColor(R.color.cancel_txt));
                Cancel.setVisibility(View.VISIBLE);
                Success.setVisibility(View.INVISIBLE);
                Failure.setVisibility(View.INVISIBLE);
                PaymentStatus.setText("Canceled");
                OrderNum.setText(getOrderId);
                PaymentAmount.setText(getPaymentAmount);
                TransactionDate.setText(getTransactionDate);
                PaymentDone.setText("Ok");
                PaymentDone.setBackgroundColor(getResources().getColor(R.color.cancel_txt));
//                statusBG.setBackgroundColor(getResources().getColor(R.color.cancel_new_bg));
//                payment.setBackground(getResources().getDrawable(R.drawable.payment_status_cancel));
                break;
            default:
                break;
        }

        PaymentDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }
} 
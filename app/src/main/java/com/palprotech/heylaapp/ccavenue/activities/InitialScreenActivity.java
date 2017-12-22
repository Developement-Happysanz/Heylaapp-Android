package com.palprotech.heylaapp.ccavenue.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.support.BookPlan;
import com.palprotech.heylaapp.bean.support.Event;
import com.palprotech.heylaapp.ccavenue.utilities.AvenuesParams;
import com.palprotech.heylaapp.ccavenue.utilities.ServiceUtility;
import com.palprotech.heylaapp.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;


public class InitialScreenActivity extends Activity {

    private EditText accessCode, merchantId, currency, amount, orderId, rsaKeyUrl, redirectUrl, cancelUrl;
    private Event event;
    String eventNoOfTicket;
    Double tickets = 1.00;

    private void init() {
        accessCode = findViewById(R.id.accessCode);
        merchantId = findViewById(R.id.merchantId);
        orderId = findViewById(R.id.orderId);
        currency = findViewById(R.id.currency);
        amount = findViewById(R.id.amount);
        rsaKeyUrl = findViewById(R.id.rsaUrl);
        redirectUrl = findViewById(R.id.redirectUrl);
        cancelUrl = findViewById(R.id.cancelUrl);
        event = (Event) getIntent().getSerializableExtra("eventObj");

        ImageView imEventBanner = findViewById(R.id.img_logo);
        String url = event.getEventBanner();
        if (((url != null) && !(url.isEmpty()))) {
            Picasso.with(this).load(url).placeholder(R.drawable.event_img).error(R.drawable.event_img).into(imEventBanner);
        }
        TextView txtEventName = findViewById(R.id.txt_event_name);
        txtEventName.setText(event.getEventName());
        TextView txtEventTime = findViewById(R.id.txt_event_time);
        txtEventTime.setText(event.getStartTime());
        TextView txtEventPlace = findViewById(R.id.txt_event_location);
        txtEventPlace.setText(event.getEventVenue());
        TextView txtTotalTickets = findViewById(R.id.txtTotalTickets);
        txtTotalTickets.setText(PreferenceStorage.getTotalNoOfTickets(getApplicationContext()) + " Tickets");
        TextView txtTicketPrice = findViewById(R.id.txtTicketPrice);
        txtTicketPrice.setText("Rs."+PreferenceStorage.getPaymentAmount(getApplicationContext()));
        TextView txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtTotalPrice.setText("Rs."+PreferenceStorage.getPaymentAmount(getApplicationContext()));

        String orderIdValue = PreferenceStorage.getOrderId(getApplicationContext());
        amount.setText(PreferenceStorage.getPaymentAmount(getApplicationContext()));
        orderId.setText(orderIdValue);

//        callPayment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_screen_ns);
        init();
    }

    public void onClick(View view) {
        //Mandatory parameters. Other parameters can be added if required.
        String vAccessCode = ServiceUtility.chkNull(accessCode.getText()).toString().trim();
        String vMerchantId = ServiceUtility.chkNull(merchantId.getText()).toString().trim();
        String vCurrency = ServiceUtility.chkNull(currency.getText()).toString().trim();
        String vAmount = ServiceUtility.chkNull(amount.getText()).toString().trim();
        String vRedirectUrl = ServiceUtility.chkNull(redirectUrl.getText()).toString().trim();
        String vCancelUrl = ServiceUtility.chkNull(cancelUrl.getText()).toString().trim();
        String vrsaKeyUrl = ServiceUtility.chkNull(rsaKeyUrl.getText()).toString().trim();
        if (!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(accessCode.getText()).toString().trim());
            intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(merchantId.getText()).toString().trim());
            intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId.getText()).toString().trim());
            intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(currency.getText()).toString().trim());
            intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(amount.getText()).toString().trim());
//            intent.putExtra(AvenuesParams.MERCH_PARAM1, PreferenceStorage.getUserId(getApplicationContext()));
            intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(redirectUrl.getText()).toString().trim());
            intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancelUrl.getText()).toString().trim());
            intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(rsaKeyUrl.getText()).toString().trim());

            startActivity(intent);
            finish();
        } else {
            showToast("All parameters are mandatory.");
        }
    }

    private void callPayment() {

        //Mandatory parameters. Other parameters can be added if required.
        String vAccessCode = ServiceUtility.chkNull(accessCode.getText()).toString().trim();
        String vMerchantId = ServiceUtility.chkNull(merchantId.getText()).toString().trim();
        String vCurrency = ServiceUtility.chkNull(currency.getText()).toString().trim();
        String vAmount = ServiceUtility.chkNull(amount.getText()).toString().trim();
        String vRedirectUrl = ServiceUtility.chkNull(redirectUrl.getText()).toString().trim();
        String vCancelUrl = ServiceUtility.chkNull(cancelUrl.getText()).toString().trim();
        String vrsaKeyUrl = ServiceUtility.chkNull(rsaKeyUrl.getText()).toString().trim();
        if (!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(accessCode.getText()).toString().trim());
            intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(merchantId.getText()).toString().trim());
            intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId.getText()).toString().trim());
            intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(currency.getText()).toString().trim());
            intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(amount.getText()).toString().trim());
//            intent.putExtra(AvenuesParams.MERCH_PARAM1, PreferenceStorage.getUserId(getApplicationContext()));
            intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(redirectUrl.getText()).toString().trim());
            intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancelUrl.getText()).toString().trim());
            intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(rsaKeyUrl.getText()).toString().trim());

            startActivity(intent);
            finish();
        } else {
            showToast("All parameters are mandatory.");
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }
} 
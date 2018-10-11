package com.palprotech.heylaapp.paytm;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.support.Event;
import com.palprotech.heylaapp.ccavenue.activities.StatusActivity;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;

public class MainActivityPost extends Activity implements IServiceListener, DialogClickListener {

    String orderId;
    private ServiceHelper serviceHelper;
    String status = null;//Test
    TextView order_id_txt;
    private Event event;
    EditText order_res;
    EditText edt_email, edt_mobile;
    TextView edt_amount, txtTickets, timerTxt;
    private ImageView imEventBanner;
    protected ProgressDialogHelper progressDialogHelper;
    boolean checkRes = false;
    String url = "https://heylaapp.com/paytm_app/generateChecksum.php";
    Map paramMap = new HashMap();
    String mid = "", order_id = "", cust_id = "CUST12345678", callback = "CALLBACK_URL",
            industry_type = "", txn_amount = "", checksum = "CHECKSUM", mobile = "MOBILE_NO", email = "EMAIL", channel_id = "";
    String website = "";
    Boolean go = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_paytm);
        initOrderId();
        event = (Event) getIntent().getSerializableExtra("eventObj");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        edt_amount = (TextView) findViewById(R.id.edt_amount);
        txtTickets = (TextView) findViewById(R.id.edt_tickets);
        timerTxt = (TextView) findViewById(R.id.timer_text);

        imEventBanner = findViewById(R.id.img_logo);
        String url = event.getEventBanner();
        /*if (((url != null) && !(url.isEmpty()))) {
            Picasso.with(this).load(url).placeholder(R.drawable.event_img).error(R.drawable.event_img).into(imEventBanner);
        }*/

        final ImageView img = new ImageView(this);
        Picasso.with(img.getContext())
                .load(url)
                .into(img, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        imEventBanner.setBackgroundDrawable(img.getDrawable());
                    }

                    @Override
                    public void onError() {
                    }
                });

        TextView txtEventName = findViewById(R.id.txt_event_name);
        txtEventName.setText(event.getEventName());
        TextView txtEventTime = findViewById(R.id.txt_event_time);
        txtEventTime.setText(PreferenceStorage.getBookingDate(getApplicationContext()) + "  " + PreferenceStorage.getBookingTime(getApplicationContext()));
        TextView txtEventPlace = findViewById(R.id.txt_event_location);
        txtEventPlace.setText(event.getEventVenue());
        TextView txtTotalTickets = findViewById(R.id.txtTotalTickets);
        txtTotalTickets.setText(PreferenceStorage.getTotalNoOfTickets(getApplicationContext()) + " Tickets");
        TextView txtTicketPrice = findViewById(R.id.txtTicketPrice);
        txtTicketPrice.setText("Rs." + PreferenceStorage.getPaymentAmount(getApplicationContext()));
        TextView txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtTotalPrice.setText("Rs." + PreferenceStorage.getPaymentAmount(getApplicationContext()));

        String orderIdValue = PreferenceStorage.getOrderId(getApplicationContext());
//        amount.setText(PreferenceStorage.getPaymentAmount(getApplicationContext()));
//        orderId.setText(orderIdValue);

        startTimer(60000);

    }

    private void startTimer(int noOfMinutes) {
        CountDownTimer  countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                timerTxt.setText(hms);//set text
            }
            public void onFinish() {
//                timerTxt.setText("TIME'S UP!!"); //On finish change timer text
                if (!go){
                    endAll();
                }
            }
        }.start();

    }

    private void endAll() {
        AlertDialogHelper.showSimpleAlertDialog(this, "Timeout");
        Toast.makeText(this, "Timeout", Toast.LENGTH_SHORT).show();
        finish();
    }

    // This is to refresh the order id: Only for the Sample App’s purpose.
    @Override
    protected void onStart() {
        super.onStart();
        initOrderId();
        edt_amount.setText(PreferenceStorage.getPaymentAmount(this));
        txtTickets.setText(PreferenceStorage.getTotalNoOfTickets(this) + " Tickets");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);
        order_id_txt = (TextView) findViewById(R.id.order_id);
        order_id_txt.setText(orderId);

    }

    public void onStartTransaction(View view) throws InterruptedException, ExecutionException {

//        if(edt_email.getText().toString().equalsIgnoreCase("")){
//            Toast.makeText(getApplicationContext(),"Please Enter Email-id",Toast.LENGTH_LONG).show();
//        }else if(edt_mobile.getText().toString().equalsIgnoreCase("")){
//            Toast.makeText(getApplicationContext(),"Please Enter Mobile Number",Toast.LENGTH_LONG).show();
//        }else if(edt_amount.getText().toString().equalsIgnoreCase("")){
//            Toast.makeText(getApplicationContext(),"Please Enter Amount",Toast.LENGTH_LONG).show();
//        }
//        else {

//        PaytmPGService Service = PaytmPGService.getStagingService();
        go = true;
        PaytmPGService Service = PaytmPGService.getProductionService();

        Log.d("before request", "some");
        String edtemail = edt_email.getText().toString().trim();
        String edtmobile = edt_mobile.getText().toString().trim();
        String edtamount = edt_amount.getText().toString().trim();
        txn_amount = edt_amount.getText().toString().trim();

        JSONObject postData = new JSONObject();

        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("ORDER_ID", PreferenceStorage.getOrderId(this));
        stringHashMap.put("email", edtemail);
        stringHashMap.put("mobile", edtmobile);
        stringHashMap.put("TXN_AMOUNT", edtamount);
        stringHashMap.put("CUST_ID", PreferenceStorage.getUserId(this));

        SendDeviceDetails sendDeviceDetails = null;
        try {
            sendDeviceDetails = new SendDeviceDetails(url, getPostDataString(stringHashMap), Service);
            sendDeviceDetails.execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        }
    }

    private boolean validateSignInResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(HeylaAppConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (((status.equalsIgnoreCase("activationError")) || (status.equalsIgnoreCase("alreadyRegistered")) ||
                            (status.equalsIgnoreCase("notRegistered")) || (status.equalsIgnoreCase("error")))) {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);

                    } else {
                        signInSuccess = true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {

        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }


    private class SendDeviceDetails extends AsyncTask<String, Void, String> {

        String url, data;
        PaytmPGService Service;

        public SendDeviceDetails(String url, String data, PaytmPGService Service) {
            this.url = url;
            this.data = data;
            this.Service = Service;
        }

        @Override
        protected String doInBackground(String... params) {

            String data1 = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes("PostData=" + params[1]);
                wr.writeBytes(data);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data1 += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data1;
        }

        @Override
        protected void onPostExecute(String result) {

            if (checkRes) {
                String msg = "";
                JSONObject mJsonObject = null;
                try {
                    mJsonObject = new JSONObject(result);
                    msg = mJsonObject.getString("message");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                if (msg.equalsIgnoreCase("Success")){
                    Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
                    intent.putExtra("transStatus", status);
                    intent.putExtra("eventObj", event);
                    startActivity(intent);
                    finish();
                } else {
                    AlertDialogHelper.showSimpleAlertDialog(getApplicationContext(), msg);
                    finish();
                }

            } else {
                super.onPostExecute(result);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
                //String json = (String) myAsyncTask.execute(url).get();
                JSONObject mJsonObject = null;
                try {
                    mJsonObject = new JSONObject(result);
                    checksum = mJsonObject.getString("CHECKSUMHASH");
                    order_id = mJsonObject.getString("ORDER_ID");
                    cust_id = mJsonObject.getString("CUST_ID");
                    industry_type = mJsonObject.getString("INDUSTRY_TYPE_ID");
                    channel_id = mJsonObject.getString("CHANNEL_ID");
                    txn_amount = mJsonObject.getString("TXN_AMOUNT");
//                mobile = mJsonObject.getString("MOBILE_NO");
//                email = mJsonObject.getString("EMAIL");
                    website = mJsonObject.getString("WEBSITE");
                    mid = mJsonObject.getString("MID");
                    callback = mJsonObject.getString("CALLBACK_URL");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Log.d("after request", "some");
//            callback = ("https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + order_id);
                paramMap.put("MID", mid);
                paramMap.put("ORDER_ID", order_id);
                paramMap.put("CUST_ID", cust_id);
                paramMap.put("CHANNEL_ID", channel_id);
                paramMap.put("INDUSTRY_TYPE_ID", industry_type);
                paramMap.put("TXN_AMOUNT", txn_amount);
                paramMap.put("WEBSITE", website);
//            paramMap.put("EMAIL",email);s
//            paramMap.put("MOBILE_NO",mobile);
                paramMap.put("CHECKSUMHASH", checksum);
                paramMap.put("CALLBACK_URL", callback);

                PaytmOrder Order = new PaytmOrder(paramMap);

                Service.initialize(Order, null);

                Service.startPaymentTransaction(MainActivityPost.this, true, true,
                        new PaytmPaymentTransactionCallback() {

                            @Override
                            public void someUIErrorOccurred(String inErrorMessage) {
// Some UI Error Occurred in Payment Gateway Activity.
// // This may be due to initialization of views in
// Payment Gateway Activity or may be due to //
// initialization of webview. // Error Message details
// the error occurred.
//                            Toast.makeText(getApplicationContext(), "Payment Transaction response " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onTransactionResponse(Bundle inResponse) {
                                Log.d("LOG", "Payment Transaction :" + inResponse);
//                            Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                                order_res = (EditText) findViewById(R.id.order_res);
                                order_res.setText(inResponse.toString());

                                String response = inResponse.getString("STATUS");
                                String checksum = inResponse.getString("CHECKSUMHASH");
                                String orderid = inResponse.getString("ORDERID");
                                String txnamt = inResponse.getString("TXNAMOUNT");
                                String txndate = inResponse.getString("TXNDATE");
                                String mid = inResponse.getString("MID");
                                String txnid = inResponse.getString("TXNID");
                                String respcode = inResponse.getString("RESPCODE");
                                String payment = inResponse.getString("PAYMENTMODE");
                                String banktxnid = inResponse.getString("BANKTXNID");
                                String currency = inResponse.getString("CURRENCY");
                                String gatename = inResponse.getString("GATEWAYNAME");
                                String respmsg = inResponse.getString("RESPMSG");

                                if (response.equals("TXN_FAILURE")) {
                                    status = "Transaction Declined!";
                                } else if (response.equals("TXN_SUCCESS")) {
                                    status = "Transaction Successful!";
                                } else if (response.equals("TXN_PENDING")) {
                                    status = "Transaction Cancelled!";
                                } else {
                                    status = "Status Not Known!";
                                }
                                String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.BOOKING_DATA;

                                PaytmPGService Service = PaytmPGService.getProductionService();

                                JSONObject postData = new JSONObject();

                                HashMap<String, String> stringHashMap = new HashMap<>();


                                stringHashMap.put("STATUS", response);
                                stringHashMap.put("CHECKSUMHASH", checksum);
                                stringHashMap.put("ORDERID", orderid);
                                stringHashMap.put("TXNAMOUNT", txnamt);
                                stringHashMap.put("TXNDATE", txndate);
                                stringHashMap.put("MID", mid);
                                stringHashMap.put("TXNID", txnid);
                                stringHashMap.put("RESPCODE", respcode);
                                stringHashMap.put("PAYMENTMODE", payment);
                                stringHashMap.put("BANKTXNID", banktxnid);
                                stringHashMap.put("CURRENCY", currency);
                                stringHashMap.put("GATEWAYNAME", gatename);
                                stringHashMap.put("RESPMSG", respmsg);

                                SendDeviceDetails sendDeviceDetails = null;

                                try {
                                    checkRes = true;
                                    sendDeviceDetails = new SendDeviceDetails(url, getPostDataString(stringHashMap), Service);
                                    sendDeviceDetails.execute();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void networkNotAvailable() {
// If network is not
// available, then this
// method gets called.
                                Toast.makeText(getApplicationContext(), "Network", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void clientAuthenticationFailed(String inErrorMessage) {
// This method gets called if client authentication
// failed. // Failure may be due to following reasons //
// 1. Server error or downtime. // 2. Server unable to
// generate checksum or checksum response is not in
// proper format. // 3. Server failed to authenticate
// that client. That is value of payt_STATUS is 2. //
// Error Message describes the reason for failure.
                                Toast.makeText(getApplicationContext(), "clientAuthenticationFailed" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onErrorLoadingWebPage(int iniErrorCode,
                                                              String inErrorMessage, String inFailingUrl) {

                                Toast.makeText(getApplicationContext(), "onErrorLoadingWebPage" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

                            }

                            // had to be added: NOTE
                            @Override
                            public void onBackPressedCancelTransaction() {
                                Toast.makeText(getApplicationContext(), "onBackPressedCancelTransaction", Toast.LENGTH_LONG).show();

                                String status = null;
                                status = "Transaction Cancelled!";
//                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
                                intent.putExtra("transStatus", status);
                                startActivity(intent);
                                finish();
// TODO Auto-generated method stub
                            }

                            @Override
                            public void onTransactionCancel(String inErrorMessage,
                                                            Bundle inResponse) {
                                Log.d("LOG", "Payment Transaction Failed "
                                        + inErrorMessage);
//                            Toast.makeText(getBaseContext(),
//                                    "Payment Transaction Failed ",
//                                    Toast.LENGTH_LONG).show();

                                String response = inResponse.getString("STATUS");
                                String checksum = inResponse.getString("CHECKSUMHASH");
                                String orderid = inResponse.getString("ORDERID");
                                String txnamt = inResponse.getString("TXNAMOUNT");
                                String txndate = inResponse.getString("TXNDATE");
                                String mid = inResponse.getString("MID");
                                String txnid = inResponse.getString("TXNID");
                                String respcode = inResponse.getString("RESPCODE");
                                String payment = inResponse.getString("PAYMENTMODE");
                                String banktxnid = inResponse.getString("BANKTXNID");
                                String currency = inResponse.getString("CURRENCY");
                                String gatename = inResponse.getString("GATEWAYNAME");
                                String respmsg = inResponse.getString("RESPMSG");

                                JSONObject jsonObject = new JSONObject();
                                try {

                                    jsonObject.put("STATUS", response);
                                    jsonObject.put("CHECKSUMHASH", checksum);
                                    jsonObject.put("ORDERID", orderid);
                                    jsonObject.put("TXNAMOUNT", txnamt);
                                    jsonObject.put("TXNDATE", txndate);
                                    jsonObject.put("MID", mid);
                                    jsonObject.put("TXNID", txnid);
                                    jsonObject.put("RESPCODE", respcode);
                                    jsonObject.put("PAYMENTMODE", payment);
                                    jsonObject.put("BANKTXNID", banktxnid);
                                    jsonObject.put("CURRENCY", currency);
                                    jsonObject.put("GATEWAYNAME", gatename);
                                    jsonObject.put("RESPMSG", respmsg);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.BOOKING_DATA;
//                            String url = "https://heylaapp.com/paytm_app/TxnStatus.php";
                                serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

//                            String status = null;
//                            if (response.equals("TXN_FAILURE")) {
//                                status = "Transaction Declined!";
//                            } else if (response.equals("TXN_SUCCESS")) {
//                                status = "Transaction Successful!";
//                            } else if (response.equals("TXN_PENDING")) {
//                                status = "Transaction Cancelled!";
//                            } else {
//                                status = "Status Not Known!";
//                            }
////                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
//                            intent.putExtra("transStatus", status);
//                            startActivity(intent);
//                            finish();
                            }

                        });
            }
        }

    }


    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

}
package com.palprotech.heylaapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.CommonUtils;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.HeylaAppValidator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 24-10-2017.
 */

public class ChangeNumberActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = ChangeNumberActivity.class.getName();

    private TextInputLayout inputMobileNo;
    private EditText edtMobileNo;
    private Button btnConfirm;
    private ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    private String oldMobileNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_number);

        inputMobileNo = (TextInputLayout) findViewById(R.id.ti_mobile_number);
        edtMobileNo = (EditText) findViewById(R.id.edtMobileNumber);
        btnConfirm = (Button) findViewById(R.id.sendcode);
        btnConfirm.setOnClickListener(this);

        oldMobileNo = getIntent().getStringExtra("mobile_no");

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
    }

    @Override
    public void onClick(View v) {

        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {
            if (v == btnConfirm) {
                if(validateFields()){
                    String username = edtMobileNo.getText().toString();

                    JSONObject jsonObject = new JSONObject();
                    try {

                        jsonObject.put(HeylaAppConstants.PARMAS_OLD_MOBILE_NUMBER, oldMobileNo);
                        jsonObject.put(HeylaAppConstants.PARMAS_NEW_MOBILE_NUMBER, edtMobileNo.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                    String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.CHANGE_MOBILE_NUMBER;
                    serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
                }

            }

        } else {
            AlertDialogHelper.showSimpleAlertDialog(getApplicationContext(), "No Network connection available");
        }

    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

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

         /*   Intent homeIntent = new Intent(getApplicationContext(), ForgotPasswordNumberVerificationActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            homeIntent.putExtra("mobile_no", edtEmailOrMobileNo.getText().toString());
            startActivity(homeIntent);
            finish();*/

        }
    }

    private boolean validateFields(){
        if (!HeylaAppValidator.checkNullString(this.edtMobileNo.getText().toString().trim())) {
            inputMobileNo.setError(getString(R.string.err_mobile));
            return false;
        } else if (!HeylaAppValidator.checkMobileNumLength(this.edtMobileNo.getText().toString().trim())) {
            inputMobileNo.setError(getString(R.string.err_mobile));
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }
}

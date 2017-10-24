package com.palprotech.heylaapp.utils;

/**
 * Created by Admin on 23-09-2017.
 */

public class HeylaAppConstants {

    //    URLS
    //    BASE URL
    public static final String BASE_URL = "http://heylaapp.com/heyla/";

    //    SignUp
    public static final String SIGN_UP = "apimain/signup/";

    //    SignUp Params
    public static final String PARAMS_EMAIL_ID = "email_id";
    public static final String PARAMS_MOBILE_NUMBER = "mobile_no";
    public static final String PARAMS_PASSWORD = "password";
    public static final String PARAMS_GCM_KEY = "gcm_key";
    public static final String PARAMS_PLATFORM_TYPE = "platform_type";
    public static final String PARAMS_SIGNUP_TYPE = "signup_type";

    //    Mobile Number Verification
    public static final String MOBILE_NUMBER_VERIFY = "apimain/mobileverify/";

    //    Mobile Number Verification Params
    public static final String PARAMS_OTP = "OTP";
    public static final String PARAMS_REQUEST_MODE = "request_mode";

    //    SignIn
    public static final String SIGN_IN = "apimain/login";

    //    SignIn Params
    public static final String PARAMS_USERNAME = "username";
    public static final String PARAMS_LOGIN_TYPE = "login_type";

    //    Forgot Password
    public static final String FORGOT_PASSWORD = "apimain/forgotpassword/";

    //    Forgot Password OTP
    public static final String FORGOT_PASSWORD_OTP_REQUEST = "apimain/fgpasswordotp/";

    //    Resend OTP
    public static final String RESEND_OTP_REQUEST = "apimain/resendOTP/";

    //    Update Password
    public static final String UPDATE_PASSWORD = "apimain/resetpassword/";

    //    Update Password Params
    public static final String PARAMS_USER_ID = "user_id";

    //    SharedPref
    private static final String FCM_SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";

    //    Service Params
    public static String PARAM_MESSAGE = "msg";

    //     Shared preferences file name
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    //    Shared FCM ID
    public static final String KEY_FCM_ID = "fcm_id";

    //    Shared IMEI No
    public static final String KEY_IMEI = "imei_code";

    //    Shared Remember me
    public static final String IS_REMEMBER_ENABLED = "IsRemembered";

    //    Shared Username
    public static final String KEY_USERNAME = "username";

    //    Shared Password
    public static final String KEY_PASSWORD = "password";

    // Alert Dialog Constants
    public static String ALERT_DIALOG_TITLE = "alertDialogTitle";
    public static String ALERT_DIALOG_MESSAGE = "alertDialogMessage";
    public static String ALERT_DIALOG_TAG = "alertDialogTag";
    public static String ALERT_DIALOG_INPUT_HINT = "alert_dialog_input_hint";
    public static String ALERT_DIALOG_POS_BUTTON = "alert_dialog_pos_button";
    public static String ALERT_DIALOG_NEG_BUTTON = "alert_dialog_neg_button";
}

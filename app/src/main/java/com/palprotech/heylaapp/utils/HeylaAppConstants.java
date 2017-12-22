package com.palprotech.heylaapp.utils;

/**
 * Created by Admin on 23-09-2017.
 */

public class HeylaAppConstants {

    //    URLS
    //    BASE URL
    public static final String BASE_URL = "http://heylaapp.com/";

    //    SignUp
    public static final String SIGN_UP = "apimain/signup/";

    //    SignUp Params
    public static final String PARAMS_EMAIL_ID = "email_id";
    public static final String PARAMS_MOBILE_NUMBER = "mobile_no";
    public static final String PARAMS_PASSWORD = "password";
    public static final String PARAMS_GCM_KEY = "gcm_key";
//    public static final String PARAMS_PLATFORM_TYPE = "platform_type";
//    public static final String PARAMS_SIGNUP_TYPE = "signup_type";

    //    Mobile Number Verification
    public static final String MOBILE_NUMBER_VERIFY = "apimain/mobileverify/";

    //    Mobile Number Verification Params
    public static final String PARAMS_OTP = "OTP";
    public static final String PARAMS_REQUEST_MODE = "request_mode";

    //    SignIn
    public static final String SIGN_IN = "apimain/login";

    //    SignIn Params
    public static final String PARAMS_USERNAME = "username";
    public static final String PARAMS_NAME = "name";
    public static final String PARAMS_LOGIN_TYPE = "login_type";
    public static final String PARAMS_MOBILE_TYPE = "mobile_type";

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

    //    Change Mobile Number
    public static final String CHANGE_MOBILE_NUMBER = "apimain/updatemobile/";

    //    Change Mobile Number Params
    public static final String PARMAS_OLD_MOBILE_NUMBER = "old_mobile_no";
    public static final String PARMAS_NEW_MOBILE_NUMBER = "new_mobile_no";

    //    Facebook Gmail login
    public static final String FB_GPLUS_LOGIN = "apimain/fbgmlogin/";

    //    Guest Login
    public static final String GUEST_LOGIN = "apimain/guestlogin/";

    //    Guest Login Params
    public static final String PARAMS_UNIQUE_ID = "unique_id";

    //    Loginmode constants
    public static final int FACEBOOK = 1;
    public static final int NORMAL_SIGNUP = 2;
    public static final int GOOGLE_PLUS = 3;

    //    Profile Image Upload
    public static final String PROFILE_IMAGE = "apimain/profile_picupload/";

    //    Profile Info Update
    public static final String PROFILE_DATA = "apimain/profileupdate/";

    //    Profile Params
    public static final String PARAMS_FULL_NAME = "full_name";
    public static final String PARAMS_DATE_OF_BIRTH = "date_of_birth";
    public static final String PARAMS_GENDER = "gender";
    public static final String PARAMS_OCCUPATION = "occupation";
    public static final String PARAMS_ADDRESS_LINE_1 = "address_line_1";
    public static final String PARAMS_ADDRESS_LINE_2 = "address_line_2";
    public static final String PARAMS_ADDRESS_LINE_3 = "address_line_3";
    public static final String PARAMS_COUNTRY_ID = "country_id";
    public static final String PARAMS_COUNTRY_NAME = "country_name";
    public static final String PARAMS_STATE_ID = "state_id";
    public static final String PARAMS_STATE_NAME = "state_name";
    public static final String PARAMS_CITY_ID = "city_id";
    public static final String PARAMS_CITY_NAME = "city_name";
    public static final String PARAMS_ZIP_CODE = "zip_code";
    public static final String PARAMS_NEWS_LETTER = "news_letter";
    public static final String PARAMS_EMAIL_VERIFY = "email_verify";
    public static final String PARAMS_USER_ROLE = "user_role";
    public static final String PARAMS_USER_ROLE_NAME = "user_role_name";
    public static final String PARAMS_USER_REFERRAL_CODE = "user_referral_code";

    //    Country list
    public static final String COUNTRY_LIST = "apimain/selectcountry/";

    //    State list
    public static final String STATE_LIST = "apimain/selectstate/";

    //    City list
    public static final String CITY_LIST = "apimain/selectcity/";

    //    Event City list
    public static final String EVENT_CITY_LIST = "apimain/selectallcity/";

    //    Preferences list
    public static final String PREFERENCES_LIST = "apimain/userpreferrence/";

    //    Preferences list
    public static final String USER_PREFERENCES_LIST = "apimain/userpreferrence/";

    //    Preferences Update
    public static final String USER_PREFERENCES_UPDATE = "apimain/updatepreferrence/";

    public static final String GET_ADVANCE_SINGLE_SEARCH = "apimain/advancesearch/";

    //    Event list
    public static final String EVENT_LIST = "apimain/viewevents/";

    //    Event list params
    public static final String KEY_EVENT_TYPE = "event_type";

    //    Booking plan dates
    public static final String BOOKING_DATES = "apimain/bookingplandates/";

    //    Booking plan dates params
    public static final String KEY_EVENT_ID = "event_id";

    //    Booking plan timings
    public static final String BOOKING_SHOW_TIMES = "apimain/bookingplantimes/";

    //    Booking plan show timings params
    public static final String KEY_EVENT_SHOW_DATE = "show_date";

    //    Booking plans
    public static final String BOOKING_SHOW_PLANS = "apimain/bookingplans/";

    //    Booking plans params
    public static final String KEY_EVENT_SHOW_TIME = "show_time";

    //    Booking process
    public static final String BOOKING_PROCESS = "apimain/bookingprocess/";

    //    Booking process params
    public static final String KEY_ORDER_ID = "order_id";
    public static final String KEY_PLAN_ID = "plan_id";
    public static final String KEY_PLAN_TIME_ID = "plan_time_id";
    public static final String KEY_NO_OF_SEATS = "number_of_seats";
    public static final String KEY_TOTAL_AMOUNT = "total_amount";
    public static final String KEY_BOOKING_DATE = "booking_date";

    //    Attendees
    public static final String UPDATE_ATTENDEES = "/apimain/bookingattendees/";


    // Leaderboard
    public static final String LEADERBOARD_USER_ACTIVITY = "apimain/useractivity/";
    public static final String LEADERBOARD = "apimain/leaderboard/";


    //    SharedPref
    private static final String FCM_SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";
    public static final String KEY_LOGIN_MODE = "loginMode";
    public static final String KEY_MOBILE_NUMBER = "mobile_number";
    public static final String KEY_USER_TYPE = "user_type";

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

    //    Shared UserID
    public static final String KEY_USER_ID = "user_id";

    //    Shared Categories Id
    public static final String KEY_CATEGORIES_ID = "category_ids";

    //    Shared Password
    public static final String KEY_PASSWORD = "password";

    //    Shared Event City Id
    public static final String KEY_EVENT_CITY_ID = "event_city_id";

    //    Shared Event City Name
    public static final String KEY_EVENT_CITY_NAME = "event_city_name";

    //    Shared ProfileState
    public static final String KEY_PROFILE_STATE = "profile_state";

    public static final String KEY_USER_HAS_PREFERENCES = "hasPreferences";

    //Profile
    public static final String KEY_USER_EMAIL_ID = "email_id";
    public static final String KEY_USER_BIRTHDAY = "birthday";
    public static final String KEY_USER_OCCUPATION = "occupation";
    public static final String KEY_USER_GENDER = "gender";
    public static final String KEY_USER_IMAGE = "user_pic";

    // Alert Dialog Constants
    public static String ALERT_DIALOG_TITLE = "alertDialogTitle";
    public static String ALERT_DIALOG_MESSAGE = "alertDialogMessage";
    public static String ALERT_DIALOG_TAG = "alertDialogTag";
    public static String ALERT_DIALOG_INPUT_HINT = "alert_dialog_input_hint";
    public static String ALERT_DIALOG_POS_BUTTON = "alert_dialog_pos_button";
    public static String ALERT_DIALOG_NEG_BUTTON = "alert_dialog_neg_button";


    //Advanced Filter
    public static final String ISFILTERAPPLY = "ISFILTERAPPLY";
    public static final String SINGLEDATEFILTER = "SINGLEDATEFILTER";
    public static final String FROMDATE = "fromdate";
    public static final String TODATE = "todate";
    public static final String FILTERCITY = "filtercity";
    public static final String FILTEREVENTTYPE = "filtereventtype";
    public static final String FILTEREVENTTYPECATEGORY = "filtereventtypecategory";
    public static final String FILTERCAT = "filtercat";
    public static final String FILTERCITYINDEX = "filtercityindex";
    public static final String FILTEREVENTTYPEINDEX = "filtereventtypeindex";
    public static final String FILTEREVENTTYPECATEGORYINDEX = "filtereventtypecategoryindex";
}

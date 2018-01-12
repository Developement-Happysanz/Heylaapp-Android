package com.palprotech.heylaapp.utils;

/**
 * Created by Admin on 23-09-2017.
 */

public class HeylaAppConstants {

    //    URLS
    //    BASE URL
    public static final String BASE_URL = "http://heylaapp.com/";

    //    SignIn
    public static final String SIGN_IN = "apimain/login/";

    //    SignIn params
    public static final String PARAMS_USERNAME = "username";
    public static final String PARAMS_NAME = "name";
    public static final String PARAMS_LOGIN_TYPE = "login_type";
    public static final String PARAMS_MOBILE_TYPE = "mobile_type";

    //    Facebook gMail login
    public static final String FB_GPLUS_LOGIN = "apimain/socialLogin/";

    //    Login mode constants
    public static final int FACEBOOK = 1;
    public static final int NORMAL_SIGNUP = 2;
    public static final int GOOGLE_PLUS = 3;

    //    Guest login
    public static final String GUEST_LOGIN = "apimain/guestLogin/";

    //    Guest login params
    public static final String PARAMS_UNIQUE_ID = "unique_id";

    //    SignUp
    public static final String SIGN_UP = "apimain/signUp/";

    //    SignUp params
    public static final String PARAMS_EMAIL_ID = "email_id";
    public static final String PARAMS_MOBILE_NUMBER = "mobile_no";
    public static final String PARAMS_PASSWORD = "password";
    public static final String PARAMS_GCM_KEY = "gcm_key";

    //    Mobile number verification
    public static final String MOBILE_NUMBER_VERIFY = "apimain/mobileVerify/";

    //    Mobile number verification params
    public static final String PARAMS_OTP = "OTP";
    public static final String PARAMS_REQUEST_MODE = "request_mode";

    //    Resend OTP
    public static final String RESEND_OTP_REQUEST = "apimain/resendOTP/";

    //    Change mobile number
    public static final String CHANGE_MOBILE_NUMBER = "apimain/updateMobile/";

    //    Change mobile number params
    public static final String PARMAS_OLD_MOBILE_NUMBER = "old_mobile_no";
    public static final String PARMAS_NEW_MOBILE_NUMBER = "new_mobile_no";

    //    Forgot password
    public static final String FORGOT_PASSWORD = "apimain/forgotPassword/";

    //    Forgot password OTP
    public static final String FORGOT_PASSWORD_OTP_REQUEST = "apimain/forgotPasswordOTP/";

    //    Update password
    public static final String UPDATE_PASSWORD = "apimain/resetPassword/";

    //    Update password params
    public static final String PARAMS_USER_ID = "user_id";

    //    Profile image upload
    public static final String PROFILE_IMAGE = "apimain/profilePictureUpload/";

    //    Profile info update
    public static final String PROFILE_DATA = "apimain/profileUpdate/";

    //    Profile params
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
    public static final String COUNTRY_LIST = "apimain/selectCountry/";

    //    State list
    public static final String STATE_LIST = "apimain/selectState/";

    //    City list
    public static final String CITY_LIST = "apimain/selectCity/";

    //    Change email id
    public static final String CHANGE_EMAIL_ID = "/apimain/updateEmail/";

    //    Change email id params
    public static final String CHANGE_EMAIL_ID1 = "full_name";

    //    Change username
    public static final String CHANGE_USERNAME = "apimain/updateUsername/";

    //    Change username params
    public static final String CHANGE_USERNAME1 = "full_name";

    //    Event city list
    public static final String EVENT_CITY_LIST = "apimain/selectAllCity/";

    //    Preferences list
    public static final String USER_PREFERENCES_LIST = "apimain/userPreference/";

    //    Preferences update
    public static final String USER_PREFERENCES_UPDATE = "apimain/updatePreference/";

    //    User activity
    public static final String USER_ACTIVITY = "apimain/userActivity/";

    //    User activity Parms
    public static final String PARAMS_DATE = "date";
    public static final String KEY_RULE_ID = "rule_id";

    //    User activity
    public static final String SHARE_EVENT_URL = BASE_URL + "apimain/userActivity/";

    //    User activity params
    public static final String PARAMS_FULL_NAME1 = "full_name";

    //    Event list
    public static final String EVENT_LIST = "apimain/viewEvents/";

    //    Event list params
    public static final String KEY_EVENT_TYPE = "event_type";

    //    Event images list
    public static final String EVENT_IMAGES = "apimain/eventImages/";

    //    Event popularity
    public static final String EVENT_POPULARITY = "apimain/eventPopularity/";

    //    Advanced filter search
    public static final String GET_ADVANCE_SINGLE_SEARCH = "apimain/advanceSearch/";

    //Advanced filter search params
    public static final String SINGLEDATEFILTER = "single_date";
    public static final String FROMDATE = "from_date";
    public static final String TODATE = "to_date";
    public static final String FILTERCITYINDEX = "filter_city_index";
    public static final String FILTERCITY = "selected_city";
    public static final String FILTEREVENTTYPE = "event_type";
    public static final String FILTEREVENTCATEGORY = "event_category";
    public static final String FILTERPREF = "selected_preference";

    //    Event review list
    public static final String EVENT_REVIEW_LIST = "apimain/listEventReview/";

    //    Event review check
    public static final String EVENT_REVIEW_CHECK = "apimain/checkReview/";

    //    Event review add
    public static final String EVENT_REVIEW_ADD = "apimain/addReview/";

    //    Event review add params
    public static final String KEY_RATINGS = "rating";
    public static final String KEY_COMMENTS = "comments";

    //    Event review update
    public static final String EVENT_REVIEW_UPDATE = "apimain/updateReview/";

    //    Event review update params
    public static final String KEY_REVIEW_ID = "review_id";

    //    Booking plan dates
    public static final String BOOKING_DATES = "apimain/bookingPlanDates/";

    //    Booking plan dates params
    public static final String KEY_EVENT_ID = "event_id";

    //    Booking plan timings
    public static final String BOOKING_SHOW_TIMES = "apimain/bookingPlanTimes/";

    //    Booking plan show timings params
    public static final String KEY_EVENT_SHOW_DATE = "show_date";

    //    Booking plans
    public static final String BOOKING_SHOW_PLANS = "apimain/bookingPlans/";

    //    Booking plans params
    public static final String KEY_EVENT_SHOW_TIME = "show_time";

    //    Booking process
    public static final String BOOKING_PROCESS = "apimain/bookingProcess/";

    //    Booking process params
    public static final String KEY_ORDER_ID = "order_id";
    public static final String KEY_PLAN_ID = "plan_id";
    public static final String KEY_PLAN_TIME_ID = "plan_time_id";
    public static final String KEY_NO_OF_SEATS = "number_of_seats";
    public static final String KEY_TOTAL_AMOUNT = "total_amount";
    public static final String KEY_BOOKING_DATE = "booking_date";

    //    Attendees
    public static final String UPDATE_ATTENDEES = "apimain/bookingAttendees/";

    //    Booking history
    public static final String BOOKING_HISTORY = "apimain/bookingHistory/";

    //    Booking history attendees details
    public static final String BOOKING_DETAILS = "apimain/bookingAttendeesDetails/";

    // LeaderBoard
    public static final String LEADER_BOARD = "apimain/leaderBoard/";

    // LeaderBoard daily login
    public static final String ACTIVITY_HISTORY = "apimain/activityHistory/";

    // Wish list add
    public static final String WISH_LIST_ADD = "apimain/addWishList/";

    // Wish list params
    public static final String PARAMS_WISH_LIST_MASTER_ID = "wishlist_master_id";


    //    SharedPref
    public static final String KEY_LOGIN_MODE = "loginMode";
    public static final String KEY_MOBILE_NUMBER = "mobile_number";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_LAST_SHARED_TIME = "timeEventShared";
    public static final String KEY_EVENT_SHARED_COUNT = "eventSharedCount";
    public static final String KEY_LAST_LOGIN_TIME = "timeDailyLogin";

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
    public static String ALERT_DIALOG_POS_BUTTON = "alert_dialog_pos_button";
    public static String ALERT_DIALOG_NEG_BUTTON = "alert_dialog_neg_button";

    // Booking Status
    public static final String KEY_PAYMENT_AMOUNT = "eventRate";
    public static final String KEY_TRANSACTION_DATE = "eventDate";
    public static final String KEY_TOTAL_NO_OF_TICKETS = "totalNoOfTickets";

    public static final long TWENTY4HOURS = 24 * 60 * 60 * 1000;//24 hours in milli seconds format
}

package com.palprotech.heylaapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Admin on 11-10-2017.
 */

public class PreferenceStorage {

    /*To check welcome screen to launch*/
    public static void setFirstTimeLaunch(Context context, boolean isFirstTime) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(HeylaAppConstants.IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public static boolean isFirstTimeLaunch(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(HeylaAppConstants.IS_FIRST_TIME_LAUNCH, true);
    }
    /*End*/

    /*To save FCM key locally*/
    public static void saveGCM(Context context, String gcmId) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_FCM_ID, gcmId);
        editor.apply();
    }

    public static String getGCM(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(HeylaAppConstants.KEY_FCM_ID, "");
    }
    /*End*/

    /*To save mobile IMEI number */
    public static void saveIMEI(Context context, String imei) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_IMEI, imei);
        editor.apply();
    }

    public static String getIMEI(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(HeylaAppConstants.KEY_IMEI, "");
    }
    /*End*/

    /*To check remember me enabled or not*/
    public static void setRememberMe(Context context, boolean isRemembered) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(HeylaAppConstants.IS_REMEMBER_ENABLED, isRemembered);
        editor.apply();
    }

    public static boolean isRemembered(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(HeylaAppConstants.IS_REMEMBER_ENABLED, false);
    }
    /*End*/

    /*To store Profile First Time Update*/
    public static void saveCheckFirstTimeProfile(Context context, String userName) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_PROFILE_STATE, userName);
        editor.apply();
    }

    public static String getCheckFirstTimeProfile(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(HeylaAppConstants.KEY_PROFILE_STATE, "");
    }
    /*End*/

    /*To store login mode like normal,fb,gplus*/
    public static void saveLoginMode(Context context, int type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HeylaAppConstants.KEY_LOGIN_MODE, type);
        editor.apply();
    }

    public static int getLoginMode(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Integer mode;
        mode = sharedPreferences.getInt(HeylaAppConstants.KEY_LOGIN_MODE, 0);
        return mode;
    }
    /*End*/

    public static void saveUserType(Context context, String userType) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_USER_TYPE, userType);
        editor.apply();
    }

    public static String getUserType(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId = sharedPreferences.getString(HeylaAppConstants.KEY_USER_TYPE, "");
        return userId;
    }

    /**/
    /*User data*/
    /**/

    /*To store userId*/
    public static void saveUserId(Context context, String userName) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_USER_ID, userName);
        editor.apply();
    }

    public static String getUserId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(HeylaAppConstants.KEY_USER_ID, "");
    }
    /*End*/

    /*To store username*/
    public static void saveUsername(Context context, String userName) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_USERNAME, userName);
        editor.apply();
    }

    public static String getUsername(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String username;
        username = sharedPreferences.getString(HeylaAppConstants.KEY_USERNAME, "");
        return username;
    }
    /*End*/

    /*To store mobile number*/
    public static void saveMobileNo(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_MOBILE_NUMBER, type);
        editor.apply();
    }

    public static String getMobileNo(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mobileNo;
        mobileNo = sharedPreferences.getString(HeylaAppConstants.KEY_MOBILE_NUMBER, "");
        return mobileNo;
    }
    /*End*/

    /*To store email id*/
    public static void saveEmailId(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_USER_EMAIL_ID, type);
        editor.apply();
    }

    public static String getEmailId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String emailId;
        emailId = sharedPreferences.getString(HeylaAppConstants.KEY_USER_EMAIL_ID, "");
        return emailId;
    }
    /*End*/

    /*To store full name*/
    public static void saveFullName(Context context, String type) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.PARAMS_FULL_NAME, type);
        editor.apply();
    }

    public static String getFullName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String fullName;
        fullName = sharedPreferences.getString(HeylaAppConstants.PARAMS_FULL_NAME, "");
        return fullName;
    }
    /*End*/

    /*To store user birthday*/
    public static void saveUserBirthday(Context context, String data) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_USER_BIRTHDAY, data);
        editor.apply();
    }

    public static String getUserBirthday(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String birthDay;
        birthDay = sharedPreferences.getString(HeylaAppConstants.KEY_USER_BIRTHDAY, "");
        return birthDay;
    }
    /*End*/

    /*To store user gender*/
    public static void saveUserGender(Context context, String data) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_USER_GENDER, data);
        editor.apply();
    }

    public static String getUserGender(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String gender;
        gender = sharedPreferences.getString(HeylaAppConstants.KEY_USER_GENDER, "");
        return gender;
    }
    /*End*/

    /*To store occupation*/
    public static void saveUserOccupation(Context context, String data) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_USER_OCCUPATION, data);
        editor.apply();
    }

    public static String getUserOccupation(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String occupation;
        occupation = sharedPreferences.getString(HeylaAppConstants.KEY_USER_OCCUPATION, "");
        return occupation;
    }
    /*End*/

    /*To store address line 1*/
    public static void saveUserAddressLine1(Context context, String data) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.PARAMS_ADDRESS_LINE_1, data);
        editor.apply();
    }

    public static String getUserAddressLine1(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String address1;
        address1 = sharedPreferences.getString(HeylaAppConstants.PARAMS_ADDRESS_LINE_1, "");
        return address1;
    }
    /*End*/

    /*To store address line 2*/
    public static void saveUserAddressLine2(Context context, String data) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.PARAMS_ADDRESS_LINE_2, data);
        editor.apply();
    }

    public static String getUserAddressLine2(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String address2;
        address2 = sharedPreferences.getString(HeylaAppConstants.PARAMS_ADDRESS_LINE_2, "");
        return address2;
    }
    /*End*/

    /*To store address line 3*/
    public static void saveUserAddressLine3(Context context, String data) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.PARAMS_ADDRESS_LINE_3, data);
        editor.apply();
    }

    public static String getUserAddressLine3(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String address3;
        address3 = sharedPreferences.getString(HeylaAppConstants.PARAMS_ADDRESS_LINE_3, "");
        return address3;
    }
    /*End*/

    /*To store country id*/
    public static void saveUserCountryId(Context context, String data) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.PARAMS_COUNTRY_ID, data);
        editor.apply();
    }

    public static String getUserCountryId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String countryId;
        countryId = sharedPreferences.getString(HeylaAppConstants.PARAMS_COUNTRY_ID, "");
        return countryId;
    }
    /*End*/

    /*To store country name*/
    public static void saveUserCountryName(Context context, String data) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.PARAMS_COUNTRY_NAME, data);
        editor.apply();
    }

    public static String getUserCountryName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String countryId;
        countryId = sharedPreferences.getString(HeylaAppConstants.PARAMS_COUNTRY_NAME, "");
        return countryId;
    }
    /*End*/

    /*To store state id*/
    public static void saveUserStateId(Context context, String data) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.PARAMS_STATE_ID, data);
        editor.apply();
    }

    public static String getUserStateId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String stateId;
        stateId = sharedPreferences.getString(HeylaAppConstants.PARAMS_STATE_ID, "");
        return stateId;
    }
    /*End*/

    /*To store state name*/
    public static void saveUserStateName(Context context, String data) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.PARAMS_STATE_NAME, data);
        editor.apply();
    }

    public static String getUserStateName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String stateId;
        stateId = sharedPreferences.getString(HeylaAppConstants.PARAMS_STATE_NAME, "");
        return stateId;
    }
    /*End*/

    /*To store city id*/
    public static void saveUserCityId(Context context, String data) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.PARAMS_CITY_ID, data);
        editor.apply();
    }

    public static String getUserCityId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String cityId;
        cityId = sharedPreferences.getString(HeylaAppConstants.PARAMS_CITY_ID, "");
        return cityId;
    }
    /*End*/

    /*To store city name*/
    public static void saveUserCityName(Context context, String data) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.PARAMS_CITY_NAME, data);
        editor.apply();
    }

    public static String getUserCityName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String cityId;
        cityId = sharedPreferences.getString(HeylaAppConstants.PARAMS_CITY_NAME, "");
        return cityId;
    }
    /*End*/

    /*To store zip code*/
    public static void saveUserZipCode(Context context, String data) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.PARAMS_ZIP_CODE, data);
        editor.apply();
    }

    public static String getUserZipcode(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String zipCode;
        zipCode = sharedPreferences.getString(HeylaAppConstants.PARAMS_ZIP_CODE, "");
        return zipCode;
    }
    /*End*/

    /* User Image*/
    public static void saveUserPicture(Context context, String userPicture) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_USER_IMAGE, userPicture);
        editor.apply();
    }

    public static String getUserPicture(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userPicture;
        userPicture = sharedPreferences.getString(HeylaAppConstants.KEY_USER_IMAGE, "");
        return userPicture;
    }
    /*End*/

    /* User news letter status*/
    public static void saveUserNewsLetterStatus(Context context, String userPicture) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.PARAMS_NEWS_LETTER, userPicture);
        editor.apply();
    }

    public static String getUserNewsLetterStatus(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String newsLetter;
        newsLetter = sharedPreferences.getString(HeylaAppConstants.PARAMS_NEWS_LETTER, "");
        return newsLetter;
    }
    /*End*/

    /* User email verify status*/
    public static void saveUserEmailVerifyStatus(Context context, String userPicture) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.PARAMS_EMAIL_VERIFY, userPicture);
        editor.apply();
    }

    public static String getUserEmailVerifyStatus(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String emailVerify;
        emailVerify = sharedPreferences.getString(HeylaAppConstants.PARAMS_EMAIL_VERIFY, "");
        return emailVerify;
    }
    /*End*/

    /* User user role*/
    public static void saveUserRole(Context context, String userPicture) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.PARAMS_USER_ROLE, userPicture);
        editor.apply();
    }

    public static String getUserRole(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userRole;
        userRole = sharedPreferences.getString(HeylaAppConstants.PARAMS_USER_ROLE, "");
        return userRole;
    }
    /*End*/

    /* User role name*/
    public static void saveUserRoleName(Context context, String userPicture) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.PARAMS_USER_ROLE_NAME, userPicture);
        editor.apply();
    }

    public static String getUserRoleName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userRoleName;
        userRoleName = sharedPreferences.getString(HeylaAppConstants.PARAMS_USER_ROLE_NAME, "");
        return userRoleName;
    }
    /*End*/

    /* User referral code*/
    public static void saveUserReferralCode(Context context, String userPicture) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.PARAMS_USER_REFERRAL_CODE, userPicture);
        editor.apply();
    }

    public static String getUserReferralCode(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userReferralCode;
        userReferralCode = sharedPreferences.getString(HeylaAppConstants.PARAMS_USER_REFERRAL_CODE, "");
        return userReferralCode;
    }
    /*End*/

    /*To store password*/
    public static void savePassword(Context context, String password) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_PASSWORD, password);
        editor.apply();
    }

    public static String getPassword(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(HeylaAppConstants.KEY_PASSWORD, "");
    }
    /*End*/

    /*To store event city id*/
    public static void saveEventCityId(Context context, String password) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_EVENT_CITY_ID, password);
        editor.apply();
    }

    public static String getEventCityId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(HeylaAppConstants.KEY_EVENT_CITY_ID, "");
    }
    /*End*/

    /*To store event city name*/
    public static void saveEventCityName(Context context, String password) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_EVENT_CITY_NAME, password);
        editor.apply();
    }

    public static String getEventCityName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(HeylaAppConstants.KEY_EVENT_CITY_NAME, "");
    }
    /*End*/

    /*Preferences Storeage*/
    public static void savePreferencesSelected(Context context, boolean selected) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(HeylaAppConstants.KEY_USER_HAS_PREFERENCES, selected);
        editor.apply();
    }

    public static boolean isPreferencesPresent(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        boolean logged = sharedPreferences.getBoolean(HeylaAppConstants.KEY_USER_HAS_PREFERENCES, false);
        return logged;
    }
    /*End*/

    public static void saveFilterSingleDate(Context context, String singledate) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.SINGLEDATEFILTER, singledate);
        editor.commit();
    }

    public static String getFilterSingleDate(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId = sharedPreferences.getString(HeylaAppConstants.SINGLEDATEFILTER, "");
        return userId;
    }

    public static void IsFilterApply(Context context, boolean IsFilterApply) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(HeylaAppConstants.ISFILTERAPPLY, IsFilterApply);
        editor.commit();

    }

    public static boolean getFilterApply(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        boolean logged = sharedPreferences.getBoolean(HeylaAppConstants.ISFILTERAPPLY, false);
        return logged;

    }

    public static void saveFilterFromDate(Context context, String singledate) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.FROMDATE, singledate);
        editor.commit();
    }

    public static String getFilterFromDate(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId = sharedPreferences.getString(HeylaAppConstants.FROMDATE, "");
        return userId;
    }

    public static void saveFilterToDate(Context context, String singledate) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.TODATE, singledate);
        editor.commit();
    }

    public static String getFilterToDate(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId = sharedPreferences.getString(HeylaAppConstants.TODATE, "");
        return userId;
    }

    public static void saveFilterEventType(Context context, String singledate) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.FILTEREVENTTYPE, singledate);
        editor.commit();
    }

    public static String getFilterEventType(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId = sharedPreferences.getString(HeylaAppConstants.FILTEREVENTTYPE, "");
        return userId;
    }

    public static void saveFilterEventTypeCategory(Context context, String singledate) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.FILTEREVENTTYPECATEGORY, singledate);
        editor.commit();
    }

    public static String getFilterEventTypeCategory(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId = sharedPreferences.getString(HeylaAppConstants.FILTEREVENTTYPECATEGORY, "");
        return userId;
    }

    public static void saveFilterCity(Context context, String singledate) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.FILTERCITY, singledate);
        editor.commit();
    }

    public static String getFilterCity(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId = sharedPreferences.getString(HeylaAppConstants.FILTERCITY, "");
        return userId;
    }

    public static void saveFilterCitySelection(Context context, int index) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HeylaAppConstants.FILTERCITYINDEX, index);
        editor.commit();
    }

    public static void saveFilterEventTypeSelection(Context context, int index) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HeylaAppConstants.FILTEREVENTTYPEINDEX, index);
        editor.commit();
    }

    public static void saveFilterEventTypeCategorySelection(Context context, int index) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HeylaAppConstants.FILTEREVENTTYPECATEGORYINDEX, index);
        editor.commit();
    }

    public static int getFilterCityIndex(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        int userId = sharedPreferences.getInt(HeylaAppConstants.FILTERCITYINDEX, -1);
        return userId;
    }

    public static int getFilterEventTypeIndex(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        int userId = sharedPreferences.getInt(HeylaAppConstants.FILTEREVENTTYPEINDEX, -1);
        return userId;
    }

    public static int getFilterEventTypeCategoryIndex(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        int userId = sharedPreferences.getInt(HeylaAppConstants.FILTEREVENTTYPECATEGORYINDEX, -1);
        return userId;
    }

    public static void saveEventSharedtime(Context context, long millisecs) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(HeylaAppConstants.KEY_LAST_SHARED_TIME, millisecs);
        editor.commit();

    }

    public static long getEventSharedTime(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Long mode = sharedPreferences.getLong(HeylaAppConstants.KEY_LAST_SHARED_TIME, 0);
        return mode;

    }

    public static void saveEventSharedcount(Context context, int count) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HeylaAppConstants.KEY_EVENT_SHARED_COUNT, count);
        editor.commit();

    }

    public static int getEventSharedcount(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Integer mode = sharedPreferences.getInt(HeylaAppConstants.KEY_EVENT_SHARED_COUNT, 0);
        return mode;

    }

    public static void saveFilterCatgry(Context context, String singledate) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.FILTERCAT, singledate);
        editor.commit();
    }

    public static String getFilterCatgry(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId = sharedPreferences.getString(HeylaAppConstants.FILTERCAT, "");
        return userId;
    }

    // Booking Status check

    public static void saveOrderId(Context context, String orderID) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_ORDER_ID, orderID);
        editor.commit();
    }

    public static String getOrderId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String orderId = sharedPreferences.getString(HeylaAppConstants.KEY_ORDER_ID, "");
        return orderId;
    }

    public static void savePaymentAmount(Context context, String eventRate) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_PAYMENT_AMOUNT, eventRate);
        editor.commit();
    }

    public static String getPaymentAmount(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String paymentAmount = sharedPreferences.getString(HeylaAppConstants.KEY_PAYMENT_AMOUNT, "");
        return paymentAmount;
    }

    public static void saveTotalNoOfTickets(Context context, String totalNoOfTickets) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_TOTAL_NO_OF_TICKETS, totalNoOfTickets);
        editor.commit();
    }

    public static String getTotalNoOfTickets(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String totalNoOfTickets = sharedPreferences.getString(HeylaAppConstants.KEY_TOTAL_NO_OF_TICKETS, "");
        return totalNoOfTickets;
    }

    public static void saveTransactionDate(Context context, String eventDate) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_TRANSACTION_DATE, eventDate);
        editor.commit();
    }

    public static String getTransactionDate(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String transactionDate = sharedPreferences.getString(HeylaAppConstants.KEY_TRANSACTION_DATE, "");
        return transactionDate;
    }

}

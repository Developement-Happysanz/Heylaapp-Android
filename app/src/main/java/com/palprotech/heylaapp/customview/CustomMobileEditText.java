package com.palprotech.heylaapp.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.palprotech.heylaapp.R;

/**
 * Created by Narendar on 23/10/17.
 */

public class CustomMobileEditText extends LinearLayout {
    private EditText mMobileOneField, mMobileTwoField, mMobileThreeField, mMobileFourField,
            mMobileFiveField, mMobileSixField, mMobileSevenField, mMobileEightField, mMobileNineField,
            mMobileTenField, mCurrentlyFocusedEditText;

    public CustomMobileEditText(Context context) {
        super(context);
        init(null);
    }

    public CustomMobileEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomMobileEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styles = getContext().obtainStyledAttributes(attrs, R.styleable.OtpEditText);
        LayoutInflater mInflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.mobile_edittext, this);
        mMobileOneField = (EditText) findViewById(R.id.mobile_one_edit_text);
        mMobileTwoField = (EditText) findViewById(R.id.mobile_two_edit_text);
        mMobileThreeField = (EditText) findViewById(R.id.mobile_three_edit_text);
        mMobileFourField = (EditText) findViewById(R.id.mobile_four_edit_text);
        mMobileFiveField = (EditText) findViewById(R.id.mobile_five_edit_text);
        mMobileSixField = (EditText) findViewById(R.id.mobile_six_edit_text);
        mMobileSevenField = (EditText) findViewById(R.id.mobile_seven_edit_text);
        mMobileEightField = (EditText) findViewById(R.id.mobile_eight_edit_text);
        mMobileNineField = (EditText) findViewById(R.id.mobile_nine_edit_text);
        mMobileTenField = (EditText) findViewById(R.id.mobile_ten_edit_text);
        styleEditTexts(styles);
        styles.recycle();
    }

    /**
     * Get an instance of the present otp
     */
    private String makeMobile(){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(mMobileOneField.getText().toString());
        stringBuilder.append(mMobileTwoField.getText().toString());
        stringBuilder.append(mMobileThreeField.getText().toString());
        stringBuilder.append(mMobileFourField.getText().toString());
        stringBuilder.append(mMobileFiveField.getText().toString());
        stringBuilder.append(mMobileSixField.getText().toString());
        stringBuilder.append(mMobileSevenField.getText().toString());
        stringBuilder.append(mMobileEightField.getText().toString());
        stringBuilder.append(mMobileNineField.getText().toString());
        stringBuilder.append(mMobileTenField.getText().toString());
        return stringBuilder.toString();
    }

    /**
     * Checks if all four fields have been filled
     * @return length of OTP
     */
    public boolean hasValidmobile(){
        return makeMobile().length()==4;
    }

    /**
     * Returns the present otp entered by the user
     * @return OTP
     */
    public String getMobile(){
        return makeMobile();
    }

    /**
     * Used to set the OTP. More of cosmetic value than functional value
     * @param mobile Send the four digit otp
     */
    public void setMobile(String mobile){
        if(mobile.length()!=4){
            Log.e("OTPView","Invalid otp param");
            return;
        }
        if(mMobileOneField.getInputType()== InputType.TYPE_CLASS_NUMBER
                && !mobile.matches("[0-9]+")){
            Log.e("OTPView","OTP doesn't match INPUT TYPE");
            return;
        }

        mMobileOneField.setText(mobile.charAt(0));
        mMobileTwoField.setText(mobile.charAt(1));
        mMobileThreeField.setText(mobile.charAt(2));
        mMobileFourField.setText(mobile.charAt(3));
        mMobileFiveField.setText(mobile.charAt(4));
        mMobileSixField.setText(mobile.charAt(5));
        mMobileSevenField.setText(mobile.charAt(6));
        mMobileEightField.setText(mobile.charAt(7));
        mMobileNineField.setText(mobile.charAt(8));
        mMobileTenField.setText(mobile.charAt(9));
    }

    private void styleEditTexts(TypedArray styles) {
        int textColor = styles.getColor(R.styleable.OtpEditText_android_textColor, Color.BLACK);
        int backgroundColor =
                styles.getColor(R.styleable.OtpEditText_text_background_color, Color.TRANSPARENT);
        if (styles.getColor(R.styleable.OtpEditText_text_background_color, Color.TRANSPARENT)
                != Color.TRANSPARENT) {
            mMobileOneField.setBackgroundColor(backgroundColor);
            mMobileTwoField.setBackgroundColor(backgroundColor);
            mMobileThreeField.setBackgroundColor(backgroundColor);
            mMobileFourField.setBackgroundColor(backgroundColor);
            mMobileFiveField.setBackgroundColor(backgroundColor);
            mMobileSixField.setBackgroundColor(backgroundColor);
            mMobileSevenField.setBackgroundColor(backgroundColor);
            mMobileEightField.setBackgroundColor(backgroundColor);
            mMobileNineField.setBackgroundColor(backgroundColor);
            mMobileTenField.setBackgroundColor(backgroundColor);
        } else {
            mMobileOneField.getBackground().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
            mMobileTwoField.getBackground().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
            mMobileThreeField.getBackground().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
            mMobileFourField.getBackground().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
            mMobileFiveField.getBackground().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
            mMobileSixField.getBackground().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
            mMobileSevenField.getBackground().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
            mMobileEightField.getBackground().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
            mMobileNineField.getBackground().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
            mMobileTenField.getBackground().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
        }
        mMobileOneField.setTextColor(textColor);
        mMobileTwoField.setTextColor(textColor);
        mMobileThreeField.setTextColor(textColor);
        mMobileFourField.setTextColor(textColor);
        mMobileFiveField.setTextColor(textColor);
        mMobileSixField.setTextColor(textColor);
        mMobileSevenField.setTextColor(textColor);
        mMobileEightField.setTextColor(textColor);
        mMobileNineField.setTextColor(textColor);
        mMobileTenField.setTextColor(textColor);
        setEditTextInputStyle(styles);
    }

    private void setEditTextInputStyle(TypedArray styles) {
        int inputType =
                styles.getInt(R.styleable.OtpEditText_android_inputType, EditorInfo.TYPE_TEXT_VARIATION_NORMAL);
        mMobileOneField.setInputType(inputType);
        mMobileTwoField.setInputType(inputType);
        mMobileThreeField.setInputType(inputType);
        mMobileFourField.setInputType(inputType);
        mMobileFiveField.setInputType(inputType);
        mMobileSixField.setInputType(inputType);
        mMobileSevenField.setInputType(inputType);
        mMobileEightField.setInputType(inputType);
        mMobileNineField.setInputType(inputType);
        mMobileTenField.setInputType(inputType);
        String text = styles.getString(R.styleable.OtpEditText_otp);
        if (!TextUtils.isEmpty(text) && text.length() == 10) {
            mMobileOneField.setText(String.valueOf(text.charAt(0)));
            mMobileTwoField.setText(String.valueOf(text.charAt(1)));
            mMobileThreeField.setText(String.valueOf(text.charAt(2)));
            mMobileFourField.setText(String.valueOf(text.charAt(3)));
            mMobileFiveField.setText(String.valueOf(text.charAt(4)));
            mMobileSixField.setText(String.valueOf(text.charAt(5)));
            mMobileSevenField.setText(String.valueOf(text.charAt(6)));
            mMobileEightField.setText(String.valueOf(text.charAt(7)));
            mMobileNineField.setText(String.valueOf(text.charAt(8)));
            mMobileTenField.setText(String.valueOf(text.charAt(9)));
        }
        setFocusListener();
        setOnTextChangeListener();
    }

    private void setFocusListener() {
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override public void onFocusChange(View v, boolean hasFocus) {
                mCurrentlyFocusedEditText = (EditText) v;
                mCurrentlyFocusedEditText.setSelection(mCurrentlyFocusedEditText.getText().length());
            }
        };
        mMobileOneField.setOnFocusChangeListener(onFocusChangeListener);
        mMobileTwoField.setOnFocusChangeListener(onFocusChangeListener);
        mMobileThreeField.setOnFocusChangeListener(onFocusChangeListener);
        mMobileFourField.setOnFocusChangeListener(onFocusChangeListener);
        mMobileFiveField.setOnFocusChangeListener(onFocusChangeListener);
        mMobileSixField.setOnFocusChangeListener(onFocusChangeListener);
        mMobileSevenField.setOnFocusChangeListener(onFocusChangeListener);
        mMobileEightField.setOnFocusChangeListener(onFocusChangeListener);
        mMobileNineField.setOnFocusChangeListener(onFocusChangeListener);
        mMobileTenField.setOnFocusChangeListener(onFocusChangeListener);
    }

    public void disableKeypad() {
        OnTouchListener touchListener = new OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm =
                        (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        };
        mMobileOneField.setOnTouchListener(touchListener);
        mMobileTwoField.setOnTouchListener(touchListener);
        mMobileThreeField.setOnTouchListener(touchListener);
        mMobileFourField.setOnTouchListener(touchListener);
        mMobileFiveField.setOnTouchListener(touchListener);
        mMobileSixField.setOnTouchListener(touchListener);
        mMobileSevenField.setOnTouchListener(touchListener);
        mMobileEightField.setOnTouchListener(touchListener);
        mMobileNineField.setOnTouchListener(touchListener);
        mMobileTenField.setOnTouchListener(touchListener);
    }

    public void enableKeypad() {
        OnTouchListener touchListener = new OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        };
        mMobileOneField.setOnTouchListener(touchListener);
        mMobileTwoField.setOnTouchListener(touchListener);
        mMobileThreeField.setOnTouchListener(touchListener);
        mMobileFourField.setOnTouchListener(touchListener);
        mMobileFiveField.setOnTouchListener(touchListener);
        mMobileSixField.setOnTouchListener(touchListener);
        mMobileSevenField.setOnTouchListener(touchListener);
        mMobileEightField.setOnTouchListener(touchListener);
        mMobileNineField.setOnTouchListener(touchListener);
        mMobileTenField.setOnTouchListener(touchListener);
    }

    public EditText getCurrentFoucusedEditText() {
        return mCurrentlyFocusedEditText;
    }

    private void setOnTextChangeListener() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override public void afterTextChanged(Editable s) {
                if (mCurrentlyFocusedEditText.getText().length() >= 1
                        && mCurrentlyFocusedEditText != mMobileTenField) {
                    mCurrentlyFocusedEditText.focusSearch(View.FOCUS_RIGHT).requestFocus();
                } else if (mCurrentlyFocusedEditText.getText().length() >= 1
                        && mCurrentlyFocusedEditText == mMobileTenField) {
                    InputMethodManager imm =
                            (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getWindowToken(), 0);
                    }
                } else {
                    String currentValue = mCurrentlyFocusedEditText.getText().toString();
                    if (currentValue.length() <= 0 && mCurrentlyFocusedEditText.getSelectionStart() <= 0) {
                        mCurrentlyFocusedEditText.focusSearch(View.FOCUS_LEFT).requestFocus();
                    }
                }
            }
        };
        mMobileOneField.addTextChangedListener(textWatcher);
        mMobileTwoField.addTextChangedListener(textWatcher);
        mMobileThreeField.addTextChangedListener(textWatcher);
        mMobileFourField.addTextChangedListener(textWatcher);
        mMobileFiveField.addTextChangedListener(textWatcher);
        mMobileSixField.addTextChangedListener(textWatcher);
        mMobileSevenField.addTextChangedListener(textWatcher);
        mMobileEightField.addTextChangedListener(textWatcher);
        mMobileNineField.addTextChangedListener(textWatcher);
        mMobileTenField.addTextChangedListener(textWatcher);
    }

    public void simulateDeletePress() {
        mCurrentlyFocusedEditText.setText("");
    }
}

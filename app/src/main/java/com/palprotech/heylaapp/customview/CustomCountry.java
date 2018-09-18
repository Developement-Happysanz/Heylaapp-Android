package com.palprotech.heylaapp.customview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.activity.BookingHistoryActivity;
import com.palprotech.heylaapp.activity.BookingHistoryDetailsActivity;
import com.palprotech.heylaapp.adapter.BookingHistoryListAdapter;
import com.palprotech.heylaapp.bean.support.BookingHistory;
import com.palprotech.heylaapp.bean.support.BookingHistoryList;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomCountry extends Dialog implements DialogInterface.OnClickListener, AdapterView.OnItemClickListener {

    private ListView list;
    private EditText filterText = null;
    ArrayAdapter<String> adapter = null;

    private static final String TAG = BookingHistoryActivity.class.getName();
    protected ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    protected BookingHistoryListAdapter bookingHistoryListAdapter;
    protected ArrayList<BookingHistory> bookingHistoryArrayList;
    protected ListView listView;
    int totalCount = 0;
    protected boolean isLoadingForFirstTime = true;
    private ImageView ivBack;

    public CustomCountry(Context context, String[] cityList) {
        super(context);

        /** Design the dialog in main.xml file */
        setContentView(R.layout.custom_country);
        this.setTitle("Select City");
        filterText = (EditText) findViewById(R.id.EditBox);
        filterText.addTextChangedListener(filterTextWatcher);
        list = (ListView) findViewById(R.id.List);
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, cityList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onEvent list item click" + position);
                BookingHistory bookingHistory = null;
                if ((bookingHistoryListAdapter != null) && (bookingHistoryListAdapter.ismSearching())) {
                    Log.d(TAG, "while searching");
                    int actualindex = bookingHistoryListAdapter.getActualEventPos(position);
                    Log.d(TAG, "actual index" + actualindex);
                    bookingHistory = bookingHistoryArrayList.get(actualindex);
                } else {
                    bookingHistory = bookingHistoryArrayList.get(position);
                }

            }
        });
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            adapter.getFilter().filter(s);
        }
    };
    @Override
    public void onStop(){
        filterText.removeTextChangedListener(filterTextWatcher);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    private void LoadListView(JSONObject response) {

        progressDialogHelper.hideProgressDialog();
        Gson gson = new Gson();
        BookingHistoryList bookingHistoryList = gson.fromJson(response.toString(), BookingHistoryList.class);
        if (bookingHistoryList.getBookingHistories() != null && bookingHistoryList.getBookingHistories().size() > 0) {
            totalCount = bookingHistoryList.getCount();
            isLoadingForFirstTime = false;
            updateListAdapter(bookingHistoryList.getBookingHistories());
        }
    }

    protected void updateListAdapter(ArrayList<BookingHistory> bookingHistoryArrayList) {
        this.bookingHistoryArrayList.addAll(bookingHistoryArrayList);
        if (bookingHistoryListAdapter == null) {
            bookingHistoryListAdapter = new BookingHistoryListAdapter(getContext(), this.bookingHistoryArrayList);
            listView.setAdapter(bookingHistoryListAdapter);
        } else {
            bookingHistoryListAdapter.notifyDataSetChanged();
        }
    }

    private void loadUI() {
        listView = findViewById(R.id.listView_booked);
        listView.setOnItemClickListener(this);
        bookingHistoryArrayList = new ArrayList<>();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
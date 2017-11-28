package com.palprotech.heylaapp.servicehelpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.app.AppController;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;

/**
 * Created by Admin on 25-09-2017.
 */

public class ServiceHelper {

    private String TAG = "Get Name";
    private Context context;
    private IServiceListener iServiceListener;

    public ServiceHelper(Context context) {
        this.context = context;
    }

    public void setServiceListener(IServiceListener iServiceListener) {
        this.iServiceListener = iServiceListener;
    }

    public String makeRawRequest(String Url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(Url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("func_name", "advanced_event_management");
            String singledate = PreferenceStorage.getFilterSingleDate(context);
            if (!singledate.equalsIgnoreCase("")) {
                jsonObject.accumulate("single_date", singledate);
            }
            String eventType = PreferenceStorage.getFilterEventType(context);
            if (!eventType.equalsIgnoreCase("")) {
                jsonObject.accumulate("event_type", eventType);
            }
            String eventTypeCategory = PreferenceStorage.getFilterEventTypeCategory(context);
            if (!eventType.equalsIgnoreCase("")) {
                jsonObject.accumulate("event_type_category", eventTypeCategory);
            }
            String catgry = PreferenceStorage.getFilterCatgry(context);
            if (!catgry.equalsIgnoreCase("")) {
                jsonObject.accumulate("selected_category", catgry);
            }
            String city = PreferenceStorage.getFilterCity(context);
            if (!city.equalsIgnoreCase("")) {
                jsonObject.accumulate("selected_city", city);
            }
            String fromdate = PreferenceStorage.getFilterFromDate(context);
            if (!fromdate.equalsIgnoreCase("")) {
                jsonObject.accumulate("from_date", fromdate);
            }
            String todate = PreferenceStorage.getFilterToDate(context);
            if (!todate.equalsIgnoreCase("")) {
                jsonObject.accumulate("to_date", todate);
            }

            // jsonObject.accumulate("twitter", person.getTwitter());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
            Log.e("Reqjson", "ajazFilter Reqjson: " + json);
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null) {

                try {
                    result = convertInputStreamToString(inputStream);
                    // Log.d(TAG, "ajazFilter : " + result.toString());
                    JSONObject obj = new JSONObject(result);
                    iServiceListener.onResponse(obj);

                    Log.d("ajazFilter resultjson: ", obj.toString());

                } catch (Exception t) {
                    Log.e("ajazFilterExce : ", "ajazFilter : ", t);
                }
                // eventServiceListener.onEventResponse(result);
            } else {
                result = "Did not work!";
                Log.d(TAG, "ajazFilter : " + result.toString());
            }

        } catch (Exception e) {
            //  e.printStackTrace();
            Log.e("InputStream", "ajazFilter : ", e);
        }

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public void makeGetServiceCall(String params, String urls) {
        Log.d(TAG, "making sign in request" + params);
        String baseURL = "";
        try {
            URI uri = new URI(urls.replace(" ", "%20"));
            baseURL = uri.toString();

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    baseURL, params,
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            iServiceListener.onResponse(response);
                        }
                    }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        Log.d(TAG, "error during sign up" + error.getLocalizedMessage());

                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            iServiceListener.onError(jsonObject.getString(HeylaAppConstants.PARAM_MESSAGE));
                            String status = jsonObject.getString("status");
                            Log.d(TAG, "signup status is" + status);
                        } catch (UnsupportedEncodingException e) {
                            iServiceListener.onError(context.getResources().getString(R.string.error_occurred));
                            e.printStackTrace();
                        } catch (JSONException e) {
                            iServiceListener.onError(context.getResources().getString(R.string.error_occurred));
                            e.printStackTrace();
                        }

                    } else {
                        iServiceListener.onError(context.getResources().getString(R.string.error_occurred));
                    }
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}

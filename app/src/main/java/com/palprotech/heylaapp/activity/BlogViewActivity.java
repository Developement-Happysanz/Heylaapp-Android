package com.palprotech.heylaapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;

public class BlogViewActivity extends AppCompatActivity {

    private static final String TAG = BlogViewActivity.class.getName();
    private TextView title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_view);
        findViewById(R.id.back_res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        WebView wb = (WebView) findViewById(R.id.webView1);
        title = (TextView) findViewById(R.id.tvtitletext);
        String page = getIntent().getStringExtra("pageval");

        if (page.equalsIgnoreCase("setting_about")) {
            title.setText("About Us");
            wb.loadUrl("https://www.heylaapp.com/about-us/");
            wb.setWebViewClient(new WebViewClient(){

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url){
                    view.loadUrl(url);
                    return true;
                }
            });
        }
//        else if (page.equalsIgnoreCase("setting_report")) {
//            wb.loadUrl("https://www.heylaapp.com/blog/");
//            wb.setWebViewClient(new WebViewClient(){
//
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, String url){
//                    view.loadUrl(url);
//                    return true;
//                }
//            });
//        }
        else if (page.equalsIgnoreCase("setting_privacy")) {
            title.setText("Privacy Policy");
            wb.loadUrl("https://www.heylaapp.com/privacy/");
            wb.setWebViewClient(new WebViewClient(){

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url){
                    view.loadUrl(url);
                    return true;
                }
            });
        }
        else if (page.equalsIgnoreCase("setting_terms")) {
            title.setText("Terms and Conditions");
            wb.loadUrl("https://www.heylaapp.com/terms/");
            wb.setWebViewClient(new WebViewClient(){

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url){
                    view.loadUrl(url);
                    return true;
                }
            });
        }
        else if (page.equalsIgnoreCase("blog")) {
            wb.loadUrl("https://www.heylaapp.com/blog/");
            wb.setWebViewClient(new WebViewClient(){

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url){
                    view.loadUrl(url);
                    return true;
                }
            });
        }


    }

}

package com.palprotech.heylaapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;

public class BlogViewActivity extends AppCompatActivity {

    private static final String TAG = UpdatePasswordActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_view);

        WebView wb = (WebView) findViewById(R.id.webView1);

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

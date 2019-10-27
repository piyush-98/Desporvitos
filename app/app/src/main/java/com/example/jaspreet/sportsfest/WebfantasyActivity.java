package com.example.jaspreet.sportsfest;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yalantis.phoenix.PullToRefreshView;

public class WebfantasyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        setContentView(R.layout.activity_webhash);

//        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

//        String link=this.getIntent().getExtras().getString("link","https://ropsten.etherscan.io/address/0x5e53892c31e1b06a37a007018ec5acebe171d0df");

       String link="https://cricketsport.herokuapp.com/";
        final WebView browser = (WebView) findViewById(R.id.web);
        browser.setWebViewClient(new MyBrowser());
        browser.getSettings().setLoadsImagesAutomatically(true);
       browser.getSettings().setJavaScriptEnabled(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.loadUrl(link);
      /*  ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browser.reload();
            }
        });
    */

      final PullToRefreshView mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                        browser.reload();
                    }
                }, 1000);
            }
        });

    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

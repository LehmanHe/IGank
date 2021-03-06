package cn.edu.ustc.igank.ui.base;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import cn.edu.ustc.igank.R;
import cn.edu.ustc.igank.support.Settings;

public class WebViewActivity extends AppCompatActivity {

    protected WebView webView;
    protected ProgressBar progressBar;
    protected boolean isLoading = true;
    protected String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initData();
    }

    protected  String getData(){
        return getIntent().getStringExtra("url");
    }
    protected  void loadData(){
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(data);
            }
        });
    }
    private void initData() {
        data = getData();

        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportMultipleWindows(false);
        /*
         cache web page
         */

        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);

       /*  if(Settings.isNightMode) {
            webView.setBackgroundColor(ContextCompat.getColor(this, R.color.night_primary));
        }*/
        if(Settings.isWIFI == false) {
            webView.getSettings().setBlockNetworkImage(Settings.getInstance().getBoolean(Settings.NO_PIC_MODE, false));
        }else {
            webView.getSettings().setBlockNetworkImage(false);
        }


        loadData();
        setProgressBarIndeterminateVisibility(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                view.loadUrl("file:///android_asset/error.html");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.loadUrl("file:///android_asset/error.html");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (isLoading) {
                    progressBar.incrementProgressBy(newProgress - progressBar.getProgress());
                    if (newProgress > 25) {
                        isLoading = false;
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}

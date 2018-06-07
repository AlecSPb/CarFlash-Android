package com.carflash.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.carflash.user.manager.SessionManager;
import com.carflash.user.urls.Apis;

public class CreditCardPaymentActivity extends AppCompatActivity {
    private static final String TAG = "CreditCardActivity";
    WebView webview;
    SessionManager sessionManager;
    WebView newWebView;

    String finishUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_payment);

        webview = (WebView) findViewById(R.id.webview);
        sessionManager = new SessionManager(this);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);


        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("" + TAG, "webclint --> " + url);

                finishUrl = url;

                if (url.contains("api/save_card.php")) {
                    finish();
                   startActivity(new Intent(CreditCardPaymentActivity.this, CreditCardPaymentActivity.class));
                }


                if (url.contains("select_card.php")) {
                    String data[] = url.split("=");
                    Intent intent = new Intent();
                    intent.putExtra("CREDIT_ID", data[1]);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                super.onPageStarted(view, url, favicon);
            }


        });


        if (!sessionManager.getUserDetails().get(SessionManager.USER_ID).equals("")) {
            Log.d("***Url", "" + Apis.paymentUrl + "?user_id=" + sessionManager.getUserDetails().get(SessionManager.USER_ID));
            renderWebPage(Apis.paymentUrl + "?user_id=" + sessionManager.getUserDetails().get(SessionManager.USER_ID));

        }

        else {
            Toast.makeText(this, "User id not found", Toast.LENGTH_SHORT).show();
        }

    }

    protected void renderWebPage(final String urlToRender) {
        Log.d("" + TAG, "Loading Url --> " + urlToRender);

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                newWebView = new WebView(CreditCardPaymentActivity.this);
                newWebView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                newWebView.getSettings().setJavaScriptEnabled(true);
                newWebView.getSettings().setSupportZoom(true);
                newWebView.getSettings().setBuiltInZoomControls(true);
                newWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
                newWebView.getSettings().setSupportMultipleWindows(true);
                view.addView(newWebView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();

                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        Log.d("" + TAG, "CreateUrl --> " + url);
                        finishUrl = url;
                        super.onPageStarted(view, url, favicon);
                    }


                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                    }
                });

                return true;
            }

        });


        webview.loadUrl(urlToRender);

    }

    @Override
    public void onBackPressed() {

        if (finishUrl.contains("https://checkout.stripe.com/")) {
            finish();

            startActivity(new Intent(this, CreditCardPaymentActivity.class));
        } else {
            finish();
        }


//


    }
}

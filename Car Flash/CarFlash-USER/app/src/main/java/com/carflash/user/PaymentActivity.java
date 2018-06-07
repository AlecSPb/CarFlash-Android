package com.carflash.user;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carflash.user.trackRideModule.TrackRideAactiviySamir;
import com.carflash.user.urls.Apis;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PaymentActivity extends AppCompatActivity {


    @Bind(R.id.webview)
    WebView webview;

    public static String DoneRideId;
    @Bind(R.id.pb)
    ProgressBar mProgressBar;
    @Bind(R.id.url_txt)
    TextView urlTxt;
    private String TAG = "PaymentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        DoneRideId = super.getIntent().getExtras().getString("DONE_RIDE_ID");

        urlTxt.setText(""+Apis.View_Ride_Info + DoneRideId);
        renderWebPage(Apis.View_Ride_Info+DoneRideId);
    }

    protected void renderWebPage(String urlToRender) {
        Log.d("" + TAG, "Loading Url --> " + urlToRender);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                try{
                    if (url.contains("FinishActivity")) {
                        finish();
                        TrackRideAactiviySamir.trackRideActivity.finish();
                    }
//                    if(url.contains("payment-successful")){
//                        renderWebPage(Apis.View_Ride_Info+DoneRideId);
//                    }if(url.contains("payment-cancelled")){
//                        renderWebPage(Apis.View_Ride_Info+DoneRideId);
//                    }
                    try{TrialRideConfirmDialogActivity.activity.finish();}catch (Exception e){}
                }catch (Exception e){}

            }

            @Override
            public void onPageFinished(WebView view, String URL) {
            }
        });


        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
            }
        });

        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(urlToRender);
    }

}

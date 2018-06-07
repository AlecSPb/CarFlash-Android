package com.carflash.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carflash.user.manager.LanguageManager;
import com.carflash.user.models.Add_Credit_Card_Model;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.trackRideModule.TrackRideAactiviySamir;
import com.carflash.user.urls.Apis;
import com.carflash.user.manager.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sam.placer.PlaceHolderView;

import java.util.HashMap;

import butterknife.Bind;

public class Add_Card_First extends AppCompatActivity {

    LinearLayout credit_or_debit, ll_back_card;
    public static TextView tv_cards;
    GsonBuilder gsonBuilder;
    Gson gson;
    RelativeLayout webLayout;
    WebView webview;
    PaystackResponseModel responseModel;

    TextView saved_card;
    SessionManager sessionManager;
    Saved_Card_Model saved_card_model;

    DeleteCardModel deleteCardModel;
    LanguageManager languageManager;

    int placeHolder_Position;

    public int pos = 0;
    int result;

    PlaceHolderView placeHolderView;
    String user_id, user_email, callback_url, msg, reference_number, authorization_url, ride_mode;

    ApiManager apiManager;
    String TAG="Add_Card_First";

    ProgressBar pb_addcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card_first);
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

//        apiManager = new ApiManager(this, this, this);
        sessionManager = new SessionManager(this);
        pb_addcart=(ProgressBar) findViewById(R.id.pb_addcart);
        user_id = sessionManager.getUserDetails().get(SessionManager.USER_ID);
      //  user_email = sessionManager.getUserDetails().get(SessionManager.USER_EMAIL);
        user_email = sessionManager.getEmail();
        languageManager = new LanguageManager(this);


        webview= (WebView) findViewById(R.id.webview);

        renderWebPage(Apis.paymentUrl + "?user_id="+user_id);

//        saved_card = (TextView) findViewById(R.id.save_cards);
//        webView = (WebView) findViewById(R.id.webview);
//        webLayout = (RelativeLayout) findViewById(R.id.webLayout);
//        placeHolderView = (PlaceHolderView) findViewById(R.id.placeHolder_one);
//        credit_or_debit = (LinearLayout) findViewById(R.id.credit_or_debit);
//        ll_back_card = (LinearLayout) findViewById(R.id.ll_back_card);


//        apiManager.execution_method_get(Config.ApiKeys.KEY_SAVED_CARD, Apis.paymentUrl + "?user_id=" + user_id, true, ApiManager.ACTION_SHOW_TOP_BAR);



//        credit_or_debit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addCard(user_id, user_email);
//            }
//        });


//        ll_back_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Add_Card_First.this.finish();
//            }
//        });
    }

    private void addCard(String user_id, String user_email) {

        HashMap<String, String> bodyparameters = new HashMap<String, String>();
        bodyparameters.put("user_id", user_id);
        bodyparameters.put("email", user_email);
        //  apiManager.execution_method_post(Config.ApiKeys.KEY_ADD_CREDIT_CARD ,""+  Apis.ADD_CREDIT_CARD, bodyparameters, true,ApiManager.ACTION_SHOW_TOP_BAR);

        apiManager.execution_method_get(Config.ApiKeys.KEY_ADD_CREDIT_CARD, Apis.ADD_CREDIT_CARD + "?user_id=" + user_id + "&email=" + user_email, true, ApiManager.ACTION_SHOW_TOP_BAR);

    }

//    @Override
//    public void onFetchComplete(Object script, String APINAME) {
//
//        if (APINAME.equals("" + Config.ApiKeys.KEY_ADD_CREDIT_CARD)) {
//            Add_Credit_Card_Model card_model = gson.fromJson("" + script, Add_Credit_Card_Model.class);
//            result = card_model.getResult();
//            callback_url = card_model.getDetails().getCall_back_url();
//            authorization_url = card_model.getDetails().getData().getAuthorization_url();
//            msg = card_model.getMsg();
//            reference_number = card_model.getDetails().getData().getReference();
//
//
//            Log.d("**Callback url_add card--", callback_url);
//            Log.d("**Authorization url_add card--", authorization_url);
//
//            if (result == 1) {
//                webLayout.setVisibility(View.VISIBLE);
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.setWebViewClient(new MyBrowser());
//                webView.loadUrl(authorization_url);
//
//            } else {
//
//                Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
//            }
//
//        }
//
//        if (APINAME.equals(Config.ApiKeys.KEY_SAVED_CARD)) {
//            Log.d("**KEY_SAVED_CARD===", "" + script);
//
//            saved_card_model = new Saved_Card_Model();
//            saved_card_model = gson.fromJson("" + script, Saved_Card_Model.class);
//            placeHolderView.removeAllViews();
//
//            if (saved_card_model.getResult() == 1) {
//
//                saved_card.setText(R.string.ADD_CARD_ACTIVITY_select_credit_card);
//                placeHolderView.refresh();
//
//                Log.d("**ARRAY===", "" + saved_card_model.getDetails().size());
//                for (int i = 0; i < saved_card_model.getDetails().size(); i++) {
//                    placeHolderView.addView(new SavedCardType(this, saved_card_model, this, this));
//                }
//
//            }
//        }
//
//        if (APINAME.equals(Config.ApiKeys.Key_Delete_Cards)) {
//            Log.d("**DELETE CARD___", "" + script);
//            deleteCardModel = new DeleteCardModel();
//
//            deleteCardModel = gson.fromJson("" + script, DeleteCardModel.class);
//
//            if (deleteCardModel.getResult() == 1) {
//
//                placeHolderView.removeView(placeHolder_Position);
//
//                Toast.makeText(this, R.string.DELETE_CARD, Toast.LENGTH_SHORT).show();
//                placeHolderView.refresh();
//
//            }
//
//        }
//
//        //confirm payment
//        if (APINAME.equals(Config.ApiKeys.CONFIRM_PAYMENT)) {
//            Log.d("**OBJECTSCRIPT_CONFIRM_PAYMENT saced api--", String.valueOf(script));
//            responseModel = gson.fromJson("" + script, PaystackResponseModel.class);
//            int result = responseModel.getResult();
//
//            if (result == 1) {
//                webLayout.setVisibility(View.GONE);
//                Toast.makeText(this, "Transaction Successful!!!", Toast.LENGTH_SHORT).show();
//                apiManager.execution_method_get(Config.ApiKeys.KEY_SAVED_CARD, Apis.SAVED_CARD + "?&user_id=" + user_id, true, ApiManager.ACTION_SHOW_TOP_BAR);
//
//                //  startActivity(new Intent(Add_Card_First.this, TrialActivity.class));
//                //payment saved api
//
//            } else {
//                //for unsuccessful payment, close the webview and show the payment screen to the user
//                Toast.makeText(this, "Transaction is unsuccessful. Please try again!", Toast.LENGTH_SHORT).show();
//                webLayout.setVisibility(View.GONE);
//            }
//
//            Log.e("RESULT_CONFIRM_PAYMENT---", String.valueOf(result));
//        }
//
//    }
//
//    @Override
//    public void onFetchResultZero(String s) {
//
//    }

    protected void renderWebPage(String urlToRender) {
        Log.d("" + TAG, "Loading Url --> " + urlToRender);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d(""+TAG,url);
                try{
                    if (url.contains("select_card.php")) {

                        String[] parts = url.split("=");
                        String month = parts[0];
                        String year = parts[1];

                        Intent intent = new Intent();
                        intent.putExtra("CREDIT_ID", year);
                        setResult(Activity.RESULT_OK, intent);
                        finish();

                    }
                }catch (Exception e){}

            }

            @Override
            public void onPageFinished(WebView view, String URL) {
            }
        });


        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                pb_addcart.setProgress(newProgress);
            }
        });

        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(urlToRender);
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
//            this.webView.goBack();
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
//
//    private class MyBrowser extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//
//            Log.d("**LOAD OVERRIDE URL=_add card=", url);
//            Log.d("**LOAD OVERRIDE view=_add card=", String.valueOf(view));
//
//            return true;
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            Log.d("**LOAD PAGEFINISH URL=_add card=", url);
//            Log.d("**created url=_add card=", callback_url);
//
//            if (url.equals(callback_url + "?trxref=" + reference_number + "&reference=" + reference_number) || url.equals("https://standard.paystack.co/close")) {
//                Log.d("**created url=inside loop_add card=", callback_url);
//                HashMap<String, String> body = new HashMap<String, String>();
//                body.put("token", reference_number);
//                body.put("user_id", user_id);
//
//
//                apiManager.execution_method_post(Config.ApiKeys.CONFIRM_PAYMENT, Apis.CONFIRM_PAYMENT, body, true, ApiManager.ACTION_SHOW_DIALOG);
//                Log.e("**API_CONFIRM_PAYMENT==add card=", Apis.CONFIRM_PAYMENT + "token=" + reference_number);
//                Log.d("**USER_ID=add card==", user_id);
//
//            }
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//    }
//
//    @Override
//    public void clickDelete(int pos) {
//        placeHolder_Position = pos;
//        Log.d("**PLCEHOLDER POSITION---", "" + pos);
//        apiManager.execution_method_get(Config.ApiKeys.Key_Delete_Cards, Apis.DELETE_SAVED_CARD + "?&paystack_id=" + "" + saved_card_model.getDetails().get(pos).getPaystack_id(), true, ApiManager.ACTION_SHOW_TOP_BAR);
//    }
//
//    @Override
//    public void layoutClick(int pos) {
//        String  paystack_id = saved_card_model.getDetails().get(pos).getPaystack_id();
//        String  paystack_last4 = saved_card_model.getDetails().get(pos).getLast4();
//        String  paystack_authCode = saved_card_model.getDetails().get(pos).getAuthorization_code();
//
//
//
//        switch (getIntent().getStringExtra("payment")){
//
//            case "normal" :
//                Intent intent = new Intent();
//                intent.putExtra("paystack_id", paystack_id);
//                intent.putExtra("paystack_last4", paystack_last4);
//                intent.putExtra("paystack_authCode", paystack_authCode);
//                setResult(Activity.RESULT_OK, intent);
//                finish();
//                break;
//
//            case "rental" :
//                Intent intent2 = new Intent();
//                intent2.putExtra("paystack_id", paystack_id);
//                intent2.putExtra("paystack_last4", paystack_id);
//                intent2.putExtra("paystack_authCode", paystack_authCode);
//                setResult(Activity.RESULT_OK, intent2);
//                finish();
//                break;
//        }
//       /* Intent intent = new Intent();
//      //  intent.putExtra("paystack_id", paystack_id);
//        setResult(Activity.RESULT_OK, intent);
//        finish();*/
//    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}